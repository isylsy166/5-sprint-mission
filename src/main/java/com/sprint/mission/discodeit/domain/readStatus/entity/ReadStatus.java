package com.sprint.mission.discodeit.domain.readStatus.entity;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.global.entity.BaseUpdatableEntity;
import jakarta.persistence.*;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "read_statuses",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "channel_id"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", columnDefinition = "uuid")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", columnDefinition = "uuid")
  private Channel channel;

  @Column(columnDefinition = "timestamp with time zone", nullable = false)
  private Instant lastReadAt;

  @Column(nullable = false)
  private boolean notification_enabled;

  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public void update(Instant newLastReadAt) {
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
    }
  }

  @PrePersist
  public void prePersist() {
    if(this.channel != null) {
      if (this.channel.getType() == ChannelType.PRIVATE) {
        this.notification_enabled = true;
      } else if (this.channel.getType() == ChannelType.PUBLIC) {
        this.notification_enabled = false;
      }
    }
  }
}