package twins.logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import twins.boundaries.InvokedByBoundary;
import twins.boundaries.Item;
import twins.boundaries.ItemIdBoundary;
import twins.boundaries.OperationBoundary;
import twins.boundaries.OperationIdBoundary;
import twins.boundaries.UserIdBoundary;
import twins.data.ItemHandler;
import twins.data.OperationEntity;
import twins.data.OperationHandler;
import twins.helpers.CheckerHelper;
import twins.logic.operations.CancelReservation;
import twins.logic.operations.ChangeReservationDetails;
import twins.logic.operations.Clasp;
import twins.logic.operations.InitialTablesMap;
import twins.logic.operations.ReserveTable;
import twins.logic.operations.UpdateTablesMap;
import twins.logic.operations.ViewTableMap;

@Service
public  class OperationsServiceImplementation implements OperationsServiceExtended {
	private String name;
	private OperationHandler operationHandler;
	private ObjectMapper jackson;
	private CheckerHelper checker;
	private ReserveTable reserveTable;
	private CancelReservation cancelReservation;
	private ChangeReservationDetails changeReservationDetails;
	private Clasp clasp;
	private UpdateTablesMap updateTablesMap;
	private ViewTableMap viewTableMap;
	private InitialTablesMap initialTablesMap;

	@Autowired	
	public OperationsServiceImplementation(OperationHandler operationHandler, ItemHandler itemHandler) {
		this.operationHandler = operationHandler;
		this.checker = new CheckerHelper();
		this.jackson = new ObjectMapper();
		this.cancelReservation = new CancelReservation(itemHandler);
		this.changeReservationDetails = new ChangeReservationDetails();
		this.reserveTable = new ReserveTable(itemHandler);
		this.clasp = new Clasp();
		this.updateTablesMap = new UpdateTablesMap();
		this.viewTableMap = new ViewTableMap();
		this.initialTablesMap = new InitialTablesMap(itemHandler);
	}

	@Value("${spring.application.name: 2021b.lidar.ben.david}")
	public void setName(String name) {
		this.name = name;
	}

