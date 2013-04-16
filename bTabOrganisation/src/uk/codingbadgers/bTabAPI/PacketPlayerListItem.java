package uk.codingbadgers.bTabAPI;

import com.comphenix.protocol.events.PacketContainer;

public class PacketPlayerListItem extends PacketContainer {

	private static final long serialVersionUID = -2795797321961872233L;
	public static final int ID = 201;
    
    public PacketPlayerListItem() {
        super(ID);
        this.getModifier().writeDefaults();
    }
        
    /**
     * Retrieve the player name.
     * <p>
     * Supports chat colouring. limited to 16 characters.
     * @return The current Player name
    */
    public String getPlayerName() {
        return this.getStrings().read(0);
    }
    
    /**
     * Set the player name.
     * <p>
     * Supports chat colouring. Limited to 16 characters.
     * @param value - new value.
    */
    public void setPlayerName(String value) {
    	this.getStrings().write(0, value);
    }
    
    /**
     * Retrieve whether or not to remove the given player from the list of online players.
     * @return The current Online
    */
    public boolean getOnline() {
        return this.getSpecificModifier(boolean.class).read(0);
    }
    
    /**
     * Set whether or not to remove the given player from the list of online players.
     * @param value - new value.
    */
    public void setOnline(boolean value) {
    	this.getSpecificModifier(boolean.class).write(0, value);
    }
    
    /**
     * Retrieve ping in milliseconds.
     * @return The current Ping
    */
    public short getPing() {
        return this.getIntegers().read(0).shortValue();
    }
    
    /**
     * Set ping in milliseconds.
     * @param value - new value.
    */
    public void setPing(int value) {
    	this.getIntegers().write(0, value);
    }
}