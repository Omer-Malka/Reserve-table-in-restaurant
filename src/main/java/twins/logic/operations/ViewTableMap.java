package twins.logic.operations;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import twins.boundaries.ItemBoundary;
import twins.data.ItemEntity;
import twins.data.ItemHandler;
import twins.logic.ItemsServiceImplementation;

public class ViewTableMap {
	private ItemHandler itemHandler;
	private ItemsServiceImplementation itemsService;

	@Autowired
	public ViewTableMap(ItemHandler itemHandler) {
		this.itemHandler = itemHandler;
	}

	public List<ItemBoundary> getAllItemsByPlayer(String email, int size, int page) {
		List<ItemEntity> activeReservations = itemHandler.findAllByEmail(email, PageRequest.of(page, size, Direction.ASC, "id"));
		
		List<ItemBoundary> rv = new ArrayList<>();
		for (ItemEntity reservation : activeReservations) {
			rv.add(this.itemsService.convertToBoundary(reservation));
		}
		return rv;
	}

}
