package com.agentplatform.business.repository;

import com.agentplatform.business.entity.KnowledgeBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeBaseRepository extends JpaRepository<KnowledgeBase, String> {

    Page<KnowledgeBase> findByCreatedBy(String createdBy, Pageable pageable);

    Page<KnowledgeBase> findByStatus(String status, Pageable pageable);
}
