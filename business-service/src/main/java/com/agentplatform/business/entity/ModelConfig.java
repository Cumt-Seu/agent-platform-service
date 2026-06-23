package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "model_config")
@EqualsAndHashCode(callSuper = true)
public class ModelConfig extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "model_id", length = 64)
    private String modelId;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "deploy_type", length = 16, nullable = false)
    private String deployType; // PRIVATE / API

    @Column(name = "api_endpoint", length = 512, nullable = false)
    private String apiEndpoint;

    @Column(name = "api_key_encrypted", length = 256)
    private String apiKeyEncrypted;

    @Column(name = "max_context_length", nullable = false)
    private Integer maxContextLength = 8192;

    @Column(name = "default_temperature", precision = 3, scale = 2, nullable = false)
    private java.math.BigDecimal defaultTemperature = new java.math.BigDecimal("0.70");

    @Column(name = "status", length = 16, nullable = false)
    private String status = "ACTIVE";

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
}
