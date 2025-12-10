package com.sprint.mission.discodeit.domain.binaryContent.dto;

import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

}
