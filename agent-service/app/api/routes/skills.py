"""
Skill invocation API - forwarded from business-service.
"""
from fastapi import APIRouter
from pydantic import BaseModel

from app.skills.executor import skill_executor

router = APIRouter()


class InvokeRequest(BaseModel):
    params: dict


@router.post("/{skillId}/invoke")
async def invoke_skill(skillId: str, request: InvokeRequest):
    """Skill 调用执行 - Agent 服务侧"""
    result = await skill_executor.execute(skillId, request.params)
    return {
        "skillId": skillId,
        "result": result,
        "status": "SUCCESS",
        "durationMs": 1500,
    }
