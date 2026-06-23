package com.agentplatform.business.controller;

import com.agentplatform.business.entity.SkillDefinition;
import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.exception.BusinessException;
import com.agentplatform.business.repository.SkillDefinitionRepository;
import com.agentplatform.business.repository.SkillCallLogRepository;
import com.agentplatform.business.entity.SkillCallLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillDefinitionRepository skillRepository;
    private final SkillCallLogRepository skillCallLogRepository;

    @PostMapping
    public ApiResponse<SkillDefinition> createSkill(@RequestBody SkillDefinition skill) {
        skill.setStatus("ACTIVE");
        SkillDefinition saved = skillRepository.save(skill);
        return ApiResponse.success(saved);
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> listSkills(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<SkillDefinition> skills;
        if (category != null) {
            skills = skillRepository.findByCategory(category, PageRequest.of(page - 1, pageSize));
        } else {
            skills = skillRepository.findAll(PageRequest.of(page - 1, pageSize));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", skills.getTotalElements());
        result.put("items", skills.getContent());
        return ApiResponse.success(result);
    }

    @GetMapping("/{skillId}")
    public ApiResponse<SkillDefinition> getSkill(@PathVariable String skillId) {
        SkillDefinition skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new BusinessException("Skill 不存在"));
        return ApiResponse.success(skill);
    }

    @PutMapping("/{skillId}")
    public ApiResponse<SkillDefinition> updateSkill(
            @PathVariable String skillId,
            @RequestBody SkillDefinition body) {

        SkillDefinition skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new BusinessException("Skill 不存在"));

        if (body.getName() != null) skill.setName(body.getName());
        if (body.getDescription() != null) skill.setDescription(body.getDescription());
        if (body.getCategory() != null) skill.setCategory(body.getCategory());
        if (body.getEndpoint() != null) skill.setEndpoint(body.getEndpoint());

        return ApiResponse.success(skillRepository.save(skill));
    }

    @DeleteMapping("/{skillId}")
    public ApiResponse<Void> deleteSkill(@PathVariable String skillId) {
        skillRepository.deleteById(skillId);
        return ApiResponse.success();
    }

    @GetMapping("/{skillId}/stats")
    public ApiResponse<Map<String, Object>> getSkillStats(@PathVariable String skillId) {
        // Mock stats data
        return ApiResponse.success(Map.of(
                "skillId", skillId,
                "totalCalls", 156,
                "successCount", 142,
                "failedCount", 14,
                "successRate", 0.91,
                "avgDurationMs", 1250,
                "p50DurationMs", 980,
                "p90DurationMs", 2100,
                "p99DurationMs", 3500,
                "callTrend", List.of(
                        Map.of("date", "2026-06-17", "count", 22),
                        Map.of("date", "2026-06-18", "count", 28),
                        Map.of("date", "2026-06-19", "count", 25)
                )
        ));
    }

    @GetMapping("/{skillId}/logs")
    public ApiResponse<Map<String, Object>> getSkillLogs(
            @PathVariable String skillId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<SkillCallLog> logs = skillCallLogRepository.findBySkillId(
                skillId, PageRequest.of(page - 1, pageSize));

        Map<String, Object> result = new HashMap<>();
        result.put("total", logs.getTotalElements());
        result.put("items", logs.getContent());
        return ApiResponse.success(result);
    }
}
