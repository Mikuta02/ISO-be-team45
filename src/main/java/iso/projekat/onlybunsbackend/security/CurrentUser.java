package iso.projekat.onlybunsbackend.security;

import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.repository.UserFollowRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    private final UserFollowRepository userRepo;

    public CurrentUser(UserFollowRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Long id() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new IllegalStateException("Niste autentifikovani.");

        Object principal = auth.getPrincipal();
        String key = auth.getName();
        if (principal instanceof org.springframework.security.core.userdetails.User u) {
            key = u.getUsername();
        }
        // pokušaj username pa email
        String finalKey = key;
        return userRepo.findByUsernameIgnoreCase(key)
                .map(User::getId)
                .or(() -> userRepo.findByEmailIgnoreCase(finalKey).map(User::getId))
                .orElseThrow(() -> new IllegalStateException("Nije moguće mapirati korisnika iz principal-a."));
    }
}
