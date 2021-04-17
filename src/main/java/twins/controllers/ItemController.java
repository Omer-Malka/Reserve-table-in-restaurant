package twins.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import twins.boundaries.ItemBoundary;
import twins.logic.ItemsService;

@RestController
public class ItemController {
	private ItemsService itemService;
	
	
	@Autowired
	public ItemController(ItemsService itemService) {
		this.itemService = itemService;
	}

	@RequestMapping(
			path="/twins/items/{userSpace}/{userEmail}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary[] getItemsOfSpecificUser(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail){
		List<ItemBoundary> allItems=itemService.getAllItems(userSpace, userEmail);
		return allItems.toArray(new ItemBoundary[0]);
	}

	@RequestMapping(
			path="/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary getSpecificItem(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId){
		return this.itemService.getSpecificItem(userSpace,userEmail,itemSpace,itemId);
	}

	@RequestMapping(
			path="/twins/items/{userSpace}/{userEmail}", 
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary storeItem(
			@RequestBody ItemBoundary input,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail){
		return this.itemService.createItem(userSpace, userEmail, input);	
	}
	
	@RequestMapping(
			path="/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", 
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void UpdateItem(
			@RequestBody ItemBoundary update,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId
			){
		this.itemService.updateItem(userSpace, userEmail, itemSpace, itemId, update);	
	}

}
