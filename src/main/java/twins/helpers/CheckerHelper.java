package twins.helpers;

import twins.boundaries.InvokedByBoundary;
import twins.boundaries.Item;
import twins.boundaries.ItemIdBoundary;
import twins.boundaries.OperationIdBoundary;

public class CheckerHelper {
	
	public boolean checkInputString(String name) {
		return !name.isEmpty();
	}
	
	public boolean checkOperationId(OperationIdBoundary id) {
		if(id != null)
			return true;
		return false;
	}

	public boolean checkOperationType(String type) {
		if(type != null)
			return true;
		return false;
	}
	
	public boolean checkOperationItem(Item item) {
		if(item.getItemId().getId()!=null && item.getItemId().getSpace()!=null)
			return true;
		return false;
	}
		
	public boolean checkOperationInvokeBy(InvokedByBoundary invokeBy) {
		if(invokeBy.getUserId().getEmail()!=null && invokeBy.getUserId().getSpace()!=null)
			return true;
		return false;
	}
}
