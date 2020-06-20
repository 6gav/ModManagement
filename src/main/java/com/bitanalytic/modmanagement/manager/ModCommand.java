package com.bitanalytic.modmanagement.manager;

import org.bukkit.entity.Player;

import java.util.List;

public class ModCommand {

	private String command;
	private int argCount;

	public ModCommand(String command, int argCount) {
		this.command = command;
		this.argCount = argCount;
	}

	public void execute(Player player, Player targetPlayer, List<String> args) {
		String executeCommand = parse(player, targetPlayer, args);
		player.sendMessage(executeCommand);
		player.performCommand(executeCommand);
	}

	private String parse(Player player, Player target, List<String> args) {
		String parsed = command.toLowerCase();

		for(int i = 0; i < args.size(); i++) {
			parsed = parsed.replace("{" + i +  "}", args.get(i));
		}

		parsed = parsed.replace("{player}", player.getName());
		parsed = parsed.replace("{target}", target.getName());

		return parsed;
	}

	public int getArgCount() {
		return argCount;
	}
}
