package dto;

public class JoinRequest {
    private Long userId;

    // 기본 생성자 두면 좋으니까
    public JoinRequest() {
    }

    // 생성자
    public JoinRequest(Long userId) {
        this.userId = userId;
    }

    // getter 없어서 오류남
    public Long getUserId() {
        return userId;
    }

    // setter 오류날까봐 추가함
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
