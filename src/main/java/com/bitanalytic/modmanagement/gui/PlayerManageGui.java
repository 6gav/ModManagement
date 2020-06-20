package com.bitanalytic.modmanagement.gui;

import com.bitanalytic.modmanagement.ModManagement;
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
	private List<String> commandArgs = new ArrayList<>();

	private boolean waitingForChat;

	private Map<Integer, ModCommand> commands = new HashMap<>();

	public PlayerManageGui() {
		inv = Bukkit.createInventory(null, 9, "Player Management");
		commands.put(0, new ModCommand(banCommand, 1));
		ItemStack banItem = new ItemStack(Material.IRON_AXE, 1);
		ItemMeta banMeta = banItem.getItemMeta();
		banMeta.setDisplayName("Ban Player");
		banItem.setItemMeta(banMeta);
		inv.setItem(0, banItem);

		commands.put(2, new ModCommand(kickCommand, 1));
		ItemStack kickItem = new ItemStack(Material.IRON_BOOTS, 1);
		ItemMeta kickMeta = kickItem.getItemMeta();
		kickMeta.setDisplayName("Kick Player");
		kickItem.setItemMeta(kickMeta);
		inv.setItem(2, kickItem);

		commands.put(4, new ModCommand(whisperCommand, 1));
		ItemStack whisperItem = new ItemStack(Material.GHAST_TEAR, 1);
		ItemMeta whisperMeta = whisperItem.getItemMeta();
		whisperMeta.setDisplayName("Whisper to Player");
		whisperItem.setItemMeta(whisperMeta);
		inv.setItem(4, whisperItem);

		commands.put(6, new ModCommand(tpCommand, 0));
		ItemStack tpItem = new ItemStack(Material.ENDER_PEARL, 1);
		tpItem.getItemMeta().setDisplayName("Teleport to Player");
		inv.setItem(6, tpItem);

		commands.put(8, new ModCommand(opCommand, 0));
		ItemStack opItem = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta opMeta = opItem.getItemMeta();
		opMeta.setDisplayName("OP Player");
		opItem.setItemMeta(opMeta);
		inv.setItem(8, opItem);
	}

	public void openInventory(Player player, Player targetPlayer) {
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
		if (event.getInventory() != inv || event.getWhoClicked() != staff) {
			return;
		}

		event.setCancelled(true);


		ModCommand command = commands.get(event.getSlot());
		if (command == null) {
			return;
		}

		currentCommand = command;

		if (currentCommand.getArgCount() == 0) {
			currentCommand.execute(staff, targetPlayer, commandArgs);
			return;
		}

		waitingForChat = true;
		staff.sendMessage("Please input a reason: ");
		staff.closeInventory();
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
