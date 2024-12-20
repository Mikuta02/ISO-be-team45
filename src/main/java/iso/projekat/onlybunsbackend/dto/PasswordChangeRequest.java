package iso.projekat.onlybunsbackend.dto;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
