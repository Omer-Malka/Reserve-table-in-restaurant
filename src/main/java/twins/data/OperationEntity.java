package twins.data;

import java.util.Date;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



/*
 * OPERATIONS
 * ID|TYPE
 */

@Document(collection="OPERATIONS")
public class OperationEntity {
	@Id
	private String operationId;
	private String type;
	private String itemId;
	private Date createdTimestamp;
	private String userEmail;
	private String userSpace;
	private String operationAttributes;
	
	
	public OperationEntity() {
	}


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
