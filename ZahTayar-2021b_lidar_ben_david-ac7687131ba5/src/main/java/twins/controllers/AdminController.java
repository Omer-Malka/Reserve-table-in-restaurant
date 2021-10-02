package twins.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twins.boundaries.OperationBoundary;
import twins.boundaries.UserBoundary;
import twins.logic.ItemsService;
import twins.logic.OperationsServiceExtended;
import twins.logic.UserServiceExtended;

@RestController
public class AdminController {

	private UserServiceExtended usersService;
	public OperationsServiceExtended operationsService;
	public ItemsService itemsService;

	@Autowired
	public  AdminController(UserServiceExtended usersService, OperationsServiceExtended operationsService, ItemsService itemsService) {
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
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size",required=false,defaultValue="20")int size,
			@RequestParam(name="page",required=false,defaultValue="0")int page){
		List<UserBoundary> allUsers= this.usersService.getAllUsers(userSpace, userEmail,size,page);
		return allUsers.toArray(new UserBoundary[0]);	
	}


	@RequestMapping(
			path="/twins/admin/operations/{userSpace}/{userEmail}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] getAllOperations(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size",required=false,defaultValue="120")int size,
			@RequestParam(name="page",required=false,defaultValue="0")int page){
		List<OperationBoundary> allOperations = this.operationsService.getAllOperations(userSpace, userEmail,size,page);

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
