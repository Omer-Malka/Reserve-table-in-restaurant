package twins.logic.operations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twins.data.ItemEntity;
import twins.data.ItemHandler;

@Component
public class ReserveTable {
	private ItemHandler itemHandler;

	@Autowired
	public ReserveTable(ItemHandler itemHandler) {
		this.itemHandler = itemHandler;
	}

	public void reserve(String numOfPeople, String customerName, String tableNum, String time, String userSpace, String userEmail, String space) {
		ItemEntity reservation = new ItemEntity();
		reservation.setName(tableNum + "-" + time);
		reservation.setActive(true);
		reservation.setCreatedTimestamp(new Date());
		reservation.setType("reservation");
		reservation.setItemId(userSpace+"@"+UUID.randomUUID().toString());
		reservation.setUserEmail(userEmail);
		reservation.setUserSpace(space);
		Map<String,Object> attr = new HashMap<>();
		attr.put("name", customerName);
		attr.put("numOfPeople", numOfPeople);
		reservation.setItemAttributes(attr);
		itemHandler.save(reservation);
	}
	//name: numOfTable-time
	//active: true -> occupied
	public ArrayList<String> findTables(String numOfPeople, String time){ // "10", "12-14-13-05"(hourStart-hourEnd-day-month)
		List<ItemEntity> reservations = itemHandler.findAllByTypeAndActive("reservation",true);
		ArrayList<String> occupiedTables = new ArrayList<>();
		for(ItemEntity reservation: reservations) {
			if(reservation.getName().contains(time)) {
				occupiedTables.add(reservation.getName().split("-")[0]);
			}
		}
		List<ItemEntity> tables = itemHandler.findAllByType("Table");
		ArrayList<String> unOccupiedTables = new ArrayList<>();
		for(ItemEntity table:tables) {
			int capacity = Integer.parseInt((String) table.getItemAttributes().get("capacity"));
			int people = Integer.parseInt(numOfPeople);			
			if(!occupiedTables.contains(table.getName())&&capacity>=people){
				unOccupiedTables.add(table.getName());
			}
		}
		return unOccupiedTables;
	}

	/* getting
	 * itemId: reservation Id
	 * itemAttributes:
	 * type: String :"Reservation"
	 * name: String : name of reserver
	 * capacity: String : number of people in the reservation
	 * time: String : time of the reservation
	 */

	/* need to change
	 * item attributes(Map<String,Object>) :
	 * tableNumber: String 
	 * capacity: String
	 * OccupancyTime: Map<String,String> of hours and who is reserved it for example : ("10-12": "lidar", "12-14": "")
	 */

}
