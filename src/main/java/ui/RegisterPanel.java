package ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class RegisterPanel extends JPanel {

    private JTextField tfUserName;
    private JPasswordField pfPassword;
    private JButton btnSignup;
    private JButton btnGoLogin;

    private BiConsumer<String, String> onSignupAttempt; // username, password
    private Runnable onBackClick;

    public RegisterPanel(BiConsumer<String, String> onSignupAttempt, Runnable onBackClick) {
        this.onSignupAttempt = onSignupAttempt;
        this.onBackClick = onBackClick;
        initUI();
    }

    private void initUI() {
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("아이디:"));
        tfUserName = new JTextField();
        add(tfUserName);

        add(new JLabel("비밀번호:"));
        pfPassword = new JPasswordField();
        add(pfPassword);

        btnSignup = new JButton("회원가입");
        add(btnSignup);

        btnGoLogin = new JButton("로그인으로");
        add(btnGoLogin);

        // 이벤트
        btnSignup.addActionListener(e -> attemptSignup());
        btnGoLogin.addActionListener(e -> onBackClick.run());
    }

    private void attemptSignup() {
        String username = tfUserName.getText().trim();
        String password = new String(pfPassword.getPassword());

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디를 입력해주세요!", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요!", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 입력 필드 초기화
        tfUserName.setText("");
        pfPassword.setText("");

        // ChatClientApp에게 회원가입 시도 알림
        onSignupAttempt.accept(username, password);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "회원가입 오류", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "회원가입 성공", JOptionPane.INFORMATION_MESSAGE);
    }
}
