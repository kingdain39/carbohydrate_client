package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color; //색갈
import java.time.LocalDateTime;
import java.time.LocalTime; //시간
import javax.swing.SwingUtilities;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


import controller.ChatController; //컨트롤러랑만 연결!!

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

	private ChatController controller;  // 컨트롤러 필드로주입
	private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


	private Long myId;
    private String myName;

	//얘네들은 컨트롤러에서 받아오는 전송버튼눌렀는지/입장했는지/퇴장했는지 변수
	private Consumer<String> onSendMessageListener; //얘는 콜백 구현을 해야해서 Consumer로 구현 함.
	private Runnable onJoinListener;
	private Runnable onDisconnectListener;



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
	    inOneSuLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
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

	    sendButton.addActionListener(e -> sendMessage());
	    inputField.addActionListener(e -> sendMessage()); //엔터쳐도 들어갈 수 있게!!

	}

	//컨트롤러세터
	public void setController(ChatController controller) {
		this.controller = controller;
	}

	public void setOnSendMessage(Consumer<String> listener) {
		this.onSendMessageListener = listener;
	}

	//입장 버튼 클릭 이벤트 리스너 설정
	public void setOnJoin(Runnable listener) {
		this.onJoinListener = listener;
	}

	//연결됏는지 아닌지 판단할 변수 세터
	public void setOnDisconnect(Runnable listener) {
		this.onDisconnectListener = listener;
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

		if (onSendMessageListener != null) {
			onSendMessageListener.accept(content);
		}

	    // 입력창 초기화
	    inputField.setText("");
	}

	//-----------------------------------채팅창에 메세지 입력하는 메서드--------------------
	public void addPublicMessage(String senderName, String content, LocalDateTime timestamp) {
		SwingUtilities.invokeLater(() -> {
			//controller에게 인자 값으로 이름, 내용, 시간 받음
			StyledDocument doc = chatArea.getStyledDocument(); //스타일doc으로 채팅area설정함. (색넣어야해서)
			String time = timestamp.format(timeFormatter); // LocalDateTime을 "HH:mm" 형식으로

			try {
				SimpleAttributeSet style = new SimpleAttributeSet(); //글씨 스타일 변경위해서 style객체 생성
				StyleConstants.setForeground(style, Color.BLACK); // 글자색 = 검정

				String message = senderName + ": " + content + "  | " + time + "\n\n"; //이건 채팅창에 띄울 메세지 포맷
				doc.insertString(doc.getLength(), message, style); //doc에 해당 메세지를 넣어줌


				chatArea.setCaretPosition(doc.getLength());// 스크롤을 맨 아래로
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		});
	}

	//---------------------------잊방 메세지!-------------------
	public void addSystemMessage(String content) {
		SwingUtilities.invokeLater(() -> {
		StyledDocument doc = chatArea.getStyledDocument();
		String time = LocalDateTime.now().format(timeFormatter);

		try {
			SimpleAttributeSet style = new SimpleAttributeSet();
			StyleConstants.setForeground(style, new Color(128, 128, 128)); // 회색

			String message = "[시스템] " + content + "  | " + time + "\n\n";
			doc.insertString(doc.getLength(), message, style);

			chatArea.setCaretPosition(doc.getLength());

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		});
	}

	//--------------------귓속말 메세지-----------------위에랑 다른거 별로 없음 걍 복붙
	public void addWhisperMessage(String senderName, String recipientName, String content, LocalDateTime timestamp) {
		SwingUtilities.invokeLater(() -> {
		StyledDocument doc = chatArea.getStyledDocument();
		String time = timestamp.format(timeFormatter);

		try {
			SimpleAttributeSet style = new SimpleAttributeSet();
			StyleConstants.setForeground(style, new Color(255, 105, 180)); // 귓속말은 핑크색!

			String message = "(" + senderName + " → " + recipientName + "): " + content + "  | " + time + "\n\n"; // 귓속말 메세지 포맷 -> (보낸사람 -> 받는사람): 내용
			doc.insertString(doc.getLength(), message, style); //그거 또doc에 삽입

			chatArea.setCaretPosition(doc.getLength()); //커서위치를 문자열 맨 마지막에 위치시키기

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		});
	}


	// 접속자 수 업데이트
	public void updateUserCount(int count) {
		SwingUtilities.invokeLater(() -> {
		inOneSuLabel.setText("👤 " + count);
		});
	}

}
