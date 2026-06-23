"""
Document processing API - reparse, import from GitLab/Confluence.
"""
from fastapi import APIRouter, UploadFile, File
from pydantic import BaseModel
from typing import List
from app.services.processor import doc_processor

router = APIRouter()


class ReparseResponse(BaseModel):
    docId: str
    status: str


@router.post("/{kbId}/documents/{docId}/reparse")
async def reparse_document(kbId: str, docId: str):
    """重新解析文档"""
    result = await doc_processor.reparse_document(docId)
    return result


@router.post("/{kbId}/documents/upload")
async def upload_document(kbId: str, file: UploadFile = File(...)):
    """上传并解析文档"""
    content = await file.read()
    # Mock: process document
    result = await doc_processor.process_document(
        doc_id=f"doc_{file.filename}",
        content=content.decode("utf-8", errors="replace"),
        doc_type="规范",
    )
    return result


class GitLabImportRequest(BaseModel):
    gitlabUrl: str
    projectId: str
    branch: str = "main"
    path: str = "/docs/"
    docType: str = "规范"


@router.post("/{kbId}/import/gitlab")
async def import_from_gitlab(kbId: str, request: GitLabImportRequest):
    """从 GitLab 仓库导入文档"""
    # Mock: import documents from GitLab
    return {
        "importedCount": 5,
        "failedCount": 0,
        "status": "PROCESSING",
    }


class ConfluenceImportRequest(BaseModel):
    confluenceUrl: str
    spaceKey: str
    pageIds: List[str] = []
    syncMode: str = "ONCE"
    cronExpression: str | None = None
    docType: str = "架构"


@router.post("/{kbId}/import/confluence")
async def import_from_confluence(kbId: str, request: ConfluenceImportRequest):
    """从 Confluence 空间同步文档"""
    # Mock: import documents from Confluence
    result = {
        "importedCount": 12,
        "failedCount": 1,
        "status": "PROCESSING",
    }
    if request.syncMode == "SCHEDULED":
        result["syncConfig"] = {
            "nextSyncAt": "2026-06-24T00:00:00Z",
            "cronExpression": request.cronExpression or "0 0 * * *",
        }
    return result
