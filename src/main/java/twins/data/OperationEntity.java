package twins.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/*
 * OPERATIONS
 * ID|TYPE
 */
@Entity
@Table(name="OPERATIONS")
public class OperationEntity {
	private String operationId;
	private String type;
	private String itemId;
	private Date createdTimestamp;
	private String userEmail;
	private String userSpace;
	private String operationAttributes;
	
	
	public OperationEntity() {
	}

	@Id
	public String getOperationId() {
		return this.operationId;
	}

	
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}


	public String getType() {
		return type;
	}

	
	public void setType(String type) {
		this.type = type;
	}

	
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}


	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	
	public String getOperationAttributes() {
		return operationAttributes;
	}

	
	public void setOperationAttributes(String operationAttributes) {
		this.operationAttributes = operationAttributes;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserSpace() {
		return userSpace;
	}

	public void setUserSpace(String userSpace) {
		this.userSpace = userSpace;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	
}
