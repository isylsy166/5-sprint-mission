-- 먼저 FK 참조될 테이블 생성

-- BINARY CONTENTS
CREATE TABLE binary_contents (
                                 id UUID PRIMARY KEY,
                                 created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                 file_name VARCHAR(255) NOT NULL,
                                 size BIGINT NOT NULL,
                                 content_type VARCHAR(100) NOT NULL,
                                 bytes BYTEA
);

-- USERS (profile_id → binary_contents.id)
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       username VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(60) NOT NULL,
                       profile_id UUID,
                       CONSTRAINT fk_users_profile FOREIGN KEY (profile_id)
                           REFERENCES binary_contents (id)
                           ON DELETE SET NULL
);

-- CHANNELS
CREATE TABLE channels (
                          id UUID PRIMARY KEY,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          name VARCHAR(100) NOT NULL,
                          description VARCHAR(500),
                          type VARCHAR(10) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- MESSAGES (channel_id → channels.id, author_id → users.id)
CREATE TABLE messages (
                          id UUID PRIMARY KEY,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          content TEXT,
                          channel_id UUID NOT NULL,
                          author_id UUID,
                          CONSTRAINT fk_messages_channel FOREIGN KEY (channel_id)
                              REFERENCES channels (id)
                              ON DELETE CASCADE,
                          CONSTRAINT fk_messages_author FOREIGN KEY (author_id)
                              REFERENCES users (id)
                              ON DELETE SET NULL
);

-- MESSAGE ATTACHMENTS
CREATE TABLE message_attachments (
                                     message_id UUID NOT NULL,
                                     attachment_id UUID NOT NULL,
                                     PRIMARY KEY (message_id, attachment_id),
                                     CONSTRAINT fk_msgatt_message FOREIGN KEY (message_id)
                                         REFERENCES messages (id)
                                         ON DELETE CASCADE,
                                     CONSTRAINT fk_msgatt_attachment FOREIGN KEY (attachment_id)
                                         REFERENCES binary_contents (id)
                                         ON DELETE CASCADE
);

-- USER STATUSES
CREATE TABLE user_statuses (
                               id UUID PRIMARY KEY,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               user_id UUID NOT NULL UNIQUE,
                               last_active_at TIMESTAMPTZ NOT NULL,
                               CONSTRAINT fk_user_status_user FOREIGN KEY (user_id)
                                   REFERENCES users (id)
                                   ON DELETE CASCADE
);

-- READ STATUSES
CREATE TABLE read_statuses (
                               id UUID PRIMARY KEY,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               user_id UUID NOT NULL,
                               channel_id UUID NOT NULL,
                               last_read_at TIMESTAMPTZ NOT NULL,
                               CONSTRAINT fk_readstatus_user FOREIGN KEY (user_id)
                                   REFERENCES users (id)
                                   ON DELETE CASCADE,
                               CONSTRAINT fk_readstatus_channel FOREIGN KEY (channel_id)
                                   REFERENCES channels (id)
                                   ON DELETE CASCADE,
                               CONSTRAINT uq_readstatus_user_channel UNIQUE (user_id, channel_id)
);
