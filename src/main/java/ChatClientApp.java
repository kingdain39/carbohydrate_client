import controller.ChatController;
import dto.LoginResponse;
import service.AuthService;
import service.ChatService;
import service.StompNetworkService;
import service.UserStateService;
import ui.ChatPanel;
import ui.LoginPanel;
import ui.RegisterPanel;

import javax.swing.*;
import java.awt.*;

public class ChatClientApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private ChatPanel chatPanel;

    // 로그인 후 저장할 정보
    private Long myId;
    private String myName;
    private String jwtToken;

    private AuthService authService;
    private ChatService chatService;
    private UserStateService userStateService;
    private ChatController chatController;


    public ChatClientApp() {
        setTitle("귓속말 채팅 프로그램");
        setSize(450, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.authService = new AuthService("http://localhost:8080");

        StompNetworkService networkService = new StompNetworkService();
        this.userStateService = new UserStateService();
        this.chatService = new ChatService(networkService, userStateService);
        this.chatController = new ChatController(chatService, userStateService);

        // CardLayout 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 각 화면 생성
        loginPanel = new LoginPanel(
                this::onLoginAttempt,              // 로그인 성공 콜백
                this::showRegisterPanel     // 회원가입 버튼 콜백
        );
        registerPanel = new RegisterPanel(
                this::onSignupAttempt,    // 가입 성공 콜백
                this::showLoginPanel        // 뒤로가기 콜백
        );
        chatPanel = new ChatPanel();

        // 카드에 추가
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registerPanel, "REGISTER");
        mainPanel.add(chatPanel, "CHAT");

        add(mainPanel);

        // 처음엔 로그인 화면
        cardLayout.show(mainPanel, "LOGIN");

        setLocationRelativeTo(null);
        setVisible(true);
    }

    //얘는 Auth서비스 한테 회원가입 부탁함. 그리고 성공시 메소드 따로 안 만들고 그냥 놔둠.
    private void onSignupAttempt(String username, String password) {
        try {
            authService.signup(username, password);
            onSignupSuccess(username);  // 분리
        } catch (AuthService.AuthException e) {
            registerPanel.showError(e.getMessage());
        }
    }

    private void onSignupSuccess(String username) {
        registerPanel.showSuccess("회원가입이 완료되었습니다! 로그인해주세요.");
        showLoginPanel();
    }

    //애는 Auth서비스한테 로그인 부탁함.
    private void onLoginAttempt(String username, String password) {
        try {
            // AuthService로 로그인 요청
            LoginResponse loginResponse = authService.login(username, password);

            // 로그인 성공
            onLoginSuccess(loginResponse.getUserId(), username, loginResponse.getToken());

        } catch (AuthService.AuthException e) {
            // 로그인 실패
            loginPanel.showError(e.getMessage());
        }
    }

    // 로그인 성공 시
    private void onLoginSuccess(Long userId, String userName, String jwtToken) {
        this.myId = userId;
        this.myName = userName;
        this.jwtToken = jwtToken;

        System.out.println("로그인 성공! ID: " + userId + ", 이름: " + userName);

        chatPanel.setUserInfo(userId, userName);
        cardLayout.show(mainPanel, "CHAT");
        chatPanel.addSystemMessage("서버에 연결 중...");

        //컨트롤러 초기화!!! 연결 완료 시 자동으로 팝업 띄우기
        chatController.initialize(chatPanel, userId, jwtToken, () -> {
            // 이 부분이 연결 완료 후 실행됨!
            SwingUtilities.invokeLater(() -> {
                int result = JOptionPane.showConfirmDialog(
                        this,
                        userName + "님, 서버 연결이 완료되었습니다!\n채팅방에 입장하시겠습니까?",
                        "연결 완료",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    chatService.joinChatRoom();
                    chatPanel.addSystemMessage("채팅방에 입장했습니다!");
                } else {
                    chatPanel.addSystemMessage("입장 대기 중입니다.");
                }
            });
        });
    }

    // 화면 전환 메서드
    private void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void showRegisterPanel() {
        cardLayout.show(mainPanel, "REGISTER");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClientApp::new);
    }
}