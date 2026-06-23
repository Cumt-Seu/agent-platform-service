"""
Diagnosis Skill execution API - forwarded from business-service.
"""
from fastapi import APIRouter
from pydantic import BaseModel

router = APIRouter()


class DiagnosisRequest(BaseModel):
    serviceName: str
    logContent: str
    alertInfo: str | None = None
    timeRange: dict | None = None


@router.post("/analyze")
async def analyze_diagnosis(request: DiagnosisRequest):
    """发起排障 - Agent 执行排障 Skill"""
    # Mock diagnosis execution
    return {
        "diagnosisId": "diag_mock_" + str(hash(request.serviceName))[:8],
        "result": {
            "summary": f"服务 {request.serviceName} 出现 NullPointerException，根因定位完成。",
            "rootCause": {
                "errorType": "NullPointerException",
                "location": "UserService.java:42",
                "description": "userService.findById() 方法返回 null，未做空值检查",
            },
            "impactScope": {
                "affectedServices": ["user-service", "order-service"],
                "affectedApis": ["GET /api/v1/users/{id}", "POST /api/v1/orders"],
                "estimatedAffectedUsers": 500,
            },
            "suggestions": [
                {
                    "priority": 1,
                    "action": "添加空值检查",
                    "description": "在调用 findById() 后添加 null 判断",
                    "estimatedResolutionTime": "5分钟",
                },
                {
                    "priority": 2,
                    "action": "添加全局异常处理",
                    "description": "在 Controller 层添加 @ExceptionHandler 处理 NullPointerException",
                    "estimatedResolutionTime": "15分钟",
                },
            ],
            "similarCases": [
                {
                    "caseId": "case_001",
                    "title": "用户查询空指针异常",
                    "similarity": 0.92,
                    "resolution": "添加 Optional 包装和空值检查",
                },
            ],
            "steps": [
                {"stepName": "日志分析", "status": "COMPLETED", "output": "发现 NullPointerException 堆栈", "durationMs": 1200},
                {"stepName": "监控指标关联", "status": "COMPLETED", "output": "CPU 正常，错误率上升 2.3%", "durationMs": 800},
                {"stepName": "故障知识库检索", "status": "COMPLETED", "output": "找到 2 个相似案例", "durationMs": 600},
                {"stepName": "综合研判", "status": "COMPLETED", "output": "根因定位完成", "durationMs": 500},
            ],
        },
    }
