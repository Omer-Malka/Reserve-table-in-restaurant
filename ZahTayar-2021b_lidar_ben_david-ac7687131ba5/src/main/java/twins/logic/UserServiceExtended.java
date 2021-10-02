package twins.logic;

import java.util.List;

import twins.boundaries.UserBoundary;

public interface UserServiceExtended extends UsersService{
	public List<UserBoundary> getAllUsers(String adminSpace, String adminMail,int size,int page);
}
