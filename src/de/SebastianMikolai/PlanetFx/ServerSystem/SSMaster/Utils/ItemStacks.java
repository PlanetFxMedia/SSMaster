package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerStatus;

public class ItemStacks {
	
	public static ItemStack getMinecraftServer(MinecraftServer mcs) {
		if (mcs.getStatus() == MinecraftServerStatus.Online) {
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
		String[] lores = "&aKaufe dir &4Premium &aum:&adiese Werbung zu überspringen".split(":");
		ArrayList<String> lore = new ArrayList<String>();
		for (int i = 0; i < lores.length; i++) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores[i]));
		}
		itemmeta.setLore(lore);
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerVerwalten() {
		ItemStack item = new ItemStack(Material.ANVIL);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.GOLD + "Server Verwalten");
		String[] lores = (ChatColor.LIGHT_PURPLE + "Erstelle und Lösche Server").split(":");
		ArrayList<String> lore = new ArrayList<String>();
		for (int i = 0; i < lores.length; i++) {
			lore.add(lores[i]);
		}
		itemmeta.setLore(lore);
		itemmeta.addEnchant(Enchantment.KNOCKBACK, 10, false);
		item.setItemMeta(itemmeta);
		return item;
	}

	public static ItemStack getBack() {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Zurück");
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getSchließen() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Schließen");
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerStart() {
		ItemStack item = new ItemStack(Material.WOOL);
		item.setDurability((short)5);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Starten");
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerStop() {
		ItemStack item = new ItemStack(Material.WOOL);
		item.setDurability((short)14);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Stoppen");
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerBetreten() {
		ItemStack item = new ItemStack(Material.WOOL);
		item.setDurability((short)4);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Betreten");
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerSteuerung() {
		ItemStack item = new ItemStack(Material.WORKBENCH);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.GOLD + "Server Steuerung");
		String[] lores = (ChatColor.LIGHT_PURPLE + "Starte und Stoppe Server").split(":");
		ArrayList<String> lore = new ArrayList<String>();
		for (int i = 0; i < lores.length; i++) {
			lore.add(lores[i]);
		}
		itemmeta.setLore(lore);
		itemmeta.addEnchant(Enchantment.KNOCKBACK, 10, false);
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerErstellen() {
		ItemStack item = new ItemStack(Material.WOOL);
		item.setDurability((short)5);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Erstellen");
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerLöschen() {
		ItemStack item = new ItemStack(Material.WOOL);
		item.setDurability((short)14);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Löschen");
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerErstellenTemplate(String gamename) {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + gamename);
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerErstellenModi(String gamename, String modi) {
		ItemStack item = new ItemStack(Material.DIAMOND);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + gamename + ": " + modi);
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerErstellenMap(String gamename, String modi, String map) {
		ItemStack item = new ItemStack(Material.REDSTONE);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + gamename + ": " + modi + " " + map);
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerErstellenBestätigen() {
		ItemStack item = new ItemStack(Material.WOOL);
		item.setDurability((short)5);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Ja Erstellen!");
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerLöschenBestätigen() {
		ItemStack item = new ItemStack(Material.WOOL);
		item.setDurability((short)5);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Ja Löschen!");
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerAbbrechen() {
		ItemStack item = new ItemStack(Material.WOOL);
		item.setDurability((short)14);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + "Nein Abbrechen!");
		item.setItemMeta(itemmeta);
		return item;
	}
	
	public static ItemStack getServerInfo(MinecraftServer mcs) {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(ChatColor.YELLOW + mcs.getBungeeCordServername());
		String[] lores = (ChatColor.LIGHT_PURPLE + "Map " + mcs.getMap() + ":" + ChatColor.LIGHT_PURPLE + "Port " + mcs.getPort() + ":" + ChatColor.LIGHT_PURPLE + "Spielmodi " + mcs.getModi()).split(":");
		ArrayList<String> lore = new ArrayList<String>();
		for (int i = 0; i < lores.length; i++) {
			lore.add(lores[i]);
		}
		itemmeta.setLore(lore);
		item.setItemMeta(itemmeta);
		return item;
	}
}