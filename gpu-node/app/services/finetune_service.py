"""
Finetune training service - mock implementation for training start/stop/logs/export.
"""
import asyncio
import uuid
from typing import AsyncGenerator


class FinetuneService:
    """微调训练服务 - mock 实现"""

    async def start_training(self, task_id: str, base_model: str, dataset_id: str,
                               lora_config: dict, training_config: dict) -> dict:
        """启动训练"""
        # Mock: In production, this would launch DeepSpeed training
        return {
            "taskId": task_id,
            "status": "TRAINING",
            "message": f"Training started with base model {base_model}",
            "pid": 12345,  # Mock PID
        }

    async def stop_training(self, task_id: str) -> dict:
        """停止训练"""
        return {
            "taskId": task_id,
            "status": "STOPPED",
            "message": "Training stopped",
        }

    async def get_training_logs(self, task_id: str) -> AsyncGenerator[str, None]:
        """训练日志 SSE 生成器"""
        mock_logs = [
            '{"epoch": 1, "train_loss": 2.5, "val_loss": 2.8, "lr": 2e-4}',
            '{"epoch": 1, "train_loss": 2.1, "val_loss": 2.4, "lr": 1.8e-4}',
            '{"epoch": 2, "train_loss": 1.8, "val_loss": 2.1, "lr": 1.5e-4}',
            '{"epoch": 2, "train_loss": 1.5, "val_loss": 1.9, "lr": 1.2e-4}',
            '{"epoch": 3, "train_loss": 1.2, "val_loss": 1.7, "lr": 8e-5}',
        ]
        for log in mock_logs:
            yield log
            await asyncio.sleep(0.5)

    async def export_lora(self, task_id: str) -> dict:
        """导出 LoRA 权重"""
        # Mock: In production, this would package and upload LoRA weights
        return {
            "taskId": task_id,
            "exportPath": f"/data/lora_exports/{task_id}/adapter_model.safetensors",
            "fileSizeMb": 128,
            "status": "COMPLETED",
        }


# Singleton
finetune_service = FinetuneService()
