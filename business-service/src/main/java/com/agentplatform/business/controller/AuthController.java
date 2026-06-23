package com.agentplatform.business.controller;

import com.agentplatform.business.dto.LoginRequest;
import com.agentplatform.business.dto.LoginResponse;
import com.agentplatform.business.dto.RefreshRequest;
import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.exception.BusinessException;
import com.agentplatform.business.security.JwtTokenProvider;
import com.agentplatform.business.entity.SysUser;
import com.agentplatform.business.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        SysUser user = sysUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(401, "用户名或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUserId(), user.getUsername(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setRole(user.getRole());
        response.setPermissions(List.of("chat:read", "skill:read", "skill:write")); // Mock permissions

        return ApiResponse.success(response);
    }

    @PostMapping("/refresh")
    public ApiResponse<Map<String, String>> refresh(@RequestBody RefreshRequest request) {
        // Mock: validate refresh token and generate new access token
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new BusinessException(401, "Refresh token 无效");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(request.getRefreshToken());
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, "mock_user", "DEVELOPER");

        return ApiResponse.success(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        // Mock: invalidate token (in production, add to blacklist in Redis)
        return ApiResponse.success();
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me() {
        // Mock: return current user info and permissions
        return ApiResponse.success(Map.of(
                "userId", "mock_user_id",
                "username", "mock_user",
                "name", "模拟用户",
                "role", "DEVELOPER",
                "permissions", List.of("chat:read", "chat:write", "skill:read", "skill:write",
                        "knowledge:read", "knowledge:write", "review:read", "review:write",
                        "diagnosis:read", "diagnosis:write", "metrics:read", "settings:read")
        ));
    }
}
