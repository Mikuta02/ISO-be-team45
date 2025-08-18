package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.model.Message;
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

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getLast10MessagesBetween(String a, String b) {
        List<Message> last10Desc =
                messageRepository.findConversation(a, b, b, a, PageRequest.of(0, 10));
        Collections.reverse(last10Desc);
        return last10Desc;
    }
}
