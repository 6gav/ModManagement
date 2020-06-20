package com.bitanalytic.modmanagement.gui;

import com.bitanalytic.modmanagement.ModManagement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.block.Skull;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class PlayerListGui implements Listener {

	private Inventory inv;

	public PlayerListGui() {
		inv = Bukkit.createInventory(null, 54, "Player List");

		initializeItems();

		ModManagement.SERVER.getPluginManager().registerEvents(this, ModManagement.PLUGIN);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		removeListeners();
	}

	private void removeListeners() {
		HandlerList.unregisterAll(this);
	}

	public void initializeItems() {
		Bukkit.getServer().getOnlinePlayers().forEach(player -> {
			inv.addItem(createPlayerItem(player.getUniqueId()));
		});
	}

	private ItemStack createPlayerItem(UUID uuid) {
		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
		skullMeta.setOwningPlayer(Bukkit.getServer().getOfflinePlayer(uuid));
		head.setItemMeta(skullMeta);
		return head;
	}

	public void updateInventory() {
		inv.clear();
		initializeItems();
	}

	public void openInventory(final HumanEntity player) {
		updateInventory();
		player.openInventory(inv);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory() == inv) {
			removeListeners();
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory() != inv) {
			return;
		}

		event.setCancelled(true);

		final ItemStack head = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();

		if (head == null || head.getType() != Material.PLAYER_HEAD) {
			return;
		}

		SkullMeta skullMeta = (SkullMeta) head.getItemMeta();

		player.sendMessage(skullMeta.getOwningPlayer().getName());

		PlayerManageGui playerManageGui = new PlayerManageGui();
		ModManagement.SERVER.getPluginManager().registerEvents(playerManageGui, ModManagement.PLUGIN);
		playerManageGui.openInventory(player, (Player) skullMeta.getOwningPlayer());

		removeListeners();

	}

}
