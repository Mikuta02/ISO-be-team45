package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.model.GroupMember;
import iso.projekat.onlybunsbackend.model.Message;
import iso.projekat.onlybunsbackend.repository.GroupMemberRepository;
import iso.projekat.onlybunsbackend.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final GroupMemberRepository groupMemberRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    // Privatni: poslednjih 10 ASC za prikaz
    public List<Message> getLast10MessagesBetween(String a, String b) {
        List<Message> last10Desc =
                messageRepository.findConversation(a, b, b, a, PageRequest.of(0, 10));
        Collections.reverse(last10Desc);
        return last10Desc;
    }

    // Grupni: poslednjih 10 ASC za prikaz
    public List<Message> getLast10GroupMessages(Long groupId) {
        List<Message> last10Desc =
                messageRepository.findByGroupTrueAndGroupIdOrderByTimestampDesc(groupId, PageRequest.of(0, 10));
        Collections.reverse(last10Desc);
        return last10Desc;
    }

    public List<GroupMember> getMembers(Long groupId) {
        return groupMemberRepository.findByGroupId(groupId);
    }

    public boolean isMember(Long groupId, String username) {
        return groupMemberRepository.existsByGroupIdAndUsername(groupId, username);
    }
}
