package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "diagnosis_task")
@EqualsAndHashCode(callSuper = true)
public class DiagnosisTask extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "diagnosis_id", length = 64)
    private String diagnosisId;

    @Column(name = "session_id", length = 64)
    private String sessionId;

    @Column(name = "service_name", length = 128)
    private String serviceName;

    @Column(name = "log_content", columnDefinition = "TEXT", nullable = false)
    private String logContent;

    @Column(name = "status", length = 16, nullable = false)
    private String status = "RUNNING";

    @Column(name = "result", columnDefinition = "JSON")
    private String result;

    @Column(name = "created_by", length = 64)
    private String createdBy;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
