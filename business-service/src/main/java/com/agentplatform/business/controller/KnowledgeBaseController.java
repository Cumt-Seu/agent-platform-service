package com.agentplatform.business.controller;

import com.agentplatform.business.entity.KnowledgeBase;
import com.agentplatform.business.entity.KbDocument;
import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.exception.BusinessException;
import com.agentplatform.business.repository.KnowledgeBaseRepository;
import com.agentplatform.business.repository.KbDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/knowledge-bases")
@RequiredArgsConstructor
public class KnowledgeBaseController {

    private final KnowledgeBaseRepository kbRepository;
    private final KbDocumentRepository documentRepository;

    @PostMapping
    public ApiResponse<KnowledgeBase> createKnowledgeBase(@RequestBody KnowledgeBase kb) {
        kb.setStatus("ACTIVE");
        kb.setDocCount(0);
        kb.setChunkCount(0);
        return ApiResponse.success(kbRepository.save(kb));
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> listKnowledgeBases(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<KnowledgeBase> kbs = kbRepository.findAll(PageRequest.of(page - 1, pageSize));
        Map<String, Object> result = new HashMap<>();
        result.put("total", kbs.getTotalElements());
        result.put("items", kbs.getContent());
        return ApiResponse.success(result);
    }

    @GetMapping("/{kbId}")
    public ApiResponse<KnowledgeBase> getKnowledgeBase(@PathVariable String kbId) {
        return ApiResponse.success(kbRepository.findById(kbId)
                .orElseThrow(() -> new BusinessException("知识库不存在")));
    }

    @PutMapping("/{kbId}")
    public ApiResponse<KnowledgeBase> updateKnowledgeBase(
            @PathVariable String kbId,
            @RequestBody KnowledgeBase body) {

        KnowledgeBase kb = kbRepository.findById(kbId)
                .orElseThrow(() -> new BusinessException("知识库不存在"));

        if (body.getName() != null) kb.setName(body.getName());
        if (body.getDescription() != null) kb.setDescription(body.getDescription());

        return ApiResponse.success(kbRepository.save(kb));
    }

    @DeleteMapping("/{kbId}")
    public ApiResponse<Void> deleteKnowledgeBase(@PathVariable String kbId) {
        kbRepository.deleteById(kbId);
        return ApiResponse.success();
    }

    @PostMapping("/{kbId}/documents")
    public ApiResponse<Map<String, String>> uploadDocument(@PathVariable String kbId) {
        // Mock: file upload handled by MultipartFile in production
        return ApiResponse.success(Map.of(
                "docId", "mock_doc_" + System.currentTimeMillis(),
                "status", "PROCESSING"
        ));
    }

    @GetMapping("/{kbId}/documents")
    public ApiResponse<Map<String, Object>> listDocuments(
            @PathVariable String kbId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<KbDocument> docs = documentRepository.findByKbId(kbId, PageRequest.of(page - 1, pageSize));
        Map<String, Object> result = new HashMap<>();
        result.put("total", docs.getTotalElements());
        result.put("items", docs.getContent());
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{kbId}/documents/{docId}")
    public ApiResponse<Void> deleteDocument(@PathVariable String kbId, @PathVariable String docId) {
        documentRepository.deleteById(docId);
        return ApiResponse.success();
    }

    @GetMapping("/{kbId}/documents/{docId}/chunks")
    public ApiResponse<Map<String, Object>> getDocumentChunks(
            @PathVariable String kbId,
            @PathVariable String docId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        // Mock chunk data
        return ApiResponse.success(Map.of(
                "total", 0,
                "items", java.util.List.of()
        ));
    }

    @PostMapping("/{kbId}/documents/{docId}/reparse")
    public ApiResponse<Map<String, String>> reparseDocument(
            @PathVariable String kbId, @PathVariable String docId) {
        return ApiResponse.success(Map.of("docId", docId, "status", "PROCESSING"));
    }

    @GetMapping("/{kbId}/documents/{docId}/download")
    public ApiResponse<Void> downloadDocument(
            @PathVariable String kbId, @PathVariable String docId) {
        // Mock: return file stream in production
        return ApiResponse.success();
    }

    @PostMapping("/{kbId}/import/gitlab")
    public ApiResponse<Map<String, Object>> importFromGitlab(
            @PathVariable String kbId, @RequestBody Map<String, Object> body) {
        // Mock: forward to RAG service
        return ApiResponse.success(Map.of(
                "importedCount", 5,
                "failedCount", 0,
                "status", "PROCESSING"
        ));
    }

    @PostMapping("/{kbId}/import/confluence")
    public ApiResponse<Map<String, Object>> importFromConfluence(
            @PathVariable String kbId, @RequestBody Map<String, Object> body) {
        // Mock: forward to RAG service
        return ApiResponse.success(Map.of(
                "importedCount", 12,
                "failedCount", 1,
                "status", "PROCESSING"
        ));
    }
}
