package twins.boundaries;

import java.util.HashMap;
import java.util.Map;

public class UserBoundary {

	private UserIdBoundary userId;
	private String role;
	private String userName;
	private String avatar;

	public UserBoundary() {
		userId=new UserIdBoundary();

	}
	
	public UserBoundary(UserIdBoundary userId, String role, String userName, String avatar) {

		this.userId = userId;
		this.role = role;
		this.userName = userName;
		this.avatar = avatar;
		
	}
	


	public UserIdBoundary getUserId() {
		return userId;
	}

	public void setUserId(UserIdBoundary userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
