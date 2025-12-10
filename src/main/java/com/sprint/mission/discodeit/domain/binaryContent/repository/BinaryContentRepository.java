package com.sprint.mission.discodeit.domain.binaryContent.repository;

import com.sprint.mission.discodeit.domain.binaryContent.entity.BinaryContent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

}
