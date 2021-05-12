package twins.logic.operations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
			ItemBoundary table = this.itemService.convertToBoundary(item);
			Map<String,String> reservations = getReservationOfTable(table);
			reservations.put(time, name);
			item.setItemAttributes(this.itemService.marshall(reservations));
			this.itemHandler.save(item);
		}
		else {
			throw new RuntimeException("Item not exists");
		}	
	}

	public String checkReservationTime(String numOfPeople, String time) {
		//get all tables with same capacity(capacity==numOfPeople)
		int capacity = Integer.parseInt(numOfPeople);
		for(int i=0; i<capacity; i++) {
			List<ItemEntity> tables = itemHandler.findAllByItemAttributesLike("capacity=" + String.valueOf(capacity+i));
			//check which table empty
			for(ItemEntity table : tables) {
				if(checksTime(this.itemService.convertToBoundary(table), time)) {
					//return it if exists
					return table.getItemId();
				}
			}
		}
		return null;
	}

	private boolean checksTime(ItemBoundary table, String time) {
		//checks item attributes.OccupancyTime if the table is empty in this time
		Map<String,String> reservations = getReservationOfTable(table);
		//return the checking results
		if(reservations.get(time) != "")
			return true;
		return false;
	}

	private Map<String,String> getReservationOfTable(ItemBoundary table) {
		Map<String,Object> tableAttributes = table.getItemAttributes();
		return (Map<String, String>) tableAttributes.get("occupancyTime");
	}
	/*
	 * operationAttributes:
	 * name: String
	 * capacity: String
	 * time: String
	 */

	/* item attributes(Map<String,Object>) :
	 * tableNumber: String 
	 * capacity: Integer
	 * OccupancyTime: Map<String,String> of hours and who is reserved it for example : ("10-12": "lidar", "12-14": "")
	 */

}
