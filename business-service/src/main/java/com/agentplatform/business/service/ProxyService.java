package com.agentplatform.business.service;

import lombok.RequiredArgsConstructor;
import com.agentplatform.business.config.RestTemplateConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Proxy service for forwarding requests to Agent/RAG/GPU services.
 * Business service handles auth, then forwards to the appropriate Python service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyService {

    private final RestTemplate restTemplate;
    private final RestTemplateConfig restTemplateConfig;

    /**
     * Forward request to Agent service (Python/LangGraph)
     * Used for: POST /agent/chat, GET /agent/sessions/{id}/dag, POST /skills/{id}/invoke
     */
    public ResponseEntity<String> forwardToAgentService(String path, HttpMethod method,
                                                         Map<String, Object> body, String authToken) {
        String url = restTemplateConfig.getAgentServiceUrl() + "/api/v1" + path;
        return forwardRequest(url, method, body, authToken);
    }

    /**
     * Forward request to RAG service
     * Used for: POST /knowledge-bases/{id}/query, POST /knowledge-bases/{id}/documents/{id}/reparse
     */
    public ResponseEntity<String> forwardToRagService(String path, HttpMethod method,
                                                       Map<String, Object> body, String authToken) {
        String url = restTemplateConfig.getRagServiceUrl() + "/api/v1" + path;
        return forwardRequest(url, method, body, authToken);
    }

    /**
     * Forward request to GPU node
     * Used for: POST /finetune/tasks/{id}/start, POST /finetune/tasks/{id}/stop,
     *           GET /finetune/tasks/{id}/logs, GET /finetune/tasks/{id}/export
     */
    public ResponseEntity<String> forwardToGpuNode(String path, HttpMethod method,
                                                    Map<String, Object> body, String authToken) {
        String url = restTemplateConfig.getGpuNodeUrl() + "/api/v1" + path;
        return forwardRequest(url, method, body, authToken);
    }

    private ResponseEntity<String> forwardRequest(String url, HttpMethod method,
                                                    Map<String, Object> body, String authToken) {
        log.info("Forwarding {} {} to {}", method, url, url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authToken != null) {
            headers.set("Authorization", "Bearer " + authToken);
        }

        HttpEntity<Object> entity;
        if (body != null && (method == HttpMethod.POST || method == HttpMethod.PUT)) {
            entity = new HttpEntity<>(body, headers);
        } else {
            entity = new HttpEntity<>(headers);
        }

        return restTemplate.exchange(url, method, entity, String.class);
    }
}
