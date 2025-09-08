package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.FollowStatusDto;
import iso.projekat.onlybunsbackend.model.Follow;
import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.repository.FollowRepository;
import iso.projekat.onlybunsbackend.repository.UserFollowRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    private final FollowRepository followRepo;
    private final UserFollowRepository userRepo;
    private final FollowRateLimiter limiter;

    public FollowService(FollowRepository followRepo,
                         UserFollowRepository userRepo,
                         FollowRateLimiter limiter) {
        this.followRepo = followRepo;
        this.userRepo = userRepo;
        this.limiter = limiter;
    }

    @Transactional
    public FollowStatusDto follow(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("Nalog ne može da zaprati sam sebe.");
        }

        limiter.assertWithinLimit(followerId);

        if (followRepo.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            long cnt = followRepo.countByFolloweeId(followeeId);
            return new FollowStatusDto(followeeId, true, cnt);
        }

        // Zaključamo target korisnika da pravilno inkrementujemo brojač pod konkurencijom
        User target = userRepo.findByIdForUpdate(followeeId)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji: " + followeeId));

        try {
            followRepo.save(new Follow(followerId, followeeId));
        } catch (DataIntegrityViolationException e) {
            // Jedinstveni constraint već uzeo trku – idempotentno
        }

        target.setFollowersCount(target.getFollowersCount() + 1);
        userRepo.save(target);

        long cnt = target.getFollowersCount();
        return new FollowStatusDto(followeeId, true, cnt);
    }

    @Transactional
    public FollowStatusDto unfollow(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("Nalog ne može da otprati sam sebe.");
        }

        if (followRepo.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            // Zaključamo target, dekrement je bezbedan
            User target = userRepo.findByIdForUpdate(followeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji: " + followeeId));

            followRepo.deleteByFollowerIdAndFolloweeId(followerId, followeeId);

            long newCnt = Math.max(0, target.getFollowersCount() - 1);
            target.setFollowersCount(newCnt);
            userRepo.save(target);
        }

        long cnt = followRepo.countByFolloweeId(followeeId);
        return new FollowStatusDto(followeeId, false, cnt);
    }

    @Transactional(readOnly = true)
    public FollowStatusDto status(Long requesterId, Long targetId) {
        boolean following = followRepo.existsByFollowerIdAndFolloweeId(requesterId, targetId);
        long cnt = followRepo.countByFolloweeId(targetId);
        return new FollowStatusDto(targetId, following, cnt);
    }

    @Transactional(readOnly = true)
    public Page<Long> followers(Long userId, Pageable pageable) {
        // vraćamo listu followerId-eva, da se ne zavisimo od vaše User šeme
        return followRepo.findByFolloweeId(userId, pageable)
                .map(Follow::getFollowerId);
    }

    @Transactional(readOnly = true)
    public Page<Long> following(Long userId, Pageable pageable) {
        return followRepo.findByFollowerId(userId, pageable)
                .map(Follow::getFolloweeId);
    }
}
