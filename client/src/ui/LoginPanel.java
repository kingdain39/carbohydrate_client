package ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class LoginPanel extends JPanel {

    private JTextField nicknameField;
    private JPasswordField passwordField;
    private JButton enterButton;
    private JButton registerButton;

    private BiConsumer<Long, String> onLoginSuccess;
    private Runnable onRegisterClick;

    public LoginPanel(BiConsumer<Long, String> onLoginSuccess, Runnable onRegisterClick) {
        this.onLoginSuccess = onLoginSuccess;
        this.onRegisterClick = onRegisterClick;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 제목
        JLabel titleLabel = new JLabel("환영합니다!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 40, 5, 40);
        add(titleLabel, gbc);

        // 부제목
        JLabel subtitleLabel = new JLabel("채팅을 시작해보세요!", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 40, 40, 40);
        add(subtitleLabel, gbc);

        // 닉네임 라벨
        JLabel nickLabel = new JLabel("닉네임");
        nickLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 40, 5, 40);
        add(nickLabel, gbc);

        // 닉네임 입력창
        nicknameField = new JTextField();
        nicknameField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        nicknameField.setPreferredSize(new Dimension(300, 45));
        nicknameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 40, 15, 40);
        add(nicknameField, gbc);

        // 비밀번호 라벨
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 40, 5, 40);
        add(pwLabel, gbc);

        // 비밀번호 입력창
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 45));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 40, 25, 40);
        add(passwordField, gbc);

        // 입장 버튼
        enterButton = new JButton("입장하기");
        enterButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        enterButton.setPreferredSize(new Dimension(300, 50));
        enterButton.setBackground(new Color(30, 80, 60));
        enterButton.setForeground(Color.WHITE);
        enterButton.setFocusPainted(false);
        enterButton.setBorderPainted(false);
        enterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 40, 10, 40);
        add(enterButton, gbc);

        // 회원가입 버튼
        registerButton = new JButton("회원가입");
        registerButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        registerButton.setPreferredSize(new Dimension(300, 45));
        registerButton.setBackground(Color.WHITE);
        registerButton.setForeground(new Color(30, 80, 60));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(30, 80, 60)));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 40, 40, 40);
        add(registerButton, gbc);

        // 이벤트
        enterButton.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin());
        registerButton.addActionListener(e -> onRegisterClick.run());
    }

    private void attemptLogin() {
        String nickname = nicknameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (nickname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "닉네임을 입력해주세요!", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요!", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: 실제 서버 로그인 요청
        // 지금은 테스트용 임시 ID
        Long tempUserId = (long) (Math.random() * 10000);

        // 입력 필드 초기화
        passwordField.setText("");

        onLoginSuccess.accept(tempUserId, nickname);
    }
}