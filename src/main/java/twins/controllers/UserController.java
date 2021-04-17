package twins.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.boundaries.NewUserDetails;
import twins.boundaries.UserBoundary;
import twins.boundaries.UserIdBoundary;
import twins.logic.UsersService;

@RestController
public class UserController {
	public UsersService usersService;
	
	@Autowired
	public UserController(UsersService usersService) {
		this.usersService=usersService;
	}
	@RequestMapping(
			path="/twins/users/login/{userSpace}/{userEmail}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getUserDetails(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail){
		
		return this.usersService.login(userSpace, userEmail);	
	}
	
	@RequestMapping(path="/twins/users", 
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary CreateNewUser(
			@RequestBody NewUserDetails userDetails)
	{
		
		UserIdBoundary userid=new UserIdBoundary();
		userid.setEmail(userDetails.getEmail());
		UserBoundary userBou=new UserBoundary(userid,userDetails.getRole()
				,userDetails.getUserName(),userDetails.getAvatar());
				
		return this.usersService.createUser(userBou);
		
		
	}
	@RequestMapping(path="/twins/users/{userSpace}/{userEmail}",
			method=RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserDetails(
			@RequestBody UserBoundary userBoundary,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail)
	{
		this.usersService.updateUser(userSpace, userEmail, userBoundary);

	}

}
