package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "skill_definition")
@EqualsAndHashCode(callSuper = true)
public class SkillDefinition extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "skill_id", length = 64)
    private String skillId;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "version", length = 32, nullable = false)
    private String version;

    @Column(name = "category", length = 32, nullable = false)
    private String category; // CODE_GEN / CODE_REVIEW / FAULT_DIAG / KNOWLEDGE_QA

    @Column(name = "input_schema", columnDefinition = "JSON", nullable = false)
    private String inputSchema;

    @Column(name = "output_schema", columnDefinition = "JSON", nullable = false)
    private String outputSchema;

    @Column(name = "execution_mode", length = 16, nullable = false)
    private String executionMode = "SYNC"; // SYNC / ASYNC

    @Column(name = "endpoint", length = 512, nullable = false)
    private String endpoint;

    @Column(name = "auth_type", length = 32)
    private String authType;

    @Column(name = "config", columnDefinition = "JSON")
    private String config;

    @Column(name = "status", length = 16, nullable = false)
    private String status = "ACTIVE";

    @Column(name = "created_by", length = 64)
    private String createdBy;
}
