package uk.codingbadgers.bfaq;

import uk.codingbadgers.bFundamentals.module.Module;

public class bFAQ extends Module {

	@Override
	public void onEnable() {
		registerCommand(new AskCommand());
	}

	@Override
	public void onDisable() {
	}

}
