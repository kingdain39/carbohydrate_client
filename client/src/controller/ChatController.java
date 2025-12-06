package controller;

import java.util.Set;

import javax.swing.JPanel;

import dto.ChatMessageResponse;
import service.ChatService;
import service.UserStateService;
import ui.ChatPanel;
import ui.LoginPanel;
import ui.RegisterPanel;

public class ChatController  {
	private final ChatService chatService;
    private final UserStateService userStateService;
    
    // View 컴포넌트 (팀원들이 만든 UI)
    private ChatPanel chatView;
    private LoginPanel loginView;
    private RegisterPanel registerView;
    
    public ChatController(ChatService chatService, UserStateService userStateService) {
        this.chatService = chatService;
        this.userStateService = userStateService;
    }
    
 // 초기화 (View 연결)
    public void initialize(ChatPanel view, Long userId) {
        this.chatView = view;
        
        // Service → Controller 리스너 등록
        chatService.initialize(userId, this::handleIncomingMessage);
        userStateService.setUserListListener(this::handleUserListUpdate);
        
        // View → Controller 이벤트 연결
        view.setOnSendMessage(this::onSendButtonClick);
        //view.setOnSendWhisper(this::onWhisperButtonClick); //우리 어차피 귓속말 버튼 없이 /w로 파싱하니까 없앰.
        view.setOnDisconnect(this::onDisconnect);
        view.setOnJoin(this::onJoinButtonClick); 
    }
    
    //View → Service 
    
    // 유저가 "입장 버튼" 클릭했을 때 실행
    public void onJoinButtonClick() {
        chatService.joinChatRoom();  // Service에서 네트워크 경로 구독 + JOIN 요청 실행
        chatView.addSystemMessage("Chatroom Join!");
    }
    
    public void onSendButtonClick(String text) {
    	  if (text == null || text.trim().isEmpty()) {
              return;
          }
    	  
    	  String trimmed = text.trim();
    	// "/w" 로 시작하면 귓속말 처리
          if (trimmed.startsWith("/w ")) {
              handleWhisperCommand(trimmed);
          } else {
              // 일반 전체 채팅
              chatService.sendPublicMessage(trimmed);
          }
    }

    private void handleWhisperCommand(String command) {
        // "/w 받는사람 메시지내용" 형식 파싱
        String[] parts = command.substring(3).split(" ", 2); // "/w " 제거 후 공백 기준 2개로 분리
        
        if (parts.length < 2) {
            showError("귓속말 형식: /w 받는사람 메시지");
            return;
        }
        
        String recipientName = parts[0].trim();
        String message = parts[1].trim();
        
    
        if (message.isEmpty()) {
        showError("메시지 내용을 입력해주세요.");
        return;
        }
        
     // recipientName -> recipientId 변환
        Long recipientId = userStateService.getUserIdByName(recipientName);
        if (recipientId == null) {
            showError("사용자를 찾을 수 없습니다: " + recipientName);
            return;
        }
        
        // 받는 사람이 접속 중인지 확인
        if (!userStateService.isUserActive(recipientName)) {
            showError(recipientName + "님은 현재 접속 중이 아닙니다.");
            return;
        }
        
        
        
        chatService.sendWhisper(recipientId, message);
    }
    
    public void onDisconnect() {
        chatService.disconnect();
        //chatView.close();
    }
    
// Service → View (서버 메시지) 
    
    private void handleIncomingMessage(ChatMessageResponse message) {
        switch (message.getType()) {
            case "CHAT":
                chatView.addPublicMessage(
                    message.getSenderName(),
                    message.getContent(),
                    message.getTimestamp()
                );
                break;
                
            case "WHISPER":
                // 핑크색으로 표시 (팀원 2 메서드 호출)
                chatView.addWhisperMessage(
                    message.getSenderName(),
                    message.getRecipientName(),
                    message.getContent(),
                    message.getTimestamp()
                );
                break;
                
            case "JOIN":
                chatView.addSystemMessage(message.getContent());
                break;
                
            case "LEAVE":
                chatView.addSystemMessage(message.getContent());
                break;
        }
    }
    private void handleUserListUpdate(Set<String> users) {
        //chatView.updateUserList(users);
    }
    
    
    private void showError(String content) {
    	//chatView.showError(string);
        chatView.addSystemMessage("ERROR: " + content);
    }
}
