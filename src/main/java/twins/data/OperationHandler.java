package twins.data;

import org.springframework.data.repository.CrudRepository;

import twins.boundaries.OperationIdBoundary;

public interface OperationHandler extends CrudRepository <OperationEntity, OperationIdBoundary>{
	// C - Create
	
	// R - Read
	
	// U - Update
	
	// D - Delete
}
