package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.GUI;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CustomGUIMenu {
	
	private Inventory inv;
	
	public CustomGUIMenu(String name, int slots) {
		this.inv = Bukkit.createInventory(null, slots, name);
	}
	
	public void addItem(ItemStack item, int slot) {
		this.inv.setItem(slot, item);
	}
	
	public Inventory getInventory() {
		return this.inv;
	}
}