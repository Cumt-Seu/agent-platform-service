package com.agentplatform.business.repository;

import com.agentplatform.business.entity.FinetuneTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinetuneTaskRepository extends JpaRepository<FinetuneTask, String> {

    Page<FinetuneTask> findByCreatedBy(String createdBy, Pageable pageable);

    Page<FinetuneTask> findByStatus(String status, Pageable pageable);
}
