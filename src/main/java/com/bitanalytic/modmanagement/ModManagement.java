package com.bitanalytic.modmanagement;

import com.bitanalytic.modmanagement.command.ModMenuCommand;
import com.bitanalytic.modmanagement.gui.PlayerListGui;
import com.bitanalytic.modmanagement.manager.CommandManager;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ModManagement extends JavaPlugin {

	public static Server SERVER;
	public static JavaPlugin PLUGIN;

	@Override
	public void onEnable() {
		PLUGIN = this;
		SERVER = getServer();
		this.getCommand("modmenu").setExecutor(new ModMenuCommand());
		try {
			CommandManager.initCommands(new File(this.getDataFolder().getAbsolutePath(), "test.yaml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
