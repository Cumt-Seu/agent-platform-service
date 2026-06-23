package com.agentplatform.business.repository;

import com.agentplatform.business.entity.DiagnosisTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisTaskRepository extends JpaRepository<DiagnosisTask, String> {

    Page<DiagnosisTask> findByCreatedBy(String createdBy, Pageable pageable);

    Page<DiagnosisTask> findByStatus(String status, Pageable pageable);
}
