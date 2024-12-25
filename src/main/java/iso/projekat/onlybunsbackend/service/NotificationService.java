package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import iso.projekat.onlybunsbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private EmailService emailService;

    @Scheduled(cron = "0 0 0 * * ?") // Schedules the task to run daily at midnight
    public void notifyInactiveUsers() {
        Instant sevenDaysAgo = Instant.now().minusSeconds(7L * 24 * 3600);
        List<User> inactiveUsers = userRepository.findInactiveSince(sevenDaysAgo);

        inactiveUsers.forEach(user -> {
            String subject = "We miss you on OnlyBuns!";
            String body = generateNotificationBody(user);
            emailService.sendEmail(user.getEmail(), subject, body);
        });
    }

    private String generateNotificationBody(User user) {
        long newFollowers = userRepository.countNewFollowers(user.getId(), Instant.now().minusSeconds(7L * 24 * 3600));
        long newLikes = postRepository.countLikesOnUserPosts(user.getId(), Instant.now().minusSeconds(7L * 24 * 3600));
        long newPosts = postRepository.countPostsByUserSince(user.getId(), Instant.now().minusSeconds(7L * 24 * 3600));

        return "Hello " + user.getUsername() + ",\n\n" +
                "Here's what you missed in the last week:\n" +
                "- New followers: " + newFollowers + "\n" +
                "- New likes on your posts: " + newLikes + "\n" +
                "- New posts created by you: " + newPosts + "\n\n" +
                "Come back and see what’s happening in the OnlyBuns community!\n\n" +
                "Best regards,\nOnlyBuns Team";
    }
}
