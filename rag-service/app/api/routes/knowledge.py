"""
Knowledge base query API.
"""
from fastapi import APIRouter
from pydantic import BaseModel
from app.services.retriever import rag_retriever

router = APIRouter()


class QueryRequest(BaseModel):
    query: str
    topK: int = 10
    rerankTopN: int = 5
    filters: dict | None = None


@router.post("/{kbId}/query")
async def query_knowledge_base(kbId: str, request: QueryRequest):
    """知识库检索"""
    results = await rag_retriever.retrieve(
        query=request.query,
        kb_id=kbId,
        top_k=request.topK,
        rerank_top_n=request.rerankTopN,
        filters=request.filters,
    )
    return {
        "results": results,
        "total": len(results),
        "query": request.query,
    }
