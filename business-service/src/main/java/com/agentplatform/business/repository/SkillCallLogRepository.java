package com.agentplatform.business.repository;

import com.agentplatform.business.entity.SkillCallLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillCallLogRepository extends JpaRepository<SkillCallLog, String> {

    Page<SkillCallLog> findBySkillId(String skillId, Pageable pageable);

    Page<SkillCallLog> findBySessionId(String sessionId, Pageable pageable);
}
