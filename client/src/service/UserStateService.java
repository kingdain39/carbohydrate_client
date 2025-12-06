package service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;


//접속자 관리 서비스
public class UserStateService {
	private final Set<String> activeUsers=new HashSet<>();
	// username ↔ userId 매핑
    private final Map<String, Long> userMap = new HashMap<>();
	private Consumer<Set<String>> onUserListUpdated;
	
	//외부에서 이벤트 리스너 등록
	 public void setUserListListener(Consumer<Set<String>> listener) {
	        this.onUserListUpdated = listener;
	    }
	// 사용자 목록 갱신 (username, userId 매핑까지 함께 갱신)
	 public void updateActiveUsers(Map<String, Long> users) {
		 this.activeUsers.clear();
		 activeUsers.addAll(users.keySet());
		 
		 userMap.clear();
	     userMap.putAll(users);
		 
		 if(onUserListUpdated!=null) {
			 onUserListUpdated.accept(new HashSet<>(activeUsers));
		 }
	 }
	 
	 // 현재 접속자 목록 반환
	    public Set<String> getActiveUsers() {
	        return new HashSet<>(activeUsers);
	    }

	    // 특정 사용자가 접속 중인지 확인
	    public boolean isUserActive(String username) {
	        return activeUsers.contains(username);
	    }

	    // username으로 userId 조회
	    public Long getUserIdByName(String recipientName) {
	        return userMap.get(recipientName);
	    }
}
