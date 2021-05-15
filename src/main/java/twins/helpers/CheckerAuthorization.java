package twins.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import twins.data.UserHandler;

@Component
public class CheckerAuthorization {
	private UserHandler userHandler;

	@Autowired
	public CheckerAuthorization(UserHandler userHandler) {
		super();
		this.userHandler = userHandler;
	}
	public boolean CheckEmailValid(String email) {
		if (email.contains("@"))
			return true;
		return false;
	}
	public boolean CheckValidUser(String space,String email) {
		//get user from db by space and email and id 
		//if user not exist false 
		return true;
		
		
	}
	public boolean CheckAdminUser(String userSpace, String userEmail) {
		//get user from db by space and email and id 
				//if user not exist false 
		//check if user is admin 
		//if not -->false 
		return true ;
	}
	public boolean CheckManagerUser(String userSpace, String userEmail) {
		//get user from db by space and email and id 
				//if user not exist false 
		//check if user is manager 
		//if not -->false 
		return true ;
	}
	public boolean CheckPlayerUser(String userSpace, String userEmail) {
		//get user from db by space and email and id 
				//if user not exist false 
		//check if user is manager 
		//if not -->false 
		return true ;
	}

}
