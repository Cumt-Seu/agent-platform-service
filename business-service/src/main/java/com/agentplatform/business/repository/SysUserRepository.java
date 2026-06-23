package com.agentplatform.business.repository;

import com.agentplatform.business.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, String> {

    Optional<SysUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
