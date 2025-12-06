package service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;


//접속자 관리 서비스
public class UserStateService {
	private final Set<String> activeUsers=new HashSet<>();
	private Consumer<Set<String>> onUserListUpdated;
	
	 public void setUserListListener(Consumer<Set<String>> listener) {
	        this.onUserListUpdated = listener;
	    }
	 
	 public void updateActiveUsers(Set<String> users) {
		 this.activeUsers.clear();
		 this.activeUsers.addAll(users);
		 
		 if(onUserListUpdated!=null) {
			 onUserListUpdated.accept(new HashSet<>(activeUsers));
		 }
	 }
	 
	 public Set<String> getActiveUsers() {
	        return new HashSet<>(activeUsers);
	    }
	 
	   public boolean isUserActive(String username) {
	        return activeUsers.contains(username);
	    }

	public Long getUserIdByName(String recipientName) {
		// TODO Auto-generated method stub
		return null;
	}
}
