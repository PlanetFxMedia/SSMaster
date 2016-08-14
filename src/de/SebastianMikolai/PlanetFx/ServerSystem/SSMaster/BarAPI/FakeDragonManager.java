package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class FakeDragonManager extends FakeDragon {
	
	private BossBar bar;
	
	public FakeDragonManager(String name, Location loc) {
		super(name, loc);   
		this.bar = Bukkit.createBossBar(name, BarColor.PINK, BarStyle.SOLID, new BarFlag[0]);
	}
	
	public BossBar getBar() {
		return this.bar;
	}
	
	public Object getSpawnPacket() {
		return null;
	}
	
	public Object getDestroyPacket() {
		return null;
	}
	
	public Object getMetaPacket(Object watcher) {
		return null;
	}
	
	public Object getTeleportPacket(Location loc) {
		return null;
	}
	
	public Object getWatcher() {
		return null;
	}
}