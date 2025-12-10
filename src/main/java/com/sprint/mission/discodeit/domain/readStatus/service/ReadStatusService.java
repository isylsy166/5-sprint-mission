package com.sprint.mission.discodeit.domain.readStatus.service;

import com.sprint.mission.discodeit.domain.readStatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.domain.readStatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.domain.readStatus.dto.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreateRequest request);

  ReadStatusDto find(UUID readStatusId);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request);

  void delete(UUID readStatusId);
}
