package iso.projekat.onlybunsbackend.dto;

import iso.projekat.onlybunsbackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String address = "";
    private boolean enabled;
    private int followersCount; // Dodato polje za broj pratilaca
    private int followingCount; // Dodato polje za broj praćenja

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.enabled = user.getEnabled();
        this.address = user.getAddress() != null ? user.getAddress() : "";
        this.followersCount = (int) user.getFollowersCount();
        this.followingCount = 0;
    }

    public UserDTO(User user, int followersCount, int followingCount) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.enabled = user.getEnabled();
        this.address = user.getAddress() != null ? user.getAddress() : "";
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }
}