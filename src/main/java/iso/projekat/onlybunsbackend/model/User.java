package iso.projekat.onlybunsbackend.model;

import iso.projekat.onlybunsbackend.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_username", columnList = "username")
}, uniqueConstraints = {
        @UniqueConstraint(name = "users_username_key", columnNames = {"username"}),
        @UniqueConstraint(name = "users_email_key", columnNames = {"email"})
})
@NoArgsConstructor
public class User {
    @Id
    @ColumnDefault("nextval('users_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 50)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Size(max = 50)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @ColumnDefault("0")
    @Column(name = "followers_count")
    private Integer followersCount;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'USER'")
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    public User(UserDTO userDTO) {
        this.username = userDTO.getUsername();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.address = ""; // Placeholder, should be set properly if needed
        this.followersCount = 0; // Placeholder, initial value
        this.role = userDTO.getRole();
        this.enabled = userDTO.isEnabled();
    }
}