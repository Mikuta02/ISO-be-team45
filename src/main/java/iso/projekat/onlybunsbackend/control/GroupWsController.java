package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.model.Message;
import iso.projekat.onlybunsbackend.service.ChatService;
import iso.projekat.onlybunsbackend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GroupWsController {

    private final GroupService groupService;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // klijent šalje na /app/group-message
    @MessageMapping("/group-message")
    public void onGroupMessage(@Payload Message msg, Principal principal) {
        if (msg.getGroupId() == null) return;

        // fallback: ako Principal fale, koristimo msg.sender (front ga šalje)
        String username = principal != null ? principal.getName() : msg.getSender();
        if (username == null || !groupService.isMember(msg.getGroupId(), username)) {
            return; // nije član → ignoriši
        }

        msg.setGroup(true);
        msg.setReceiver(null);
        msg.setSender(username);
        chatService.saveMessage(msg);

        // broadcast svim pretplatnicima grupe
        messagingTemplate.convertAndSend("/topic/group/" + msg.getGroupId(), msg);
    }
}
