package com.bitanalytic.modmanagement.manager;

import com.bitanalytic.modmanagement.domain.RawCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CommandManager {
	public static final List<ModCommand> commands = new ArrayList<>();

	public static void initCommands(File f) throws IOException {
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		ConfigurationSection rawCommands = config.getConfigurationSection("menu.commands");
		Set<String> keys = rawCommands.getKeys(false);
		for(String key : keys) {
			RawCommand rawCommand = new RawCommand(rawCommands.getConfigurationSection(key));
			ModCommand modCommand = new ModCommand(rawCommand);
			commands.add(modCommand);
		}

	}

}
