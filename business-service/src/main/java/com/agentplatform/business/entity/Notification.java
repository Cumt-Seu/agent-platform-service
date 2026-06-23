package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "notification_id", length = 64)
    private String notificationId;

    @Column(name = "user_id", length = 64, nullable = false)
    private String userId;

    @Column(name = "type", length = 32, nullable = false)
    private String type; // REVIEW_COMPLETED / DIAGNOSIS_COMPLETED / TRAINING_COMPLETED / SYSTEM_ALERT

    @Column(name = "title", length = 256, nullable = false)
    private String title;

    @Column(name = "summary", length = 512)
    private String summary;

    @Column(name = "resource_id", length = 64)
    private String resourceId;

    @Column(name = "read", nullable = false)
    private Boolean read = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