	@Override
	@Transactional
	public Object invokeOperation(OperationBoundary operation) {
		//check input 
		if(!this.checker.checkOperationType(operation.getType())) {
			throw new RuntimeException("Type can not be null");
		}

		if(!this.checker.checkOperationItem(operation.getItem())) {
			throw new RuntimeException("Item can not be null");
		}

		if(!this.checker.checkOperationInvokeBy(operation.getInvokedBy())) {
			throw new RuntimeException("User Id can not be null");
		}
		String email = operation.getInvokedBy().getUserId().getEmail();
		String userSpace = operation.getInvokedBy().getUserId().getSpace();
		//insert to db
		switch (operation.getType()) {
		case "cancelReservation":
			/* operation attributes:
			 * option : String -> cancelAllPassedReservation for the automatic deleting
			 * time: String -> "12-14-13-05"(hourStart-hourEnd-day-month) ->must
			 * name: String ->must
			 */
			String option = (String)operation.getOperationAttributes().get("option");
			if(!this.checker.checkInputString(option))
				throw new RuntimeException("option can't be null");
			if(option.equals("cancelAllPassedReservation")) {
				DateFormat df = new SimpleDateFormat("HH-dd-MM");
				Date dateobj = new Date();
				String date = df.format(dateobj);//(untilHour-day-month)
				this.cancelReservation.cancelAllPassedReservation(date);
			}
			else {
				String name = (String)operation.getOperationAttributes().get("name");
				String timeOfRes = (String)operation.getOperationAttributes().get("time");
				if(!this.checker.checkInputString(name)
						&&!this.checker.checkInputString(timeOfRes)){
					throw new RuntimeException("attributes can't be null");
				}
				this.cancelReservation.cancelReservation(name, timeOfRes, email);
			}
			break;

		case "reserveTable":
			/* operation attributes:
			 * time: String -> "12-14-13-05"(hourStart-hourEnd-day-month) ->must
			 * capacity: String ->must
			 * customerName: String ->must
			 * chosenTable String -> exists if the client chose a table from emptyTables list ->must
			 * emptyTables: ArrayList<String> -> exists after posting reservation
			 */
			String numOfPeople = (String) operation.getOperationAttributes().get("capacity");
			String time = (String) operation.getOperationAttributes().get("time");
			String customerName = (String) operation.getOperationAttributes().get("customerName");
			String chosenTable = (String)operation.getOperationAttributes().get("chosenTable");
			if(!this.checker.checkInputString(numOfPeople)
					&&!this.checker.checkInputString(time)
					&&!this.checker.checkInputString(customerName)){
				throw new RuntimeException("attributes can't be null");
			}
			if(!chosenTable.isEmpty()) {
				this.reserveTable.reserve(numOfPeople, customerName, chosenTable, time, userSpace, email, this.name);
			}
			else {
				ArrayList<String> tables = this.reserveTable.findTables(numOfPeople, time);
				operation.getOperationAttributes().put("emptyTables", tables);
			}
			break;

		case "changeReservationDetails":
			/* operation attributes:
			 * capacity: String
			 * newTime: String(hourStart-hourEnd-day-month)
			 * oldTime: String(hourStart-hourEnd-day-month)
			 * name: String
			 */
			String newCapacity = (String) operation.getOperationAttributes().get("capacity");
			String newTime = (String) operation.getOperationAttributes().get("newTime");
			String oldTime = (String) operation.getOperationAttributes().get("oldTime");
			String nameOfChanger = (String) operation.getOperationAttributes().get("name");
			ArrayList<String> tables = this.reserveTable.findTables(newCapacity, newTime);
			if(!tables.isEmpty()) {
				this.cancelReservation.cancelReservation(nameOfChanger, oldTime, email);
				this.reserveTable.reserve(newCapacity, nameOfChanger, tables.get(0), newTime, userSpace, email, this.name);
			}
			else {
				throw new RuntimeException("Cant replace reservation");
			}
			break;

		case "clasp":

			break;

		case "updateTablesMap":

			break;

		case "viewTableMap":

			break;

		case "initialTablesMap":
			/* operation attributes:
			 * tableNumber: String 
			 * capacity: String
			 */
			this.initialTablesMap.storeTable(operation.getOperationAttributes(), 
					operation.getInvokedBy().getUserId().getSpace(), 
					operation.getInvokedBy().getUserId().getEmail(),
					operation.getItem().getItemId().getSpace()+"@"+operation.getItem().getItemId().getId());
			break;

		default:
			break;
		}
		//create new entity ,fill server's fields and save
		OperationEntity entity = this.convertToEntity(operation);

		//generate id + timestamp
		entity.setUserEmail(operation.getInvokedBy().getUserId().getEmail());
		entity.setUserSpace(operation.getInvokedBy().getUserId().getSpace());
		entity.setCreatedTimestamp(new Date());
		entity.setOperationId(this.name.concat("@").concat(UUID.randomUUID().toString()));
		return this.convertToBoundary(this.operationHandler.save(entity));
	}

	@Override
	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) {
		//check input 
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
		entity.setUserEmail(operation.getInvokedBy().getUserId().getEmail());
		entity.setUserSpace(operation.getInvokedBy().getUserId().getSpace());
		entity.setCreatedTimestamp(new Date());
		entity.setOperationId(this.name.concat("@").concat(UUID.randomUUID().toString()));
		//insert to db
		return this.convertToBoundary(this.operationHandler.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail,int size,int page) {
		List<OperationBoundary> rv = new ArrayList<>(); 
		Iterable<OperationEntity> allEntities = this.operationHandler.findAll(PageRequest.of(page, size,Direction.ASC,"id"));
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
		rv.setItem(new Item(new ItemIdBoundary(operation.getItemId().split("@")[0], operation.getItemId().split("@")[1])));
		return rv;
	}

	private OperationEntity convertToEntity(OperationBoundary operation) {
		OperationEntity entity = new OperationEntity();
		entity.setItemId(operation.getItem().getItemId().getSpace().concat("@").concat(operation.getItem().getItemId().getId()));
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

	@Override
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		// TODO Auto-generated method stub
		return null;
	}

}
