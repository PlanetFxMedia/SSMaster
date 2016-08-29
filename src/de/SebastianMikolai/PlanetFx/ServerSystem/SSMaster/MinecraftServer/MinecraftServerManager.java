package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.SSMaster;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Datenbank.MySQL;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.ChatUtils;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils.CopyDirectory;

public class MinecraftServerManager {

	private static MinecraftServerManager instance;
	private Map<String, MinecraftServer> MinecraftServers;
	
	public static MinecraftServerManager getInstance() {
		if (instance == null) {
			instance = new MinecraftServerManager();
		}
		return instance;
	}
	
	public Map<String, MinecraftServer> getMinecraftServers() {
		return MinecraftServers;
	}
	
	public void load() {
		MinecraftServers = new HashMap<String, MinecraftServer>();
		MinecraftServers = MySQL.getMinecraftServers();
	}
	
	public void addMinecraftServer(MinecraftServer mcs) {
		if (MinecraftServers.containsKey(mcs.getBungeeCordServername())) {
			removeMinecraftServer(mcs.getBungeeCordServername());
		}
		MySQL.addMinecraftServer(mcs);
		MinecraftServers.put(mcs.getBungeeCordServername(), mcs);
	}

	public void removeMinecraftServer(String BungeeCordServername) {
		if (MinecraftServers.containsKey(BungeeCordServername)) {
			MySQL.deleteMinecraftServer(BungeeCordServername);
			MinecraftServers.remove(BungeeCordServername);
		}
	}
	
	public MinecraftServer getMinecraftServer(String BungeeCordServername) {
		return MinecraftServers.get(BungeeCordServername);
	}
	
	public MinecraftServer getFreeOnlineMinecraftServer(String gamename, String Map, String Modi) {
		for (MinecraftServer srv : MinecraftServers.values()) {
			if (srv.getBungeeCordServername().contains(gamename) && srv.getMap().equalsIgnoreCase(Map) && srv.getModi().equalsIgnoreCase(Modi) && srv.getStatus() == MinecraftServerStatus.Online) {
				return srv;
			}
		}
		return null;
	}
	
	public MinecraftServer getFreeOfflineMinecraftServer(String gamename, String Map, String Modi) {
		for (MinecraftServer srv : MinecraftServers.values()) {
			if (srv.getBungeeCordServername().contains(gamename) && srv.getMap().equalsIgnoreCase(Map) && srv.getModi().equalsIgnoreCase(Modi) && srv.getStatus() == MinecraftServerStatus.Offline) {
				return srv;
			}
		}
		return null;
	}
	
	public void stopMinecraftServer(String BungeeCordServername) {
		if (MinecraftServers.containsKey(BungeeCordServername)) {
			MinecraftServer mcs = getMinecraftServer(BungeeCordServername);
			if (mcs != null) {		        
				List<String> pid = new ArrayList<String>();
		        try {
					String[] cmdexec = {"/bin/sh", "-c", "ps -ef | grep " + mcs.getBungeeCordServername() + " | grep -v grep | awk '{print $2}'"};
					Runtime rt = Runtime.getRuntime();
					Process proc = rt.exec(cmdexec);
			        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			        String s = null;
			        while ((s = stdInput.readLine()) != null) {
			        	pid.add(s);
			        }
			        while ((s = stdError.readLine()) != null) {
			        	System.out.println(s);
			        }
				} catch (IOException e) {
					e.printStackTrace();
				}
		        if (!pid.isEmpty()) {
		        	for (String processID : pid) {
		        		try {
							String[] cmdexec = {"/bin/sh", "-c", "kill " + processID};
							Runtime.getRuntime().exec(cmdexec);
						} catch (IOException e) {
							e.printStackTrace();
						}
		        	}
		        }
			}
		}
	}
	
	public void startMinecraftServer(String BungeeCordServername) {
		if (MinecraftServers.containsKey(BungeeCordServername)) {
			MinecraftServer mcs = getMinecraftServer(BungeeCordServername);
			if (mcs != null) {
				String cmdexec = "screen -dmS " + mcs.getBungeeCordServername() + " taskset -c 1-8 " + SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/./start.sh";
		        try {
		        	File dir = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/");
		        	Runtime.getRuntime().exec(cmdexec, null, dir);
		        } catch (IOException ioe) {
		        	ioe.printStackTrace();
		       	}
			}
		}
	}
	
