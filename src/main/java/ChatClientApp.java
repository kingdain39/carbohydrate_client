import controller.ChatController;
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

    private ChatService chatService;
    private UserStateService userStateService;
    private ChatController chatController;


    public ChatClientApp() {
        setTitle("귓속말 채팅 프로그램");
        setSize(450, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StompNetworkService networkService = new StompNetworkService();
        this.chatService = new ChatService(networkService);
        this.userStateService = new UserStateService();
        this.chatController = new ChatController(chatService, userStateService);

        // CardLayout 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 각 화면 생성
        loginPanel = new LoginPanel(
                this::onLogin,              // 로그인 성공 콜백
                this::showRegisterPanel     // 회원가입 버튼 콜백
        );
        registerPanel = new RegisterPanel(
                this::onRegisterSuccess,    // 가입 성공 콜백
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

    // 로그인 성공 시
    private void onLogin(Long userId, String userName) {
        this.myId = userId;
        this.myName = userName;

        System.out.println("로그인 성공! ID: " + userId + ", 이름: " + userName);

        //컨트롤러 초기화!!! 하고 챗 패널 연결!
        chatController.initialize(chatPanel, userId);

        // 채팅 화면으로 전환
        chatPanel.setUserInfo(userId, userName);
        cardLayout.show(mainPanel, "CHAT");

        // 입장 메세지
       chatPanel.addSystemMessage(userName + "님이 입장하셨습니다.");
    }

    // 회원가입 성공 시
    private void onRegisterSuccess(String userName) {
        System.out.println("회원가입 성공! 이름: " + userName);
        JOptionPane.showMessageDialog(this,
                "회원가입이 완료되었습니다!\n로그인해주세요.",
                "가입 완료",
                JOptionPane.INFORMATION_MESSAGE);
        showLoginPanel();
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