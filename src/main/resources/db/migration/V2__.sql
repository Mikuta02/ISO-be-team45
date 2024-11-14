CREATE TABLE likes
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id BIGINT                                  NOT NULL,
    post_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (id)
);

ALTER TABLE likes
    ADD CONSTRAINT uc_88d5108ea5e46ca11e828b43e UNIQUE (user_id, post_id);

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE INDEX idx_users_username ON users (username);