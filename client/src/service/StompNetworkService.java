package service;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

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
	public void connect(String url, Runnable onConnected, Consumer<Throwable> onError) {
		// TODO Auto-generated method stub
		StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
			
			public void afterConnected(StompSession session) {
				StompNetworkService.this.session=session;
				onConnected.run();
			}
			
			public void handleException(StompSession session,StompCommand c, 
                    StompHeaders h, byte[] p, Throwable ex ) {
					onError.accept(ex);
			}
		};
		stompClient.connect(url, sessionHandler);
	
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
	        }
	        
	        session.subscribe(destination, new StompFrameHandler() {
	            @Override
	            public Type getPayloadType(StompHeaders headers) {
	                return String.class; //메시지를 문자열로 받음
	            }
	            
	            @Override
	            public void handleFrame(StompHeaders headers, Object payload) {
	                handler.handle((String) payload); //payload를 string으로 캐스팅 후 처리
	            }
	        });
	}

	@Override
	public void send(String destination, Object payload) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return  session != null && session.isConnected();
	}
	
	
	
	
}
