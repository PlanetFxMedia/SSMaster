package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.SSMaster;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.MinecraftServer.MinecraftServer;

public class MySQL {

	public static Connection c = null;
	public static Statement database;
	
	public static void Connect() {
		try {
			c = DriverManager.getConnection("jdbc:mysql://" + SSMaster.getInstance().getConfig().getString("database.host") + ":" + 
					SSMaster.getInstance().getConfig().getInt("database.port") + "/" + SSMaster.getInstance().getConfig().getString("database.db") + 
					"?user=" + SSMaster.getInstance().getConfig().getString("database.user") + "&password=" + SSMaster.getInstance().getConfig().getString("database.password"));
			database = c.createStatement();
			SSMaster.getInstance().getLogger().info("Die Verbindung zur Datenbank wurde hergestellt!");
		} catch (Exception e) {
			SSMaster.getInstance().getPluginLoader().disablePlugin(SSMaster.getInstance());
		}
	}
	
	public static void LadeTabellen() {
		try {
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SHOW TABLES LIKE 'MinecraftServer'");
			if (rss.next()) {
				SSMaster.getInstance().getLogger().info("Die Tabelle MinecraftServer wurde geladen!");
			} else {
				int rs = stmt.executeUpdate("CREATE TABLE MinecraftServer (id INTEGER PRIMARY KEY AUTO_INCREMENT, BungeeCordServername TEXT, Port INTEGER, Map TEXT, Modi TEXT)");
				SSMaster.getInstance().getLogger().info("Die Tabelle MinecraftServer wurde erstellt! (" + rs + ")");
			}
			rss = stmt.executeQuery("SHOW TABLES LIKE 'ServerStatus'");
			if (rss.next()) {
				SSMaster.getInstance().getLogger().info("Die Tabelle ServerStatus wurde geladen!");
			} else {
				int rs = stmt.executeUpdate("CREATE TABLE ServerStatus (id INTEGER PRIMARY KEY AUTO_INCREMENT, BungeeCordServername TEXT, Online INTEGER, Status TEXT)");
				SSMaster.getInstance().getLogger().info("Die Tabelle ServerStatus wurde erstellt! (" + rs + ")");
			}
		} catch (SQLException e) {
			SSMaster.getInstance().getPluginLoader().disablePlugin(SSMaster.getInstance());
		}
	}
	
	public static void addMinecraftServer(MinecraftServer mcs) {
		try {
			Statement stmt = c.createStatement();
			stmt.execute("INSERT INTO MinecraftServer (BungeeCordServername, Port, Map, Modi) VALUES ('" + mcs.getBungeeCordServername() + "', '" + mcs.getPort() +  "', '" + mcs.getMap() + "', '" + mcs.getModi() + "')");
		} catch (SQLException e) {}
	}
	
	public static void deleteMinecraftServer(String BungeeCordServername) {
		try {
			Statement stmt = c.createStatement();
			stmt.execute("DELETE FROM MinecraftServer WHERE BungeeCordServername='" + BungeeCordServername + "'");
		} catch (SQLException e) {}
	}
	
	public static Map<String, MinecraftServer> getMinecraftServers() {
		Map<String, MinecraftServer> MinecraftServers = new HashMap<String, MinecraftServer>();
		try {
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SELECT * FROM MinecraftServer");
			while (rss.next()) {
				MinecraftServers.put(rss.getString("BungeeCordServername"), new MinecraftServer(rss.getString("BungeeCordServername"), rss.getInt("Port"), rss.getString("Map"), rss.getString("Modi")));
			}
		} catch (SQLException e) {}
		return MinecraftServers;
	}
	
	public static void addMinecraftServerStatus(MinecraftServer mcs) {
		try {
			Statement stmt = c.createStatement();
			stmt.execute("INSERT INTO ServerStatus (BungeeCordServername, Online, Status) VALUES ('" + mcs.getBungeeCordServername() + "', '" + mcs.getOnlinePlayer() +  "', '" + mcs.getStatus() + "')");
		} catch (SQLException e) {}
	}

	public static void deleteMinecraftServerStatus(String BungeeCordServername) {
		try {
			Statement stmt = c.createStatement();
			stmt.execute("DELETE FROM ServerStatus WHERE BungeeCordServername='" + BungeeCordServername + "'");
		} catch (SQLException e) {}
	}
	
	public static JsonObject getMinecraftServerStatus(String BungeeCordServername) {
		JsonObject jsonObject = new JsonObject();
		try {
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SELECT * FROM ServerStatus WHERE BungeeCordServername='" + BungeeCordServername + "'");
			while (rss.next()) {
				jsonObject.addProperty("servername", rss.getString("BungeeCordServername"));
				jsonObject.addProperty("online", rss.getInt("Online"));
				jsonObject.addProperty("status", rss.getString("Status"));
			}
		} catch (SQLException e) {}
		return jsonObject;
	}
}