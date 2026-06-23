from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    service_name: str = "rag-service"
    service_port: int = 8001

    # Business service (Java)
    business_service_url: str = "http://localhost:8080/api/v1"

    # Milvus
    milvus_host: str = "localhost"
    milvus_port: int = 19530
    milvus_collection: str = "knowledge_base"

    # Embedding
    embedding_model: str = "text-embedding-3-small"
    embedding_dim: int = 1024
    embedding_api_key: str = "mock_api_key"
    embedding_api_base: str = "https://api.openai.com/v1"

    # Redis
    redis_url: str = "redis://localhost:6379"

    # Chunking
    default_chunk_strategy: str = "SEMANTIC"
    default_chunk_size: int = 512
    default_chunk_overlap: int = 64

    model_config = {"env_prefix": "RAG_", "env_file": ".env"}


settings = Settings()
