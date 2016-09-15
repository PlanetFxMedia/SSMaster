package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonObject;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.BarAPI.BarAPI;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Datenbank.MySQL;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerManager;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServerStatus;

public class SSMaster extends JavaPlugin {
	
	private static SSMaster instance;
	public String cspath;
	public static int teleport;
	public List<String> BossBarMessages;
	public int TaskIDBossBarMessages;
	
	public static SSMaster getInstance() {
		return instance;
	}
	
	@Override
	public void onLoad() {
		instance = this;
		saveDefaultConfig();
		teleport = getConfig().getInt("teleport");
		cspath = getConfig().getString("cspath");
		BossBarMessages = getConfig().getStringList("BossBarMessages");
		File cs = new File(cspath);
		if (!cs.exists()) {
			cs.mkdir();
		}
		File files = new File(cspath + "files/");
		if (!files.exists()) {
			files.mkdir();
		}
		File server = new File(cspath + "server/");
		if (!server.exists()) {
			server.mkdir();
		}
		File templates = new File(cspath + "templates/");
		if (!templates.exists()) {
			templates.mkdir();
		}
	}
		
	@Override
	public void onEnable() {
		loadConfig();
		MySQL.LadeTabellen();
		MinecraftServerManager.getInstance().load();
		getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
		getCommand("cs").setExecutor(new CommandListener());
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EventListener(), this);
		for (World w : Bukkit.getWorlds()) {
			w.setTime(6000L);
			w.setDifficulty(Difficulty.PEACEFUL);
			w.setPVP(false);
			w.setAnimalSpawnLimit(0);
			w.setAmbientSpawnLimit(0);
			w.setMonsterSpawnLimit(0);
			w.setWaterAnimalSpawnLimit(0);
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (MinecraftServer mcs : MinecraftServerManager.getInstance().getMinecraftServers().values()) {
					JsonObject jsonObject = MySQL.getMinecraftServerStatus(mcs.getBungeeCordServername());
					if (jsonObject.get("servername").getAsString().equalsIgnoreCase(mcs.getBungeeCordServername())) {
						mcs.setOnlinePlayer(jsonObject.get("online").getAsInt());
						mcs.setStatus(MinecraftServerStatus.valueOf(jsonObject.get("status").getAsString()));
					}
				}
			}
		}, 0L, 20L);
		TaskIDBossBarMessages = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			private int i = 0;
			
			@Override
			public void run() {
				if (i < BossBarMessages.size()) {
					BarAPI.setMessage(ChatColor.translateAlternateColorCodes('&', BossBarMessages.get(i)));
					i++;
				} else {
					i = 1;
					BarAPI.setMessage(ChatColor.translateAlternateColorCodes('&', BossBarMessages.get(0)));
				}
			}
		}, 0L, 3*20L);
	}
	
	@Override
	public void onDisable() {
		BarAPI.disable();
		Bukkit.getScheduler().cancelTasks(getInstance());
	}
	
	public void loadConfig() {
		SSMaster.getInstance().reloadConfig();
		ConfigurationSection cs = getConfig().getConfigurationSection("Minigames");
		List<String> Minigames = new ArrayList<String>();
		Map<String, String> Spielmodis = new HashMap<String, String>();
		Map<String, String> Maps = new HashMap<String, String>();
		for (String gamename : cs.getKeys(false)) {
			Minigames.add(gamename);
			Spielmodis.put(gamename, cs.getString(gamename + ".Spielmodis"));
			Maps.put(gamename, cs.getString(gamename + ".Maps"));
		}
		MinecraftServerManager.getInstance().Minigames = Minigames;
		MinecraftServerManager.getInstance().Spielmodis = Spielmodis;
		MinecraftServerManager.getInstance().Maps = Maps;
	}
}