package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "skill_call_log")
public class SkillCallLog {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "log_id", length = 64)
    private String logId;

    @Column(name = "session_id", length = 64, nullable = false)
    private String sessionId;

    @Column(name = "plan_id", length = 64)
    private String planId;

    @Column(name = "skill_id", length = 64, nullable = false)
    private String skillId;

    @Column(name = "input_params", columnDefinition = "JSON", nullable = false)
    private String inputParams;

    @Column(name = "output_result", columnDefinition = "JSON")
    private String outputResult;

    @Column(name = "status", length = 16, nullable = false)
    private String status; // SUCCESS / FAILED / TIMEOUT

    @Column(name = "duration_ms")
    private Integer durationMs;

    @Column(name = "token_usage", columnDefinition = "JSON")
    private String tokenUsage;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
