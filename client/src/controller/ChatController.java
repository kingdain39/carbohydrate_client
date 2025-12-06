package controller;

import service.ChatService;
import service.UserStateService;
import ui.ChatPanel;

public class ChatController  {
	private final ChatService chatService;
    private final UserStateService userStateService;
    
    // View 컴포넌트 (팀원들이 만든 UI)
    private ChatPanel chatView;  // 인터페이스로 추상화 권장
    
    public ChatController(ChatService chatService, UserStateService userStateService) {
        this.chatService = chatService;
        this.userStateService = userStateService;
    }
    
	public void sendMessage() {
		
	}
}
