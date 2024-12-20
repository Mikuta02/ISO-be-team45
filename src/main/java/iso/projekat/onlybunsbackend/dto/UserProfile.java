package iso.projekat.onlybunsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private String name;
    private String email;
    private String address;
    private int followersCount;
    private List<PostDTO> posts;
    private List<UserDTO> followers;
    private List<UserDTO> following;
}
