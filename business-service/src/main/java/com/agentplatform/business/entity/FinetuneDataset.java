package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "finetune_dataset")
@EqualsAndHashCode(callSuper = true)
public class FinetuneDataset extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "dataset_id", length = 64)
    private String datasetId;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "type", length = 32, nullable = false)
    private String type; // CODE_GEN / CODE_REVIEW / FAULT_DIAG

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "sample_count", nullable = false)
    private Integer sampleCount = 0;

    @Column(name = "status", length = 16, nullable = false)
    private String status = "BUILDING";

    @Column(name = "created_by", length = 64)
    private String createdBy;
}
