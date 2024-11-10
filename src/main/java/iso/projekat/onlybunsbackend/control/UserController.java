package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.LoginDTO;
import iso.projekat.onlybunsbackend.dto.LoginResponse;
import iso.projekat.onlybunsbackend.dto.UserDTO;
import iso.projekat.onlybunsbackend.jwt.JWTService;
import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserService userService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        User user = userService.getUserByUsername(loginDto.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        String jwtToken = jwtService.generateToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping
    public ResponseEntity<LoginResponse> createUser(@RequestBody UserDTO userDTO) {
        User user = userService.createUser(userDTO);

        String jwtToken = jwtService.generateToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
