package twins.logic.operations;

import java.util.HashMap;
import java.util.Map;
import twins.boundaries.ItemBoundary;
import twins.data.ItemHandler;
import twins.logic.ItemsService;
import twins.logic.ItemsServiceImplementation;

public class InitialTablesMap {
	private ItemsService itemService;
	private static boolean isInitialized;
	private ItemHandler itemHandler;
	private final String HOURS_IN_DAY_ARR[] = {"10-10.5","10.5-11","11.5-12",
			"12-12.5","12.5-13","13-13.5","13.5-14","14-14.5","15-15.5","15.5-16","16-16.5",
			"16.5-17","17.5-18","18-18.5","18.5-19","19-19.5","19.5-20","20-20.5","20.5-21",
			"21-21.5","21.5-22"};
	
	public InitialTablesMap() {
		isInitialized = false; 
		this.itemService = new ItemsServiceImplementation(itemHandler);
	}
	
	public static boolean isInitialized() {
		return isInitialized;
	}

	public void initial(Map<String,Object> tablesItems, String userSpace, String userEmail) {
		for(Map.Entry<String,Object> entry : tablesItems.entrySet()) {
			ItemBoundary item = (ItemBoundary) entry.getValue();
			Map<String,String> hoursMap = this.initialHoursMap();
			item.getItemAttributes().put("OccupancyTime", hoursMap);
			itemService.createItem(userSpace, userEmail, item);
		}
		isInitialized = true;
	}

	private Map<String,String> initialHoursMap() {
		Map<String,String> rv = new HashMap<>();
		for(int i=0;i<HOURS_IN_DAY_ARR.length;i++) {
			rv.put(HOURS_IN_DAY_ARR[i], "");
		}
		return rv;
	}
	
	/* type:table
	 * created by: who created it 
	 * item attributes: 
	 * capacity: int
	 * OccupancyTime: map of hours and who is reserved it for example : ("10-12": "lidar", "12-14": "")
	 */
}
