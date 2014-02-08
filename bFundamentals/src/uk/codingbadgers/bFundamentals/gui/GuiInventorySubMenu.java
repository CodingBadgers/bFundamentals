package uk.codingbadgers.bFundamentals.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.bFundamentals.gui.callbacks.GuiReturnCallback;

public class GuiInventorySubMenu extends GuiSubInventory {

    public GuiInventorySubMenu(GuiInventory ownerMenu, String title, int rowCount) {
        super(ownerMenu, title, rowCount);
        this.addMenuItem("Back", new ItemStack(Material.NETHER_STAR), new String[]{"Return to", ownerMenu.getTitle()}, (rowCount - 1) * 9, new GuiReturnCallback(ownerMenu));
    }

}
