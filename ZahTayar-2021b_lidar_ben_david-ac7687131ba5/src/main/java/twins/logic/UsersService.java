package twins.logic;



import java.util.List;

import twins.boundaries.UserBoundary;

public interface UsersService {
	
	public UserBoundary createUser(UserBoundary user);
	
	public UserBoundary login(String userSpace, String userEmail );
	
	public void updateUser(String userSpace, String userEmail, UserBoundary upadte);
	
	@Deprecated public List<UserBoundary> getAllUsers(String adminSpace, String adminMail);
	
	public void deleteAllUsers(String adminSpace, String adminMail);
	
}
