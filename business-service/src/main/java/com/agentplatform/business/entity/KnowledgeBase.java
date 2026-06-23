package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "knowledge_base")
@EqualsAndHashCode(callSuper = true)
public class KnowledgeBase extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "kb_id", length = 64)
    private String kbId;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "embedding_model", length = 64, nullable = false)
    private String embeddingModel;

    @Column(name = "chunk_strategy", length = 32, nullable = false)
    private String chunkStrategy = "SEMANTIC";

    @Column(name = "milvus_collection", length = 128, nullable = false)
    private String milvusCollection;

    @Column(name = "doc_count", nullable = false)
    private Integer docCount = 0;

    @Column(name = "chunk_count", nullable = false)
    private Integer chunkCount = 0;

    @Column(name = "status", length = 16, nullable = false)
    private String status = "ACTIVE";

    @Column(name = "created_by", length = 64)
    private String createdBy;
}
