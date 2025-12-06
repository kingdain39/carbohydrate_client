package dto;

public class WhisperRequest {
	private Long senderId;
    private Long recipientId;
    private String content;
	public WhisperRequest(Long currentUserId, Long recipientId2, String content2) {
		// TODO Auto-generated constructor stub
		this.senderId=currentUserId;
		this.recipientId=recipientId2;
		this.content=content2;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public Long getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
