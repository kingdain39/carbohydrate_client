package service;
import java.util.function.Consumer;

public interface NetworkService { //로그인 후에 유저 아이디를 넘겨주지 않고 연결을 해서 오류낫음. 그래서 추가
	void connect(String url,  String jwtToken, Runnable onConnected, Consumer<Throwable> onError);
    void disconnect();
    void subscribe(String destination, MessageHandler handler);
    void send(String destination, Object payload);
    boolean isConnected();
	
}