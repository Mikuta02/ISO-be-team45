package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.UserDTO;
import iso.projekat.onlybunsbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }
}
