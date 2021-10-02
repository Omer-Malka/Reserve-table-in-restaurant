package twins.helpers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import twins.data.UserEntity;
import twins.data.UserHandler;
import twins.data.UserRole;

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
	public boolean CheckValidUser(String id) {
		//get user from db by space and email and id 
		Optional<UserEntity> retuUser=userHandler.findById(id);
		if(retuUser.isPresent()) {
			return true;
		}
		//if user not exist false 
		return false;
		
	}
	public boolean CheckAdminUser(String id) {
		//get user from db by space and email and id 
		Optional<UserEntity> returUser=userHandler.findById(id);
		if(!returUser.isPresent())
			return false;//if user not exist false
				 
		UserEntity user=returUser.get();//get object from optional
		//check if user is admin 
		if(!user.getRole().equalsIgnoreCase(UserRole.ADMIN.name()))
			return false;//if not -->false 
		
		return true ;
	}
	public boolean CheckManagerUser(String id) {
		//get user from db by space and email and id 
		Optional<UserEntity> returUser=userHandler.findById(id);
		if(!returUser.isPresent()) {
			System.out.println("no user");
			return false;//if user not exist false
		}
				 
		UserEntity user=returUser.get();//get object from optional
		//check if user is MANAGER 
		if(!user.getRole().equals(UserRole.MANAGER.name())) {
			return false;//if not -->false 		
		}
		return true ;
	}
	public boolean CheckPlayerUser(String id) {
		//get user from db by space and email and id 
		Optional<UserEntity> returUser=userHandler.findById(id);
		if(!returUser.isPresent())
			return false;//if user not exist false
				 
		UserEntity user=returUser.get();//get object from optional
		//check if user is PLAYER 
		if(!user.getRole().equalsIgnoreCase(UserRole.PlAYER.name())) {
			System.out.println(user.getRole());
			System.out.println(UserRole.PlAYER.name());
			return false;//if not -->false 
		}
		
		return true ;
	}

}
