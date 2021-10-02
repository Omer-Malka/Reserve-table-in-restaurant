package twins.logic;

import java.util.List;

import twins.boundaries.ItemBoundary;

public interface ItemsService {

	@Deprecated List<ItemBoundary> getAllItems(String userSpace, String userEmail);

	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId);

	public ItemBoundary createItem(String userSpace, String userEmail,ItemBoundary item);

	public void updateItem(String userSpace, String userEmail, String itemSpace, String itemId, ItemBoundary update);
	
	public void deleteAllItems(String adminSpace, String adminEmail);
	
	
	

	
}
