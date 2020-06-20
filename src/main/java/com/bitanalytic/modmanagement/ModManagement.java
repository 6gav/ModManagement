package com.bitanalytic.modmanagement;

import com.bitanalytic.modmanagement.command.ModMenuCommand;
import com.bitanalytic.modmanagement.gui.PlayerListGui;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModManagement extends JavaPlugin {

	public static Server SERVER;

	public static JavaPlugin PLUGIN;

	@Override
	public void onEnable() {
		// Plugin startup logic
		PLUGIN = this;
		SERVER = getServer();
		this.getCommand("modmenu").setExecutor(new ModMenuCommand());
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
