package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.SSMaster;

public class ChatUtils {
	
	public static String Minigames_prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Minigames" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD;
	
	public static void sendMessage(Player p, String msg) {
		p.sendMessage(Minigames_prefix + ChatColor.translateAlternateColorCodes('&', msg));
	}
		
	public static void sendMessage(CommandSender cs, String msg) {
		cs.sendMessage(Minigames_prefix + ChatColor.translateAlternateColorCodes('&', msg));
	}

	public static void asendMessageConfig(Player p, String msg) {
		p.sendMessage(Minigames_prefix + ChatColor.translateAlternateColorCodes('&', SSMaster.getInstance().getConfig().getString("Messages." + msg)));
	}
	
	public static void asendMessageConfig(Player p, String msg, String replace, String replace_mitwas) {
		p.sendMessage(Minigames_prefix + ChatColor.translateAlternateColorCodes('&', SSMaster.getInstance().getConfig().getString("Messages." + msg).replace(replace, replace_mitwas)));
	}
}