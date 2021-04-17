package twins.boundaries;

public class UserIdBoundary {
	private String space;
	private String email;
	
	public UserIdBoundary() {
		
	}

	public UserIdBoundary(String space, String email) {
		this.space = space;
		this.email = email;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	

}
