package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.model.Message;
import iso.projekat.onlybunsbackend.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload Message message) {
        if (message.getGroup() == null) message.setGroup(false);
        chatService.saveMessage(message);

        // 1) isporuči PRIMAOcu na njegov lični topic
        messagingTemplate.convertAndSend(
                "/private-message/" + message.getReceiver(),
                message
        );

        // 2) isporuči i POŠILJAOcu (da i on dobije kroz isti tok)
        messagingTemplate.convertAndSend(
                "/private-message/" + message.getSender(),
                message
        );
    }


    // REST: istorija između {sender} i {receiver}
    @GetMapping("/{sender}/history/{receiver}")
    public List<Message> getChatHistory(@PathVariable String sender, @PathVariable String receiver) {
        return chatService.getLast10MessagesBetween(sender, receiver);
    }
}
