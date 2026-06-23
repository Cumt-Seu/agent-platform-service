package com.agentplatform.business.controller;

import com.agentplatform.business.exception.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @GetMapping
    public ApiResponse<Map<String, Object>> globalSearch(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "ALL") String type) {
        // Mock: global search across Skills, KB docs, Sessions
        return ApiResponse.success(Map.of(
                "skills", List.of(
                        Map.of("id", "skill_1", "name", "代码生成", "description", "根据需求生成标准化代码", "type", "SKILL")
                ),
                "kbDocs", List.of(
                        Map.of("id", "doc_1", "name", "编码规范文档", "description", "行内编码规范", "type", "KB_DOC")
                ),
                "sessions", List.of(
                        Map.of("id", "sess_1", "name", "代码生成-用户注册接口", "description", "用户注册接口代码生成", "type", "SESSION")
                )
        ));
    }
}
