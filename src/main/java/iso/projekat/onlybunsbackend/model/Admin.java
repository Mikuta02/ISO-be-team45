package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "admins", uniqueConstraints = {
        @UniqueConstraint(name = "admins_username_key", columnNames = {"username"}),
        @UniqueConstraint(name = "admins_email_key", columnNames = {"email"})
})
public class Admin {
    @Id
    @ColumnDefault("nextval('admins_id_seq')")
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

    @Size(max = 255)
    @NotNull
    @Column(name = "permissions", nullable = false)
    private String permissions;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}