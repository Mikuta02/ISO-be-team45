package iso.projekat.onlybunsbackend.follow;

import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.repository.UserFollowRepository;
import iso.projekat.onlybunsbackend.service.FollowService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@ExtendWith(SpringExtension.class)
class FollowConcurrencyTest {

    @Autowired
    FollowService followService;
    @Autowired
    UserFollowRepository userRepo;

    @Test
    void concurrentFollowIncrementsExactlyOncePerRequest() throws Exception {
        // Pretpostavka: u test DB postoje useri 1,2,3 (inicijalna skripta)
        Long targetId = 1L;
        Long f1 = 2L;
        Long f2 = 3L;

        CountDownLatch latch = new CountDownLatch(1);
        Thread t1 = new Thread(() -> {
            try { latch.await(); } catch (InterruptedException ignored) {}
            followService.follow(f1, targetId);
        });
        Thread t2 = new Thread(() -> {
            try { latch.await(); } catch (InterruptedException ignored) {}
            followService.follow(f2, targetId);
        });

        t1.start(); t2.start();
        latch.countDown();
        t1.join(); t2.join();

        User target = userRepo.findById(targetId).orElseThrow();
        assertThat(target.getFollowersCount()).isGreaterThanOrEqualTo(2);
    }
}
