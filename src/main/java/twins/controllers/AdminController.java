package twins.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import twins.boundaries.OperationBoundary;
import twins.boundaries.UserBoundary;
import twins.logic.ItemsService;
import twins.logic.OperationsService;
import twins.logic.UsersService;

@RestController
public class AdminController {

	public UsersService usersService;
	public OperationsService operationsService;
	public ItemsService itemsService;

	@Autowired
	public  AdminController(UsersService usersService, OperationsService operationsService, ItemsService itemsService) {
		this.usersService=usersService;
		this.operationsService=operationsService;
		this.itemsService=itemsService;
	}

	@RequestMapping(
			path="/twins/admin/users/{userSpace}/{userEmail}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getAllUsers(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail){
		List<UserBoundary> allUsers= this.usersService.getAllUsers(userSpace, userEmail);
		return allUsers.toArray(new UserBoundary[0]);	
	}


	@RequestMapping(
			path="/twins/admin/operations/{userSpace}/{userEmail}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] getAllOperations(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail){
		List<OperationBoundary> allOperations = this.operationsService.getAllOperations(userSpace, userEmail);

		return allOperations.toArray(new OperationBoundary[0]);	
	}

	@RequestMapping(path="/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void DeleteAllUsersInSpace(
			@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) 
	{
		this.usersService.deleteAllUsers(space, email);

	}


	@RequestMapping(path="/twins/admin/items/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void DeleteAllItemsInSpace(
			@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) 
	{
		this.itemsService.deleteAllItems(space, email);
	}


	@RequestMapping(path="/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void DeleteAllOperationsInSpace(
			@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) 
	{
		this.operationsService.deleteAllOperations(space,email);
	}

}
