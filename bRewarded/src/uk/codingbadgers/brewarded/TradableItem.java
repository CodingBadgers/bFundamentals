package uk.codingbadgers.brewarded;

public class TradableItem {

	private final int m_id;
	
	///////////////////////////////////////////////////////////
	
	private final String m_name;
	
	private final int m_itemId;
	
	private final int m_itemData;
	
	private final int m_quantity;

	private final int m_price;
	
	public TradableItem(int id, String name, String itemId, int quantity, int price) {
		m_id = id;
		
		if (itemId.contains(":"))
		{
			String itemStr = itemId.substring(0, itemId.indexOf(":"));
			String dataStr = itemId.replace(itemStr + ":", "");
			
			m_itemId = Integer.parseInt(itemStr);
			m_itemData = Integer.parseInt(dataStr);
		}
		else
		{
			m_itemId = Integer.parseInt(itemId);
			m_itemData = 0;
		}
		
		m_name = name;
		m_price = price;
		m_quantity = quantity;
	}
	
	public int getID() {
		return m_id;
	}
	
	public String getName() {
		return m_name;
	}
	
	public int getPrice() {
		return m_price;
	}
	
	public int getQuantity() {
		return m_quantity;
	}
	
	public int getItemId() {
		return m_itemId;
	}
	
	public int getItemData() {
		return m_itemData;
	}
}
