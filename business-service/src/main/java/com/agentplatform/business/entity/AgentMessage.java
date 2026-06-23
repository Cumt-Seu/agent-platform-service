package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "agent_message")
public class AgentMessage {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "message_id", length = 64)
    private String messageId;

    @Column(name = "session_id", length = 64, nullable = false)
    private String sessionId;

    @Column(name = "role", length = 16, nullable = false)
    private String role; // USER / ASSISTANT / SYSTEM / TOOL

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "tool_calls", columnDefinition = "JSON")
    private String toolCalls;

    @Column(name = "token_count")
    private Integer tokenCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
