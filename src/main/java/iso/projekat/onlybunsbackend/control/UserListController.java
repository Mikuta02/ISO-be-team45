package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.UserListItemDTO;
import iso.projekat.onlybunsbackend.service.UserListService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userslist")
@AllArgsConstructor
public class UserListController {

    private final UserListService userListService;

    @GetMapping("/paged")
    public ResponseEntity<Page<UserListItemDTO>> getUsersPaged(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(defaultValue = "email") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<UserListItemDTO> result = userListService.getUsersPaged(
                firstName, lastName, email, minPosts, maxPosts, sortBy, order, page, size
        );
        return ResponseEntity.ok(result);
    }
}
