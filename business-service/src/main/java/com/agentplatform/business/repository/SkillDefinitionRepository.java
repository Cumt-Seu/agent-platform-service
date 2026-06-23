package com.agentplatform.business.repository;

import com.agentplatform.business.entity.SkillDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillDefinitionRepository extends JpaRepository<SkillDefinition, String> {

    Page<SkillDefinition> findByCategory(String category, Pageable pageable);

    Page<SkillDefinition> findByStatus(String status, Pageable pageable);
}
