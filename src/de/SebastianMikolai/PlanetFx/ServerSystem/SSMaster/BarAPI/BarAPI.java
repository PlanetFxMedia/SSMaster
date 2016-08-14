package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.BarAPI;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.SSMaster;

public class BarAPI {
	
	private static HashMap<UUID, FakeDragon> players = new HashMap<UUID, FakeDragon>();
	private static HashMap<UUID, Integer> timers = new HashMap<UUID, Integer>();
	
	public static void setMessage(String message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setMessage(player, message);
		}
	}
	
	public static void setMessage(Player player, String message) {
		if (hasBar(player)) {
			removeBar(player);
		}
		FakeDragon dragon = getDragon(player, message);
		dragon.name = cleanMessage(message);
		dragon.health = dragon.getMaxHealth();
		cancelTimer(player);
		sendDragon(dragon, player);
	}
	
	public static void setMessage(String message, float percent) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setMessage(player, message, percent);
		}
	}
	
	public static void setMessage(Player player, String message, float percent) {
		Validate.isTrue((0.0F <= percent) && (percent <= 100.0F), "Percent must be between 0F and 100F, but was: ", percent);
		if (hasBar(player)) {
			removeBar(player);
		}
		FakeDragon dragon = getDragon(player, message); 
		dragon.name = cleanMessage(message);
		dragon.health = (percent / 100.0F * dragon.getMaxHealth()); 
		cancelTimer(player); 
		sendDragon(dragon, player);
	}
	
	public static void setMessage(String message, int seconds) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setMessage(player, message, seconds);
		}
	}
	
	public static void setMessage(Player player, String message, int seconds) {
		Validate.isTrue(seconds > 0, "Seconds must be above 1 but was: ", seconds);
		if (hasBar(player)) {
			removeBar(player);
		}
		FakeDragon dragon = getDragon(player, message);
		dragon.name = cleanMessage(message);
		dragon.health = dragon.getMaxHealth();
		final float dragonHealthMinus = dragon.getMaxHealth() / seconds; 
		cancelTimer(player); 
		timers.put(player.getUniqueId(), Integer.valueOf(Bukkit.getScheduler().runTaskTimer(SSMaster.getInstance(), new Runnable() {
			public void run() {
				FakeDragon drag = BarAPI.getDragon(player, "");
				drag.health -= dragonHealthMinus;
				if (drag.health <= 1.0F) {
					BarAPI.removeBar(player);
					BarAPI.cancelTimer(player);
				} else {
					BarAPI.sendDragon(drag, player);
				}
			}
		}, 20L, 20L).getTaskId())); 
		sendDragon(dragon, player);
	}
	
  	public static boolean hasBar(Player player) {
  		return players.get(player.getUniqueId()) != null;
  	}
  
  	public static void removeBar(Player player){
  		if (!hasBar(player)) {
  			return;
  		}
  		FakeDragon dragon = getDragon(player, "");
  		if ((dragon instanceof FakeDragonManager)) {
  			((FakeDragonManager)dragon).getBar().removePlayer(player);
  		} else {
  			Util.sendPacket(player, getDragon(player, "").getDestroyPacket());
  		}
  		players.remove(player.getUniqueId());
  		cancelTimer(player);
  	}
  	
  	public static void setHealth(Player player, float percent) {
  		if (!hasBar(player)) {
  			return;
  		}
  		FakeDragon dragon = getDragon(player, "");
  		dragon.health = (percent / 100.0F * dragon.getMaxHealth());
  		cancelTimer(player);
  		if (percent == 0.0F) {
  			removeBar(player);
  		} else {
  			sendDragon(dragon, player);
  		}
  	}
  	
  	public static float getHealth(Player player) {
  		if (!hasBar(player)) {
  			return -1.0F;
  		}
  		return getDragon(player, "").health;
  	}
  	
  	public static String getMessage(Player player) {
  		if (!hasBar(player)) {
  			return "";
  		}
  		return getDragon(player, "").name;
  	}
  	
  	private static String cleanMessage(String message) {
  		if (message.length() > 64) {
  			message = message.substring(0, 63);
  		}
  		return message;
  	}
  	
  	private static void cancelTimer(Player player) {
  		Integer timerID = (Integer)timers.remove(player.getUniqueId());
  		if (timerID != null) {
  			Bukkit.getScheduler().cancelTask(timerID.intValue());
  		}
  	}
  	
  	private static void sendDragon(FakeDragon dragon, Player player) {
  		if ((dragon instanceof FakeDragonManager)) {
  			BossBar bar = ((FakeDragonManager)dragon).getBar();
  			bar.addPlayer(player);
  			bar.setProgress(dragon.health / dragon.getMaxHealth());
  		} else {
  			Util.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
  			Util.sendPacket(player, dragon.getTeleportPacket(getDragonLocation(player.getLocation())));
  		}
  	}
  	
  	private static FakeDragon getDragon(Player player, String message) {
  		if (hasBar(player)) {
  			return (FakeDragon)players.get(player.getUniqueId());
  		}
  		return addDragon(player, cleanMessage(message));
  	}
  	
  	private static FakeDragon addDragon(Player player, String message) {
  		FakeDragon dragon = Util.newDragon(message, getDragonLocation(player.getLocation()));
  		if ((dragon instanceof FakeDragonManager)) {
  			BossBar bar = ((FakeDragonManager)dragon).getBar();
  			bar.addPlayer(player);
  		} else {
  			Util.sendPacket(player, dragon.getSpawnPacket());
  		}
  		players.put(player.getUniqueId(), dragon); 
  		return dragon;
  	}
  	
  	private static FakeDragon addDragon(Player player, Location loc, String message) {
  		FakeDragon dragon = Util.newDragon(message, getDragonLocation(loc));
  		if ((dragon instanceof FakeDragonManager)) {
  			BossBar bar = ((FakeDragonManager)dragon).getBar();  
  			bar.addPlayer(player);
  		} else {
  			Util.sendPacket(player, dragon.getSpawnPacket());
  		}
  		players.put(player.getUniqueId(), dragon); 
  		return dragon;
  	}
  	
  	private static Location getDragonLocation(Location loc) {
  		if (Util.isBelowGround) {
  			loc.subtract(0.0D, 300.0D, 0.0D);
  			return loc;
  		}
  		float pitch = loc.getPitch();
  		if (pitch >= 55.0F) {
  			loc.add(0.0D, -300.0D, 0.0D);
  		} else if (pitch <= -55.0F) {
  			loc.add(0.0D, 300.0D, 0.0D);
  		} else {
  			loc = loc.getBlock().getRelative(getDirection(loc), Bukkit.getServer().getViewDistance() * 16).getLocation();
  		}
  		return loc;
  	}
  	
  	private static BlockFace getDirection(Location loc) {
  		float dir = Math.round(loc.getYaw() / 90.0F);
  		if ((dir == -4.0F) || (dir == 0.0F) || (dir == 4.0F)) {
  			return BlockFace.SOUTH;
  		}
  		if ((dir == -1.0F) || (dir == 3.0F)) {
  			return BlockFace.EAST;
  		}
  		if ((dir == -2.0F) || (dir == 2.0F)) {
  			return BlockFace.NORTH;
  		}
  		if ((dir == -3.0F) || (dir == 1.0F)) {
 		  return BlockFace.WEST;
  		}
  		return null;
  	}
  	
  	public static void disable() {
  		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
  			removeBar(player);
  		}
  		players.clear();
  		for (Iterator<Integer> i$ = timers.values().iterator(); i$.hasNext();) {
  			int timerID = ((Integer)i$.next()).intValue();
  			Bukkit.getScheduler().cancelTask(timerID);
  		}
  		timers.clear();
  	}

  	public static void handleTeleport(final Player player, final Location loc) {
  		if (!hasBar(player)) {
  			return;
  		}
  		final FakeDragon oldDragon = getDragon(player, "");
  		if ((oldDragon instanceof FakeDragonManager)) {
  			return;
  		}
  		Bukkit.getScheduler().runTaskLater(SSMaster.getInstance(), new Runnable() {
  			public void run() {
  				if (!BarAPI.hasBar(player)) {
  					return;
  				}
  				float health = oldDragon.health;
  				String message = oldDragon.name; 
  				Util.sendPacket(player, BarAPI.getDragon(player, "").getDestroyPacket()); 
  				BarAPI.players.remove(player.getUniqueId());
  				FakeDragon dragon = BarAPI.addDragon(player, loc, message);
  				dragon.health = health;
  				BarAPI.sendDragon(dragon, player);
  			}
  		}, 2L);
  	}
}