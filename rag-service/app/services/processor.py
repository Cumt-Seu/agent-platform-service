"""
Document processing service - parsing, chunking, embedding.
"""
from typing import List
from app.core.config import settings


class ChunkingStrategy:
    """切片策略"""

    def chunk_by_heading(self, content: str, metadata: dict) -> List[dict]:
        """按标题/章节切分"""
        # Mock chunking
        return [
            {"content": content[:200], "metadata": metadata},
        ]

    def chunk_by_semantic(self, content: str, metadata: dict,
                          max_tokens: int = 512, overlap: int = 64) -> List[dict]:
        """按语义边界切分"""
        chunks = []
        sentences = content.split("\n")
        current_chunk = []
        for sent in sentences:
            if len(current_chunk) >= max_tokens:
                chunks.append({"content": "\n".join(current_chunk), "metadata": metadata})
                current_chunk = current_chunk[-overlap:] if overlap else []
            current_chunk.append(sent)
        if current_chunk:
            chunks.append({"content": "\n".join(current_chunk), "metadata": metadata})
        return chunks

    def chunk_code(self, content: str, metadata: dict) -> List[dict]:
        """代码专用切分"""
        return [{"content": content, "metadata": metadata}]


class DocumentProcessor:
    """文档处理 - 解析 + 切片 + 向量化"""

    def __init__(self):
        self.chunking = ChunkingStrategy()

    async def process_document(self, doc_id: str, content: str,
                                doc_type: str, strategy: str = "SEMANTIC") -> dict:
        """处理文档：切片 + 向量化"""
        metadata = {"source": doc_id, "docType": doc_type}

        if strategy == "HEADING":
            chunks = self.chunking.chunk_by_heading(content, metadata)
        elif strategy == "CODE":
            chunks = self.chunking.chunk_code(content, metadata)
        else:
            chunks = self.chunking.chunk_by_semantic(content, metadata)

        # Mock: embedding and Milvus insert
        return {
            "docId": doc_id,
            "chunkCount": len(chunks),
            "status": "COMPLETED",
            "chunks": chunks,
        }

    async def reparse_document(self, doc_id: str) -> dict:
        """重新解析文档"""
        return {"docId": doc_id, "status": "PROCESSING"}


# Singleton
doc_processor = DocumentProcessor()
