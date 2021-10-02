package twins.logic;

import java.util.List;
import twins.boundaries.ItemBoundary;

public interface ItemServiceExtended extends ItemsService {
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail, int size, int page);

}
