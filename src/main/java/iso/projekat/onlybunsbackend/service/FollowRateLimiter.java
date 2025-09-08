package iso.projekat.onlybunsbackend.service;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory leaky bucket: max 50 follow akcija po 60 sekundi po nalogu.
 * Nije distribuirano.
 */
@Component
public class FollowRateLimiter {

    private static final int LIMIT = 50;
    private static final long WINDOW_MS = Duration.ofMinutes(1).toMillis();

    private final Map<Long, Deque<Long>> buckets = new ConcurrentHashMap<>();

    public void assertWithinLimit(Long followerUserId) {
        long now = System.currentTimeMillis();
        Deque<Long> q = buckets.computeIfAbsent(followerUserId, k -> new ArrayDeque<>());

        synchronized (q) {
            // odstrani sve starije od 60s
            while (!q.isEmpty() && now - q.peekFirst() > WINDOW_MS) {
                q.pollFirst();
            }
            if (q.size() >= LIMIT) {
                throw new RateLimitExceeded("Premašeno 50 praćenja u minutu.");
            }
            q.addLast(now);
        }
    }

    public static class RateLimitExceeded extends RuntimeException {
        public RateLimitExceeded(String msg) { super(msg); }
    }
}
