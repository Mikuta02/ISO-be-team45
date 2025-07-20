package iso.projekat.onlybunsbackend.service;


import iso.projekat.onlybunsbackend.dto.*;
import iso.projekat.onlybunsbackend.model.VerificationToken;
import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.repository.FollowRepository;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import iso.projekat.onlybunsbackend.repository.UserRepository;
import iso.projekat.onlybunsbackend.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(UserService.class.getName());

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private PostRepository postRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private EmailService emailService;
    private FollowRepository followerRepository;


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        int followersCount = followerRepository.countFollowersByUserId(id);  // broj pratilaca
        int followingCount = followerRepository.countFollowingByUserId(id); // broj praćenja
        return new UserDTO(user, followersCount, followingCount);
    }

    public User createUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setEnabled(false);
        userDTO.setRole("USER");
        User user = new User(userDTO);
        userRepository.save(user);

        // Generišemo verifikacioni token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(verificationToken);

        String link = "http://localhost:8080/api/users/verify?token=" + token;
        emailService.sendEmail(user.getEmail(), "Verify your account", "Please click the following link to verify your account: " + link);


        return user;
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public List<UserDTO> getUsersFiltered(String firstName, String lastName, String email, Integer minPosts, Integer maxPosts) {
        return userRepository.findAll().stream()
                .filter(user -> firstName == null || user.getFirstName().equalsIgnoreCase(firstName))
                .filter(user -> lastName == null || user.getLastName().equalsIgnoreCase(lastName))
                .filter(user -> email == null || user.getEmail().equalsIgnoreCase(email))
                .filter(user -> minPosts == null || postRepository.findAll().stream().filter(p -> p.getUser().getId().equals(user.getId())).toList().size() >= minPosts)
                .filter(user -> maxPosts == null || postRepository.findAll().stream().filter(p -> p.getUser().getId().equals(user.getId())).toList().size() <= maxPosts)
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersSorted(String sortBy) {
        return userRepository.findAll().stream()
                .sorted(getComparator(sortBy))
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    private Comparator<User> getComparator(String sortBy) {
        if ("followersCount".equalsIgnoreCase(sortBy)) {
            return Comparator.comparingInt(User::getFollowersCount);
        } else if ("email".equalsIgnoreCase(sortBy)) {
            return Comparator.comparing(User::getEmail);
        } else {
            return Comparator.comparing(User::getId);
        }
    }

    public boolean verifyUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        return true;
    }

    public void deleteInactiveAccounts() {
        logger.info("Deleting inactive accounts");
        Instant thresholdDate = Instant.now().minus(30, ChronoUnit.DAYS);
        Optional<List<User>> inactiveUsers = userRepository.findInactiveAccounts(thresholdDate);
        if (inactiveUsers.isEmpty()) {
            return;
        }
        userRepository.deleteAll(inactiveUsers.get());
        logger.info("Deleted " + inactiveUsers.get().size() + " inactive accounts");
    }

    public Page<User> getUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    public List<User> getFollowers(Long userId) {
        return followerRepository.findFollowersByUserId(userId);
    }

    public List<User> getFollowing(Long userId) {
        return followerRepository.findFollowingByFollowerId(userId);
    }

    public UserProfile getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<User> followers = followerRepository.findFollowersByUserId(userId);
        List<User> following = followerRepository.findFollowingByFollowerId(userId);
        UserProfile profile = new UserProfile();
        profile.setName(user.getFirstName() + " " + user.getLastName());
        profile.setEmail(user.getEmail());
        profile.setPosts(postRepository.findAllByUser(user).stream().map(PostDTO::new).collect(Collectors.toList()));
        profile.setFollowersCount(followers.size());
        profile.setFollowers(followers.stream().map(UserDTO::new).collect(Collectors.toList()));
        profile.setFollowing(following.stream().map(UserDTO::new).collect(Collectors.toList()));
        return profile;
    }

    public String updateProfile(Long userId, UpdateProfileRequest updateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(updateRequest.getFirstName());
        user.setAddress(updateRequest.getAddress());
        user.setLastName(updateRequest.getLastName());
        userRepository.save(user);
        return "Profile updated successfully!";
    }

    public String changePassword(Long userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return "Current password is incorrect!";
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return "New passwords do not match!";
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Password changed successfully!";
    }


}