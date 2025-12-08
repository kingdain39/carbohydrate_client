package ui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.BiConsumer;

public class LoginPanel extends JPanel {

    private JTextField tfUserName;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JButton btnSignup;

    private BiConsumer<String, String> onLoginAttempt;
    private Runnable onRegisterClick;

    public LoginPanel(BiConsumer<String, String> onLoginAttempt, Runnable onRegisterClick) {
        this.onLoginAttempt = onLoginAttempt;
        this.onRegisterClick = onRegisterClick;
        initUI();
    }

    private void initUI() {
        // 아연이거 사용
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("아이디:"));
        tfUserName = new JTextField();
        add(tfUserName);

        add(new JLabel("비밀번호:"));
        pfPassword = new JPasswordField();
        add(pfPassword);

        btnLogin = new JButton("로그인");
        add(btnLogin);

        btnSignup = new JButton("회원가입");
        add(btnSignup);

        // 이벤트처리
        btnLogin.addActionListener(e -> attemptLogin());
        pfPassword.addActionListener(e -> attemptLogin());
        btnSignup.addActionListener(e -> onRegisterClick.run());
    }

    private void attemptLogin() {
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
        pfPassword.setText("");

        // ChatClientApp에게 로그인 시도 알림
        onLoginAttempt.accept(username, password);

    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "로그인 오류", JOptionPane.ERROR_MESSAGE);
    }
}
