package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.UserDTO;
import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserDTO::new).orElse(null);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User(userDTO);
        userRepository.save(user);
        return new UserDTO(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}