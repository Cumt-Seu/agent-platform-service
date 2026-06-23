package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sys_permission")
public class SysPermission {

    @Id
    @Column(name = "permission_id", length = 64)
    private String permissionId;

    @Column(name = "resource", length = 32, nullable = false)
    private String resource;

    @Column(name = "action", length = 16, nullable = false)
    private String action; // read / write

    @Column(name = "description", length = 256)
    private String description;
}
