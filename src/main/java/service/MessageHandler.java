package service;
@FunctionalInterface
public interface MessageHandler {
	 void handle(String payload);
}
