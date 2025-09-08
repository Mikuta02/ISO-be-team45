package iso.projekat.onlybunsbackend.dto;

public record FollowStatusDto(
        Long userId,
        boolean following,
        long followersCount
) {}