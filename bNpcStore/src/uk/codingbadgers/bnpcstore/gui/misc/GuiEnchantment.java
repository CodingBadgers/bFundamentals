/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.bnpcstore.gui.misc;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author N3wton
 */
public class GuiEnchantment extends Enchantment {

    private final int id;

    public GuiEnchantment() {
        super(112);
        id = 112;
    }
    
    @Override
    public String getName() {
        return "GuiEnchantment";
    }

    @Override
    public int getMaxLevel() {
        return 128;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment e) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack is) {
        return true;
    }
    
}
