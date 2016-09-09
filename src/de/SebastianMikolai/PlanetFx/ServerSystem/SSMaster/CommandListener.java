package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.BarAPI.BarAPI;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Datenbank.MySQL;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.GUI.GUI;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerManager;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ChatUtils;

public class CommandListener implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cs.hasPermission("pfx.cs.system")) {
			if (args.length != 0) {
				if (args[0].equalsIgnoreCase("help")) {
					cs.sendMessage("§6= = = = §a§lPlanet-Fx CloudSystem §6= = = =");
					cs.sendMessage("§6Hilfe: §f/cs help");
					cs.sendMessage("");
					cs.sendMessage("§6Server erstellen:     §f/cs add <Spielname> <Map> <Modi>");
					cs.sendMessage("§6Server löschen:       §f/cs delete <Servername>");
					cs.sendMessage("§6Server starten:       §f/cs start <Servername>");
					cs.sendMessage("§6Server stoppen:       §f/cs stop <Servername>");
					cs.sendMessage("§6Server betreten:      §f/cs server <Servername>");
					cs.sendMessage("§6Server updaten:       §f/cs update <Servername>");
					cs.sendMessage("§6Server auflisten:     §f/cs list");
					cs.sendMessage("§6Alle Server löschen:  §f/cs deleteall");
					cs.sendMessage("§6Alle Server starten:  §f/cs startall");
					cs.sendMessage("§6Alle Server stoppen:  §f/cs stopall");
					cs.sendMessage("§6Alle Server updaten:  §f/cs updateall");
					cs.sendMessage("§6BossBar Reload:       §f/cs bb reload");
				} else if (args[0].equalsIgnoreCase("server")) {
					if (cs instanceof Player) {
						Player p = (Player) cs;
						if (args.length >= 2) {
							String servername = args[1];
							MinecraftServerManager.sendToServer(p, servername);
						} else {
							ChatUtils.sendMessage(p, "Du musst einen Servernamen angeben!");
						}
					} else {
						ChatUtils.sendMessage(cs, "Du musst ein Spieler sein!");
					}
				} else if (args[0].equalsIgnoreCase("update")) {
					if (args.length == 2) {
						File serverpath = new File(SSMaster.getInstance().cspath + "server/" + args[1] + "/");
						if (serverpath.exists()) {
							MinecraftServer mcs = MinecraftServerManager.getInstance().getMinecraftServer(args[1]);
							MinecraftServerManager.getInstance().updateTemplateServer(cs, mcs);
						} else {
							ChatUtils.sendMessage(cs, "Server existiert nicht!");
						}
					} else {
						ChatUtils.sendMessage(cs, "Du musst einen Servernamen angeben!");
					}
				} else if (args[0].equalsIgnoreCase("updateall")) {
					for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values())	{	
						MinecraftServerManager.getInstance().updateTemplateServer(cs, mcs);
					}
				} else if (args[0].equalsIgnoreCase("bb")) {
					if (args.length >= 2) {
						if (args[1].equalsIgnoreCase("reload")) {
							Bukkit.getScheduler().cancelTask(SSMaster.getInstance().TaskIDBossBarMessages);
							SSMaster.getInstance().reloadConfig();
							SSMaster.getInstance().BossBarMessages = SSMaster.getInstance().getConfig().getStringList("BossBarMessages");
							SSMaster.getInstance().TaskIDBossBarMessages = Bukkit.getScheduler().scheduleSyncRepeatingTask(SSMaster.getInstance(), new Runnable() {
								private int i = 0;
								
								@Override
								public void run() {
									if (i < SSMaster.getInstance().BossBarMessages.size()) {
										BarAPI.setMessage(ChatColor.translateAlternateColorCodes('&', SSMaster.getInstance().BossBarMessages.get(i)));
										i++;
									} else {
										i = 1;
										BarAPI.setMessage(ChatColor.translateAlternateColorCodes('&', SSMaster.getInstance().BossBarMessages.get(0)));
									}
								}
							}, 0L, 3*20L);
							ChatUtils.sendMessage(cs, "BossBar Messages Neu geladen!");
						} else {
							ChatUtils.sendMessage(cs, "Versuche: /cs help");
						}
					} else {
						ChatUtils.sendMessage(cs, "Es fehlen Argumente!");						
					}
				} else if (args[0].equalsIgnoreCase("add")) {
					if (args.length == 4) {
						String servername = args[1] + MinecraftServerManager.getInstance().nextBungeeCordServername();
						String map = args[2];
						String modi = args[3];
						MinecraftServer mcs = new MinecraftServer(servername, MinecraftServerManager.getInstance().nextPort(), map, modi);
						File template = new File(SSMaster.getInstance().cspath + "templates/" + mcs.getBungeeCordServername().replace("0", "").replace("1", "").replace("2", "").replace("3", "").replace("4", "").replace("5", "").replace("6", "").replace("7", "").replace("8", "").replace("9", "") + "_" + mcs.getMap() + mcs.getModi() + "/");
						if (template.exists()) {
							File serverpath = new File(SSMaster.getInstance().cspath + "server/" + servername + "/");
							if (!serverpath.exists()) {
								ChatUtils.sendMessage(cs, "Dies dauert kurz!");
								MinecraftServerManager.getInstance().addMinecraftServer(mcs);
								MinecraftServerManager.getInstance().copyTemplateToServer(cs, mcs);
								MySQL.addMinecraftServerStatus(mcs);
							} else {
								ChatUtils.sendMessage(cs, "Server existiert bereits!");
							}
						} else {
							ChatUtils.sendMessage(cs, "Kein Template für diesen Server vorhanden!");
						}
					} else {
						ChatUtils.sendMessage(cs, "Es fehlen Argumente!");
					}
				} else if (args[0].equalsIgnoreCase("delete")) {
					if (args.length == 2) {
						File serverpath = new File(SSMaster.getInstance().cspath + "server/" + args[1] + "/");
						if (serverpath.exists()) {
							ChatUtils.sendMessage(cs, "Dies dauert kurz!");
							MinecraftServer mcs = MinecraftServerManager.getInstance().getMinecraftServer(args[1]);
							MinecraftServerManager.getInstance().stopMinecraftServer(mcs);
							Bukkit.getScheduler().scheduleSyncDelayedTask(SSMaster.getInstance(), new Runnable() {
								@Override
								public void run() {
									MinecraftServerManager.getInstance().stopMinecraftServer(mcs);
									MinecraftServerManager.getInstance().removeMinecraftServer(mcs.getBungeeCordServername());
									MinecraftServerManager.getInstance().deleteServerPath(cs, mcs.getBungeeCordServername());
									MySQL.deleteMinecraftServerStatus(mcs.getBungeeCordServername());
								}	
							}, 3*20L);
						} else {
							ChatUtils.sendMessage(cs, "Server existiert nicht!");
						}
					} else {
						ChatUtils.sendMessage(cs, "Es fehlen Argumente!");
					}
				} else if (args[0].equalsIgnoreCase("deleteall")) {
					for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
						File serverpath = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/");
						if (serverpath.exists()) {
							ChatUtils.sendMessage(cs, "Dies dauert kurz!");
							MinecraftServerManager.getInstance().stopMinecraftServer(mcs);
							Bukkit.getScheduler().scheduleSyncDelayedTask(SSMaster.getInstance(), new Runnable() {
								@Override
								public void run() {
									MinecraftServerManager.getInstance().stopMinecraftServer(mcs);
									MinecraftServerManager.getInstance().removeMinecraftServer(mcs.getBungeeCordServername());
									MinecraftServerManager.getInstance().deleteServerPath(cs, mcs.getBungeeCordServername());
									MySQL.deleteMinecraftServerStatus(mcs.getBungeeCordServername());
								}	
							}, 3*20L);
						} else {
							ChatUtils.sendMessage(cs, "Server existiert nicht!");
						}
					}
				} else if (args[0].equalsIgnoreCase("start")) {
					if (args.length == 2) {
						File serverpath = new File(SSMaster.getInstance().cspath + "server/" + args[1] + "/");
						if (serverpath.exists()) {
							MinecraftServer mcs = MinecraftServerManager.getInstance().getMinecraftServer(args[1]);
							if (mcs != null) {
								try {
									Socket socket = new Socket(InetAddress.getLocalHost(), mcs.getPort());
									socket.close();
									ChatUtils.sendMessage(cs, "Der Server ist bereits Online!");
								} catch (IOException ex) {
									MinecraftServerManager.getInstance().startMinecraftServer(mcs);
									ChatUtils.sendMessage(cs, "Server " + args[1] + " startet!");
								}
							}
						} else {
							ChatUtils.sendMessage(cs, "Server existiert nicht!");
						}
					} else {
						ChatUtils.sendMessage(cs, "Du musst einen Servernamen angeben!");
					}
				} else if (args[0].equalsIgnoreCase("startall")) {
					for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
						File serverpath = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/");
						if (serverpath.exists()) {
							if (mcs != null) {
								try {
									Socket socket = new Socket(InetAddress.getLocalHost(), mcs.getPort());
									socket.close();
									ChatUtils.sendMessage(cs, "Der Server ist bereits Online!");
								} catch (IOException ex) {
									MinecraftServerManager.getInstance().startMinecraftServer(mcs);
									ChatUtils.sendMessage(cs, "Server " + mcs.getBungeeCordServername() + " startet!");
								}
							}
						} else {
							ChatUtils.sendMessage(cs, "Server existiert nicht!");
						}
					}
				} else if (args[0].equalsIgnoreCase("stop")) {
					if (args.length == 2) {
						File serverpath = new File(SSMaster.getInstance().cspath + "server/" + args[1] + "/");
						if (serverpath.exists()) {
							MinecraftServer mcs = MinecraftServerManager.getInstance().getMinecraftServer(args[1]);
							if (mcs != null) {
								try {
									Socket socket = new Socket(InetAddress.getLocalHost(), mcs.getPort());
									socket.close();
									MinecraftServerManager.getInstance().stopMinecraftServer(mcs);
						          	ChatUtils.sendMessage(cs, "Server " + args[1] + " stoppt!");
								} catch (IOException ex) {
									ChatUtils.sendMessage(cs, "Der Server ist bereits Offline!");
								}
							}
						} else {
							ChatUtils.sendMessage(cs, "Server existiert nicht!");
						}
					} else {
						ChatUtils.sendMessage(cs, "Du musst einen Servernamen angeben!");
					}
				} else if (args[0].equalsIgnoreCase("stopall")) {
					for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
						File serverpath = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/");
						if (serverpath.exists()) {
							if (mcs != null) {
								try {
									Socket socket = new Socket(InetAddress.getLocalHost(), mcs.getPort());
									socket.close();
									MinecraftServerManager.getInstance().stopMinecraftServer(mcs);
						          	ChatUtils.sendMessage(cs, "Server " + mcs.getBungeeCordServername() + " stoppt!");
								} catch (IOException ex) {
									ChatUtils.sendMessage(cs, "Der Server ist bereits Offline!");
								}
							}
						} else {
							ChatUtils.sendMessage(cs, "Server existiert nicht!");
						}
					}
				} else if (args[0].equalsIgnoreCase("list")) {
					List<String> msg = new ArrayList<String>();
					int i = 1;
					for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
						msg.add(i + " " + mcs.getPort() + " BC: " + mcs.getBungeeCordServername() + " MP: " + mcs.getMap());
						i++;
					}
					i--;
					if (i == 0) {
						ChatUtils.sendMessage(cs, "Es sind keine Server vorhanden!");
					} else {
						ChatUtils.sendMessage(cs, "Folgende Server sind vorhanden:");
						for (String str : msg) {
							cs.sendMessage(str);
						}
						ChatUtils.sendMessage(cs, "Es sind " + i + " Server vorhanden!");
					}
				} else {
					ChatUtils.sendMessage(cs, "Versuche: /cs help");
				}
			} else {
				if (cs instanceof Player) {
					Player p = (Player)cs;
					GUI.openMainMenu(p);
				} else {
					ChatUtils.sendMessage(cs, "Versuche: /cs help");
				}
			}
		} else {
			ChatUtils.sendMessage(cs, "Dafür hast du keine Berechtigung!");
		}
		return true;
	}
}