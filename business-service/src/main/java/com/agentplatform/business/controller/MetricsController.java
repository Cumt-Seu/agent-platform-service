package com.agentplatform.business.controller;

import com.agentplatform.business.exception.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> getOverview() {
        // Mock metrics overview
        return ApiResponse.success(Map.of(
                "totalSessions", 1580,
                "totalSkillCalls", 8920,
                "avgResponseTimeMs", 1800,
                "skillSuccessRate", 0.94,
                "reviewCoverageRate", 0.85,
                "diagnosisAvgTimeMin", 12.5,
                "topSkills", List.of(
                        Map.of("skillId", "code_gen", "name", "代码生成", "calls", 3200, "successRate", 0.92),
                        Map.of("skillId", "code_review", "name", "代码评审", "calls", 2800, "successRate", 0.95),
                        Map.of("skillId", "fault_diag", "name", "故障排障", "calls", 1200, "successRate", 0.88),
                        Map.of("skillId", "knowledge_qa", "name", "知识库问答", "calls", 1720, "successRate", 0.96)
                )
        ));
    }

    @GetMapping("/trends")
    public ApiResponse<Map<String, Object>> getTrends(
            @RequestParam(defaultValue = "SEVEN_DAYS") String timeRange,
            @RequestParam(defaultValue = "SKILL_CALLS") String type) {
        // Mock trend data
        return ApiResponse.success(Map.of(
                "timeRange", timeRange,
                "type", type,
                "data", List.of(
                        Map.of("date", "2026-06-17", "value", 130),
                        Map.of("date", "2026-06-18", "value", 145),
                        Map.of("date", "2026-06-19", "value", 160),
                        Map.of("date", "2026-06-20", "value", 155),
                        Map.of("date", "2026-06-21", "value", 170),
                        Map.of("date", "2026-06-22", "value", 165),
                        Map.of("date", "2026-06-23", "value", 180)
                )
        ));
    }
}
