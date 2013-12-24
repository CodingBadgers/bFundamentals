package uk.codingbadgers.bsetblock;

import uk.codingbadgers.bFundamentals.module.Module;

public class bSetBlock extends Module {

	@Override
	public void onEnable() {
		registerCommand(new SetBlockCommand());
	}

	@Override
	public void onDisable() {
	}

}
