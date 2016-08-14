package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.NPC;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.SSMaster;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerManager;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerStatus;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ItemStacks;

public class NPCInventory {

	public static Map<Player, Integer> TaskIDWerbungScheduler = new HashMap<Player, Integer>();
	public static Map<Player, Integer> TaskIDGameAktualisierenScheduler = new HashMap<Player, Integer>();
	
	public static void openInventory(Player p, String name, String gamename, String modi) {
		if (p.hasPermission("pfx.cs.vip")) {
			openInventoryMinigames(p, name, gamename, modi);
		} else {
			openInventoryWerbung(p, name, gamename, modi);
		}
	}
	
	public static void openInventoryWerbung(Player p, String name, String gamename, String modi) {
		if (TaskIDWerbungScheduler.containsKey(p)) {
			Bukkit.getScheduler().cancelTask(TaskIDWerbungScheduler.get(p));
			TaskIDWerbungScheduler.remove(p);
		}
		TaskIDWerbungScheduler.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(SSMaster.getInstance(), new WerbungScheduler(p, name, gamename, modi), 0L, 15L));
	}
	
	public static void openInventoryMinigames(Player p, String name, String gamename, String modi) {
		Inventory inv = Bukkit.createInventory(null, 27, "Game: " + name);
		int i = 0;
		for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
			if (mcs.getBungeeCordServername().contains(gamename) && mcs.getModi().equalsIgnoreCase(modi) && mcs.getStatus() != MinecraftServerStatus.Running && i < 7) {
				inv.setItem(10 + i, ItemStacks.getMinecraftServer(mcs));
				i++;
			}
		}
		p.openInventory(inv);
		if (TaskIDGameAktualisierenScheduler.containsKey(p)) {
			Bukkit.getScheduler().cancelTask(TaskIDGameAktualisierenScheduler.get(p));
			TaskIDGameAktualisierenScheduler.remove(p);
		}
		TaskIDGameAktualisierenScheduler.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(SSMaster.getInstance(), new GameAktualisierenScheduler(p, name, gamename, modi), 20L, 20L));
	}
}