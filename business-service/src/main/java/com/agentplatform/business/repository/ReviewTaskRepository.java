package com.agentplatform.business.repository;

import com.agentplatform.business.entity.ReviewTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewTaskRepository extends JpaRepository<ReviewTask, String> {

    Page<ReviewTask> findByCreatedBy(String createdBy, Pageable pageable);

    Page<ReviewTask> findByProjectId(String projectId, Pageable pageable);

    Page<ReviewTask> findByStatus(String status, Pageable pageable);
}
