package twins.helpers;

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

}
