package com.bitanalytic.modmanagement.domain;

import com.bitanalytic.modmanagement.manager.ModCommand;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RawCommand {
	private Material material;
	private String template;
	private String name;
	private String description;
	private String permission;
	private List<RawCommand> menuCommands = new ArrayList<>();

	public RawCommand(ConfigurationSection section) {
		material = Material.valueOf((String) section.get("item"));
		name = (String) section.get("name");
		description = (String) section.get("description");
		permission = (String) section.get("permission");
		ConfigurationSection menu = section.getConfigurationSection("menu");
		if (menu != null) {
			Set<String> keys = menu.getKeys(false);
			for(String key : keys) {
				RawCommand rawCommand = new RawCommand(menu.getConfigurationSection(key));
				menuCommands.add(rawCommand);
			}
		} else {
			template = (String) section.get("template");
		}
	}

	public String getTemplate() {
		return template;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Material getMaterial() {
		return material;
	}

	public String getPermission() {
		return permission;
	}

	public List<RawCommand> getMenuCommands() {
		return menuCommands;
	}
}
