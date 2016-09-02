package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.GUI;

import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerManager;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ChatUtils;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ItemStacks;

public class GUI {
	
	public static void openMainMenu(Player p) {
		if (p.hasPermission("pfx.cs.menu.system")) {
			CustomGUIMenu menu = new CustomGUIMenu("CloudSystem", 9);
			menu.addItem(ItemStacks.getServerVerwalten(), 0);
			p.openInventory(menu.getInventory());
		}
	}
	
	public static void openServerVerwalten(Player p) {
		if (p.hasPermission("pfx.cs.menu.verwalten")) {
			CustomGUIMenu menu = new CustomGUIMenu("Server Verwalten", 9);
			int i = 0;
			if (MinecraftServerManager.getInstance().getMinecraftServers().values().size() < menu.getInventory().getSize()) {
				for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
					menu.addItem(ItemStacks.getMinecraftServer(mcs), i);
					i++;
				}
			} else {
				ChatUtils.sendMessage(p, "Es können maximal " + menu.getInventory().getSize() + " Server angezeigt werden!");
			}
			p.openInventory(menu.getInventory());
		}
	}
	
	public static void openServerVerwaltenServer(Player p, MinecraftServer mcs) {
		if (p.hasPermission("pfx.cs.menu.verwalten.server")) {
			CustomGUIMenu menu = new CustomGUIMenu("Server: " + mcs.getBungeeCordServername(), 9);
			menu.addItem(ItemStacks.getServerStart(), 0);
			menu.addItem(ItemStacks.getServerStop(), 4);
			menu.addItem(ItemStacks.getServerBetreten(), 8);
			p.openInventory(menu.getInventory());
		}
	}
}