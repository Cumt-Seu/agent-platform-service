from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.routes import finetune, inference
from app.core.config import settings

app = FastAPI(
    title="GPU Node - 企业研发全流程智能体平台",
    description="GPU 节点，负责模型推理（vLLM/TGI）、微调训练启停、训练日志SSE、LoRA 权重导出",
    version="0.1.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(finetune.router, prefix="/api/v1/finetune/tasks", tags=["Finetune Training"])
app.include_router(inference.router, prefix="/api/v1/inference", tags=["Model Inference"])


@app.get("/health")
async def health():
    return {"status": "healthy", "service": "gpu-node", "version": "0.1.0"}
