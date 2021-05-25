package twins.logic.operations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import twins.boundaries.ItemBoundary;

import twins.data.ItemEntity;
import twins.data.ItemHandler;
import twins.data.UserHandler;
import twins.logic.ItemsServiceImplementation;

public class ViewTableMap {
	private ItemHandler itemHandler;
	private ItemsServiceImplementation itemsService;

	@Autowired
	public ViewTableMap(ItemHandler itemHandler,UserHandler userHandler){
		this.itemHandler = itemHandler;
		this.itemsService = new ItemsServiceImplementation(itemHandler,userHandler);

	}

	public List<ItemBoundary> getAllItemsByPlayer(String userEmail, int size, int page) {
		Page<ItemEntity> activeReservations = itemHandler.findAllByUserSpaceAndUserEmail(userEmail, "2021b.lidar.ben.david",  PageRequest.of(page, size, Direction.ASC, "timestamp"));
		List<ItemBoundary> rv = new ArrayList<>();
		for (ItemEntity reservation : activeReservations) {
			rv.add(this.itemsService.convertToBoundary(reservation));
		}
		return rv;
	}

}
