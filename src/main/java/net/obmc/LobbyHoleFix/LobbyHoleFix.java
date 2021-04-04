package net.obmc.LobbyHoleFix;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.obmc.LobbyHoleFix.PlayerCommandListener;

public class LobbyHoleFix extends JavaPlugin implements Listener
{
	
	@Override
	public void onEnable() {
		Bukkit.getLogger().info("[LobbyHoleFix] Plugin Version " + this.getDescription().getVersion() + " activated");

        this.getCommand("holefix").setExecutor(new PlayerCommandListener());
		
	}
	
	@Override
	public void onDisable() {
		Bukkit.getLogger().info("[LobbyHoleFix] Plugin unloaded");
	}
}

