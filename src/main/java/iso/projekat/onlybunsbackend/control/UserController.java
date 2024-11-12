package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.LoginDTO;
import iso.projekat.onlybunsbackend.dto.LoginResponse;
import iso.projekat.onlybunsbackend.dto.UserDTO;
import iso.projekat.onlybunsbackend.jwt.JWTService;
import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.service.BloomFilterService;
import iso.projekat.onlybunsbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final BloomFilterService bloomFilterService;


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

        String jwtToken = jwtService.generateToken(user, user.getId(), user.getRole());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping
    public ResponseEntity<LoginResponse> createUser(@RequestBody UserDTO userDTO) {
        User user = userService.createUser(userDTO);

        String jwtToken = jwtService.generateToken(user, user.getId(), user.getRole());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
        String userIp = request.getRemoteAddr();

        // Proveravamo da li je IP dozvoljena
        if (!bloomFilterService.isIpAllowed(userIp)) {
            logger.warning("Unauthorized IP access attempt from: " + userIp);
            return ResponseEntity.status(403).body("Access from this IP is not allowed");
        }

        // Preuzimamo autentifikovanog korisnika
        String username = jwtService.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        UserDTO user = userService.getUserByUsername(username).map(UserDTO::new)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts) {

        List<UserDTO> users = userService.getUsersFiltered(firstName, lastName, email, minPosts, maxPosts);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<UserDTO>> getAllUsersSorted(
            @RequestParam String sortBy) {

        List<UserDTO> users = userService.getUsersSorted(sortBy);
        return ResponseEntity.ok(users);
    }
}
