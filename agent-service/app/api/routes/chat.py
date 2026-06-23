"""
Chat API - SSE streaming endpoint for Agent conversation.
"""
import asyncio
import uuid
from fastapi import APIRouter, Request
from sse_starlette.sse import EventSourceResponse
from pydantic import BaseModel

from app.core.agent import agent_graph
from app.core.sse_events import (
    ThinkingEvent, ToolCallEvent, ToolResultEvent,
    ContentEvent, DagUpdateEvent, StepUpdateEvent, DoneEvent,
)
from app.skills.executor import skill_executor

router = APIRouter()


class ChatRequest(BaseModel):
    sessionId: str | None = None
    message: str
    skill: str | None = None
    context: dict = {}
    stream: bool = True


@router.post("/chat")
async def chat(request: ChatRequest):
    """Agent 对话接口 - SSE 流式返回"""
    if request.stream:
        return EventSourceResponse(generate_sse_response(request))
    else:
        return await generate_sync_response(request)


async def generate_sse_response(request: ChatRequest):
    """SSE 流式响应生成器"""
    session_id = request.sessionId or str(uuid.uuid4())

    # Event 1: thinking
    yield ThinkingEvent(content="正在分析您的需求...").to_sse()
    await asyncio.sleep(0.1)

    # Event 2: intent recognition
    yield ThinkingEvent(content="已识别意图类型...").to_sse()
    await asyncio.sleep(0.1)

    # Event 3: DAG update - planning node
    yield DagUpdateEvent(
        node_id="intent_recognition",
        label="意图识别",
        node_type="BUILTIN",
        status="SUCCESS",
    ).to_sse()

    yield DagUpdateEvent(
        node_id="task_planning",
        label="任务规划",
        node_type="BUILTIN",
        status="RUNNING",
    ).to_sse()
    await asyncio.sleep(0.2)

    # Event 4: tool calls
    if request.skill:
        yield ToolCallEvent(tool_name=request.skill, args={"query": request.message}).to_sse()
        await asyncio.sleep(0.3)
        result = await skill_executor.execute(request.skill, {"query": request.message})
        yield ToolResultEvent(tool_name=request.skill, result=result).to_sse()

        # DAG update for skill execution
        yield DagUpdateEvent(
            node_id=f"skill_{request.skill}",
            label=request.skill,
            node_type="SKILL",
            status="SUCCESS",
            skill_name=request.skill,
        ).to_sse()
    else:
        # Default flow: knowledge retrieval + content generation
        yield ToolCallEvent(tool_name="knowledge_retrieval", args={"query": request.message}).to_sse()
        await asyncio.sleep(0.2)

        yield DagUpdateEvent(
            node_id="skill_knowledge_retrieval",
            label="知识库检索",
            node_type="SKILL",
            status="RUNNING",
            skill_name="knowledge_retrieval",
        ).to_sse()

        kb_result = await skill_executor.execute("knowledge_retrieval", {"query": request.message})
        yield ToolResultEvent(tool_name="knowledge_retrieval", result=kb_result).to_sse()

        yield DagUpdateEvent(
            node_id="skill_knowledge_retrieval",
            label="知识库检索",
            node_type="SKILL",
            status="SUCCESS",
            skill_name="knowledge_retrieval",
        ).to_sse()
        await asyncio.sleep(0.1)

    # Event 5: result synthesis
    yield DagUpdateEvent(
        node_id="result_synthesis",
        label="结果汇总",
        node_type="BUILTIN",
        status="RUNNING",
    ).to_sse()

    # Simulate streaming content
    mock_response = f"根据您的需求「{request.message}」，我已完成分析。以下是建议方案：\n\n1. 建议采用标准化实现方案\n2. 参考行内编码规范进行开发\n3. 注意安全与性能方面的检查\n"
    for i, char in enumerate(mock_response):
        yield ContentEvent(content=char).to_sse()
        if i % 10 == 0:
            await asyncio.sleep(0.05)

    yield DagUpdateEvent(
        node_id="result_synthesis",
        label="结果汇总",
        node_type="BUILTIN",
        status="SUCCESS",
    ).to_sse()

    # Event 6: done
    yield DoneEvent(total_tokens=1250, duration_ms=3200).to_sse()


async def generate_sync_response(request: ChatRequest) -> dict:
    """同步响应（非 SSE）"""
    session_id = request.sessionId or str(uuid.uuid4())
    return {
        "sessionId": session_id,
        "output": f"Mock response for: {request.message}",
        "toolCalls": [],
        "status": "COMPLETED",
        "metadata": {"tokens": 800, "durationMs": 1500},
    }
