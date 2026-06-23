"""
Finetune training API - start/stop training, logs SSE, export LoRA.
"""
import asyncio
from fastapi import APIRouter
from sse_starlette.sse import EventSourceResponse
from pydantic import BaseModel

from app.services.finetune_service import finetune_service

router = APIRouter()


class StartTrainingRequest(BaseModel):
    baseModel: str
    datasetId: str
    loraConfig: dict
    trainingConfig: dict


@router.post("/{taskId}/start")
async def start_training(taskId: str, request: StartTrainingRequest):
    """启动微调训练"""
    result = await finetune_service.start_training(
        task_id=taskId,
        base_model=request.baseModel,
        dataset_id=request.datasetId,
        lora_config=request.loraConfig,
        training_config=request.trainingConfig,
    )
    return result


@router.post("/{taskId}/stop")
async def stop_training(taskId: str):
    """停止微调训练"""
    result = await finetune_service.stop_training(taskId)
    return result


@router.get("/{taskId}/logs")
async def training_logs(taskId: str):
    """训练日志 SSE"""
    async def event_generator():
        async for log in finetune_service.get_training_logs(taskId):
            yield {"event": "log", "data": log}
        yield {"event": "done", "data": '{"status": "COMPLETED"}'}

    return EventSourceResponse(event_generator())


@router.get("/{taskId}/export")
async def export_lora(taskId: str):
    """导出 LoRA 权重"""
    result = await finetune_service.export_lora(taskId)
    return result
