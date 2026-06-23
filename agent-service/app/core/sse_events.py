"""
SSE Event types for Agent service streaming output.

Event types:
- thinking: Agent 思考过程
- tool_call: 工具调用
- tool_result: 工具返回结果
- content: 内容片段
- dag_update: DAG 节点状态变更
- step_update: 排障步骤状态变更
- done: 完成
"""
from dataclasses import dataclass, asdict
from typing import Any
import json


@dataclass
class SSEEvent:
    """Base SSE event"""
    event: str
    data: dict

    def to_sse(self) -> str:
        return f"event: {self.event}\ndata: {json.dumps(self.data, ensure_ascii=False)}\n\n"


@dataclass
class ThinkingEvent(SSEEvent):
    """Agent 思考过程"""
    def __init__(self, content: str):
        super().__init__(event="thinking", data={"content": content})


@dataclass
class ToolCallEvent(SSEEvent):
    """工具调用事件"""
    def __init__(self, tool_name: str, args: dict):
        super().__init__(event="tool_call", data={"toolName": tool_name, "args": args})


@dataclass
class ToolResultEvent(SSEEvent):
    """工具返回结果"""
    def __init__(self, tool_name: str, result: Any):
        super().__init__(event="tool_result", data={"toolName": tool_name, "result": result})


@dataclass
class ContentEvent(SSEEvent):
    """内容片段"""
    def __init__(self, content: str):
        super().__init__(event="content", data={"content": content})


@dataclass
class DagUpdateEvent(SSEEvent):
    """DAG 节点状态变更"""
    def __init__(self, node_id: str, label: str, node_type: str,
                 status: str, skill_name: str | None = None):
        super().__init__(event="dag_update", data={
            "nodeId": node_id,
            "label": label,
            "nodeType": node_type,
            "status": status,
            "skillName": skill_name,
        })


@dataclass
class StepUpdateEvent(SSEEvent):
    """排障步骤状态变更"""
    def __init__(self, step_name: str, status: str,
                 output: str | None = None, duration_ms: int | None = None):
        super().__init__(event="step_update", data={
            "stepName": step_name,
            "status": status,
            "output": output,
            "durationMs": duration_ms,
        })


@dataclass
class DoneEvent(SSEEvent):
    """完成事件"""
    def __init__(self, total_tokens: int = 0, duration_ms: int = 0):
        super().__init__(event="done", data={
            "totalTokens": total_tokens,
            "durationMs": duration_ms,
        })
