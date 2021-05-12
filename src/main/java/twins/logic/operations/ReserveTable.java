package twins.logic.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twins.data.ItemHandler;

@Component
public class ReserveTable {
	private ItemHandler itemHandler;
	
	@Autowired
	public ReserveTable(ItemHandler itemHandler) {
		this.itemHandler = itemHandler;
	}

	public void reserve(String itemId, String time, String name) {
		// TODO Auto-generated method stub
		//get the item entity by itemid and update it
	}
	
	public String checkReservationTime(int numOfPeople, String time) {
		//get all tables with same capacity(capacity==numOfPeople)
		
		//check which table empty
		
		//return it if exists
		
		return null;
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

}
