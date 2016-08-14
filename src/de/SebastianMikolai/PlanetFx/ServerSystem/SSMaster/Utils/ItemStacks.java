package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerStatus;

public class ItemStacks {
	
	public static ItemStack getMinecraftServer(MinecraftServer mcs) {
		if (mcs.getStatus() == MinecraftServerStatus.Online || mcs.getStatus() == MinecraftServerStatus.Waiting) {
			if (mcs.getOnlinePlayer() == 0) {
				ItemStack item = new ItemStack(Material.IRON_BLOCK);
				ItemMeta itemmeta = item.getItemMeta();
				itemmeta.setDisplayName(ChatColor.GOLD + mcs.getBungeeCordServername());
				String[] lores = (ChatColor.LIGHT_PURPLE + "0/" + mcs.getMaxPlayer()).split(":");
				ArrayList<String> lore = new ArrayList<String>();
				for (int i = 0; i < lores.length; i++) {
					lore.add(lores[i]);
				}
				item.setAmount(0);
				itemmeta.setLore(lore);
				item.setItemMeta(itemmeta);
				return item;
			} else {
				ItemStack item = new ItemStack(Material.GOLD_BLOCK);
				ItemMeta itemmeta = item.getItemMeta();
				itemmeta.setDisplayName(ChatColor.GOLD + mcs.getBungeeCordServername());
				String[] lores = (ChatColor.LIGHT_PURPLE + "" + mcs.getOnlinePlayer() + "/" + mcs.getMaxPlayer()).split(":");
				ArrayList<String> lore = new ArrayList<String>();
				for (int i = 0; i < lores.length; i++) {
					lore.add(lores[i]);
				}
				item.setAmount(mcs.getOnlinePlayer());
				itemmeta.setLore(lore);
				item.setItemMeta(itemmeta);
				return item;
			}
		} else if (mcs.getStatus() == MinecraftServerStatus.Offline) {
			ItemStack item = new ItemStack(Material.COAL_BLOCK);
			ItemMeta itemmeta = item.getItemMeta();
			itemmeta.setDisplayName(ChatColor.GOLD + mcs.getBungeeCordServername());
			String[] lores = (ChatColor.RED + "Server ist Offline").split(":");
			ArrayList<String> lore = new ArrayList<String>();
			for (int i = 0; i < lores.length; i++) {
				lore.add(lores[i]);
			}
			itemmeta.setLore(lore);
			item.setItemMeta(itemmeta);
			return item;
		} else {
			ItemStack item = new ItemStack(Material.COAL_BLOCK);
			ItemMeta itemmeta = item.getItemMeta();
			itemmeta.setDisplayName(ChatColor.GOLD + mcs.getBungeeCordServername());
			String[] lores = (ChatColor.RED + "Server ist Offline").split(":");
			ArrayList<String> lore = new ArrayList<String>();
			for (int i = 0; i < lores.length; i++) {
				lore.add(lores[i]);
			}
			itemmeta.setLore(lore);
			item.setItemMeta(itemmeta);
			return item;
		}
	}
	
	public static ItemStack Werbung() {
		ItemStack item = new ItemStack(Material.WOOL);
		item.setDurability((short)4);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Werbung");
		String[] lores = "&aKaufe dir &4Premium &aum:&adiese Werbung zu �berspringen".split(":");
		ArrayList<String> lore = new ArrayList<String>();
		for (int i = 0; i < lores.length; i++) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores[i]));
		}
		itemmeta.setLore(lore);
		item.setItemMeta(itemmeta);
		return item;
	}
}