package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "review_task")
@EqualsAndHashCode(callSuper = true)
public class ReviewTask extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "review_id", length = 64)
    private String reviewId;

    @Column(name = "session_id", length = 64)
    private String sessionId;

    @Column(name = "mr_id", length = 64)
    private String mrId;

    @Column(name = "mr_title", length = 256)
    private String mrTitle;

    @Column(name = "project_id", length = 64, nullable = false)
    private String projectId;

    @Column(name = "reviewer_type", length = 16, nullable = false)
    private String reviewerType = "AI";

    @Column(name = "status", length = 16, nullable = false)
    private String status = "PENDING";

    @Column(name = "summary", columnDefinition = "JSON")
    private String summary;

    @Column(name = "created_by", length = 64)
    private String createdBy;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
