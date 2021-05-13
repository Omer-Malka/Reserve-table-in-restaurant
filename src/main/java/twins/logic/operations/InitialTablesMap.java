package twins.logic.operations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twins.boundaries.ItemBoundary;
import twins.boundaries.ItemIdBoundary;
import twins.data.ItemEntity;
import twins.data.ItemHandler;
import twins.logic.ItemsService;
import twins.logic.ItemsServiceImplementation;

@Component
public class InitialTablesMap {
	private static boolean isInitialized = false;
	private ItemsServiceImplementation itemService;
	private ItemHandler itemHandler;
	private final String HOURS_IN_DAY_ARR[] = { "10-10.5", "10.5-11", "11.5-12",
			"12-12.5", "12.5-13", "13-13.5", "13.5-14", "14-14.5", "15-15.5", "15.5-16", "16-16.5",
			"16.5-17", "17.5-18", "18-18.5", "18.5-19", "19-19.5", "19.5-20", "20-20.5", "20.5-21",
			"21-21.5", "21.5-22" };
	
	@Autowired
	public InitialTablesMap(ItemHandler itemHandler) {
		this.itemService = new ItemsServiceImplementation(itemHandler);
		this.itemHandler = itemHandler;
	}
	
	public static boolean isInitialized() {
		return isInitialized;
	}

	public void storeTable(Map<String,String> tablesDetails, String userSpace, String userEmail, String itemId) {
		ItemBoundary item = new ItemBoundary();
		item.setName(tablesDetails.get("tableNumber"));
		item.setType("Table");
		item.getItemAttributes().put("capacity", tablesDetails.get("capacity"));
		item.getItemAttributes().put("occupancyTime", this.initialHoursMap());
		ItemEntity entity = this.itemService.convertToEntity(item);
		entity.setUserSpace(userSpace);
		entity.setUserEmail(userEmail);
		//generate id + timestamp
		entity.setCreatedTimestamp(new Date());
		entity.setItemId(itemId);
		this.itemHandler.save(entity);
		isInitialized = true;
	}

	private Map<String,String> initialHoursMap() {
		Map<String,String> rv = new HashMap<>();
		for(int i=0;i<HOURS_IN_DAY_ARR.length;i++) {
			rv.put(HOURS_IN_DAY_ARR[i], "");
		}
		return rv;
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
