package com.sprint.mission.discodeit.domain.auth.service;

import com.sprint.mission.discodeit.domain.auth.dto.JwtInformation;
import com.sprint.mission.discodeit.domain.user.dto.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.RoleUpdateRequest;

public interface AuthService {

  UserDto updateRole(RoleUpdateRequest request);

  UserDto updateRoleInternal(RoleUpdateRequest request);

  JwtInformation refreshToken(String refreshToken);
}
