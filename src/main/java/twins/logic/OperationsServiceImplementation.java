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
import twins.boundaries.InvokedByBoundary;
import twins.boundaries.Item;
import twins.boundaries.ItemBoundary;
import twins.boundaries.ItemIdBoundary;
import twins.boundaries.OperationBoundary;
import twins.boundaries.OperationIdBoundary;
import twins.boundaries.UserIdBoundary;
import twins.data.ItemHandler;
import twins.data.OperationEntity;
import twins.data.OperationHandler;
import twins.data.UserHandler;
import twins.helpers.CheckerAuthorization;
import twins.helpers.CheckerHelper;
import twins.logic.operations.CancelReservation;
import twins.logic.operations.Clasp;
import twins.logic.operations.InitialTablesMap;
import twins.logic.operations.ReserveTable;
import twins.logic.operations.ShowPreviousReservations;
import twins.logic.operations.UpdateTablesMap;
import twins.logic.operations.ViewTableMap;

@Service
public  class OperationsServiceImplementation implements OperationsServiceExtended {
	private String name;
	private OperationHandler operationHandler;
	//private ObjectMapper jackson;
	private CheckerHelper checker;
	private CheckerAuthorization checkerAutho; 
	private ReserveTable reserveTable;
	private CancelReservation cancelReservation;
	private Clasp clasp;
	private UpdateTablesMap updateTablesMap;
	private ViewTableMap viewTableMap;
	private InitialTablesMap initialTablesMap;
	private ShowPreviousReservations showPreviousReservations;

	@Autowired	
	public OperationsServiceImplementation(OperationHandler operationHandler, ItemHandler itemHandler,UserHandler userHandler) {
		this.operationHandler = operationHandler;
		this.checkerAutho=new CheckerAuthorization(userHandler);
		this.checker = new CheckerHelper();
		//this.jackson = new ObjectMapper();
		this.cancelReservation = new CancelReservation(itemHandler);
		this.reserveTable = new ReserveTable(itemHandler);
		this.clasp = new Clasp(itemHandler);
		this.updateTablesMap = new UpdateTablesMap(itemHandler);
		this.viewTableMap = new ViewTableMap(itemHandler);
		this.initialTablesMap = new InitialTablesMap(itemHandler,userHandler);
		this.showPreviousReservations = new ShowPreviousReservations(itemHandler,userHandler);
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
			throw new RuntimeException("Type can not be null or empty String");
		}
		
		if(!this.checker.checkOperationItem(operation.getItem())) {
			throw new RuntimeException("Item can not be null or empty String");
		}

		if(!this.checker.checkOperationInvokeBy(operation.getInvokedBy())) {
			throw new RuntimeException("User Id can not be null or empty String");
		}
		String email = operation.getInvokedBy().getUserId().getEmail();
		String userSpace = operation.getInvokedBy().getUserId().getSpace();

		//check if user is present and his roll="PLAYER"
		if (!checkerAutho.CheckPlayerUser(userSpace+'%'+email)) {
			throw new RuntimeException("User not authorized to do this action");
		}
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
			this.clasp.Clasp_CancelAllReservation();
			break;

		case "updateTablesMap":
			String tableNumber = (String) operation.getOperationAttributes().get("tableNumber");
			String newCapacityOfTable = (String) operation.getOperationAttributes().get("newCapacity");
			if (!this.updateTablesMap.hasReservation(tableNumber)) {
				this.updateTablesMap.updateTable(tableNumber, newCapacityOfTable, userSpace, email,
						operation.getItem().getItemId().getSpace()+"@"+operation.getItem().getItemId().getId());
			}
			else {
				throw new RuntimeException("Can't update the capacity : this table has a reservation");
			}

			break;

		case "viewTableMap":
			List<ItemBoundary> reservations = this.viewTableMap.getAllItemsByPlayer(email, 0, 5);
			operation.getOperationAttributes().put("reservation", reservations);
			break;
			

		case "initialTablesMap":
			/* operation attributes:
			 * tableNumber: String 
			 * capacity: String
			 */
			this.initialTablesMap.storeTable(operation.getOperationAttributes(), userSpace, email,
					operation.getItem().getItemId().getSpace()+"@"+operation.getItem().getItemId().getId());
			break;
		case "showPreviousReservations":
			/* operation attributes:
			 * previousReservations: List<ItemBoundary> prevoiusReservations ->after invoking
			 */
			List<ItemBoundary> prevoiusReservations = this.showPreviousReservations.showReservations(email);
			operation.getOperationAttributes().put("previousReservations", prevoiusReservations);
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
		//insert to db
		return this.convertToBoundary(this.operationHandler.save(entity));
	}

	@Override
	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) {
		//check input 
		if(!this.checker.checkOperationType(operation.getType())) {
			throw new RuntimeException("Type can not be null or empty String");
		}

		if(!this.checker.checkOperationItem(operation.getItem())) {
			throw new RuntimeException("Item can not be null or empty String");
		}

		if(!this.checker.checkOperationInvokeBy(operation.getInvokedBy())) {
			throw new RuntimeException("User Id can not be null or empty String");
		}
		//check if user is present and his roll="PLAYER"
		if(!this.checkerAutho.CheckPlayerUser(operation.getInvokedBy().getUserId().getSpace()+'%'+operation.getInvokedBy().getUserId().getEmail())) {
			throw new RuntimeException("User not authorized to do this action");
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
		//check if user is present and his roll="ADMIN"
		if(!checkerAutho.CheckAdminUser(adminSpace+"%"+adminEmail)){
			throw new RuntimeException("User not authorized to do this action");
		}
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
		OperationBoundary boundary = new OperationBoundary();
		boundary.setOperationId(new OperationIdBoundary(this.name, operation.getOperationId()));
		boundary.setType(operation.getType()); 
		boundary.setItem(new Item(new ItemIdBoundary(operation.getItemId().split("@")[0], operation.getItemId().split("@")[1])));
		//rv.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(operation.getUserSpace(),operation.getUserEmail())));
		boundary.setCreatedTimestamp(operation.getCreatedTimestamp());
		boundary.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(operation.getUserSpace(), operation.getUserEmail())));
		Map<String,Object> operationAttributes = operation.getOperationAttributes();
		boundary.setOperationAttributes(operationAttributes);
		return boundary;
	}

	private OperationEntity convertToEntity(OperationBoundary operation) {
		OperationEntity entity = new OperationEntity();
		entity.setOperationId(operation.getOperationId().getId());
		entity.setType(operation.getType());
		entity.setItemId(operation.getItem().getItemId().getSpace().concat("@").concat(operation.getItem().getItemId().getId()));
		entity.setCreatedTimestamp(operation.getCreatedTimestamp());
		entity.setOperationAttributes(operation.getOperationAttributes());
		return entity;
	}


	@Override
	@Transactional
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		//check if user is present and his roll="ADMIN"
		if(!checkerAutho.CheckAdminUser(adminSpace+"%"+adminEmail)){
			throw new RuntimeException("User not authorized to do this action");
		}
		this.operationHandler.deleteAll();

	}
	/*	private String marshall(Object value) {
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
	 */
	@Override
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		// TODO Auto-generated method stub
		return null;
	}

}
