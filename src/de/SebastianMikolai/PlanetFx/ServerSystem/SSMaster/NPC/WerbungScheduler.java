package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.NPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ItemStacks;

public class WerbungScheduler implements Runnable {
	
	private final Player p;
	private int status = -1;
	private String name;
	private String gamename;
	private String modi;
	private int TaskID;
	
	public WerbungScheduler(Player _p, String _name, String _gamename, String _modi) {
		p = _p;
		name = _name;
		gamename = _gamename;
		modi = _modi;
	}
	
	@Override
	public void run() {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_RED +  "*** Werbung ***");
		if (status == -1) {
			status++;
			TaskID = NPCInventory.TaskIDWerbungScheduler.get(p);
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			p.openInventory(inv);
			NPCInventory.TaskIDWerbungScheduler.put(p, TaskID);
		} else if (status == 0) {
			inv.setItem(0, ItemStacks.Werbung());
			status++;
			TaskID = NPCInventory.TaskIDWerbungScheduler.get(p);
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			p.openInventory(inv);
			NPCInventory.TaskIDWerbungScheduler.put(p, TaskID);
		} else if (status == 1) {
			inv.setItem(0, ItemStacks.Werbung());
			inv.setItem(1, ItemStacks.Werbung());
			status++;
			TaskID = NPCInventory.TaskIDWerbungScheduler.get(p);
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			p.openInventory(inv);
			NPCInventory.TaskIDWerbungScheduler.put(p, TaskID);
		} else if (status == 2) {
			inv.setItem(0, ItemStacks.Werbung());
			inv.setItem(1, ItemStacks.Werbung());
			inv.setItem(2, ItemStacks.Werbung());
			status++;
			TaskID = NPCInventory.TaskIDWerbungScheduler.get(p);
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			p.openInventory(inv);
			NPCInventory.TaskIDWerbungScheduler.put(p, TaskID);
		} else if (status == 3) {
			inv.setItem(0, ItemStacks.Werbung());
			inv.setItem(1, ItemStacks.Werbung());
			inv.setItem(2, ItemStacks.Werbung());
			inv.setItem(3, ItemStacks.Werbung());
			status++;
			TaskID = NPCInventory.TaskIDWerbungScheduler.get(p);
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			p.openInventory(inv);
			NPCInventory.TaskIDWerbungScheduler.put(p, TaskID);
		} else if (status == 4) {
			inv.setItem(0, ItemStacks.Werbung());
			inv.setItem(1, ItemStacks.Werbung());
			inv.setItem(2, ItemStacks.Werbung());
			inv.setItem(3, ItemStacks.Werbung());
			inv.setItem(4, ItemStacks.Werbung());
			status++;
			TaskID = NPCInventory.TaskIDWerbungScheduler.get(p);
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			p.openInventory(inv);
			NPCInventory.TaskIDWerbungScheduler.put(p, TaskID);
		} else if (status == 5) {
			inv.setItem(0, ItemStacks.Werbung());
			inv.setItem(1, ItemStacks.Werbung());
			inv.setItem(2, ItemStacks.Werbung());
			inv.setItem(3, ItemStacks.Werbung());
			inv.setItem(4, ItemStacks.Werbung());
			inv.setItem(5, ItemStacks.Werbung());
			status++;
			TaskID = NPCInventory.TaskIDWerbungScheduler.get(p);
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			p.openInventory(inv);
			NPCInventory.TaskIDWerbungScheduler.put(p, TaskID);
		} else if (status == 6) {
			inv.setItem(0, ItemStacks.Werbung());
			inv.setItem(1, ItemStacks.Werbung());
			inv.setItem(2, ItemStacks.Werbung());
			inv.setItem(3, ItemStacks.Werbung());
			inv.setItem(4, ItemStacks.Werbung());
			inv.setItem(5, ItemStacks.Werbung());
			inv.setItem(6, ItemStacks.Werbung());
			status++;
			TaskID = NPCInventory.TaskIDWerbungScheduler.get(p);
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			p.openInventory(inv);
			NPCInventory.TaskIDWerbungScheduler.put(p, TaskID);
		} else if (status == 7) {
			inv.setItem(0, ItemStacks.Werbung());
			inv.setItem(1, ItemStacks.Werbung());
			inv.setItem(2, ItemStacks.Werbung());
			inv.setItem(3, ItemStacks.Werbung());
			inv.setItem(4, ItemStacks.Werbung());
			inv.setItem(5, ItemStacks.Werbung());
			inv.setItem(6, ItemStacks.Werbung());
			inv.setItem(7, ItemStacks.Werbung());
			status++;
			TaskID = NPCInventory.TaskIDWerbungScheduler.get(p);
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			p.openInventory(inv);
			NPCInventory.TaskIDWerbungScheduler.put(p, TaskID);
		} else if (status == 8) {
			inv.setItem(0, ItemStacks.Werbung());
			inv.setItem(1, ItemStacks.Werbung());
			inv.setItem(2, ItemStacks.Werbung());
			inv.setItem(3, ItemStacks.Werbung());
			inv.setItem(4, ItemStacks.Werbung());
			inv.setItem(5, ItemStacks.Werbung());
			inv.setItem(6, ItemStacks.Werbung());
			inv.setItem(7, ItemStacks.Werbung());
			inv.setItem(8, ItemStacks.Werbung());
			status++;
			TaskID = NPCInventory.TaskIDWerbungScheduler.get(p);
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			p.openInventory(inv);
			NPCInventory.TaskIDWerbungScheduler.put(p, TaskID);
		} else {
			Bukkit.getScheduler().cancelTask(NPCInventory.TaskIDWerbungScheduler.get(p));
			NPCInventory.TaskIDWerbungScheduler.remove(p);
			NPCInventory.openInventoryMinigames(p, name, gamename, modi);
		}
	}
}