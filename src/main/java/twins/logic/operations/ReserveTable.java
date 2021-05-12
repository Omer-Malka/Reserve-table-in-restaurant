package twins.logic.operations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twins.logic.ItemsService;
import twins.boundaries.ItemBoundary;
import twins.data.ItemEntity;
import twins.data.ItemHandler;
import twins.logic.ItemsServiceImplementation;

@Component
public class ReserveTable {
	private ItemHandler itemHandler;
	private ItemsServiceImplementation itemService;

	@Autowired
	public ReserveTable(ItemHandler itemHandler) {
		this.itemHandler = itemHandler;
		this.itemService = new ItemsServiceImplementation(itemHandler);
	}

	public void reserve(String itemId, String time, String name) {
		//get the item entity by itemid and update it
		Optional<ItemEntity> itemOp = this.itemHandler.findById(itemId);
		if(itemOp.isPresent()) {
			ItemEntity item = itemOp.get();
			
		}
		else {
			throw new RuntimeException("Item not exists");
		}
			
	}

	public String checkReservationTime(String numOfPeople, String time) {
		//get all tables with same capacity(capacity==numOfPeople)
		List<ItemEntity> tables = itemHandler.findAllByItemAttributesLike("capacity=" + numOfPeople);
		//check which table empty
		for(ItemEntity table : tables) {
			if(checksTime(this.itemService.convertToBoundary(table), time)) {
				//return it if exists
				return table.getItemId();
			}
		}
		return null;
	}

	private boolean checksTime(ItemBoundary table, String time) {
		//checks item attributes.OccupancyTime if the table is empty in this time
		Map<String,Object> tableAttributes = table.getItemAttributes();
		Map<String,String> reservations = (Map<String, String>) tableAttributes.get("occupancyTime");
		//return the checking results
		if(reservations.get(time) != "")
			return true;
		return false;
	}

	public String getBetterOption(int numOfPeople, String time) {
		//suggest a better option of time and return this time
		return time;	
	}
	/*
	 * operationAttributes:
	 * name: String
	 * capacity: int
	 * time: String
	 */

	/* item attributes(Map<String,Object>) :
	 * tableNumber: String 
	 * capacity: Integer
	 * OccupancyTime: Map<String,String> of hours and who is reserved it for example : ("10-12": "lidar", "12-14": "")
	 */

}
