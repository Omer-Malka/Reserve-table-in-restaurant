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
import com.fasterxml.jackson.databind.ObjectMapper;
import twins.boundaries.CreatedByBoundary;
import twins.boundaries.ItemBoundary;
import twins.boundaries.ItemIdBoundary;
import twins.boundaries.LocationBoundary;
import twins.boundaries.UserIdBoundary;
import twins.data.ItemEntity;
import twins.data.ItemHandler;
import twins.helpers.CheckerHelper;

@Service
public class ItemsServiceImplementation implements ItemServiceExtended{
	private String name;
	private ItemHandler itemHandler;
	private ObjectMapper jackson;
	private CheckerHelper checker;

	@Autowired	
	public ItemsServiceImplementation(ItemHandler itemHandler) {
		super();
		this.itemHandler = itemHandler;
		this.checker = new CheckerHelper();
		this.jackson = new ObjectMapper();
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
		String newItemId = itemSpace + "@" + itemId;
		Optional<ItemEntity> itemOp = this.itemHandler.findById(newItemId);
		if(itemOp.isPresent() 
				&& this.checker.checkInputString(itemId)
				&& this.checker.checkInputString(update.getName()) 
				&& this.checker.checkInputString(update.getType())) {
			update.setItemId(new ItemIdBoundary(this.name, itemId));
			ItemEntity toUpdate = this.convertToEntity(update);
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
		Page<ItemEntity> entitiesPage = this.itemHandler.findAll(PageRequest.of(page, size, Direction.ASC, "type", "createdTimestamp", "userEmail"));
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
	}
	
	public ItemBoundary convertToBoundary(ItemEntity itemEntity) {
		ItemBoundary rv = new ItemBoundary();
		rv.setItemId(new ItemIdBoundary(this.name,itemEntity.getItemId().split("@")[1]));
		rv.setActive(itemEntity.getActive()); 
		rv.setCreatedBy(new CreatedByBoundary(new UserIdBoundary(itemEntity.getUserSpace(),itemEntity.getUserEmail())));
		rv.setCreatedTimestamp(itemEntity.getCreatedTimestamp());
		Map<String,Object> itemAttributes = this.unmarshall(itemEntity.getItemAttributes(),Map.class);
		rv.setItemAttributes(itemAttributes);
		rv.setLocation(new LocationBoundary(itemEntity.getLocationLat(),itemEntity.getLocationLng()));
		rv.setName(itemEntity.getName());
		rv.setType(itemEntity.getType());
		return rv;
	}
	
	private ItemEntity convertToEntity(ItemBoundary item) {
		ItemEntity entity = new ItemEntity();
		entity.setName(item.getName());
		entity.setType(item.getType());
		entity.setLocationLat(item.getLocation().getLat());
		entity.setLocationLng(item.getLocation().getLng());
		entity.setActive(item.isActive());
		String itemAttributes = this.marshall(item.getItemAttributes());
		entity.setItemAttributes(itemAttributes);
		return entity;
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
