from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    service_name: str = "gpu-node"
    service_port: int = 8002

    # Business service (Java) for metadata updates
    business_service_url: str = "http://localhost:8080/api/v1"

    # GPU config
    gpu_device: str = "cuda:0"
    gpu_memory_utilization: float = 0.9

    # Redis
    redis_url: str = "redis://localhost:6379"

    # Model inference
    vllm_model: str = "Qwen2-7B-Instruct"
    vllm_port: int = 8080

    model_config = {"env_prefix": "GPU_", "env_file": ".env"}


settings = Settings()
