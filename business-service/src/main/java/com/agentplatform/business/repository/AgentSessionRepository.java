package com.agentplatform.business.repository;

import com.agentplatform.business.entity.AgentSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentSessionRepository extends JpaRepository<AgentSession, String> {

    Page<AgentSession> findByUserIdAndStatus(String userId, String status, Pageable pageable);

    @Query("SELECT s FROM AgentSession s WHERE s.userId = :userId AND s.status = :status AND s.title LIKE %:keyword%")
    Page<AgentSession> findByUserIdAndStatusAndKeyword(@Param("userId") String userId,
                                                        @Param("status") String status,
                                                        @Param("keyword") String keyword,
                                                        Pageable pageable);

    Page<AgentSession> findByUserIdOrderByPinnedDescLastMessageAtDesc(String userId, Pageable pageable);
}
