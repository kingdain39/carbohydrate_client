
public class StompNetworkService {
	WebSocketClient webSocketClient = new StandardWebSocketClient();
	WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
}
