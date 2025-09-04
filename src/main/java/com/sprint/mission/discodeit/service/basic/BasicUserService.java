package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.local.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  //
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentStorage storage;

  @Override
  public User create(UserCreateRequest req, Optional<BinaryContentCreateRequest> fileData) {

    if (userRepository.existsByEmail(req.email())) {
      throw new IllegalArgumentException("User with email " + req.email() + " already exists");
    }
    if (userRepository.existsByUsername(req.username())) {
      throw new IllegalArgumentException("User with username " + req.username() + " already exists");
    }

    UUID nullableProfileId = fileData.map(file -> {
          BinaryContent binaryContent = new BinaryContent(
                file.fileName(),
                (long) file.bytes().length,
                file.contentType()
          );

          UUID fileId = binaryContentRepository.save(binaryContent).getId();
          storage.put(fileId, file.bytes());

          return fileId;
        })
        .orElse(null);

    User user = new User(req.username(), req.email(), req.password(), nullableProfileId);
    User createdUser = userRepository.save(user);

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(createdUser, now);
    userStatusRepository.save(userStatus);

    return createdUser;
  }

  @Override
  public User find(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public User update(UUID userId, UserUpdateRequest req, Optional<BinaryContentCreateRequest> fileData) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    if (userRepository.existsByEmail(req.newEmail())) {
      throw new IllegalArgumentException("User with email " + req.newEmail() + " already exists");
    }
    if (userRepository.existsByUsername(req.newUsername())) {
      throw new IllegalArgumentException("User with username " + req.newUsername() + " already exists");
    }

    UUID nullableProfileId = fileData.map(file -> {
          Optional.ofNullable(user.getProfileId())
              .ifPresent(binaryContentRepository::deleteById);

          BinaryContent binaryContent = new BinaryContent(
                  file.fileName(),
                  (long) file.bytes().length,
                  file.contentType()
          );

          storage.put(binaryContent.getId(), file.bytes());
          return binaryContentRepository.save(binaryContent).getId();
        })
        .orElse(null);

    String newPassword = req.newPassword();
    user.update(req.newUsername(), req.newEmail(), newPassword, nullableProfileId);

    return userRepository.save(user);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    Optional.ofNullable(user.getProfileId())
        .ifPresent(binaryContentRepository::deleteById);
    userStatusRepository.deleteById(userId);

    userRepository.deleteById(userId);
  }

  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId(),
        online
    );
  }
}
