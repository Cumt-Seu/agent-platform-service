from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.routes import knowledge, documents
from app.core.config import settings

app = FastAPI(
    title="RAG Service - 企业研发全流程智能体平台",
    description="RAG 检索服务 (Python)，负责知识库检索、文档解析切片、向量化入库",
    version="0.1.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(knowledge.router, prefix="/api/v1/knowledge-bases", tags=["Knowledge Base"])
app.include_router(documents.router, prefix="/api/v1/knowledge-bases", tags=["Documents"])


@app.get("/health")
async def health():
    return {"status": "healthy", "service": "rag-service", "version": "0.1.0"}
