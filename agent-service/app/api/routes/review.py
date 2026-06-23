"""
Review Skill execution API - forwarded from business-service.
"""
from fastapi import APIRouter
from pydantic import BaseModel
from typing import List

router = APIRouter()


class ReviewSubmitRequest(BaseModel):
    mrId: str
    projectId: str
    diff: str
    files: List[dict]
    reviewDimensions: List[str] = ["NAMING", "SECURITY", "PERFORMANCE", "LOGIC"]


@router.post("/submit")
async def submit_review(request: ReviewSubmitRequest):
    """发起评审 - Agent 执行评审 Skill"""
    # Mock review execution
    return {
        "reviewId": "review_mock_" + str(hash(request.mrId))[:8],
        "summary": {
            "totalIssues": 3,
            "blockerCount": 0,
            "majorCount": 1,
            "minorCount": 2,
            "infoCount": 0,
            "qualityScore": 85.5,
            "overallComment": "代码整体质量良好，存在少量需要改进的问题。",
        },
        "issues": [
            {
                "issueId": "issue_1",
                "severity": "MAJOR",
                "category": "SECURITY",
                "filePath": "src/main/java/com/bank/user/controller/UserController.java",
                "startLine": 42,
                "endLine": 44,
                "codeSnippet": "String sql = \"SELECT * FROM user WHERE id=\" + userId;",
                "description": "存在 SQL 注入风险，使用了字符串拼接构建 SQL",
                "suggestion": "使用参数化查询替代字符串拼接",
                "fixedCode": "String sql = \"SELECT * FROM user WHERE id=?\";\nPreparedStatement ps = conn.prepareStatement(sql);\nps.setString(1, userId);",
                "status": "OPEN",
            },
            {
                "issueId": "issue_2",
                "severity": "MINOR",
                "category": "NAMING",
                "filePath": "src/main/java/com/bank/user/service/UserServiceImpl.java",
                "startLine": 15,
                "endLine": 15,
                "codeSnippet": "private int cnt = 0;",
                "description": "变量名 cnt 不符合驼峰命名规范",
                "suggestion": "改为 count 或使用更具描述性的名称",
                "fixedCode": "private int loginCount = 0;",
                "status": "OPEN",
            },
            {
                "issueId": "issue_3",
                "severity": "MINOR",
                "category": "PERFORMANCE",
                "filePath": "src/main/java/com/bank/user/service/UserServiceImpl.java",
                "startLine": 68,
                "endLine": 72,
                "codeSnippet": "for (User user : users) {\n    user.setRole(roleDao.findById(user.getRoleId()));\n}",
                "description": "循环内逐条查询数据库，存在 N+1 查询性能隐患",
                "suggestion": "批量查询角色数据，避免循环内数据库访问",
                "fixedCode": "List<Long> roleIds = users.stream().map(User::getRoleId).collect(Collectors.toList());\nMap<Long, Role> roleMap = roleDao.findByIds(roleIds).stream().collect(Collectors.toMap(Role::getId, r -> r));\nusers.forEach(u -> u.setRole(roleMap.get(u.getRoleId())));",
                "status": "OPEN",
            },
        ],
    }
