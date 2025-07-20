package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.model.Message;
import iso.projekat.onlybunsbackend.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@AllArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // Privatne poruke
    @MessageMapping("/private-message/{recipientUsername}")
    public void sendPrivateMessage(@Payload Message message) {
        chatService.saveMessage(message); // Sačuvaj poruku u bazi
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/private-message/" + message.getReceiver(),
                message
        );
    }

    @GetMapping("/{sender}/history/{receiver}")
    public List<Message> getChatHistory(@PathVariable String sender, @PathVariable String receiver) {
        return chatService.getLast10MessagesForUser(sender);
    }


    /*// Grupne poruke
    @MessageMapping("/group/{groupId}")
    @SendTo("/topic/group/{groupId}")
    public Message sendGroupMessage(@Payload Message message) {
        return chatService.saveGroupMessage(message); // Sačuvaj grupnu poruku i emituj svim pretplatnicima
    }
    */
}
