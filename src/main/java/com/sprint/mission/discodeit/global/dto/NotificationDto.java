package com.sprint.mission.discodeit.global.dto;

import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
      UUID uuid,
      Instant createdAt,
      UUID receiverId,                    // 알림을 수신할 User의 id
      String title,
      String content
) {
}
