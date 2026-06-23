package com.agentplatform.business.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String username;
    private String name;
    private String role;
    private List<String> permissions;
}
