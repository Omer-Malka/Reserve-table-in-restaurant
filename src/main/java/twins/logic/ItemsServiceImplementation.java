package twins.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import com.fasterxml.jackson.databind.ObjectMapper;
import twins.boundaries.CreatedByBoundary;
import twins.boundaries.ItemBoundary;
import twins.boundaries.ItemIdBoundary;
import twins.boundaries.LocationBoundary;
import twins.boundaries.UserIdBoundary;
import twins.data.ItemEntity;
import twins.data.ItemHandler;
import twins.data.UserHandler;
import twins.helpers.CheckerAuthorization;
//import twins.helpers.CheckerAuthorization;
import twins.helpers.CheckerHelper;

@Service
public class ItemsServiceImplementation implements ItemServiceExtended{
	private String name;
	private ItemHandler itemHandler;
	private UserHandler userHandler;
//	private ObjectMapper jackson;
	private CheckerHelper checker;
	private CheckerAuthorization checkAuth;
	@Autowired	
	public ItemsServiceImplementation(ItemHandler itemHandler,UserHandler userHandler) {
		super();
		this.itemHandler = itemHandler;
		this.checker = new CheckerHelper();
		this.checkAuth=new CheckerAuthorization(userHandler);
//		this.jackson = new ObjectMapper();
	}
	
	@Value("${spring.application.name: 2021b.lidar.ben.david}")
	public void setName(String name) {
		this.name = name;
	}
	
	@PostConstruct
	public void init() {
	}
	
	@Override
	@Transactional
	public void deleteAllItems(String adminSpace, String adminEmail) {
		//delete all items
		this.itemHandler.deleteAll();
	}

	@Override
	@Transactional
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {
		//check if the user is manager
		if(!checkAuth.CheckManagerUser(userSpace+"%"+userEmail))
			throw new RuntimeException("User not authorized to do this action");
		//check input item boundary
		if(!this.checker.checkInputString(item.getName()) 
				|| !this.checker.checkInputString(item.getType())) {
			throw new RuntimeException("name and type attributes must not be null");
		}
		//create new entity ,fill server's fields and save
		ItemEntity entity = this.convertToEntity(item);
		entity.setUserSpace(userSpace);
		entity.setUserEmail(userEmail);
		//generate id + timestamp
		entity.setCreatedTimestamp(new Date());
		entity.setItemId(this.name.concat("@").concat(UUID.randomUUID().toString()));
		//insert to db
		return this.convertToBoundary(this.itemHandler.save(entity));
	}

	@Override
	@Transactional
	public void updateItem(String userSpace, String userEmail, String itemSpace, String itemId, ItemBoundary update) {
		//check if the user is manager
		if(!checkAuth.CheckManagerUser(userSpace+"%"+userEmail))
			throw new RuntimeException("User not authorized to do this action");
		String newItemId = itemSpace + "@" + itemId;
		Optional<ItemEntity> itemOp = this.itemHandler.findById(newItemId);
		if(itemOp.isPresent() 
				&& this.checker.checkInputString(itemId)
				&& this.checker.checkInputString(update.getName()) 
				&& this.checker.checkInputString(update.getType())) {
			update.setItemId(new ItemIdBoundary(this.name, itemId));
			System.err.println(update.getItemAttributes().get(itemId));
			ItemEntity toUpdate = this.convertToEntity(update);
			System.err.println(toUpdate.getItemAttributes().get("capacity"));
			toUpdate.setUserSpace(itemOp.get().getUserSpace());
			toUpdate.setUserEmail(itemOp.get().getUserEmail());
			//generate id + timestamp
			toUpdate.setItemId(itemOp.get().getItemId());
			toUpdate.setCreatedTimestamp(itemOp.get().getCreatedTimestamp());
			this.itemHandler.save(toUpdate);
		}
		else {
			throw new RuntimeException();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
//		List<ItemBoundary> rv = new ArrayList<>();
//		//get all entities
//		Iterable<ItemEntity> allEntities = this.itemHandler.findAll();
//		//covert them to boundaries and add to the array
//		for (ItemEntity item : allEntities) {				
//			rv.add(convertToBoundary(item));
//		}
//		return rv;
		throw new RuntimeException("function canceled");
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail, int size, int page) {
		//check if the user is manager
		if(checkAuth.CheckPlayerUser(userSpace+"%"+userEmail))
			throw new RuntimeException("User not authorized to do this action");
		
		//if yes 
		Page<ItemEntity> entitiesPage = this.itemHandler.findAllByUserSpaceAndUserEmail(userSpace, userEmail, PageRequest.of(page, size, Direction.ASC, "type", "createdTimestamp", "itemId"));
		List<ItemBoundary> rv = new ArrayList<>();
		Iterable<ItemEntity> allEntities = entitiesPage.getContent();
		for (ItemEntity item : allEntities) {				
			rv.add(convertToBoundary(item));
		}
		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		//check if the user is manager or player
		if(checkAuth.CheckManagerUser(userSpace+"%"+userEmail)||checkAuth.CheckPlayerUser(userSpace+"%"+userEmail)) {
			// find item entity by id
			String newItemId = itemSpace + "@" + itemId;
			Optional<ItemEntity> itemOp = this.itemHandler.findById(newItemId);
			//if it not empty convert to boundary and return
			if(itemOp.isPresent() 
					&& this.checker.checkInputString(itemId)) {
				return convertToBoundary(itemOp.get());
			}
			else {
				throw new RuntimeException();
			}
		}else
			throw new RuntimeException("User not authorized to do this action");
	
	}
	
	public ItemBoundary convertToBoundary(ItemEntity itemEntity) {
		ItemBoundary rv = new ItemBoundary();
		rv.setItemId(new ItemIdBoundary(this.name,itemEntity.getItemId().split("@")[1]));
		rv.setActive(itemEntity.getActive()); 
		rv.setCreatedBy(new CreatedByBoundary(new UserIdBoundary(itemEntity.getUserSpace(),itemEntity.getUserEmail())));
		rv.setCreatedTimestamp(itemEntity.getCreatedTimestamp());
		Map<String,Object> itemAttributes = itemEntity.getItemAttributes();
		rv.setItemAttributes(itemAttributes);
		rv.setLocation(new LocationBoundary(itemEntity.getLocationLat(),itemEntity.getLocationLng()));
		rv.setName(itemEntity.getName());
		rv.setType(itemEntity.getType());
		return rv;
	}
	
	public ItemEntity convertToEntity(ItemBoundary item) {
		ItemEntity entity = new ItemEntity();
		
		entity.setName(item.getName());
		entity.setType(item.getType());
		entity.setLocationLat(item.getLocation().getLat());
		entity.setLocationLng(item.getLocation().getLng());
		entity.setActive(item.isActive());
		entity.setItemAttributes(item.getItemAttributes());
		return entity;
	}
	
//	public String marshall(Object value) {
//		try {
//			return this.jackson.writeValueAsString(value);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			throw new RuntimeException(e);
//		}
//	}
//	private <T> T unmarshall(String json, Class<T> requiredType) {
//		try {
//			return this.jackson.readValue(json, requiredType);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}



}