	public void chmodServer(String BungeeCordServername) {
		if (MinecraftServers.containsKey(BungeeCordServername)) {
			MinecraftServer mcs = getMinecraftServer(BungeeCordServername);
			if (mcs != null) {
				String cmdexec = "chmod +x " + SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/./start.sh";
		        try {
		        	File dir = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/");
		        	Runtime.getRuntime().exec(cmdexec, null, dir);
		        } catch (IOException ioe) {
		        	ioe.printStackTrace();
		       	}
			}
		}
	}
	
	public void deleteServerPath(CommandSender p, String BungeeCordServername) {
		String cmdexec = "rm -r " + SSMaster.getInstance().cspath + "server/" + BungeeCordServername + "/";
		try {
			File dir = new File(SSMaster.getInstance().cspath + "server/" + BungeeCordServername + "/");
		    Runtime.getRuntime().exec(cmdexec, null, dir);
		} catch (IOException ioe) {}
		Bukkit.getScheduler().scheduleSyncDelayedTask(SSMaster.getInstance(), new Runnable() {
			@Override
			public void run() {
				String cmdexec = "rm -r " + SSMaster.getInstance().cspath + "server/" + BungeeCordServername + "/";
				try {
				File dir = new File(SSMaster.getInstance().cspath + "server/" + BungeeCordServername + "/");
					Runtime.getRuntime().exec(cmdexec, null, dir);
				} catch (IOException ioe) {}
				ChatUtils.sendMessage(p, "Server " + BungeeCordServername + " gelöscht!");
			}
		}, 10*20L);
	}
	
