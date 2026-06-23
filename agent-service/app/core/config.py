from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    # Service
    service_name: str = "agent-service"
    service_port: int = 8000

    # Business service (Java) for metadata queries
    business_service_url: str = "http://localhost:8080/api/v1"

    # RAG service
    rag_service_url: str = "http://localhost:8001/api/v1"

    # LLM
    llm_model: str = "gpt-4"
    llm_api_key: str = "mock_api_key"
    llm_api_base: str = "https://api.openai.com/v1"
    llm_temperature: float = 0.7
    llm_max_tokens: int = 4096

    # Redis (for LangGraph Checkpointer)
    redis_url: str = "redis://localhost:6379"

    # Agent config
    max_iterations: int = 5
    max_concurrent_skills: int = 10

    model_config = {"env_prefix": "AGENT_", "env_file": ".env"}


settings = Settings()
