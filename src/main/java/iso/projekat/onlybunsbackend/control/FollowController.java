package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@AllArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/follow/{followerId}/{followeeId}")
    public String followUser(@PathVariable Long followerId, @PathVariable Long followeeId) {
        return followService.followUser(followerId, followeeId);
    }

    @DeleteMapping("/follow/{followerId}/{followeeId}")
    public String unfollowUser(@PathVariable Long followerId, @PathVariable Long followeeId) {
        return followService.unfollowUser(followerId, followeeId);
    }
}
