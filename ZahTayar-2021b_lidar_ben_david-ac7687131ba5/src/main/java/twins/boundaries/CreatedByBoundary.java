package twins.boundaries;

public class CreatedByBoundary {
	private UserIdBoundary UserId;
	
	

	public CreatedByBoundary() {
		UserId = new UserIdBoundary();
	}


	public CreatedByBoundary(UserIdBoundary userId) {
		UserId = userId;
	}


	public UserIdBoundary getUserId() {
		return UserId;
	}


	public void setUserId(UserIdBoundary userId) {
		UserId = userId;
	}
	
	
	

}
