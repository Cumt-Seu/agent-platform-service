package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "kb_document")
@EqualsAndHashCode(callSuper = true)
public class KbDocument extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "doc_id", length = 64)
    private String docId;

    @Column(name = "kb_id", length = 64, nullable = false)
    private String kbId;

    @Column(name = "title", length = 256, nullable = false)
    private String title;

    @Column(name = "source", length = 512, nullable = false)
    private String source;

    @Column(name = "doc_type", length = 32, nullable = false)
    private String docType; // 规范/架构/接口/故障案例

    @Column(name = "format", length = 16, nullable = false)
    private String format; // PDF/WORD/MARKDOWN/CONFLUENCE

    @Column(name = "file_path", length = 512)
    private String filePath;

    @Column(name = "chunk_count", nullable = false)
    private Integer chunkCount = 0;

    @Column(name = "status", length = 16, nullable = false)
    private String status = "PROCESSING";
}
