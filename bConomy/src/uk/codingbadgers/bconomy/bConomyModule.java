package uk.codingbadgers.bconomy;

import uk.codingbadgers.bFundamentals.module.Module;

public class bConomyModule extends Module {

	/** The Constant NAME of the module. */
	final public static String NAME = "bConomy";
	
	/** The Constant VERSION of the module. */
	final public static String VERSION = "1.00";

	/**
	 * The bConomy module constructor
	 * passing the name and version to its base class
	 */
	public bConomyModule() {
		super(NAME, VERSION);
	}

	/**
	 * This is called when the module is unloaded
	 */
	@Override
	public void onDisable() {
		
	}

	/**
	 * Called when the module is loaded.
	 */
	@Override
	public void onEnable() {

	}
	
}
