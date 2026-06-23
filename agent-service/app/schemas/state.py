"""
Agent State definitions for LangGraph StateGraph.
"""
from typing import TypedDict, Annotated, Literal
from langchain_core.messages import BaseMessage
from langgraph.graph import add_messages
import operator


class AgentState(TypedDict):
    """Agent 全局状态，StateGraph 各节点共享"""
    messages: Annotated[list[BaseMessage], add_messages]
    session_id: str
    intent: str | None
    entities: dict
    task_plan: list[dict] | None
    tool_results: list[dict]
    iteration: int
    max_iterations: int
    final_output: str | None
    needs_clarification: bool


class SubTaskState(TypedDict):
    """并行子任务状态"""
    task_id: str
    skill_name: str
    params: dict
    result: dict | None


class SynthesisState(TypedDict):
    """结果汇聚状态"""
    subtask_results: Annotated[list[dict], operator.add]
    final_output: str | None


class AgentRequest(TypedDict):
    """Agent 请求 DTO"""
    session_id: str | None
    user_input: str
    context: dict
    skill: str | None


class AgentResponse(TypedDict):
    """Agent 响应 DTO"""
    output: str
    tool_calls: list[dict]
    status: Literal["COMPLETED", "NEED_MORE_INPUT", "ERROR"]
    metadata: dict


# DAG state types for DagViewer
class DagNode(TypedDict):
    nodeId: str
    nodeType: str  # BUILTIN / SKILL
    label: str
    status: str  # PENDING / RUNNING / SUCCESS / FAILED / SKIPPED
    skillName: str | None
    durationMs: int | None


class DagEdge(TypedDict):
    sourceId: str
    targetId: str
    edgeType: str  # FIXED / CONDITIONAL / SEND
