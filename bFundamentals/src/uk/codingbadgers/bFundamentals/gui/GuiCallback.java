package uk.codingbadgers.bFundamentals.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface GuiCallback {
	
	/**
	 * Called when an item is click in an inventory
	 * @param inventory The owner inventory
	 * @param clickEvent The inventory click event
	 */
	public void onClick(GuiInventory inventory, InventoryClickEvent clickEvent);

}
