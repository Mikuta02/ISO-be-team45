package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.model.GroupChat;
import iso.projekat.onlybunsbackend.model.GroupMember;
import iso.projekat.onlybunsbackend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat/groups")
@RequiredArgsConstructor
public class ChatGroupApiController {

    private final GroupService groupService;

    @GetMapping("/my")
    public List<GroupChat> myGroups(Principal principal) {
        return groupService.myGroups(principal.getName());
    }

    @GetMapping("/{groupId}")
    public GroupChat meta(@PathVariable Long groupId) {
        return groupService.getGroupOrThrow(groupId);
    }

    @GetMapping("/{groupId}/members")
    public List<GroupMember> members(@PathVariable Long groupId) {
        return groupService.members(groupId);
    }

    @PostMapping("/{groupId}/leave")
    public ResponseEntity<Void> leave(@PathVariable Long groupId, Principal principal) {
        groupService.leave(groupId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/members/{username}")
    public ResponseEntity<Void> add(@PathVariable Long groupId,
                                    @PathVariable String username,
                                    Principal principal) {
        groupService.addMember(groupId, username, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{groupId}/members/{username}")
    public ResponseEntity<Void> remove(@PathVariable Long groupId,
                                       @PathVariable String username,
                                       Principal principal) {
        groupService.removeMember(groupId, username, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
