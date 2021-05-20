package twins.logic.operations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import twins.data.ItemEntity;
import twins.data.ItemHandler;


public class UpdateTablesMap {
	private ItemHandler itemHandler;
	
	@Autowired
	public UpdateTablesMap(ItemHandler itemHandler) {
		this.itemHandler = itemHandler;
	}
	
	public void updateTable(String tableNumber,String newCapacity,  String userSpace, String userEmail, String itemId) {
		List<ItemEntity> tables = itemHandler.findAllByType("Table");
		for (ItemEntity table : tables) {
			if (table.getItemAttributes().get("name").equals(tableNumber)) {
				table.getItemAttributes().put("capacity", newCapacity);
				this.itemHandler.save(table);
			}
		}
		
	}
	
	public boolean hasReservation(String tableNumber) {
		List<ItemEntity> activeReservations = itemHandler.findAllByTypeAndActive("reservation",true);
		
		for(ItemEntity reservation : activeReservations) {
			if(reservation.getName().split("-")[0].equals(tableNumber)) {
				return true;
			}
		}
		
		return false;
	}
}
