package com.agentplatform.business.repository;

import com.agentplatform.business.entity.ModelConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModelConfigRepository extends JpaRepository<ModelConfig, String> {

    Optional<ModelConfig> findByIsDefaultTrue();
}
