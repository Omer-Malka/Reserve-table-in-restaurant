package twins.boundaries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OperationBoundary {
	
	private OperationIdBoundary operationId;
	private String type;
	private Item item;
	private Date createdTimestamp;
	private InvokedByBoundary invokedBy;
	private Map<String,Object> operationAttributes;
	
	
	
	public OperationBoundary() {
		operationAttributes = new HashMap<>();
		invokedBy = new InvokedByBoundary();
		operationId = new OperationIdBoundary();
		item = new Item();
	}



	public OperationBoundary(OperationIdBoundary operationId, String type, Item item, Date createdTimestamp,
			InvokedByBoundary invokedBy,Map<String, Object> operationAttributes) {
		
		this.operationId = operationId;
		this.type = type;
		this.createdTimestamp = createdTimestamp;
		this.invokedBy = invokedBy;
		this.item = item;
			
	}



	public OperationIdBoundary getOperationId() {
		return operationId;
	}



	public void setOperationId(OperationIdBoundary operationId) {
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



	public InvokedByBoundary getInvokedBy() {
		return invokedBy;
	}



	public void setInvokedBy(InvokedByBoundary invokedBy) {
		this.invokedBy = invokedBy;
	}



	public Item getItem() {
		return item;
	}



	public void setItem(Item item) {
		this.item = item;
	}



	public Map<String, Object> getOperationAttributes() {
		return operationAttributes;
	}



	public void setOperationAttributes(Map<String, Object> operationAttributes) {
		this.operationAttributes = operationAttributes;
	}
	
	

}
