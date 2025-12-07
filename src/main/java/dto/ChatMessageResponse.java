package dto;

import java.time.LocalDateTime;

public class ChatMessageResponse {
    private Long id;
    private String type;
    private String senderName;
    private String recipientName;
    private String content;
    private LocalDateTime timestamp;

    public ChatMessageResponse() {}

    public Long getId() { return id; }
    public String getType() { return type; }
    public String getSenderName() { return senderName; }
    public String getRecipientName() { return recipientName; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setId(Long id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public void setContent(String content) { this.content = content; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}