package com.agentplatform.business.repository;

import com.agentplatform.business.entity.AgentMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentMessageRepository extends JpaRepository<AgentMessage, String> {

    List<AgentMessage> findBySessionIdOrderByCreatedAtDesc(String sessionId);

    Page<AgentMessage> findBySessionIdOrderByCreatedAtDesc(String sessionId, Pageable pageable);

    long countBySessionId(String sessionId);
}
