package service;

import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.ChatMessageResponse;

//비지니스 로직
public class ChatService {
	private final NetworkService networkService; //네트워크 통신담당
	private final ObjectMapper objectMapper;//json 객체 변환기
	private Long currentUserId;//현재 사용자 id
	
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
    
	private Object onConnected(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendPublic() {
		
	}
	
	public void sendWhisper() {
		
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public NetworkService getNetworkService() {
		return networkService;
	}

	private void onError       // 에러 발생 시
(Throwable throwable1) {
	}

}

