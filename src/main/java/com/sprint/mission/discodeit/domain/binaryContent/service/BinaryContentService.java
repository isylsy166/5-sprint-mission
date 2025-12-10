package com.sprint.mission.discodeit.domain.binaryContent.service;

import com.sprint.mission.discodeit.domain.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binaryContent.dto.BinaryContentCreateRequest;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentDto create(BinaryContentCreateRequest request);

  BinaryContentDto find(UUID binaryContentId);

  List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);

  void delete(UUID binaryContentId);
}
