package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatRoomService {
    private final Map<String, List<ChatMessage>> chatRooms = new HashMap<>();

    public void createChatRoom(String chatId) {
        chatRooms.putIfAbsent(chatId, new ArrayList<>());
    }

    public List<ChatMessage> getLast10Messages(String chatId) {
        List<ChatMessage> messages = chatRooms.getOrDefault(chatId, new ArrayList<>());
        int fromIndex = Math.max(messages.size() - 10, 0);
        return messages.subList(fromIndex, messages.size());
    }

    public void addMessage(String chatId, ChatMessage message) {
        chatRooms.computeIfAbsent(chatId, k -> new ArrayList<>()).add(message);
    }
}
