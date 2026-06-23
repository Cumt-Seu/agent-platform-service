package com.agentplatform.business.controller;

import com.agentplatform.business.entity.DiagnosisTask;
import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.exception.BusinessException;
import com.agentplatform.business.repository.DiagnosisTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/diagnosis")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisTaskRepository diagnosisTaskRepository;

    @GetMapping("/tasks")
    public ApiResponse<Map<String, Object>> listDiagnosisTasks(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<DiagnosisTask> tasks;
        if (status != null) {
            tasks = diagnosisTaskRepository.findByStatus(status, PageRequest.of(page - 1, pageSize));
        } else {
            tasks = diagnosisTaskRepository.findAll(PageRequest.of(page - 1, pageSize));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", tasks.getTotalElements());
        result.put("items", tasks.getContent());
        return ApiResponse.success(result);
    }

    @GetMapping("/tasks/{diagnosisId}")
    public ApiResponse<DiagnosisTask> getDiagnosisTask(@PathVariable String diagnosisId) {
        return ApiResponse.success(diagnosisTaskRepository.findById(diagnosisId)
                .orElseThrow(() -> new BusinessException("排障任务不存在")));
    }

    @DeleteMapping("/tasks/{diagnosisId}")
    public ApiResponse<Map<String, Boolean>> deleteDiagnosisTask(@PathVariable String diagnosisId) {
        diagnosisTaskRepository.deleteById(diagnosisId);
        return ApiResponse.success(Map.of("deleted", true));
    }
}
