package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.model.GroupChat;
import iso.projekat.onlybunsbackend.model.GroupMember;
import iso.projekat.onlybunsbackend.service.ChatService;
import iso.projekat.onlybunsbackend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final ChatService chatService;

    public static record CreateGroupRequest(String name, List<String> members) {}

    @PostMapping
    public GroupChat create(@RequestBody CreateGroupRequest req, Principal principal) {
        String creator = principal.getName();
        String name = req != null ? req.name() : null;

        GroupChat saved = groupService.createGroup(name, creator);

        // opciono dodaj ostale članove
        List<String> members = (req != null ? req.members() : null);
        if (members != null) {
            for (String m : members) {
                if (m == null || m.isBlank()) continue;
                groupService.addMember(saved.getId(), m, creator);
            }
        }
        return saved;
    }

    @GetMapping("/{groupId}/is-member")
    public ResponseEntity<Boolean> isMember(@PathVariable Long groupId, Principal principal) {
        return ResponseEntity.ok(groupService.isMember(groupId, principal.getName()));
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

    @DeleteMapping("/{groupId}/members/{username}")
    public ResponseEntity<Void> remove(@PathVariable Long groupId,
                                       @PathVariable String username,
                                       Principal principal) {
        groupService.removeMember(groupId, username, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/members/{username}")
    public ResponseEntity<Void> add(@PathVariable Long groupId,
                                    @PathVariable String username,
                                    Principal principal) {
        groupService.addMember(groupId, username, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public List<GroupChat> myGroups(Principal principal) {
        return groupService.myGroups(principal.getName());
    }
}
