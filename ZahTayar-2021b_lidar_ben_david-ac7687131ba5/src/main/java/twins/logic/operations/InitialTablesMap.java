package twins.logic.operations;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twins.boundaries.ItemBoundary;
import twins.data.ItemEntity;
import twins.data.ItemHandler;
import twins.data.UserHandler;
import twins.logic.ItemsServiceImplementation;

@Component
public class InitialTablesMap {
	private static boolean isInitialized = false;
	private ItemsServiceImplementation itemService;
	private ItemHandler itemHandler;
	
	@Autowired
	public InitialTablesMap(ItemHandler itemHandler,UserHandler userHandler) {
		this.itemService = new ItemsServiceImplementation(itemHandler,userHandler);
		this.itemHandler = itemHandler;
	}
	
	public static boolean isInitialized() {
		return isInitialized;
	}

	public void storeTable(Map<String,Object> tablesDetails, String userSpace, String userEmail, String itemId) {
		ItemBoundary item = new ItemBoundary();
		item.setName((String) tablesDetails.get("tableNumber"));
		item.setType("Table");
		item.getItemAttributes().put("capacity", tablesDetails.get("capacity"));
		ItemEntity entity = this.itemService.convertToEntity(item);
		entity.setUserSpace(userSpace);
		entity.setUserEmail(userEmail);
		//generate id + timestamp
		entity.setCreatedTimestamp(new Date());
		entity.setItemId(itemId);
		this.itemHandler.save(entity);
		isInitialized = true;
	}

	 
	/* post: operation attributes:
	 * tableNumber: String 
	 * capacity: String
	 */
	
	/* get: item attributes(Map<String,Object>) :
	 * tableNumber: String 
	 * capacity: String
	 * OccupancyTime: Map<String,String> of hours and who is reserved it for example : ("10-12": "lidar", "12-14": "")
	 */
}
