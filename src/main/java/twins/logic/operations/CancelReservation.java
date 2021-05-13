package twins.logic.operations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import twins.data.ItemEntity;
import twins.data.ItemHandler;

@Component
public class CancelReservation {
	private ItemHandler itemHandler;

	@Autowired
	public CancelReservation(ItemHandler itemHandler) {
		this.itemHandler = itemHandler;
	}
	
	public void cancelAllPassedReservation(String time) {//"14-13-05"(untilHour-day-month)
		List<ItemEntity> reservations = this.itemHandler.findAllByActiveAndTypeAndNameLike(true, "reservation", "-" + time);
		for(ItemEntity reservation : reservations) {
			reservation.setActive(false);
			itemHandler.save(reservation);
		}
	}
	
	public boolean cancelReservation(String name, String time, String email) {
		List<ItemEntity> reservations = this.itemHandler.findAllByUserEmailAndActiveAndTypeAndNameLike(email, true, "reservation", time);
		for(ItemEntity reservation : reservations) {
			if(reservation.getItemAttributes().get("name").equals(name)) {
				reservation.setActive(false);
				itemHandler.save(reservation);
				return true;
			}
		}
		return false;
		
	}
	
	

}
