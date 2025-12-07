package service;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.util.MimeTypeUtils;




import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

public class StompNetworkService implements NetworkService{
	private StompSession session;
	private WebSocketStompClient stompClient;
	
	public StompNetworkService() {
        // SockJS + STOMP 클라이언트 초기화
        SockJsClient sockJsClient = new SockJsClient(
            List.of(new WebSocketTransport(new StandardWebSocketClient()))
        );
        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

	@Override
	public void connect(String url,  String jwtToken, Runnable onConnected, Consumer<Throwable> onError) {
		// TODO Auto-generated method stub
		StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {

			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				StompNetworkService.this.session=session;
				onConnected.run();
			}

			public void handleException(StompSession session,StompCommand c,
                    StompHeaders h, byte[] p, Throwable ex ) {
					onError.accept(ex);
			}
		};
		//http헤더도 만들어줌(웹소켓연결용)
		WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
		//STOMP헤더 생성하는 로직(JWT건네주는 거 반영함)
		StompHeaders headers = new StompHeaders();
		headers.add("Authorization", "Bearer " + jwtToken);
		stompClient.connect(url, httpHeaders, headers, sessionHandler);

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		 if (session != null) {
	            session.disconnect();
	            session = null;
	        }
	}

	@Override
	public void subscribe(String destination, MessageHandler handler) {
		// TODO Auto-generated method stub
		
		 if (session == null || !session.isConnected()) {
	            throw new IllegalStateException("Not connected");
	        }//세션이 없거나 연결되지 않았다면 예외를 던져 진행되지 않게한다.
	        
		 //특정 채널(destication)을 구독
	        session.subscribe(destination, new StompFrameHandler() {
	            @Override
	            public Type getPayloadType(StompHeaders headers) {
	                return byte[].class; //메시지를 어떻게 받을지 정의.메시지를 문자열로 받음 ----->오류나서 바이트로 받는걸로 수정
	            }
	            
	            @Override
	            public void handleFrame(StompHeaders headers, Object payload) {
	                String json = new String((byte[]) payload);  // 바이트를 string으로 받도록 수정
					handler.handle(json);
	            }
	        });
	}

	@Override
	public void send(String destination, Object payload) {
		// TODO Auto-generated method stub
		if (session == null || !session.isConnected()) {
			throw new IllegalStateException("Not connected");
		}

		StompHeaders headers = new StompHeaders();
		headers.setContentType(org.springframework.util.MimeTypeUtils.APPLICATION_JSON);

		session.send(headers, payload);
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return  session != null && session.isConnected();
	}
	
	
	
	
}
