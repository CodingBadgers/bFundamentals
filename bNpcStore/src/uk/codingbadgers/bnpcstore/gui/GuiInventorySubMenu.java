package uk.codingbadgers.bnpcstore.gui;

import org.bukkit.Material;
import uk.codingbadgers.bnpcstore.gui.callbacks.GuiReturnCallback;

public class GuiInventorySubMenu extends GuiSubInventory {

    public GuiInventorySubMenu(GuiInventory ownerMenu, String title, int rowCount) {
        super(ownerMenu, title, rowCount);
        this.addMenuItem("Back", Material.NETHER_STAR, new String[]{"Return to", ownerMenu.getTitle()}, (rowCount - 1) * 9, new GuiReturnCallback(ownerMenu));
    }

}
