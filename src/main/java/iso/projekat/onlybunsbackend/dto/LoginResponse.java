package iso.projekat.onlybunsbackend.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResponse {
    private String token;
    private long expiresIn;
}
