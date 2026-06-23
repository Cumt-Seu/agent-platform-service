"""
Model inference API - mock for vLLM/TGI inference service.
"""
from fastapi import APIRouter
from pydantic import BaseModel

router = APIRouter()


class ChatCompletionRequest(BaseModel):
    model: str = "default"
    messages: list[dict]
    temperature: float = 0.7
    max_tokens: int = 2048
    stream: bool = False


@router.post("/chat/completions")
async def chat_completions(request: ChatCompletionRequest):
    """模型推理接口 - mock vLLM 兼容 OpenAI API"""
    # Mock inference
    return {
        "id": "chatcmpl-mock",
        "object": "chat.completion",
        "model": request.model,
        "choices": [
            {
                "index": 0,
                "message": {
                    "role": "assistant",
                    "content": "这是模拟的模型推理回复。",
                },
                "finish_reason": "stop",
            }
        ],
        "usage": {
            "prompt_tokens": 100,
            "completion_tokens": 50,
            "total_tokens": 150,
        },
    }


@router.get("/models")
async def list_models():
    """列出可用模型"""
    return {
        "data": [
            {"id": "Qwen2-7B-Instruct", "object": "model", "owned_by": "private"},
            {"id": "DeepSeek-Coder-6.7B", "object": "model", "owned_by": "private"},
        ]
    }
