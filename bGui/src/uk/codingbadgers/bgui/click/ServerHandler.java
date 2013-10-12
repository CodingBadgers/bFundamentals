package uk.codingbadgers.bgui.click;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;

public class ServerHandler implements ClickHandler {

    private String value;

    public ServerHandler(String value) {
        this.value = value;
    }
    
    @Override
    public void handle(Player player) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
         
            out.writeUTF("Connect");
            out.writeUTF(value);
         
         
            player.sendPluginMessage(bFundamentals.getInstance(), "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
