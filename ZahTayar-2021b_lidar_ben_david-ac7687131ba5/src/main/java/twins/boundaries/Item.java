package twins.boundaries;

public class Item {
	private ItemIdBoundary itemId;

	public Item() {
		itemId = new ItemIdBoundary();
	}

	public Item(ItemIdBoundary itemId) {
		this.itemId = itemId;
	}

	public ItemIdBoundary getItemId() {
		return itemId;
	}

	public void setItemId(ItemIdBoundary itemId) {
		this.itemId = itemId;
	}
	
	
	
	
	

}
