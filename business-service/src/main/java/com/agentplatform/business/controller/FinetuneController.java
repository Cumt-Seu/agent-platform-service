package com.agentplatform.business.controller;

import com.agentplatform.business.entity.FinetuneTask;
import com.agentplatform.business.entity.FinetuneDataset;
import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.exception.BusinessException;
import com.agentplatform.business.repository.FinetuneTaskRepository;
import com.agentplatform.business.repository.FinetuneDatasetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/finetune")
@RequiredArgsConstructor
public class FinetuneController {

    private final FinetuneTaskRepository finetuneTaskRepository;
    private final FinetuneDatasetRepository finetuneDatasetRepository;

    // ---- 微调任务 ----

    @PostMapping("/tasks")
    public ApiResponse<FinetuneTask> createTask(@RequestBody FinetuneTask task) {
        task.setStatus("PENDING");
        return ApiResponse.success(finetuneTaskRepository.save(task));
    }

    @GetMapping("/tasks")
    public ApiResponse<Map<String, Object>> listTasks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<FinetuneTask> tasks = finetuneTaskRepository.findAll(PageRequest.of(page - 1, pageSize));
        Map<String, Object> result = new HashMap<>();
        result.put("total", tasks.getTotalElements());
        result.put("items", tasks.getContent());
        return ApiResponse.success(result);
    }

    @GetMapping("/tasks/{taskId}")
    public ApiResponse<FinetuneTask> getTask(@PathVariable String taskId) {
        return ApiResponse.success(finetuneTaskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("微调任务不存在")));
    }

    @PutMapping("/tasks/{taskId}")
    public ApiResponse<FinetuneTask> updateTask(
            @PathVariable String taskId, @RequestBody FinetuneTask body) {
        FinetuneTask task = finetuneTaskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("微调任务不存在"));
        if (body.getName() != null) task.setName(body.getName());
        if (body.getLoraConfig() != null) task.setLoraConfig(body.getLoraConfig());
        return ApiResponse.success(finetuneTaskRepository.save(task));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ApiResponse<Void> deleteTask(@PathVariable String taskId) {
        finetuneTaskRepository.deleteById(taskId);
        return ApiResponse.success();
    }

    // ---- 数据集 ----

    @PostMapping("/datasets")
    public ApiResponse<FinetuneDataset> createDataset(@RequestBody FinetuneDataset dataset) {
        dataset.setSampleCount(0);
        dataset.setStatus("BUILDING");
        return ApiResponse.success(finetuneDatasetRepository.save(dataset));
    }

    @GetMapping("/datasets")
    public ApiResponse<Map<String, Object>> listDatasets(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<FinetuneDataset> datasets = finetuneDatasetRepository.findAll(PageRequest.of(page - 1, pageSize));
        Map<String, Object> result = new HashMap<>();
        result.put("total", datasets.getTotalElements());
        result.put("items", datasets.getContent());
        return ApiResponse.success(result);
    }

    @GetMapping("/datasets/{datasetId}")
    public ApiResponse<FinetuneDataset> getDataset(@PathVariable String datasetId) {
        return ApiResponse.success(finetuneDatasetRepository.findById(datasetId)
                .orElseThrow(() -> new BusinessException("数据集不存在")));
    }

    @DeleteMapping("/datasets/{datasetId}")
    public ApiResponse<Void> deleteDataset(@PathVariable String datasetId) {
        finetuneDatasetRepository.deleteById(datasetId);
        return ApiResponse.success();
    }

    @GetMapping("/datasets/{datasetId}/samples")
    public ApiResponse<Map<String, Object>> getDatasetSamples(
            @PathVariable String datasetId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        // Mock sample data
        return ApiResponse.success(Map.of("total", 0, "items", List.of()));
    }
}
