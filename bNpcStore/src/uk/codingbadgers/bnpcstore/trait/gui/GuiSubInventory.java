package uk.codingbadgers.bnpcstore.trait.gui;

public abstract class GuiSubInventory extends GuiInventory {

    final private GuiInventory ownerMenu;
    
	public GuiSubInventory(GuiInventory ownerMenu, String title, int rowCount) {
		super(ownerMenu.getPlugin(), title, rowCount);
        this.ownerMenu = ownerMenu;
	}
    
     /**
     * 
     * @return 
     */
    public String getOwnerTitle() {
        return this.ownerMenu.getOwnerTitle();
    }

}
