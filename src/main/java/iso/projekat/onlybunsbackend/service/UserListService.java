package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.UserListItemDTO;
import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.repository.FollowRepository;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import iso.projekat.onlybunsbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserListService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;

    public Page<UserListItemDTO> getUsersPaged(String firstName, String lastName, String email,
                                               Integer minPosts, Integer maxPosts,
                                               String sortBy, String order,
                                               int page, int size) {

        // 1) osnovni filter po imenu/prezimenu/email (case-insensitive, contains)
        List<User> allUsers = userRepository.findAll();
        String fn = firstName == null ? null : firstName.toLowerCase(Locale.ROOT);
        String ln = lastName == null ? null : lastName.toLowerCase(Locale.ROOT);
        String em = email == null ? null : email.toLowerCase(Locale.ROOT);

        List<UserListItemDTO> enriched = allUsers.stream()
                .filter(u -> fn == null || (u.getFirstName() != null && u.getFirstName().toLowerCase(Locale.ROOT).contains(fn)))
                .filter(u -> ln == null || (u.getLastName() != null && u.getLastName().toLowerCase(Locale.ROOT).contains(ln)))
                .filter(u -> em == null || (u.getEmail() != null && u.getEmail().toLowerCase(Locale.ROOT).contains(em)))
                .map(u -> {
                    long postsCount = postRepository.findPostsByUser(u.getId()).size();
                    long followersCount = followRepository.countByFolloweeId(u.getId());
                    long followingCount = followRepository.findByFollowerId(u.getId(), Pageable.unpaged()).getTotalElements();
                    return new UserListItemDTO(
                            u.getId(),
                            u.getFirstName(),
                            u.getLastName(),
                            u.getEmail(),
                            postsCount,
                            followersCount,
                            followingCount
                    );
                })
                .filter(dto -> minPosts == null || dto.getPostsCount() >= minPosts)
                .filter(dto -> maxPosts == null || dto.getPostsCount() <= maxPosts)
                .collect(Collectors.toCollection(ArrayList::new));

        // 2) sortiranje (email / followingCount), default: id
        Comparator<UserListItemDTO> comparator;
        if ("followingCount".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparingLong(UserListItemDTO::getFollowingCount);
        } else if ("email".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(UserListItemDTO::getEmail,
                    Comparator.nullsLast(String::compareToIgnoreCase));
        } else {
            comparator = Comparator.comparingLong(UserListItemDTO::getId);
        }
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        enriched.sort(comparator);

        // 3) ručna paginacija
        int fromIndex = Math.min(page * size, enriched.size());
        int toIndex = Math.min(fromIndex + size, enriched.size());
        List<UserListItemDTO> pageContent = enriched.subList(fromIndex, toIndex);

        return new PageImpl<>(
                pageContent,
                PageRequest.of(page, size, Sort.by(sortBy == null ? "id" : sortBy)),
                enriched.size()
        );
    }
}
