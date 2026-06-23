package com.agentplatform.business.repository;

import com.agentplatform.business.entity.FinetuneDataset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinetuneDatasetRepository extends JpaRepository<FinetuneDataset, String> {

    Page<FinetuneDataset> findByCreatedBy(String createdBy, Pageable pageable);
}
