package dto;

public class LoginResponse {
    private Long userId;
    private String token;  // JWT 토큰

    public LoginResponse(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}