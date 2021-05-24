package twins.logic.operations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import twins.boundaries.ItemBoundary;

import twins.data.ItemEntity;
import twins.data.ItemHandler;
import twins.logic.ItemsServiceImplementation;

public class ViewTableMap {
	/*private ItemHandler itemHandler;
	private ItemsServiceImplementation itemsService;

	@Autowired
	public ViewTableMap(ItemHandler itemHandler) {
		this.itemHandler = itemHandler;
	}

	public List<ItemBoundary> getAllItemsByPlayer(String userEmail) {
		List<ItemEntity> activeReservations = itemHandler.findAllByEmail(userEmail);
		
		List<ItemBoundary> rv = new ArrayList<>();
		for (ItemEntity reservation : activeReservations) {
			rv.add(this.itemsService.convertToBoundary(reservation));
		}
		return rv;
	}*/

}
