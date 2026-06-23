from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.routes import chat, sessions, skills, review, diagnosis
from app.core.config import settings

app = FastAPI(
    title="Agent Service - 企业研发全流程智能体平台",
    description="Agent 服务 (Python/LangGraph)，负责 AI 编排、Agent Loop、Skill 执行、SSE 流式输出",
    version="0.1.0",
)

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Register routers
app.include_router(chat.router, prefix="/api/v1/agent", tags=["Agent Chat"])
app.include_router(sessions.router, prefix="/api/v1/agent/sessions", tags=["Sessions"])
app.include_router(skills.router, prefix="/api/v1/skills", tags=["Skills"])
app.include_router(review.router, prefix="/api/v1/review", tags=["Review"])
app.include_router(diagnosis.router, prefix="/api/v1/diagnosis", tags=["Diagnosis"])


@app.get("/health")
async def health():
    return {"status": "healthy", "service": "agent-service", "version": "0.1.0"}
