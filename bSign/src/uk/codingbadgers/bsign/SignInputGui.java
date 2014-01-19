package uk.codingbadgers.bsign;

import uk.codingbadgers.bsign.sign.Sign;
import me.cybermaxke.inputgui.api.InputGui;
import me.cybermaxke.inputgui.api.InputPlayer;

public class SignInputGui implements InputGui {

	private Sign sign;
	private String message;

	public SignInputGui(Sign sign, String message) {
		this.sign = sign;
		this.message = message;
	}

	@Override
	public String getDefaultText() {
		return message;
	}

	@Override
	public void onCancel(InputPlayer player) {
		sign.getLocation().getBlock().breakNaturally();
		bSignModule.SIGNS.remove(sign);
	}

	@Override
	public void onConfirm(InputPlayer player, String message) {
		if (sign.init(message)) {
			bSignModule.sendMessage("bSign", player.getPlayer(), bSignModule.MODULE.getLanguageValue("SIGN-CONTEXT-SET"));
			
			String type = sign.getType(); 
			String location = sign.getLocation().getX() + "," + sign.getLocation().getY() + "," + sign.getLocation().getZ() + "," + sign.getLocation().getWorld().getName();
			
			String addSign = "INSERT INTO " + bSignModule.DBPREFIX + "bSign " +
					"VALUES ('" +
					type + "', '" +
					sign.getContext() + "', '" +
					sign.getCreator().getName() + "', '" +
					location +
					"')";
			bSignModule.DATABASE.query(addSign);
		}
	}

}
