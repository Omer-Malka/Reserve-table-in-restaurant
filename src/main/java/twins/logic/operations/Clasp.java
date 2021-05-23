package twins.logic.operations;

import java.util.List;

import twins.data.ItemEntity;
import twins.data.ItemHandler;

public class Clasp {
	
	private ItemHandler itemHandler;

	public Clasp(ItemHandler itemHandler) {
		this.itemHandler = itemHandler;
	}
	
	public void Clasp_CancelAllReservation() {//"14-13-05"(untilHour-day-month)
		List<ItemEntity> reservations = this.itemHandler.findAllByTypeAndActive("reservation", true);
		for(ItemEntity reservation : reservations) {
			reservation.setActive(false);
			itemHandler.save(reservation);
		}
	}
	
	

}
