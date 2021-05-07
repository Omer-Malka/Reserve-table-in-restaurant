package twins.data;

import java.util.Date;





import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection="ITEMS")
public class ItemEntity {
	@Id
	private String itemId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private String userSpace;
	private String userEmail;
	private Double locationLat;
	private Double locationLng;
	private String itemAttributes;
	
	public ItemEntity() {
		
	}


	public String getItemId() {
		return this.itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	@Field("CREATED_DATE")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public String getUserSpace() {
		return userSpace;
	}
	public void setUserSpace(String userSpace) {
		this.userSpace = userSpace;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public Double getLocationLat() {
		return locationLat;
	}
	public void setLocationLat(Double locationLat) {
		this.locationLat = locationLat;
	}
	public Double getLocationLng() {
		return locationLng;
	}
	public void setLocationLng(Double locationLng) {
		this.locationLng = locationLng;
	}

	public String getItemAttributes() {
		return itemAttributes;
	}
	public void setItemAttributes(String itemAttributes) {
		this.itemAttributes = itemAttributes;
	}
}
