package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.GUI;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.SSMaster;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerManager;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ChatUtils;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ItemStacks;

public class GUI {
	
	public static void openMainMenu(Player p) {
		if (p.hasPermission("pfx.cs.menu.system")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "CloudSystem", 9);
			menu.addItem(ItemStacks.getServerSteuerung(), 1);
			menu.addItem(ItemStacks.getSchlieﬂen(), 4);
			menu.addItem(ItemStacks.getServerVerwalten(), 7);
			p.openInventory(menu.getInventory());
		}
	}
	
	public static void openServerSteuerung(Player p) {
		if (p.hasPermission("pfx.cs.menu.steuern")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "Server Steuerung", 45);
			int i = 0;
			if (MinecraftServerManager.getInstance().getMinecraftServers().values().size() < (menu.getInventory().getSize() - 6)) {
				for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
					menu.addItem(ItemStacks.getMinecraftServer(mcs), i);
					i++;
				}
				menu.addItem(ItemStacks.getBack(), 40);
				p.openInventory(menu.getInventory());
			} else {
				ChatUtils.sendMessage(p, "Es kˆnnen maximal " + (menu.getInventory().getSize() - 7) + " Server angezeigt werden!");
			}
		}
	}
	
	public static void openServerSteuerungServer(Player p, MinecraftServer mcs) {
		if (p.hasPermission("pfx.cs.menu.steuern.server")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "Server: " + mcs.getBungeeCordServername(), 9);
			menu.addItem(ItemStacks.getServerStart(), 0);
			menu.addItem(ItemStacks.getServerStop(), 1);
			menu.addItem(ItemStacks.getServerBetreten(), 2);
			menu.addItem(ItemStacks.getBack(), 8);
			p.openInventory(menu.getInventory());
		}
	}
	
	public static void openServerVerwalten(Player p) {
		if (p.hasPermission("pfx.cs.menu.verwalten")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "Server Verwalten", 9);
			menu.addItem(ItemStacks.getServerErstellen(), 0);
			menu.addItem(ItemStacks.getServerLˆschen(), 1);
			menu.addItem(ItemStacks.getBack(), 8);
			p.openInventory(menu.getInventory());
		}
	}
	
	public static void openServerVerwaltenErstellen(Player p) {
		if (p.hasPermission("pfx.cs.menu.verwalten")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "W‰hle ein Spiel", 9);
			int i = 0;
			if (MinecraftServerManager.getInstance().Minigames.size() < 8) {
				for (String gamename : MinecraftServerManager.getInstance().Minigames) {
					menu.addItem(ItemStacks.getServerErstellenTemplate(gamename), i);
					i++;
				}
				p.openInventory(menu.getInventory());
			} else {
				ChatUtils.sendMessage(p, "Es kˆnnen maximal 7 Minigames angezeigt werden!");
			}
		}
	}
	
	public static void openServerVerwaltenErstellenModi(Player p, String gamename) {
		if (p.hasPermission("pfx.cs.menu.verwalten")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "W‰hle einen Spielmodus", 9);
			String modis = MinecraftServerManager.getInstance().Spielmodis.get(gamename);
			int i = 0;
			if (modis.split(":").length < 8) {
				for (String modi : modis.split(":")) {
					menu.addItem(ItemStacks.getServerErstellenModi(gamename, modi), i);
					i++;
				}
				menu.addItem(ItemStacks.getBack(), 8);
				p.openInventory(menu.getInventory());
			} else {
				ChatUtils.sendMessage(p, "Es kˆnnen maximal 7 Spielmodis angezeigt werden!");
			}
		}
	}
	
	public static void openServerVerwaltenErstellenMap(Player p, String gamename, String modi) {
		if (p.hasPermission("pfx.cs.menu.verwalten")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "W‰hle eine Map", 9);
			String maps = MinecraftServerManager.getInstance().Maps.get(ChatColor.stripColor(gamename));
			int i = 0;
			for (String map : maps.split(":")) {
				menu.addItem(ItemStacks.getServerErstellenMap(ChatColor.stripColor(gamename), modi, map), i);
				i++;
			}
			menu.addItem(ItemStacks.getBack(), 8);
			p.openInventory(menu.getInventory());
		}
	}
	
	public static void openServerVerwaltenLˆschen(Player p) {
		if (p.hasPermission("pfx.cs.menu.verwalten")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "Server Lˆschen", 45);
			int i = 0;
			if (MinecraftServerManager.getInstance().getMinecraftServers().values().size() < (menu.getInventory().getSize() - 6)) {
				for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
					menu.addItem(ItemStacks.getMinecraftServer(mcs), i);
					i++;
				}
				menu.addItem(ItemStacks.getBack(), 40);
			} else {
				ChatUtils.sendMessage(p, "Es kˆnnen maximal " + (menu.getInventory().getSize() - 7) + " Server angezeigt werden!");
			}
			p.openInventory(menu.getInventory());
		}
	}
	
	public static void openServerVerwaltenLˆschenBest‰tigen(Player p, MinecraftServer mcs) {
		if (p.hasPermission("pfx.cs.menu.verwalten")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "Lˆschen: " + mcs.getBungeeCordServername(), 9);
			menu.addItem(ItemStacks.getServerLˆschenBest‰tigen(), 0);
			menu.addItem(ItemStacks.getServerInfo(mcs), 4);
			menu.addItem(ItemStacks.getServerAbbrechen(), 8);
			p.openInventory(menu.getInventory());
		}
	}
	
	public static void openServerVerwaltenErstellenBest‰tigen(Player p, String gamename, String modi, String map) {
		if (p.hasPermission("pfx.cs.menu.verwalten")) {
			MinecraftServer mcs = new MinecraftServer(gamename + MinecraftServerManager.getInstance().nextBungeeCordServername(), MinecraftServerManager.getInstance().nextPort(), map, modi);
			File template = new File(SSMaster.getInstance().cspath + "templates/" + mcs.getBungeeCordServername().replace("0", "").replace("1", "").replace("2", "").replace("3", "").replace("4", "").replace("5", "").replace("6", "").replace("7", "").replace("8", "").replace("9", "") + "_" + mcs.getMap() + mcs.getModi() + "/");
			if (template.exists()) {
				CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "Erstellen: " + gamename, 9);
				menu.addItem(ItemStacks.getServerErstellenBest‰tigen(), 0);
				menu.addItem(ItemStacks.getServerInfo(mcs), 4);
				menu.addItem(ItemStacks.getServerAbbrechen(), 8);
				p.openInventory(menu.getInventory());
			} else {
				p.closeInventory();
				ChatUtils.sendMessage(p, "Kein Template f¸r diesen Server vorhanden!");
			}
		}
	}
}