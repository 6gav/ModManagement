package com.bitanalytic.modmanagement.command;

import com.bitanalytic.modmanagement.gui.PlayerListGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModMenuCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Must be run by a player");
			return true;
		}

		Player player = (Player) sender;

		//create mod menu
		PlayerListGui playerListGui = new PlayerListGui();

		//open mod menu
		playerListGui.openInventory(player);

		return true;
	}
}