	public void copyTemplateToServer(CommandSender p, MinecraftServer mcs) {
		try {
			File quelle = new File(SSMaster.getInstance().cspath + "templates/" + mcs.getBungeeCordServername().replace("0", "").replace("1", "").replace("2", "").replace("3", "").replace("4", "").replace("5", "").replace("6", "").replace("7", "").replace("8", "").replace("9", "")  + "_" + mcs.getMap() + mcs.getModi() + "/");
			File ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/");
			CopyDirectory cd = new CopyDirectory();
			cd.copyDir(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/spigot.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/spigot.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/SSClient.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/SSClient.jar");
			cd.copyFile(quelle, ziel);
			if (mcs.getBungeeCordServername().toLowerCase().contains("bedwars")) {
				quelle = new File(SSMaster.getInstance().cspath + "files/BedwarsRel.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/BedwarsRel.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/CleanroomGenerator.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/CleanroomGenerator.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/HolographicDisplays.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/HolographicDisplays.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/Multiverse-Core.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/Multiverse-Core.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/AAC.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/AAC.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/ProtocolLib.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/ProtocolLib.jar");
				cd.copyFile(quelle, ziel);
				FileWriter writer = new FileWriter(new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/server.properties") ,true);
				writer.write("level-name=lobby");
				writer.write(System.getProperty("line.separator"));
				writer.write("level-type=FLAT");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-protection=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("force-gamemode=true");
				writer.write(System.getProperty("line.separator"));
				writer.write("allow-nether=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("gamemode=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("difficulty=1");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-monsters=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-animals=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-npcs=true");
				writer.write(System.getProperty("line.separator"));
				writer.write("pvp=true");
				writer.write(System.getProperty("line.separator"));
				int maxplayer = mcs.getMaxPlayer();
				maxplayer++;
				writer.write("max-players=" + maxplayer);
				writer.write(System.getProperty("line.separator"));
				writer.write("server-port=" + mcs.getPort());
				writer.write(System.getProperty("line.separator"));
				writer.write("online-mode=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("motd=mc_mg_" + mcs.getBungeeCordServername());
				writer.write(System.getProperty("line.separator"));
				writer.write("server-name=" + mcs.getBungeeCordServername());
				writer.flush();
				writer.close();
				writer = new FileWriter(new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/start.sh") ,true);
				writer.write("java -Xmx800M -jar " + SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/spigot.jar nogui");
				writer.flush();
				writer.close();
			} else if (mcs.getBungeeCordServername().toLowerCase().contains("spleef")) {
				quelle = new File(SSMaster.getInstance().cspath + "files/Multiverse-Core.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/Multiverse-Core.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/PlanetFxSpleef.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/PlanetFxSpleef.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/CleanroomGenerator.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/CleanroomGenerator.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/AAC.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/AAC.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/ProtocolLib.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/ProtocolLib.jar");
				cd.copyFile(quelle, ziel);
				FileWriter writer = new FileWriter(new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/server.properties") ,true);
				writer.write("level-name=lobby");
				writer.write(System.getProperty("line.separator"));
				writer.write("level-type=FLAT");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-protection=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("force-gamemode=true");
				writer.write(System.getProperty("line.separator"));
				writer.write("allow-nether=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("gamemode=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("difficulty=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-monsters=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-animals=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-npcs=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("pvp=false");
				writer.write(System.getProperty("line.separator"));
				int maxplayer = mcs.getMaxPlayer();
				maxplayer++;
				writer.write("max-players=" + maxplayer);
				writer.write(System.getProperty("line.separator"));
				writer.write("server-port=" + mcs.getPort());
				writer.write(System.getProperty("line.separator"));
				writer.write("online-mode=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("motd=mc_mg_" + mcs.getBungeeCordServername());
				writer.write(System.getProperty("line.separator"));
				writer.write("server-name=" + mcs.getBungeeCordServername());
				writer.flush();
				writer.close();
				writer = new FileWriter(new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/start.sh") ,true);
				writer.write("java -Xmx800M -jar " + SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/spigot.jar nogui");
				writer.flush();
				writer.close();
			} else if (mcs.getBungeeCordServername().toLowerCase().contains("hungergames")) {
				quelle = new File(SSMaster.getInstance().cspath + "files/BukkitGames.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/BukkitGames.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/SQLibrary.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/SQLibrary.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/CleanroomGenerator.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/CleanroomGenerator.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/AAC.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/AAC.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/ProtocolLib.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/ProtocolLib.jar");
				cd.copyFile(quelle, ziel);
				FileWriter writer = new FileWriter(new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/server.properties") ,true);
				writer.write("level-name=world");
				writer.write(System.getProperty("line.separator"));
				writer.write("level-type=DEFAULT");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-protection=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("force-gamemode=true");
				writer.write(System.getProperty("line.separator"));
				writer.write("allow-nether=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("gamemode=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("difficulty=2");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-monsters=true");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-animals=true");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-npcs=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("pvp=true");
				writer.write(System.getProperty("line.separator"));
				int maxplayer = mcs.getMaxPlayer();
				maxplayer++;
				writer.write("max-players=" + maxplayer);
				writer.write(System.getProperty("line.separator"));
				writer.write("server-port=" + mcs.getPort());
				writer.write(System.getProperty("line.separator"));
				writer.write("online-mode=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("motd=mc_mg_" + mcs.getBungeeCordServername());
				writer.write(System.getProperty("line.separator"));
				writer.write("server-name=" + mcs.getBungeeCordServername());
				writer.flush();
				writer.close();
				writer = new FileWriter(new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/start.sh") ,true);
				writer.write("while true");
				writer.write(System.getProperty("line.separator"));
				writer.write("do");
				writer.write(System.getProperty("line.separator"));
				writer.write("java -Xmx800M -jar " + SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/spigot.jar nogui");
				writer.write(System.getProperty("line.separator"));
				writer.write("sleep 5");
				writer.write(System.getProperty("line.separator"));
				writer.write("done");
				writer.flush();
				writer.close();
			} else if (mcs.getBungeeCordServername().toLowerCase().contains("turfwars")) {
				quelle = new File(SSMaster.getInstance().cspath + "files/Multiverse-Core.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/Multiverse-Core.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/TurfWars.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/TurfWars.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/CleanroomGenerator.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/CleanroomGenerator.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/AAC.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/AAC.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/ProtocolLib.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/ProtocolLib.jar");
				cd.copyFile(quelle, ziel);
				FileWriter writer = new FileWriter(new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/server.properties") ,true);
				writer.write("level-name=world");
				writer.write(System.getProperty("line.separator"));
				writer.write("level-type=FLAT");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-protection=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("force-gamemode=true");
				writer.write(System.getProperty("line.separator"));
				writer.write("allow-nether=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("gamemode=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("difficulty=1");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-monsters=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-animals=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-npcs=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("pvp=true");
				writer.write(System.getProperty("line.separator"));
				int maxplayer = mcs.getMaxPlayer();
				maxplayer++;
				writer.write("max-players=" + maxplayer);
				writer.write(System.getProperty("line.separator"));
				writer.write("server-port=" + mcs.getPort());
				writer.write(System.getProperty("line.separator"));
				writer.write("online-mode=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("motd=mc_mg_" + mcs.getBungeeCordServername());
				writer.write(System.getProperty("line.separator"));
				writer.write("server-name=" + mcs.getBungeeCordServername());
				writer.flush();
				writer.close();
				writer = new FileWriter(new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/start.sh") ,true);
				writer.write("while true");
				writer.write(System.getProperty("line.separator"));
				writer.write("do");
				writer.write(System.getProperty("line.separator"));
				writer.write("java -Xmx800M -jar " + SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/spigot.jar nogui");
				writer.write(System.getProperty("line.separator"));
				writer.write("sleep 5");
				writer.write(System.getProperty("line.separator"));
				writer.write("done");
				writer.flush();
				writer.close();
			} else if (mcs.getBungeeCordServername().toLowerCase().contains("icehockey")) {
				quelle = new File(SSMaster.getInstance().cspath + "files/Multiverse-Core.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/Multiverse-Core.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/PlanetFxIceHockey.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/PlanetFxIceHockey.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/CleanroomGenerator.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/CleanroomGenerator.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/AAC.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/AAC.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/ProtocolLib.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/ProtocolLib.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/WorldEdit.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/WorldEdit.jar");
				cd.copyFile(quelle, ziel);
				quelle = new File(SSMaster.getInstance().cspath + "files/WorldGuard.jar");
				ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/WorldGuard.jar");
				cd.copyFile(quelle, ziel);
				FileWriter writer = new FileWriter(new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/server.properties") ,true);
				writer.write("level-name=world");
				writer.write(System.getProperty("line.separator"));
				writer.write("level-type=FLAT");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-protection=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("force-gamemode=true");
				writer.write(System.getProperty("line.separator"));
				writer.write("allow-nether=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("gamemode=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("difficulty=0");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-monsters=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-animals=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("spawn-npcs=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("pvp=true");
				writer.write(System.getProperty("line.separator"));
				int maxplayer = mcs.getMaxPlayer();
				maxplayer++;
				writer.write("max-players=" + maxplayer);
				writer.write(System.getProperty("line.separator"));
				writer.write("server-port=" + mcs.getPort());
				writer.write(System.getProperty("line.separator"));
				writer.write("online-mode=false");
				writer.write(System.getProperty("line.separator"));
				writer.write("motd=mc_mg_" + mcs.getBungeeCordServername());
				writer.write(System.getProperty("line.separator"));
				writer.write("server-name=" + mcs.getBungeeCordServername());
				writer.flush();
				writer.close();
				writer = new FileWriter(new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/start.sh") ,true);
				writer.write("java -Xmx800M -jar " + SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/spigot.jar nogui");
				writer.flush();
				writer.close();
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(SSMaster.getInstance(), new Runnable() {
				@Override
				public void run() {
					chmodServer(mcs.getBungeeCordServername());
					if (p != null) {
						ChatUtils.sendMessage(p, "Server " + mcs.getBungeeCordServername() + " erstellt!");
					}
					MinecraftServerManager.getInstance().startMinecraftServer(mcs.getBungeeCordServername());
				}
				
			}, 5*20L);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateTemplateServer(CommandSender p, MinecraftServer mcs) {
		File quelle = new File(SSMaster.getInstance().cspath + "templates/" + mcs.getBungeeCordServername().replace("0", "").replace("1", "").replace("2", "").replace("3", "").replace("4", "").replace("5", "").replace("6", "").replace("7", "").replace("8", "").replace("9", "")  + "_" + mcs.getMap() + mcs.getModi() + "/");
		File ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/");
		CopyDirectory cd = new CopyDirectory();
		quelle = new File(SSMaster.getInstance().cspath + "files/spigot.jar");
		ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/spigot.jar");
		cd.copyFile(quelle, ziel);
		quelle = new File(SSMaster.getInstance().cspath + "files/SSClient.jar");
		ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/SSClient.jar");
		cd.copyFile(quelle, ziel);
		if (mcs.getBungeeCordServername().toLowerCase().contains("bedwars")) {
			quelle = new File(SSMaster.getInstance().cspath + "files/BedwarsRel.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/BedwarsRel.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/CleanroomGenerator.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/CleanroomGenerator.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/HolographicDisplays.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/HolographicDisplays.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/Multiverse-Core.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/Multiverse-Core.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/AAC.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/AAC.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/ProtocolLib.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/ProtocolLib.jar");
			cd.copyFile(quelle, ziel);
		} else if (mcs.getBungeeCordServername().toLowerCase().contains("spleef")) {
			quelle = new File(SSMaster.getInstance().cspath + "files/Multiverse-Core.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/Multiverse-Core.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/PlanetFxSpleef.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/PlanetFxSpleef.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/CleanroomGenerator.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/CleanroomGenerator.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/AAC.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/AAC.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/ProtocolLib.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/ProtocolLib.jar");
			cd.copyFile(quelle, ziel);
		} else if (mcs.getBungeeCordServername().toLowerCase().contains("hungergames")) {
			quelle = new File(SSMaster.getInstance().cspath + "files/BukkitGames.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/BukkitGames.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/SQLibrary.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/SQLibrary.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/CleanroomGenerator.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/CleanroomGenerator.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/AAC.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/AAC.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/ProtocolLib.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/ProtocolLib.jar");
			cd.copyFile(quelle, ziel);
		} else if (mcs.getBungeeCordServername().toLowerCase().contains("turfwars")) {
			quelle = new File(SSMaster.getInstance().cspath + "files/Multiverse-Core.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/Multiverse-Core.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/TurfWars.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/TurfWars.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/CleanroomGenerator.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/CleanroomGenerator.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/AAC.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/AAC.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/ProtocolLib.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/ProtocolLib.jar");
			cd.copyFile(quelle, ziel);
		} else if (mcs.getBungeeCordServername().toLowerCase().contains("icehockey")) {
			quelle = new File(SSMaster.getInstance().cspath + "files/Multiverse-Core.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/Multiverse-Core.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/PlanetFxIceHockey.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/PlanetFxIceHockey.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/CleanroomGenerator.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/CleanroomGenerator.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/AAC.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/AAC.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/ProtocolLib.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/ProtocolLib.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/WorldEdit.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/WorldEdit.jar");
			cd.copyFile(quelle, ziel);
			quelle = new File(SSMaster.getInstance().cspath + "files/WorldGuard.jar");
			ziel = new File(SSMaster.getInstance().cspath + "server/" + mcs.getBungeeCordServername() + "/plugins/WorldGuard.jar");
			cd.copyFile(quelle, ziel);
		}
		if (p != null) {
			ChatUtils.sendMessage(p, "Server " + mcs.getBungeeCordServername() + " geupdatet!");
		}
	}
	
	public int nextPort() {
		String ports = null;
		for (MinecraftServer mcs : MinecraftServers.values()) {
			if (ports == null) {
				ports = String.valueOf(mcs.getPort());
			} else {
				ports = ports + ":" + String.valueOf(mcs.getPort());
			}
		}
		String[] numberStrs = ports.split(":");
		int[] zahlen = new int[numberStrs.length];
		for (int i = 0;i < numberStrs.length;i++) {
			zahlen[i] = Integer.parseInt(numberStrs[i]);
		}
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < zahlen.length; i++) {
			if (zahlen[i] < min) min = zahlen[i];
			if (zahlen[i] > max) max = zahlen[i];
		}
		max = max + 5;
		return max;
	}

	public int nextBungeeCordServername() {
		return MinecraftServers.size() + 1;
	}
	
	public static void sendToServer(Player p, String BungeeCordServername) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(BungeeCordServername);
		} catch (IOException e) {}
		p.sendPluginMessage(SSMaster.getInstance(), "BungeeCord", b.toByteArray());
	}
}