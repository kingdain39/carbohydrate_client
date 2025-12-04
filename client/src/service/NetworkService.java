
public interface NetworkService {
	void connect(String url, Runnable onConnected, Consumer<Throwable> onError);
    void disconnect();
    void subscribe(String destination, MessageHandler handler);
    void send(String destination, Object payload);
    boolean isConnected();
}