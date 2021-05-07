package twins.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;


import twins.boundaries.InvokedByBoundary;

import twins.boundaries.OperationBoundary;
import twins.boundaries.OperationIdBoundary;
import twins.boundaries.UserIdBoundary;
import twins.data.OperationEntity;
import twins.data.OperationHandler;
import twins.helpers.CheckerHelper;
import twins.logic.operations.CancelReservation;
import twins.logic.operations.ChangeReservationDetails;
import twins.logic.operations.Clasp;
import twins.logic.operations.ReserveTable;
import twins.logic.operations.UpdateRestaurantDetails;
import twins.logic.operations.ViewTableMap;

@Service
public  class OperationsServiceImplementation implements OperationsService {
	private String name;
	private OperationHandler operationHandler;
	private ObjectMapper jackson;
	private CheckerHelper checker;
	private ReserveTable reserveTable;
	private CancelReservation cancelReservation;
	private ChangeReservationDetails changeReservationDetails;
	private Clasp clasp;
	private UpdateRestaurantDetails updateRestaurantDetails;
	private ViewTableMap viewTableMap;

	@Autowired	
	public OperationsServiceImplementation(OperationHandler operationHandler) {
		super();
		this.operationHandler = operationHandler;
		this.checker = new CheckerHelper();
		this.jackson = new ObjectMapper();
		this.cancelReservation = new CancelReservation();
		this.changeReservationDetails = new ChangeReservationDetails();
		this.reserveTable = new ReserveTable();
		this.clasp = new Clasp();
		this.updateRestaurantDetails = new UpdateRestaurantDetails();
		this.viewTableMap = new ViewTableMap();
	}

	@Value("${spring.application.name: 2021b.lidar.ben.david}")
	public void setName(String name) {
		this.name = name;
	}

	@Override
	@Transactional
	public Object invokeOperation(OperationBoundary operation) {
		//check input item boundary
		if(!this.checker.checkOperationId(operation.getOperationId())) {
			throw new RuntimeException("Id can not be null");
		}

		if(!this.checker.checkOperationType(operation.getType())) {
			throw new RuntimeException("Type can not be null");
		}

		if(!this.checker.checkOperationItem(operation.getItem())) {
			throw new RuntimeException("Item can not be null");
		}

		if(!this.checker.checkOperationInvokeBy(operation.getInvokedBy())) {
			throw new RuntimeException("User Id can not be null");
		}

		//create new entity ,fill server's fields and save
		OperationEntity entity = this.convertToEntity(operation);

		//generate id + timestamp
		entity.setCreatedTimestamp(new Date());
		entity.setOperationId(this.name.concat("@").concat(UUID.randomUUID().toString()));
		//insert to db
		switch (operation.getType()) {
		case "cancelReservation":
			
			break;

		case "reserveTable":
			this.reserveTable.reserve(entity);
			break;

		case "changeReservationDetails":

			break;

		case "clasp":

			break;

		case "updateRestaurantDetails":

			break;

		case "viewTableMap":

			break;

		default:
			break;
		}
		return this.convertToBoundary(this.operationHandler.save(entity));
	}

	@Override
	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) {
		if(!this.checker.checkOperationId(operation.getOperationId())) {
			throw new RuntimeException("id can not be null");
		}

		if(!this.checker.checkOperationType(operation.getType())) {
			throw new RuntimeException("Type can not be null");
		}

		if(!this.checker.checkOperationItem(operation.getItem())) {
			throw new RuntimeException("Item can not be null");
		}

		if(!this.checker.checkOperationInvokeBy(operation.getInvokedBy())) {
			throw new RuntimeException("User Id can not be null");
		}

		//create new entity ,fill server's fields and save
		OperationEntity entity = this.convertToEntity(operation);

		//generate id + timestamp
		entity.setCreatedTimestamp(new Date());
		entity.setOperationId(this.name.concat("@").concat(UUID.randomUUID().toString()));
		//insert to db
		return this.convertToBoundary(this.operationHandler.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		List<OperationBoundary> rv = new ArrayList<>(); 
		Iterable<OperationEntity> allEntities = this.operationHandler.findAll();
		if(allEntities == null) {
			throw new RuntimeException("No operation to return");
		}

		else {
			//covert them to boundaries and add to the array
			for (OperationEntity operation : allEntities) {				
				rv.add(convertToBoundary(operation));
			}
			return rv;
		}
	}

	private OperationBoundary convertToBoundary(OperationEntity operation) {
		OperationBoundary rv = new OperationBoundary();
		rv.setOperationId(new OperationIdBoundary(this.name, operation.getOperationId()));
		rv.setType(operation.getType()); 
		rv.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(operation.getUserSpace(),operation.getUserEmail())));
		rv.setCreatedTimestamp(operation.getCreatedTimestamp());
		Map<String,Object> operationAttributes = this.unmarshall(operation.getOperationAttributes(),Map.class);
		rv.setOperationAttributes(operationAttributes);
		return rv;
	}

	private OperationEntity convertToEntity(OperationBoundary operation) {
		OperationEntity entity = new OperationEntity();
		entity.setType(operation.getType());
		String operationAttributes = this.marshall(operation.getOperationAttributes());
		entity.setOperationAttributes(operationAttributes);
		return entity;
	}


	@Override
	@Transactional
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		this.operationHandler.deleteAll();

	}
	private String marshall(Object value) {
		try {
			return this.jackson.writeValueAsString(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	private <T> T unmarshall(String json, Class<T> requiredType) {
		try {
			return this.jackson.readValue(json, requiredType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
