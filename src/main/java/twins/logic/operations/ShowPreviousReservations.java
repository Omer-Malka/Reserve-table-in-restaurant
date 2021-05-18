package twins.logic.operations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import twins.boundaries.ItemBoundary;
import twins.data.ItemEntity;
import twins.data.ItemHandler;
import twins.data.UserHandler;
import twins.logic.ItemsServiceImplementation;
@Component
public class ShowPreviousReservations {
	private ItemHandler itemHandler;
	private ItemsServiceImplementation itemsService;

	@Autowired
	public ShowPreviousReservations(ItemHandler itemHandler,UserHandler userHandler) {
		this.itemHandler = itemHandler;
		this.itemsService = new ItemsServiceImplementation(itemHandler,userHandler);
	}
	
	public List<ItemBoundary> showReservations(String email) {
		List<ItemEntity> entities = itemHandler.findAllByUserEmailAndType(email, "reservation");
		List<ItemBoundary> rv = new ArrayList<>();
		for(ItemEntity entity: entities) {
			rv.add(this.itemsService.convertToBoundary(entity));
		}
		return rv;
	}
}
