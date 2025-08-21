package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.model.Message;
import iso.projekat.onlybunsbackend.service.ChatService;
import iso.projekat.onlybunsbackend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final GroupService groupService;
    private final SimpMessagingTemplate messagingTemplate;

    /* ======= STOMP: PRIVATNE PORUKE ======= */
    // klijent šalje na /app/private-message
    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload Message message, Principal principal) {
        // fallback: ako STOMP Principal ne postoji, koristimo sender iz payload-a
        String sender = principal != null ? principal.getName() : message.getSender();
        message.setSender(sender);
        if (message.getGroup() == null) message.setGroup(false);
        message.setGroupId(null);

        chatService.saveMessage(message);

        // isporuka primaocu i pošiljaocu (front sluša /private-message/{username})
        if (message.getReceiver() != null) {
            messagingTemplate.convertAndSend("/private-message/" + message.getReceiver(), message);
        }
        if (message.getSender() != null) {
            messagingTemplate.convertAndSend("/private-message/" + message.getSender(), message);
        }
    }

    /* ======= REST: PRIVATNA ISTORIJA ======= */
    // front koristi obe varijante; zadržavamo obe
    @GetMapping("/{other}/history")
    public List<Message> getPrivateHistoryWith(@PathVariable("other") String otherUsername,
                                               Principal principal) {
        String me = principal.getName();
        return chatService.getLast10MessagesBetween(me, otherUsername);
    }

    @GetMapping("/history/{other}")
    public List<Message> getPrivateHistoryWithCompat(@PathVariable("other") String otherUsername,
                                                     Principal principal) {
        String me = principal.getName();
        return chatService.getLast10MessagesBetween(me, otherUsername);
    }

    /* ======= REST: GRUPNA ISTORIJA (proxy radi front rute) ======= */
    // front zove: GET /api/chat/group/{groupId}/history
    @GetMapping("/group/{groupId}/history")
    public List<Message> groupHistory(@PathVariable Long groupId, Principal p) {
        if (!groupService.isMember(groupId, p.getName())) {
            throw new org.springframework.security.access.AccessDeniedException("Not a member");
        }
        return chatService.getLast10GroupMessages(groupId);
    }
}
