package com.bitanalytic.modmanagement.gui;

import com.bitanalytic.modmanagement.ModManagement;
import com.bitanalytic.modmanagement.manager.CommandManager;
import com.bitanalytic.modmanagement.manager.ModCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManageGui implements Listener {

	private static final String banCommand = "ban {target} {0}";
	private static final String kickCommand = "kick {target} {0}";
	private static final String whisperCommand = "w {target} {0}";
	private static final String tpCommand = "tp {player} {target}";
	private static final String opCommand = "op {target}";


	private Inventory inv;

	private Player staff;
	private Player targetPlayer;

	private ModCommand currentCommand;
	private List<ModCommand> commandList;
	private List<String> commandArgs = new ArrayList<>();

	private boolean waitingForChat;

	private Map<Integer, ModCommand> commands = new HashMap<>();

	public PlayerManageGui(List<ModCommand> commandList) {
		inv = Bukkit.createInventory(null, 9, "Player Management");
		this.commandList = commandList;
	}

	private void createInventory(Player player) {
		int index = 0;
		for (ModCommand command : commandList) {
			if (command.doesPlayerHavePermission(player)) {
				commands.put(index, command);
				inv.setItem(index, command.getItemStack());
				index++;
			}
		}
	}

	public void openInventory(Player player, Player targetPlayer) {
		createInventory(player);
		player.openInventory(inv);
		this.staff = player;
		this.targetPlayer = targetPlayer;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		removeListeners();
	}

	private void removeListeners() {
		HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() == staff) {
			event.setCancelled(true);
		}

		if (event.getClickedInventory() != inv || event.getWhoClicked() != staff) {
			return;
		}


		ModCommand command = commands.get(event.getSlot());
		if (command == null) {
			return;
		}

		currentCommand = command;

		if (currentCommand.getArgCount() == 0) {
			staff.closeInventory();
			currentCommand.execute(staff, targetPlayer, commandArgs);
			return;
		}

		waitingForChat = true;
		staff.closeInventory();
		staff.sendMessage("Please input a reason: ");
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (!waitingForChat || event.getPlayer() != staff) {
			return;
		}

		event.setCancelled(true);

		commandArgs.add(event.getMessage());
		if (commandArgs.size() == currentCommand.getArgCount()) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ModManagement.PLUGIN,
					() -> currentCommand.execute(staff, targetPlayer, commandArgs), 1);
			removeListeners();
		}

	}
}
