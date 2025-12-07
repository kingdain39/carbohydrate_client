package ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class RegisterPanel extends JPanel {

    private JTextField nicknameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;

    private Consumer<String> onRegisterSuccess;
    private Runnable onBackClick;

    public RegisterPanel(Consumer<String> onRegisterSuccess, Runnable onBackClick) {
        this.onRegisterSuccess = onRegisterSuccess;
        this.onBackClick = onBackClick;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 뒤로가기 버튼 (상단)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        backButton = new JButton("← 뒤로");
        backButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(new Color(30, 80, 60));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topPanel.add(backButton);
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 0, 40);
        add(topPanel, gbc);

        // 제목
        JLabel titleLabel = new JLabel("회원가입", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 40, 5, 40);
        add(titleLabel, gbc);

        // 부제목
        JLabel subtitleLabel = new JLabel("새 계정을 만들어보세요!", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 40, 30, 40);
        add(subtitleLabel, gbc);

        // 닉네임 라벨
        JLabel nickLabel = new JLabel("닉네임");
        nickLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        gbc.gridy = 3;
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
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 40, 15, 40);
        add(nicknameField, gbc);

        // 비밀번호 라벨
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        gbc.gridy = 5;
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
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 40, 15, 40);
        add(passwordField, gbc);

        // 비밀번호 확인 라벨
        JLabel confirmLabel = new JLabel("비밀번호 확인");
        confirmLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 40, 5, 40);
        add(confirmLabel, gbc);

        // 비밀번호 확인 입력창
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(300, 45));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 40, 25, 40);
        add(confirmPasswordField, gbc);

        // 가입하기 버튼
        registerButton = new JButton("가입하기");
        registerButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        registerButton.setPreferredSize(new Dimension(300, 50));
        registerButton.setBackground(new Color(30, 80, 60));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 9;
        gbc.insets = new Insets(10, 40, 40, 40);
        add(registerButton, gbc);

        // 이벤트
        registerButton.addActionListener(e -> attemptRegister());
        confirmPasswordField.addActionListener(e -> attemptRegister());
        backButton.addActionListener(e -> {
            clearFields();
            onBackClick.run();
        });
    }

    private void attemptRegister() {
        String nickname = nicknameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // 검증
        if (nickname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "닉네임을 입력해주세요!", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요!", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다!", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "비밀번호는 4자 이상이어야 합니다!", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: 실제 서버 회원가입 요청

        clearFields();
        onRegisterSuccess.accept(nickname);
    }

    private void clearFields() {
        nicknameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}