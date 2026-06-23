package com.agentplatform.business.controller;

import com.agentplatform.business.entity.AgentSession;
import com.agentplatform.business.entity.AgentMessage;
import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.exception.BusinessException;
import com.agentplatform.business.repository.AgentSessionRepository;
import com.agentplatform.business.repository.AgentMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agent/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final AgentSessionRepository sessionRepository;
    private final AgentMessageRepository messageRepository;

    @GetMapping
    public ApiResponse<Map<String, Object>> listSessions(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "ACTIVE") String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<AgentSession> sessions = sessionRepository.findByUserIdOrderByPinnedDescLastMessageAtDesc(
                "mock_user_id", PageRequest.of(page - 1, pageSize));

        Map<String, Object> result = new HashMap<>();
        result.put("total", sessions.getTotalElements());
        result.put("items", sessions.getContent());
        return ApiResponse.success(result);
    }

    @GetMapping("/{sessionId}/messages")
    public ApiResponse<Map<String, Object>> getMessages(
            @PathVariable String sessionId,
            @RequestParam(required = false) String beforeId,
            @RequestParam(defaultValue = "50") int limit) {

        List<AgentMessage> messages = messageRepository.findBySessionIdOrderByCreatedAtDesc(sessionId);

        Map<String, Object> result = new HashMap<>();
        result.put("items", messages);
        result.put("hasMore", false);
        return ApiResponse.success(result);
    }

    @PutMapping("/{sessionId}")
    public ApiResponse<AgentSession> updateSession(
            @PathVariable String sessionId,
            @RequestBody Map<String, Object> body) {

        AgentSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException("会话不存在"));

        if (body.containsKey("title")) {
            session.setTitle((String) body.get("title"));
        }
        if (body.containsKey("pinned")) {
            session.setPinned((Boolean) body.get("pinned"));
        }

        sessionRepository.save(session);
        return ApiResponse.success(session);
    }

    @DeleteMapping("/{sessionId}")
    public ApiResponse<Map<String, Boolean>> deleteSession(@PathVariable String sessionId) {
        sessionRepository.deleteById(sessionId);
        return ApiResponse.success(Map.of("deleted", true));
    }
}
