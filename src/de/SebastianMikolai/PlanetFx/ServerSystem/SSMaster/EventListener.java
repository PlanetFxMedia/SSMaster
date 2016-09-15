package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.BarAPI.BarAPI;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Datenbank.MySQL;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.GUI.GUI;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerManager;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerStatus;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.NPC.NPCInventory;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ChatUtils;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ItemStacks;

import net.citizensnpcs.npc.entity.EntityHumanNPC.PlayerNPC;

public class EventListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("pfx.cs.noforcegamemode")) {
			p.setGameMode(GameMode.SURVIVAL);
		}
		p.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		BarAPI.removeBar(e.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerKick(PlayerKickEvent e) {
		BarAPI.removeBar(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if (e.getRightClicked() instanceof PlayerNPC) {
			PlayerNPC pnpc = (PlayerNPC)e.getRightClicked();
			String gamename = ChatColor.stripColor(pnpc.getName()).split(" ")[0];
			String modi = ChatColor.stripColor(pnpc.getName()).split(" ")[1];
			NPCInventory.openInventory(p, pnpc.getName(), gamename, modi);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player)e.getWhoClicked();
			if (!p.hasPermission("pfx.cs.inventoryclick")) {
				e.setCancelled(true);
			}
			if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
				if (ChatColor.stripColor(e.getClickedInventory().getTitle()).contains("Game: ")) {
					e.setCancelled(true);
					MinecraftServer mcs = MinecraftServerManager.getInstance().getMinecraftServer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					if (mcs != null) {
						if (mcs.getStatus() == MinecraftServerStatus.Online) {
							MinecraftServerManager.sendToServer(p, mcs.getBungeeCordServername());
						} else if (mcs.getStatus() == MinecraftServerStatus.Offline) {
							p.closeInventory();
							ChatUtils.sendMessage(p, "Dieser Server ist im Offline Modus");
						}
					}
				} else if (ChatColor.stripColor(e.getClickedInventory().getTitle()).contains("CloudSystem")) {
					e.setCancelled(true);
					if (e.getCurrentItem().isSimilar(ItemStacks.getServerSteuerung())) {
						GUI.openServerSteuerung(p);
					} else if (e.getCurrentItem().isSimilar(ItemStacks.getServerVerwalten())) {
						GUI.openServerVerwalten(p);
					} else if (e.getCurrentItem().isSimilar(ItemStacks.getSchließen())) {
						p.closeInventory();
					}
				} else if (ChatColor.stripColor(e.getClickedInventory().getName()).contains("Server Steuerung")) {
					e.setCancelled(true);
					MinecraftServer mcs = MinecraftServerManager.getInstance().getMinecraftServer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					if (mcs != null) {
						GUI.openServerSteuerungServer(p, mcs);
					} else if (e.getCurrentItem().isSimilar(ItemStacks.getBack())) {
						GUI.openMainMenu(p);
					}
				} else if (ChatColor.stripColor(e.getClickedInventory().getName()).contains("Server Verwalten")) {
					e.setCancelled(true);
					if (e.getCurrentItem().isSimilar(ItemStacks.getServerErstellen())) {
						GUI.openServerVerwaltenErstellen(p);
					} else if (e.getCurrentItem().isSimilar(ItemStacks.getServerLöschen())) {
						GUI.openServerVerwaltenLöschen(p);
					} else if (e.getCurrentItem().isSimilar(ItemStacks.getBack())) {
						GUI.openMainMenu(p);
					}
				} else if (ChatColor.stripColor(e.getClickedInventory().getName()).contains("Server Löschen")) {
					e.setCancelled(true);
					MinecraftServer mcs = MinecraftServerManager.getInstance().getMinecraftServer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					if (mcs != null) {
						GUI.openServerVerwaltenLöschenBestätigen(p, mcs);
					} else if (e.getCurrentItem().isSimilar(ItemStacks.getBack())) {
						GUI.openServerVerwalten(p);
					}
				} else if (ChatColor.stripColor(e.getClickedInventory().getName()).contains("Löschen: ")) {
					e.setCancelled(true);
					MinecraftServer mcs = MinecraftServerManager.getInstance().getMinecraftServer(ChatColor.stripColor(e.getClickedInventory().getName().split(" ")[1]));
					if (mcs != null) {
						if (e.getCurrentItem().isSimilar(ItemStacks.getServerLöschenBestätigen())) {
							File serverpath = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/");
							if (serverpath.exists()) {
								p.closeInventory();
								ChatUtils.sendMessage(p, "Dies dauert kurz!");
								MinecraftServerManager.getInstance().stopMinecraftServer(mcs);
								Bukkit.getScheduler().scheduleSyncDelayedTask(SSMaster.getInstance(), new Runnable() {
									@Override
									public void run() {
										MinecraftServerManager.getInstance().stopMinecraftServer(mcs);
										MinecraftServerManager.getInstance().removeMinecraftServer(mcs.getBungeeCordServername());
										MinecraftServerManager.getInstance().deleteServerPath(p, mcs.getBungeeCordServername());
										MySQL.deleteMinecraftServerStatus(mcs.getBungeeCordServername());
									}	
								}, 3*20L);
							} else {
								p.closeInventory();
								ChatUtils.sendMessage(p, "Server existiert nicht!");
							}
						} else if (e.getCurrentItem().isSimilar(ItemStacks.getServerAbbrechen())) {
							GUI.openServerVerwaltenLöschen(p);
						}
					}
				} else if (ChatColor.stripColor(e.getClickedInventory().getName()).contains("Wähle ein Spiel")) {
					e.setCancelled(true);
					if (e.getCurrentItem().isSimilar(ItemStacks.getBack())) {
						GUI.openServerVerwalten(p);
					} else {
						GUI.openServerVerwaltenErstellenModi(p, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					}
				} else if (ChatColor.stripColor(e.getClickedInventory().getName()).contains("Wähle einen Spielmodus")) {
					e.setCancelled(true);
					if (e.getCurrentItem().isSimilar(ItemStacks.getBack())) {
						GUI.openServerVerwaltenErstellen(p);
					} else {
						String gamename = e.getCurrentItem().getItemMeta().getDisplayName().split(":")[0];
						String modi = e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1];
						GUI.openServerVerwaltenErstellenMap(p, gamename, modi);
					}
				} else if (ChatColor.stripColor(e.getClickedInventory().getName()).contains("Wähle eine Map")) {
					e.setCancelled(true);
					if (e.getCurrentItem().isSimilar(ItemStacks.getBack())) {
						GUI.openServerVerwaltenErstellen(p);
					} else {
						String gamename = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().split(":")[0]);
						String modi = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]);
						String map = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[2]);
						GUI.openServerVerwaltenErstellenBestätigen(p, gamename, modi, map);
					}
				} else if (ChatColor.stripColor(e.getClickedInventory().getName()).contains("Erstellen: ") && e.getInventory().getItem(4) != null && e.getInventory().getItem(4).hasItemMeta()) {
					e.setCancelled(true);
					if (e.getCurrentItem().isSimilar(ItemStacks.getServerAbbrechen())) {
						GUI.openServerVerwaltenErstellen(p);
					} else if (e.getCurrentItem().isSimilar(ItemStacks.getServerErstellenBestätigen())) {
						String servername = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName());
						String map = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getLore().get(0).split(" ")[1]);
						Integer port = Integer.valueOf(ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getLore().get(1).split(" ")[1]));
						String modi = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getLore().get(2).split(" ")[1]);
						MinecraftServer mcs = new MinecraftServer(servername, port, map, modi);
						File template = new File(SSMaster.getInstance().cspath + "templates/" + mcs.getBungeeCordServername().replace("0", "").replace("1", "").replace("2", "").replace("3", "").replace("4", "").replace("5", "").replace("6", "").replace("7", "").replace("8", "").replace("9", "") + "_" + mcs.getMap() + mcs.getModi() + "/");
						if (template.exists()) {
							File serverpath = new File(SSMaster.getInstance().cspath + "server/" + servername + "/");
							if (!serverpath.exists()) {
								p.closeInventory();
								ChatUtils.sendMessage(p, "Dies dauert kurz!");
								MinecraftServerManager.getInstance().addMinecraftServer(mcs);
								MinecraftServerManager.getInstance().copyTemplateToServer(p, mcs);
								MySQL.addMinecraftServerStatus(mcs);
							} else {
								p.closeInventory();
								ChatUtils.sendMessage(p, "Server existiert bereits!");
							}
						} else {
							p.closeInventory();
							ChatUtils.sendMessage(p, "2 Kein Template für diesen Server vorhanden!");
						}
					}
				} else if (ChatColor.stripColor(e.getClickedInventory().getName()).contains("Server: ")) {
					e.setCancelled(true);
					MinecraftServer mcs = MinecraftServerManager.getInstance().getMinecraftServer(ChatColor.stripColor(e.getClickedInventory().getName().split(" ")[1]));
					if (mcs != null) {
						if (e.getCurrentItem().isSimilar(ItemStacks.getServerStart())) {
							p.closeInventory();
							try {
								Socket socket = new Socket(InetAddress.getLocalHost(), mcs.getPort());
								socket.close();
								ChatUtils.sendMessage(p, "Der Server ist bereits Online!");
							} catch (IOException ex) {
								MinecraftServerManager.getInstance().startMinecraftServer(mcs);
					          	ChatUtils.sendMessage(p, "Server " + mcs.getBungeeCordServername() + " startet!");
							}
						} else if (e.getCurrentItem().isSimilar(ItemStacks.getServerStop())) {
							p.closeInventory();
							try {
								Socket socket = new Socket(InetAddress.getLocalHost(), mcs.getPort());
								socket.close();
								MinecraftServerManager.getInstance().stopMinecraftServer(mcs);
					          	ChatUtils.sendMessage(p, "Server " + mcs.getBungeeCordServername() + " stoppt!");
							} catch (IOException ex) {
								ChatUtils.sendMessage(p, "Der Server ist bereits Offline!");
							}
						} else if (e.getCurrentItem().isSimilar(ItemStacks.getServerBetreten())) {
							p.closeInventory();
							MinecraftServerManager.sendToServer(p, mcs.getBungeeCordServername());
						} else if (e.getCurrentItem().isSimilar(ItemStacks.getBack())) {
							GUI.openServerSteuerung(p);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (e.getPlayer() instanceof Player) {
			Player p = (Player)e.getPlayer();
			if (ChatColor.stripColor(e.getInventory().getTitle()).contains("Game: ")) {
				if (NPCInventory.TaskIDGameAktualisierenScheduler.containsKey(p)) {
					Bukkit.getScheduler().cancelTask(NPCInventory.TaskIDGameAktualisierenScheduler.get(p));
					Bukkit.getScheduler().cancelTask(NPCInventory.TaskIDGameAktualisierenScheduler.get(p));
					NPCInventory.TaskIDGameAktualisierenScheduler.remove(p);
				}
			} else if (ChatColor.stripColor(e.getInventory().getTitle()).contains("Werbung")) {
				if (NPCInventory.TaskIDWerbungScheduler.containsKey(p)) {
					Bukkit.getScheduler().cancelTask(NPCInventory.TaskIDWerbungScheduler.get(p));
					Bukkit.getScheduler().cancelTask(NPCInventory.TaskIDWerbungScheduler.get(p));
					NPCInventory.TaskIDWerbungScheduler.remove(p);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (!e.getPlayer().hasPermission("pfx.cs.build")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (!e.getPlayer().hasPermission("pfx.cs.build")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getTo().getWorld().getName().equalsIgnoreCase("lobby")) {
			if (e.getTo().getBlockY() <= SSMaster.teleport) {
				e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
		    }
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHungerUpdate(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
	    BarAPI.handleTeleport(event.getPlayer(), event.getTo().clone());
	}
	  
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		BarAPI.handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
	}
}