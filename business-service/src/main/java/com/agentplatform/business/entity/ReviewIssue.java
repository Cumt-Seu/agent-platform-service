package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "review_issue")
public class ReviewIssue {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "issue_id", length = 64)
    private String issueId;

    @Column(name = "review_id", length = 64, nullable = false)
    private String reviewId;

    @Column(name = "severity", length = 16, nullable = false)
    private String severity; // BLOCKER / MAJOR / MINOR / INFO

    @Column(name = "category", length = 32, nullable = false)
    private String category; // NAMING / SECURITY / PERFORMANCE / LOGIC / ARCH

    @Column(name = "file_path", length = 512, nullable = false)
    private String filePath;

    @Column(name = "start_line", nullable = false)
    private Integer startLine;

    @Column(name = "end_line")
    private Integer endLine;

    @Column(name = "code_snippet", columnDefinition = "TEXT")
    private String codeSnippet;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "suggestion", columnDefinition = "TEXT")
    private String suggestion;

    @Column(name = "fixed_code", columnDefinition = "TEXT")
    private String fixedCode;

    @Column(name = "status", length = 16, nullable = false)
    private String status = "OPEN";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
