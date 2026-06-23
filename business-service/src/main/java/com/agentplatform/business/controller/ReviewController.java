package com.agentplatform.business.controller;

import com.agentplatform.business.entity.ReviewTask;
import com.agentplatform.business.entity.ReviewIssue;
import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.exception.BusinessException;
import com.agentplatform.business.repository.ReviewTaskRepository;
import com.agentplatform.business.repository.ReviewIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewTaskRepository reviewTaskRepository;
    private final ReviewIssueRepository reviewIssueRepository;

    @GetMapping("/tasks")
    public ApiResponse<Map<String, Object>> listReviewTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String projectId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<ReviewTask> tasks;
        if (projectId != null) {
            tasks = reviewTaskRepository.findByProjectId(projectId, PageRequest.of(page - 1, pageSize));
        } else if (status != null) {
            tasks = reviewTaskRepository.findByStatus(status, PageRequest.of(page - 1, pageSize));
        } else {
            tasks = reviewTaskRepository.findAll(PageRequest.of(page - 1, pageSize));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", tasks.getTotalElements());
        result.put("items", tasks.getContent());
        return ApiResponse.success(result);
    }

    @GetMapping("/tasks/{reviewId}")
    public ApiResponse<Map<String, Object>> getReviewTask(@PathVariable String reviewId) {
        ReviewTask task = reviewTaskRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException("评审任务不存在"));

        List<ReviewIssue> issues = reviewIssueRepository.findByReviewId(reviewId);

        Map<String, Object> result = new HashMap<>();
        result.put("task", task);
        result.put("issues", issues);
        return ApiResponse.success(result);
    }

    @PutMapping("/tasks/{reviewId}/issues/{issueId}")
    public ApiResponse<ReviewIssue> updateIssueStatus(
            @PathVariable String reviewId,
            @PathVariable String issueId,
            @RequestBody Map<String, String> body) {

        ReviewIssue issue = reviewIssueRepository.findById(issueId)
                .orElseThrow(() -> new BusinessException("评审问题不存在"));

        issue.setStatus(body.getOrDefault("status", issue.getStatus()));
        return ApiResponse.success(reviewIssueRepository.save(issue));
    }

    @PostMapping("/tasks/{reviewId}/issues/{issueId}/apply-fix")
    public ApiResponse<Map<String, Object>> applyFix(
            @PathVariable String reviewId,
            @PathVariable String issueId,
            @RequestBody Map<String, String> body) {
        // Mock: forward to GitLab API to create fix branch + MR
        return ApiResponse.success(Map.of(
                "issueId", issueId,
                "branchName", "fix/issue-" + issueId,
                "mrUrl", "https://gitlab.example.com/merge_requests/123",
                "status", "FIXED"
        ));
    }
}
