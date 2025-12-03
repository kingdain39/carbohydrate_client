
public class NetworkService {
	WebSocketClient webSocketClient = new StandardWebSocketClient();
	WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
}