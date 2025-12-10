package com.sprint.mission.discodeit.domain.message.dto;

import com.sprint.mission.discodeit.domain.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.domain.user.dto.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserDto author,
    List<BinaryContentDto> attachments
) {

}
