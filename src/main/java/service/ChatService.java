package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.ChatMessageResponse;
import dto.ChatSendRequest;
import dto.JoinRequest;
import dto.WhisperRequest;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

//비지니스 로직
public class ChatService {
	private final NetworkService networkService; //네트워크 통신담당
	private final ObjectMapper objectMapper;//json 객체 변환기
	private Long currentUserId;//현재 사용자 id
	private UserStateService userService;
	
	//이벤트 리스너 (Controller가 등록)
	private Consumer<ChatMessageResponse> onMessageReceived;
	
	public ChatService(NetworkService netwrokService) {
		this.networkService=netwrokService;
		this.objectMapper= new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		
	}
	
	// 연결 및 구독 초기화
    public void initialize(Long userId, Consumer<ChatMessageResponse> messageListener) {
        this.currentUserId = userId;
        this.onMessageReceived = messageListener;
        
        String serverUrl = "ws://localhost:8080/ws";
        
        networkService.connect(
            serverUrl,
            () -> onConnected(null),  // 연결 성공 시
            this::onError       // 에러 발생 시
        );
    }
    
	private void onConnected(Object object) {
        System.out.println("WebSocket Connected");        
        }
    
    private void onError(Throwable error) {
        System.err.println("Connection Error: " + error.getMessage());
    }
    
    
    // 입장 처리
    private void requestJoin() {
        JoinRequest request = new JoinRequest(currentUserId);
        networkService.send("/app/chat.join", request);
    }
    
    //구독, client 입장 버튼 클릭시 실행 될 코드
    public void joinChatRoom() {
        // 구독 먼저
        networkService.subscribe("/topic/public", this::handlePublicMessage);
        networkService.subscribe("/user/queue/whisper", this::handleWhisperMessage);
        networkService.subscribe("/user/queue/history", this::handleHistoryMessage);
        networkService.subscribe("/topic/users", this::handleUserListMessage);

        // 그 다음 입장 요청
        requestJoin();
    }
    
 // 전체 채팅 전송
    public void sendPublicMessage(String content) {
        ChatSendRequest request = new ChatSendRequest(currentUserId, content);
        networkService.send("/app/chat.send", request);
    }
    
    // 귓속말 전송
    public void sendWhisper(Long recipientId, String content) {
        WhisperRequest request = new WhisperRequest(currentUserId, recipientId, content);
        networkService.send("/app/chat.whisper", request);
    }
    
    // 메시지 핸들러들 (JSON 파싱 후 Controller에 전달)
    private void handlePublicMessage(String json) {
        try {
            ChatMessageResponse msg = objectMapper.readValue(json, ChatMessageResponse.class);
            if (onMessageReceived != null) {
                onMessageReceived.accept(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    private void handleWhisperMessage(String json) {
        // 동일 로직
        handlePublicMessage(json);
    }
    
    private void handleHistoryMessage(String json) {
        try {
            List<ChatMessageResponse> history = objectMapper.readValue(
                json, 
                new TypeReference<List<ChatMessageResponse>>() {}
            );
            history.forEach(msg -> {
                if (onMessageReceived != null) {
                    onMessageReceived.accept(msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleUserListMessage(String json) {
    	try {
            Map<String, Long> users = objectMapper.readValue(
                json, new TypeReference<Map<String, Long>>() {}
            );
            userService.updateActiveUsers(users); // UserStateService 갱신
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    
    public void disconnect() {
        networkService.disconnect();
    }

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public NetworkService getNetworkService() {
		return networkService;
	}

	
	

}

