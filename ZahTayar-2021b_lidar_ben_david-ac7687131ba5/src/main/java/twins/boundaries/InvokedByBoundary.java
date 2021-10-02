package twins.boundaries;

public class InvokedByBoundary {
	private UserIdBoundary UserId;
	
	

	public InvokedByBoundary() {
		UserId = new UserIdBoundary();
	}



	public InvokedByBoundary(UserIdBoundary userId) {
		UserId = userId;
	}



	public UserIdBoundary getUserId() {
		return UserId;
	}



	public void setUserId(UserIdBoundary userId) {
		UserId = userId;
	}

}
