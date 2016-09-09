package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.GUI;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerManager;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ChatUtils;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ItemStacks;

public class GUI {
	
	public static void openMainMenu(Player p) {
		if (p.hasPermission("pfx.cs.menu.system")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "CloudSystem", 9);
			menu.addItem(ItemStacks.getServerVerwalten(), 0);
			p.openInventory(menu.getInventory());
		}
	}
	
	public static void openServerVerwalten(Player p) {
		if (p.hasPermission("pfx.cs.menu.verwalten")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "Server Verwalten", 45);
			int i = 0;
			if (MinecraftServerManager.getInstance().getMinecraftServers().values().size() < (menu.getInventory().getSize() - 6)) {
				for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
					menu.addItem(ItemStacks.getMinecraftServer(mcs), i);
					i++;
				}
				menu.addItem(ItemStacks.getBack(), 40);
			} else {
				ChatUtils.sendMessage(p, "Es können maximal " + (menu.getInventory().getSize() - 7) + " Server angezeigt werden!");
			}
			p.openInventory(menu.getInventory());
		}
	}
	
	public static void openServerVerwaltenServer(Player p, MinecraftServer mcs) {
		if (p.hasPermission("pfx.cs.menu.verwalten.server")) {
			CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_GREEN + "Server: " + mcs.getBungeeCordServername(), 9);
			menu.addItem(ItemStacks.getServerStart(), 0);
			menu.addItem(ItemStacks.getServerStop(), 1);
			menu.addItem(ItemStacks.getServerBetreten(), 2);
			menu.addItem(ItemStacks.getBack(), 8);
			p.openInventory(menu.getInventory());
		}
	}
}