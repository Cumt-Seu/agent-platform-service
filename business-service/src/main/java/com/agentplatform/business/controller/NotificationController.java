package com.agentplatform.business.controller;

import com.agentplatform.business.entity.Notification;
import com.agentplatform.business.exception.ApiResponse;
import com.agentplatform.business.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping
    public ApiResponse<Map<String, Object>> listNotifications(
            @RequestParam(required = false) Boolean unreadOnly,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<Notification> notifications;
        String userId = "mock_user_id";

        if (Boolean.TRUE.equals(unreadOnly)) {
            notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, pageSize));
        } else {
            notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page - 1, pageSize));
        }

        long unreadCount = notificationRepository.countByUserIdAndReadFalse(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("total", notifications.getTotalElements());
        result.put("unreadCount", unreadCount);
        result.put("items", notifications.getContent());
        return ApiResponse.success(result);
    }

    @PutMapping("/{notificationId}/read")
    public ApiResponse<Void> markAsRead(@PathVariable String notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
        return ApiResponse.success();
    }

    @PutMapping("/read-all")
    public ApiResponse<Void> markAllAsRead() {
        String userId = "mock_user_id";
        List<Notification> unread = notificationRepository.findByUserIdAndReadFalse(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
        return ApiResponse.success();
    }
}
