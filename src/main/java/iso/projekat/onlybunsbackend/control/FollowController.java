package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.FollowStatusDto;
import iso.projekat.onlybunsbackend.security.CurrentUser;
import iso.projekat.onlybunsbackend.service.FollowRateLimiter;
import iso.projekat.onlybunsbackend.service.FollowService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService service;
    private final CurrentUser currentUser;

    public FollowController(FollowService service, CurrentUser currentUser) {
        this.service = service;
        this.currentUser = currentUser;
    }

    @PostMapping("/{targetId}")
    public ResponseEntity<FollowStatusDto> follow(@PathVariable Long targetId) {
        Long me = currentUser.id();
        return ResponseEntity.ok(service.follow(me, targetId));
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<FollowStatusDto> unfollow(@PathVariable Long targetId) {
        Long me = currentUser.id();
        return ResponseEntity.ok(service.unfollow(me, targetId));
    }

    @GetMapping("/status/{targetId}")
    public ResponseEntity<FollowStatusDto> status(@PathVariable Long targetId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            // nije ulogovan korisnik
            return ResponseEntity.status(403).build();
        }

        Long me = currentUser.id();
        try {
            return ResponseEntity.ok(service.status(me, targetId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @GetMapping("/{userId}/followers")
    public ResponseEntity<Page<Long>> followers(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.followers(userId, PageRequest.of(page, size)));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<Page<Long>> following(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.following(userId, PageRequest.of(page, size)));
    }

    @ExceptionHandler(FollowRateLimiter.RateLimitExceeded.class)
    public ResponseEntity<String> tooManyFollows(FollowRateLimiter.RateLimitExceeded e) {
        return ResponseEntity.status(429).body(e.getMessage());
    }
}

