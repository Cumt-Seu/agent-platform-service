package com.agentplatform.business.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notification_channel")
public class NotificationChannel {

    @Id
    @Column(name = "channel_type", length = 32)
    private String channelType; // EMAIL / WECHAT / DINGTALK / FEISHU

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Column(name = "config", columnDefinition = "JSON", nullable = false)
    private String config;
}
