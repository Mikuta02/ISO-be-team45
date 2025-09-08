-- Kreiramo tabelu follow-a sa unikatnim (follower_id, followee_id)
CREATE TABLE IF NOT EXISTS follows (
                                       id BIGSERIAL PRIMARY KEY,
                                       follower_id BIGINT NOT NULL,
                                       followee_id BIGINT NOT NULL,
                                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                                       CONSTRAINT uq_follows UNIQUE (follower_id, followee_id),
                                       CONSTRAINT fk_follows_follower FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
                                       CONSTRAINT fk_follows_followee FOREIGN KEY (followee_id) REFERENCES users(id) ON DELETE CASCADE,
                                       CONSTRAINT chk_not_self_follow CHECK (follower_id <> followee_id)
);

-- Brze pretrage
CREATE INDEX IF NOT EXISTS idx_follows_follower ON follows(follower_id);
CREATE INDEX IF NOT EXISTS idx_follows_followee ON follows(followee_id);

-- Ako nemate kolonu followers_count na users, dodajemo je
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS followers_count BIGINT NOT NULL DEFAULT 0;

-- Konzistentnost: poravnaj brojač sa realnim stanjem (idempotentno)
UPDATE users u
SET followers_count = COALESCE(x.cnt, 0)
FROM (SELECT followee_id AS uid, COUNT(*) AS cnt FROM follows GROUP BY followee_id) x
WHERE u.id = x.uid;

