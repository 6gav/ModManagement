package com.bitanalytic.modmanagement.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerManageGui implements Listener {

	private Inventory inv;

	private Player staff;
	private Player targetPlayer;

	public PlayerManageGui() {
		inv = Bukkit.createInventory(null, 9, "Player Management");
		inv.addItem(new ItemStack(Material.IRON_AXE, 1));
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
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory() == inv && event.getPlayer() == staff) {
			removeListeners();
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory() != inv || event.getWhoClicked() != staff) {
			return;
		}

		event.setCancelled(true);

		event.getWhoClicked().sendMessage("Player " + staff + " attempted to act upon " + targetPlayer);

	}
}
