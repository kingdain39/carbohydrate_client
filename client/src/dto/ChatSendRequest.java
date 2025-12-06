package dto;


public class ChatSendRequest {
	  private Long senderId;
      private String content;
	public ChatSendRequest(Long currentUserId, String content2) {
		// TODO Auto-generated constructor stub
		this.senderId=currentUserId;
		this.content=content2;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
