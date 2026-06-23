"""
Session API - DAG state query, session info (forwarded from business-service).
"""
from fastapi import APIRouter
from pydantic import BaseModel
from typing import Literal

router = APIRouter()


class DagStateResponse(BaseModel):
    sessionId: str
    nodes: list[dict]
    edges: list[dict]
    status: str


@router.get("/{sessionId}/dag")
async def get_dag_state(
    sessionId: str,
    snapshot: Literal["LATEST", "ARCHIVE"] = "LATEST",
):
    """查询 DAG 执行状态

    LATEST: 从 LangGraph get_state() 实时获取
    ARCHIVE: 从 task_plan 表读取存档快照
    """
    if snapshot == "LATEST":
        # Mock: return live DAG state from LangGraph
        return DagStateResponse(
            sessionId=sessionId,
            nodes=[
                {"nodeId": "intent_recognition", "nodeType": "BUILTIN", "label": "意图识别",
                 "status": "SUCCESS", "skillName": None, "durationMs": 300},
                {"nodeId": "task_planning", "nodeType": "BUILTIN", "label": "任务规划",
                 "status": "SUCCESS", "skillName": None, "durationMs": 200},
                {"nodeId": "skill_knowledge_retrieval", "nodeType": "SKILL", "label": "知识库检索",
                 "status": "SUCCESS", "skillName": "knowledge_qa", "durationMs": 1200},
                {"nodeId": "result_synthesis", "nodeType": "BUILTIN", "label": "结果汇总",
                 "status": "SUCCESS", "skillName": None, "durationMs": 500},
            ],
            edges=[
                {"sourceId": "intent_recognition", "targetId": "task_planning", "edgeType": "FIXED"},
                {"sourceId": "task_planning", "targetId": "skill_knowledge_retrieval", "edgeType": "SEND"},
                {"sourceId": "skill_knowledge_retrieval", "targetId": "result_synthesis", "edgeType": "FIXED"},
            ],
            status="COMPLETED",
        )
    else:
        # Mock: return archived DAG state from database
        return DagStateResponse(
            sessionId=sessionId,
            nodes=[
                {"nodeId": "intent_recognition", "nodeType": "BUILTIN", "label": "意图识别",
                 "status": "SUCCESS", "skillName": None, "durationMs": 300},
                {"nodeId": "skill_code_gen", "nodeType": "SKILL", "label": "代码生成",
                 "status": "SUCCESS", "skillName": "code_generation", "durationMs": 2500},
                {"nodeId": "result_synthesis", "nodeType": "BUILTIN", "label": "结果汇总",
                 "status": "SUCCESS", "skillName": None, "durationMs": 800},
            ],
            edges=[
                {"sourceId": "intent_recognition", "targetId": "skill_code_gen", "edgeType": "FIXED"},
                {"sourceId": "skill_code_gen", "targetId": "result_synthesis", "edgeType": "FIXED"},
            ],
            status="COMPLETED",
        )
