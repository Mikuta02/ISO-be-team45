package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.model.GroupChat;
import iso.projekat.onlybunsbackend.model.GroupMember;
import iso.projekat.onlybunsbackend.repository.GroupChatRepository;
import iso.projekat.onlybunsbackend.repository.GroupMemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
public class GroupService {

    private final GroupChatRepository groupChatRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupChat createGroup(String name, String adminUsername) {
        GroupChat g = new GroupChat();
        g.setName(name);
        g.setAdminUsername(adminUsername);
        GroupChat saved = groupChatRepository.save(g);

        GroupMember self = new GroupMember();
        self.setGroupId(saved.getId());
        self.setUsername(adminUsername);
        groupMemberRepository.save(self);

        return saved;
    }

    public boolean isMember(Long groupId, String username) {
        return groupMemberRepository.existsByGroupIdAndUsername(groupId, username);
    }

    public GroupChat getGroupOrThrow(Long id) {
        return groupChatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
    }

    public void addMember(Long groupId, String username, String requester) {
        // samo admin
        GroupChat g = getGroupOrThrow(groupId);
        if (!g.getAdminUsername().equals(requester)) {
            throw new SecurityException("Only admin can add members");
        }
        if (!groupMemberRepository.existsByGroupIdAndUsername(groupId, username)) {
            GroupMember m = new GroupMember();
            m.setGroupId(groupId);
            m.setUsername(username);
            groupMemberRepository.save(m);
        }
    }

    public void removeMember(Long groupId, String username, String requester) {
        GroupChat g = getGroupOrThrow(groupId);
        if (!g.getAdminUsername().equals(requester)) {
            throw new SecurityException("Only admin can remove members");
        }
        // admin može da izbaci bilo koga (i sebe, ako želi)
        groupMemberRepository.deleteByGroupIdAndUsername(groupId, username);
    }

    public void leave(Long groupId, String username) {
        // član napušta grupu
        groupMemberRepository.deleteByGroupIdAndUsername(groupId, username);
    }

    public List<GroupMember> members(Long groupId) {
        return groupMemberRepository.findByGroupId(groupId);
    }

    public List<GroupChat> myGroups(String username) {
        // mapirati membership -> GroupChat
        List<GroupMember> mems = groupMemberRepository.findByUsername(username);
        return mems.stream()
                .map(m -> groupChatRepository.findById(m.getGroupId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }
}
