package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "username", length = 64, nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", length = 256, nullable = false)
    private String passwordHash;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "role", length = 32, nullable = false)
    private String role; // DEVELOPER / TESTER / ARCHITECT / OPS / MANAGER / ADMIN

    @Column(name = "department", length = 128)
    private String department;

    @Column(name = "status", length = 16, nullable = false)
    private String status = "ENABLED";

    @Column(name = "last_login_at")
    private java.time.LocalDateTime lastLoginAt;
}
