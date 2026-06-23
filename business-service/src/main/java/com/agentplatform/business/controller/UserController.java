package com.agentplatform.business.controller;

import com.agentplatform.business.entity.SysUser;
import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.exception.BusinessException;
import com.agentplatform.business.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ApiResponse<SysUser> createUser(@RequestBody SysUser user) {
        if (sysUserRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        user.setPasswordHash(passwordEncoder.encode("default_password"));
        user.setStatus("ENABLED");
        return ApiResponse.success(sysUserRepository.save(user));
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        var users = sysUserRepository.findAll(org.springframework.data.domain.PageRequest.of(page - 1, pageSize));
        Map<String, Object> result = new HashMap<>();
        result.put("total", users.getTotalElements());
        result.put("items", users.getContent());
        return ApiResponse.success(result);
    }

    @GetMapping("/{userId}")
    public ApiResponse<SysUser> getUser(@PathVariable String userId) {
        return ApiResponse.success(sysUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在")));
    }

    @PutMapping("/{userId}")
    public ApiResponse<SysUser> updateUser(@PathVariable String userId, @RequestBody SysUser body) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (body.getName() != null) user.setName(body.getName());
        if (body.getEmail() != null) user.setEmail(body.getEmail());
        if (body.getRole() != null) user.setRole(body.getRole());
        if (body.getDepartment() != null) user.setDepartment(body.getDepartment());
        if (body.getStatus() != null) user.setStatus(body.getStatus());
        return ApiResponse.success(sysUserRepository.save(user));
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable String userId) {
        sysUserRepository.deleteById(userId);
        return ApiResponse.success();
    }

    @PutMapping("/{userId}/reset-password")
    public ApiResponse<Void> resetPassword(@PathVariable String userId, @RequestBody Map<String, String> body) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setPasswordHash(passwordEncoder.encode(body.get("newPassword")));
        sysUserRepository.save(user);
        return ApiResponse.success();
    }
}
