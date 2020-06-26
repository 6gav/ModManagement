package com.bitanalytic.modmanagement.manager;

import com.bitanalytic.modmanagement.ModManagement;
import com.bitanalytic.modmanagement.domain.RawCommand;
import com.bitanalytic.modmanagement.gui.PlayerManageGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModCommand {

	private String command;
	private int argCount;
	private String permission;
	private ItemStack itemStack;
	private List<ModCommand> menuCommands = new ArrayList<>();

	public ModCommand(RawCommand rawCommand) {
		if (rawCommand.getTemplate() != null) {
			this.command = rawCommand.getTemplate();
			this.argCount = parseArgCount(rawCommand.getTemplate());
		}
		this.permission = rawCommand.getPermission();
		this.itemStack = new ItemStack(rawCommand.getMaterial(), 1);
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.setDisplayName(rawCommand.getName());
		itemMeta.setLore(Arrays.asList(rawCommand.getDescription().split("\\|")));
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		this.itemStack.setItemMeta(itemMeta);

		for (RawCommand r : rawCommand.getMenuCommands()) {
			menuCommands.add(new ModCommand(r));
		}
	}

	private int parseArgCount(String template) {
		int count = 0;

		Pattern pattern = Pattern.compile("\\{[0-9]*\\}");
		Matcher matcher = pattern.matcher(template);
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	public void execute(Player player, Player targetPlayer, List<String> args) {
		if (isPlayerAbleToOpenMenu(player)) {
			openMenu(player, targetPlayer);
		} else {
			String executeCommand = parse(player, targetPlayer, args);
			player.performCommand(executeCommand);
		}
	}

	private void openMenu(Player player, Player targetPlayer) {
		PlayerManageGui playerManageGui = new PlayerManageGui(menuCommands);
		ModManagement.SERVER.getPluginManager().registerEvents(playerManageGui, ModManagement.PLUGIN);
		playerManageGui.openInventory(player, targetPlayer);
	}

	private boolean isPlayerAbleToOpenMenu(Player player) {
		if (menuCommands.size() == 0) {
			return false;
		}

		for (ModCommand c : menuCommands) {
			if (!c.doesPlayerHavePermission(player)) {
				return false;
			}
		}

		return true;
	}

	private String parse(Player player, Player target, List<String> args) {
		String parsed = command.toLowerCase();

		for (int i = 0; i < args.size(); i++) {
			parsed = parsed.replace("{" + i + "}", args.get(i));
		}

		parsed = parsed.replace("{player}", player.getName());
		parsed = parsed.replace("{target}", target.getName());

		return parsed;
	}

	public boolean doesPlayerHavePermission(Player player) {
		if (permission == null || permission.isEmpty()) {
			return true;
		}
		return player.hasPermission(permission);
	}

	public int getArgCount() {
		return argCount;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}
}
