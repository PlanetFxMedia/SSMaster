package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.NPC;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerManager;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerStatus;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ItemStacks;

public class GameAktualisierenScheduler implements Runnable {
	
	private final Player p;
	private String name;
	private String gamename;
	private String modi;
	private int TaskID;
	
	public GameAktualisierenScheduler(Player _p, String _name, String _gamename, String _modi) {
		p = _p;
		name = _name;
		gamename = _gamename;
		modi = _modi;
	}
	
	@Override
	public void run() {
		Inventory inv = Bukkit.createInventory(null, 27, "Game: " + name);
		int i = 0;
		for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
			if (mcs.getBungeeCordServername().contains(gamename) && mcs.getModi().equalsIgnoreCase(modi) && mcs.getStatus() != MinecraftServerStatus.Running && i < 7) {
				inv.setItem(10 + i, ItemStacks.getMinecraftServer(mcs));
				i++;
			}
		}
		TaskID = NPCInventory.TaskIDGameAktualisierenScheduler.get(p);
		NPCInventory.TaskIDGameAktualisierenScheduler.remove(p);
		p.openInventory(inv);
		NPCInventory.TaskIDGameAktualisierenScheduler.put(p, TaskID);
	}
}