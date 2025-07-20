package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.model.Message;
import iso.projekat.onlybunsbackend.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getLast10MessagesForUser(String username) {
        return messageRepository.findTop10ByReceiverOrderByTimestampDesc(username);
    }


}
