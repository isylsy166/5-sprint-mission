package com.sprint.mission.discodeit.domain.auth.dto;

import com.sprint.mission.discodeit.domain.user.dto.UserDto;

public record JwtDto(
    UserDto userDto,
    String accessToken
) {
}