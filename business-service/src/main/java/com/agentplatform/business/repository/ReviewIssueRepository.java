package com.agentplatform.business.repository;

import com.agentplatform.business.entity.ReviewIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewIssueRepository extends JpaRepository<ReviewIssue, String> {

    List<ReviewIssue> findByReviewId(String reviewId);
}
