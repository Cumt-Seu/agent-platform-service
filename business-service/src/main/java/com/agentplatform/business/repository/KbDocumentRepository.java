package com.agentplatform.business.repository;

import com.agentplatform.business.entity.KbDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KbDocumentRepository extends JpaRepository<KbDocument, String> {

    Page<KbDocument> findByKbId(String kbId, Pageable pageable);
}
