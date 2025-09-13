package iso.projekat.onlybunsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListItemDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private long postsCount;
    private long followersCount;
    private long followingCount;
}
