package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "agent_session")
@EqualsAndHashCode(callSuper = true)
public class AgentSession extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "session_id", length = 64)
    private String sessionId;

    @Column(name = "user_id", length = 64, nullable = false)
    private String userId;

    @Column(name = "title", length = 256)
    private String title;

    @Column(name = "agent_type", length = 32, nullable = false)
    private String agentType; // CODE_GEN / CODE_REVIEW / FAULT_DIAG / KNOWLEDGE_QA

    @Column(name = "status", length = 16, nullable = false)
    private String status = "ACTIVE"; // ACTIVE / ARCHIVED

    @Column(name = "pinned")
    private Boolean pinned = false;

    @Column(name = "context", columnDefinition = "JSON")
    private String context;

    @Column(name = "last_message_at")
    private java.time.LocalDateTime lastMessageAt;
}
