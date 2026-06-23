package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "finetune_task")
@EqualsAndHashCode(callSuper = true)
public class FinetuneTask extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "task_id", length = 64)
    private String taskId;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "base_model", length = 128, nullable = false)
    private String baseModel;

    @Column(name = "dataset_id", length = 64, nullable = false)
    private String datasetId;

    @Column(name = "status", length = 16, nullable = false)
    private String status = "PENDING";

    @Column(name = "lora_config", columnDefinition = "JSON", nullable = false)
    private String loraConfig;

    @Column(name = "training_config", columnDefinition = "JSON", nullable = false)
    private String trainingConfig;

    @Column(name = "training_progress", columnDefinition = "JSON")
    private String trainingProgress;

    @Column(name = "training_result", columnDefinition = "JSON")
    private String trainingResult;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_by", length = 64)
    private String createdBy;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
