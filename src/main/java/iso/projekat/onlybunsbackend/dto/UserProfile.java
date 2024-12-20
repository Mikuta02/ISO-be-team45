package iso.projekat.onlybunsbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserProfile {
    private String name;
    private String email;
    private String address;
    private int followersCount;
    private List<PostDTO> posts;
    private List<UserDTO> followers;
    private List<UserDTO> following;
}
