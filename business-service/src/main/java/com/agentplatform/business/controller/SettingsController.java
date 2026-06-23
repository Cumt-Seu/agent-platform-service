package com.agentplatform.business.controller;

import com.agentplatform.business.entity.ModelConfig;
import com.agentplatform.business.entity.NotificationChannel;
import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.exception.BusinessException;
import com.agentplatform.business.repository.ModelConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final ModelConfigRepository modelConfigRepository;

    // ---- 模型配置 ----

    @GetMapping("/models")
    public ApiResponse<List<ModelConfig>> listModels() {
        return ApiResponse.success(modelConfigRepository.findAll());
    }

    @PostMapping("/models")
    public ApiResponse<ModelConfig> createModel(@RequestBody ModelConfig model) {
        return ApiResponse.success(modelConfigRepository.save(model));
    }

    @PutMapping("/models/{modelId}")
    public ApiResponse<ModelConfig> updateModel(
            @PathVariable String modelId, @RequestBody ModelConfig body) {
        ModelConfig model = modelConfigRepository.findById(modelId)
                .orElseThrow(() -> new BusinessException("模型配置不存在"));
        if (body.getName() != null) model.setName(body.getName());
        if (body.getApiEndpoint() != null) model.setApiEndpoint(body.getApiEndpoint());
        if (body.getDeployType() != null) model.setDeployType(body.getDeployType());
        if (body.getMaxContextLength() != null) model.setMaxContextLength(body.getMaxContextLength());
        return ApiResponse.success(modelConfigRepository.save(model));
    }

    @DeleteMapping("/models/{modelId}")
    public ApiResponse<Void> deleteModel(@PathVariable String modelId) {
        modelConfigRepository.deleteById(modelId);
        return ApiResponse.success();
    }

    @PostMapping("/models/{modelId}/test")
    public ApiResponse<Map<String, Object>> testModelConnection(@PathVariable String modelId) {
        // Mock: test model connection
        return ApiResponse.success(Map.of("success", true, "latencyMs", 150));
    }

    @PutMapping("/models/{modelId}/default")
    public ApiResponse<Map<String, String>> setDefaultModel(@PathVariable String modelId) {
        // Mock: set as default model
        return ApiResponse.success(Map.of("modelId", modelId, "isDefault", "true"));
    }

    // ---- 代码模板 ----

    @GetMapping("/templates")
    public ApiResponse<List<Map<String, Object>>> listTemplates() {
        // Mock templates
        return ApiResponse.success(List.of(
                Map.of("templateId", "tpl_controller", "name", "SpringBoot Controller", "category", "CONTROLLER"),
                Map.of("templateId", "tpl_service", "name", "SpringBoot Service", "category", "SERVICE"),
                Map.of("templateId", "tpl_dao", "name", "MyBatis DAO", "category", "DAO")
        ));
    }

    // ---- MCP 工具 ----

    @GetMapping("/mcp-tools")
    public ApiResponse<List<Map<String, Object>>> listMcpTools() {
        // Mock MCP tools (read-only from MCP Server discovery)
        return ApiResponse.success(List.of(
                Map.of("toolName", "gitlab_get_merge_request", "description", "获取 GitLab MR 变更详情",
                        "server", "business-service", "status", "ACTIVE"),
                Map.of("toolName", "monitor_query_metrics", "description", "查询监控指标",
                        "server", "business-service", "status", "ACTIVE"),
                Map.of("toolName", "dev_platform_get_project", "description", "获取研发中台项目信息",
                        "server", "business-service", "status", "ACTIVE")
        ));
    }

    @GetMapping("/mcp-tools/{toolName}")
    public ApiResponse<Map<String, Object>> getMcpToolDetail(@PathVariable String toolName) {
        return ApiResponse.success(Map.of(
                "toolName", toolName,
                "description", "工具描述",
                "inputSchema", Map.of("type", "object", "properties", Map.of()),
                "server", "business-service",
                "status", "ACTIVE"
        ));
    }

    @PostMapping("/mcp-tools/{toolName}/test")
    public ApiResponse<Map<String, Object>> testMcpTool(@PathVariable String toolName) {
        // Mock test call
        return ApiResponse.success(Map.of("success", true, "result", "Mock test result"));
    }

    // ---- 通知渠道/规则 ----

    @GetMapping("/notification-channels")
    public ApiResponse<List<Map<String, Object>>> listNotificationChannels() {
        return ApiResponse.success(List.of(
                Map.of("channelType", "EMAIL", "enabled", false),
                Map.of("channelType", "WECHAT", "enabled", false),
                Map.of("channelType", "DINGTALK", "enabled", true),
                Map.of("channelType", "FEISHU", "enabled", false)
        ));
    }
}
