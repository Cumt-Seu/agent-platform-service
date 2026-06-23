"""
RAG retrieval service - hybrid search + reranking.
"""
from typing import List
from app.core.config import settings


class RetrievalResult:
    def __init__(self, content: str, score: float, metadata: dict):
        self.content = content
        self.score = score
        self.metadata = metadata


class RAGRetriever:
    """RAG 检索服务 - 混合检索 + 重排"""

    async def retrieve(self, query: str, kb_id: str, top_k: int = 10,
                       rerank_top_n: int = 5, filters: dict = None) -> List[dict]:
        """混合检索 + 重排"""
        # Mock retrieval
        mock_results = [
            {
                "chunkId": "chunk_1",
                "content": "行内编码规范要求：所有 Controller 类必须包含 JavaDoc 注释...",
                "score": 0.95,
                "metadata": {
                    "source": "编码规范.pdf",
                    "heading": "Controller 规范",
                    "docType": "规范",
                },
            },
            {
                "chunkId": "chunk_2",
                "content": "RESTful API 设计规范：GET 请求用于查询，POST 用于创建...",
                "score": 0.88,
                "metadata": {
                    "source": "API设计规范.md",
                    "heading": "接口设计",
                    "docType": "接口",
                },
            },
            {
                "chunkId": "chunk_3",
                "content": "NullPointerException 常见原因：方法返回 null 未做空值检查...",
                "score": 0.82,
                "metadata": {
                    "source": "故障案例库.docx",
                    "heading": "空指针异常",
                    "docType": "故障案例",
                },
            },
        ]

        return mock_results[:rerank_top_n]


# Singleton
rag_retriever = RAGRetriever()
