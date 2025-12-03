package ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatPanel extends JPanel {

    private JTextPane chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JLabel userCountLabel;
    private JLabel titleLabel;

    private Long myId;
    private String myName;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public ChatPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === 상단: 헤더 ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 250, 250));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        titleLabel = new JLabel("귓속말 채팅 프로그램");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        userCountLabel = new JLabel("👤 0");
        userCountLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        userCountLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userCountLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // === 중앙: 채팅 영역 ===
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        chatArea.setBackground(new Color(250, 250, 250));

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // === 하단: 입력 영역 ===
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(new Color(250, 250, 250));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        // 기능은 없는데 잇길래 만듦
        JButton plusButton = new JButton("+");
        plusButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        plusButton.setPreferredSize(new Dimension(45, 45));
        plusButton.setBackground(Color.WHITE);
        plusButton.setFocusPainted(false);
        plusButton.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        plusButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        inputField = new JTextField();
        inputField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        inputField.setBackground(Color.WHITE);

        // placeholder 효과
        inputField.setText("메세지를 입력하세요...");
        inputField.setForeground(Color.GRAY);
        inputField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (inputField.getText().equals("메세지를 입력하세요...")) {
                    inputField.setText("");
                    inputField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (inputField.getText().isEmpty()) {
                    inputField.setText("메세지를 입력하세요...");
                    inputField.setForeground(Color.GRAY);
                }
            }
        });

        sendButton = new JButton("➤");
        sendButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        sendButton.setPreferredSize(new Dimension(50, 45));
        sendButton.setBackground(new Color(30, 80, 60));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        inputPanel.add(plusButton, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // 이벤트 달아놓음. 전송버튼 클릭
        sendButton.addActionListener(e -> sendMessage());
        //엔터로 쳣을때도 보내짐
        inputField.addActionListener(e -> sendMessage());
    }

    public void setUserInfo(Long userId, String userName) {
        this.myId = userId;
        this.myName = userName;
    }

    private void sendMessage() {
        String content = inputField.getText().trim();
        if (content.isEmpty() || content.equals("메세지를 입력하세요...")) return;

        // 내 메시지 표시
        appendMessage(myName, content, false, false);

        // TODO: 실제 서버로 전송

        inputField.setText("");
    }

    // 시스템 메시지 (입장/퇴장)
    public void addSystemMessage(String message) {
        appendMessage(null, message, true, false);
    }

    // 귓속말 메시지
    public void addWhisperMessage(String sender, String recipient, String content, boolean isSent) {
        String prefix = isSent ? "(나→" + recipient + " <보냄>)" : "(" + sender + "→나 <받음>)";
        appendMessageWithStyle(prefix, content, new Color(200, 50, 50), true);
    }

    // 메시지 추가
    public void appendMessage(String sender, String content, boolean isSystem, boolean isWhisper) {
        StyledDocument doc = chatArea.getStyledDocument();
        String time = LocalTime.now().format(timeFormatter);

        try {
            if (isSystem) {
                // 시스템 메시지 (입장/퇴장)
                SimpleAttributeSet style = new SimpleAttributeSet();
                StyleConstants.setForeground(style, new Color(200, 50, 50));

                // "입장" 부분만 빨간색으로
                String text = content.replace("입장", "");
                int idx = content.indexOf("입장");

                if (idx >= 0) {
                    String before = content.substring(0, idx);
                    String after = content.substring(idx + 2);

                    SimpleAttributeSet normalStyle = new SimpleAttributeSet();
                    StyleConstants.setForeground(normalStyle, new Color(100, 100, 100));

                    SimpleAttributeSet redStyle = new SimpleAttributeSet();
                    StyleConstants.setForeground(redStyle, new Color(200, 50, 50));

                    doc.insertString(doc.getLength(), before, normalStyle);
                    doc.insertString(doc.getLength(), "입장", redStyle);
                    doc.insertString(doc.getLength(), after + " | " + time + "\n\n", normalStyle);
                } else {
                    doc.insertString(doc.getLength(), content + " | " + time + "\n\n", style);
                }
            } else {
                // 일반 메시지
                SimpleAttributeSet style = new SimpleAttributeSet();
                StyleConstants.setForeground(style, Color.BLACK);

                String message = sender + ": " + content + "  | " + time + "\n\n";
                doc.insertString(doc.getLength(), message, style);
            }

            chatArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // 스타일지정 메시지 추가(귓속말처리해야해서)
    private void appendMessageWithStyle(String prefix, String content, Color color, boolean isWhisper) {
        StyledDocument doc = chatArea.getStyledDocument();
        String time = LocalTime.now().format(timeFormatter);

        try {
            SimpleAttributeSet style = new SimpleAttributeSet();
            StyleConstants.setForeground(style, color);

            String message = prefix + ": " + content + "  | " + time + "\n\n";
            doc.insertString(doc.getLength(), message, style);

            chatArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // 접속자 수 업데이트
    public void updateUserCount(int count) {
        userCountLabel.setText("👤 " + count);
    }
}

