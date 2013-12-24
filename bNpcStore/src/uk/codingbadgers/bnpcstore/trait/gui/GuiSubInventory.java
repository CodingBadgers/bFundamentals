package uk.codingbadgers.bnpcstore.trait.gui;

public abstract class GuiSubInventory extends GuiInventory {

	public GuiSubInventory(GuiInventory ownerMenu, String title, int rowCount) {
		super(ownerMenu.getPlugin(), title, ownerMenu.getOwner(), rowCount);
	}

}
