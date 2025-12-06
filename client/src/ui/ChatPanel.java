package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ChatPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final JScrollPane chatScreen = new JScrollPane();
	private JLabel headerLable;
	private JLabel inOneSuLabel;
	private JPanel headerPanel;
	private JTextPane chatArea;
	private JTextField inputField;
	private JButton sendButton;
	private JPanel inputPanel;
	private JScrollPane scrollPane;


	private Long myId;
    private String myName;

	/**
	 * Create the panel.
	 */
	public ChatPanel() {
	    setLayout(new BorderLayout(0, 0));

	    headerPanel = new JPanel();
	    headerPanel.setLayout(new BorderLayout(0, 0));
	    headerPanel.setPreferredSize(new Dimension(0, 60));
	    add(headerPanel, BorderLayout.NORTH);

	    headerLable = new JLabel("귓속말채팅 프로그램");
	    headerLable.setFont(new Font("맑은 고딕", Font.BOLD, 18));
	    headerPanel.add(headerLable, BorderLayout.WEST);

	    inOneSuLabel = new JLabel("👤 0");
	    inOneSuLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
	    headerPanel.add(inOneSuLabel, BorderLayout.EAST);

	    chatArea = new JTextPane();
	    chatArea.setEditable(false);  //챗area를 수정불가하게 만들기 위함!!!!!!!!
	    chatArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

	    scrollPane = new JScrollPane(chatArea);
	    add(scrollPane, BorderLayout.CENTER);

	    inputPanel = new JPanel();
	    inputPanel.setLayout(new BorderLayout(10, 0));
	    add(inputPanel, BorderLayout.SOUTH);

	    inputField = new JTextField();
	    inputField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
	    inputPanel.add(inputField, BorderLayout.CENTER);

	    sendButton = new JButton("전송");
	    sendButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
	    inputPanel.add(sendButton, BorderLayout.EAST);

	    // 이벤트 리스너
	    sendButton.addActionListener(e -> sendMessage());
	    inputField.addActionListener(e -> sendMessage());

	}

	//현재 사용자 정보 세터
	public void setUserInfo(Long userId, String userName) {
	    this.myId = userId;
	    this.myName = userName;
	}


	// 메시지 전송
	private void sendMessage() {
	    String content = inputField.getText().trim();

	    //빈 메시지 체크
	    if (content.isEmpty()) {
	        return;
	    }

	    // TODO: 나중에 서버로 전송
	    System.out.println(myName + ": " + content);

	    // 입력창 초기화
	    inputField.setText("");
	}

	// 접속자 수 업데이트
	public void updateUserCount(int count) {
	    inOneSuLabel.setText("👤 " + count);
	}

}
