package iso.projekat.onlybunsbackend.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class MonitoringService {
    private MeterRegistry meterRegistry;

    private final Timer postCreationTimer;
    private final ConcurrentHashMap<String, Integer> activeUsers;

    @Autowired
    public MonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.postCreationTimer = meterRegistry.timer("http.requests.create_post.duration");
        this.activeUsers = new ConcurrentHashMap<>();
    }

    public void recordPostCreationTime(long durationMillis) {
        postCreationTimer.record(durationMillis, TimeUnit.MILLISECONDS);
    }

    public void updateActiveUsers(String username, boolean isActive) {
        if (isActive) {
            activeUsers.put(username, 1);
        } else {
            activeUsers.remove(username);
        }
        meterRegistry.gauge("active_users.count", activeUsers, ConcurrentHashMap::size);
    }
}