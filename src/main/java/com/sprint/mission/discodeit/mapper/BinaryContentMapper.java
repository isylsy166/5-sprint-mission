package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.domain.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binaryContent.entity.BinaryContent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  BinaryContentDto toDto(BinaryContent binaryContent);
}
