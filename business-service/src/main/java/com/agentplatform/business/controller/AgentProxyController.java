package com.agentplatform.business.controller;

import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.service.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Proxy controller that forwards requests to Agent service (Python/LangGraph).
 * Business service handles JWT auth, then forwards the request.
 */
@RestController
@RequiredArgsConstructor
public class AgentProxyController {

    private final ProxyService proxyService;

    /**
     * Proxy: POST /agent/chat -> Agent service
     * SSE streaming is handled by Agent service directly
     */
    @PostMapping("/agent/chat")
    public ResponseEntity<String> chat(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        String authToken = extractToken(request);
        return proxyService.forwardToAgentService("/agent/chat", HttpMethod.POST, body, authToken);
    }

    /**
     * Proxy: GET /agent/sessions/{sessionId}/dag -> Agent service
     */
    @GetMapping("/agent/sessions/{sessionId}/dag")
    public ResponseEntity<String> getDagState(@PathVariable String sessionId,
                                               @RequestParam(defaultValue = "LATEST") String snapshot,
                                               HttpServletRequest request) {
        String authToken = extractToken(request);
        Map<String, Object> params = new HashMap<>();
        params.put("snapshot", snapshot);
        return proxyService.forwardToAgentService(
                "/agent/sessions/" + sessionId + "/dag?snapshot=" + snapshot,
                HttpMethod.GET, null, authToken);
    }

    /**
     * Proxy: POST /skills/{skillId}/invoke -> Agent service
     */
    @PostMapping("/skills/{skillId}/invoke")
    public ResponseEntity<String> invokeSkill(@PathVariable String skillId,
                                               @RequestBody Map<String, Object> body,
                                               HttpServletRequest request) {
        String authToken = extractToken(request);
        return proxyService.forwardToAgentService(
                "/skills/" + skillId + "/invoke", HttpMethod.POST, body, authToken);
    }

    /**
     * Proxy: POST /review/submit -> Agent service
     */
    @PostMapping("/review/submit")
    public ResponseEntity<String> submitReview(@RequestBody Map<String, Object> body,
                                                HttpServletRequest request) {
        String authToken = extractToken(request);
        return proxyService.forwardToAgentService("/review/submit", HttpMethod.POST, body, authToken);
    }

    /**
     * Proxy: POST /diagnosis/analyze -> Agent service
     */
    @PostMapping("/diagnosis/analyze")
    public ResponseEntity<String> analyzeDiagnosis(@RequestBody Map<String, Object> body,
                                                     HttpServletRequest request) {
        String authToken = extractToken(request);
        return proxyService.forwardToAgentService("/diagnosis/analyze", HttpMethod.POST, body, authToken);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
