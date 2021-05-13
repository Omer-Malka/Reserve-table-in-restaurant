package twins.boundaries;

public class NewUserDetails {

	private String email;
	private String role;
	private String username;
	private String avatar;
	
	public NewUserDetails() {
		
	}
	
	
	public NewUserDetails(String email, String role, String userName, String avatar) {
		this();
		this.email = email;
		this.role = role;
		this.username = userName;
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUserName() {
		return username;
	}
	public void setUserName(String userName) {
		this.username = userName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
