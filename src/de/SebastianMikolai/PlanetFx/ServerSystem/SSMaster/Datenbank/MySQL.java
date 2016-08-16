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

	public static Connection Connect() {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://" + SSMaster.getInstance().getConfig().getString("database.host") + ":" + 
					SSMaster.getInstance().getConfig().getInt("database.port") + "/" + SSMaster.getInstance().getConfig().getString("database.db") + 
					"?user=" + SSMaster.getInstance().getConfig().getString("database.user") + "&password=" + SSMaster.getInstance().getConfig().getString("database.password"));
			return con;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void Close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {}
	}
	
	public static void LadeTabellen() {
		try {
			Connection con = Connect();
			Statement stmt = con.createStatement();
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
			Close(con);
		} catch (SQLException e) {
			SSMaster.getInstance().getPluginLoader().disablePlugin(SSMaster.getInstance());
		}
	}
	
	public static void addMinecraftServer(MinecraftServer mcs) {
		try {
			Connection con = Connect();
			if (con != null) {
				Statement stmt = con.createStatement();
				stmt.execute("INSERT INTO MinecraftServer (BungeeCordServername, Port, Map, Modi) VALUES ('" + mcs.getBungeeCordServername() + "', '" + mcs.getPort() +  "', '" + mcs.getMap() + "', '" + mcs.getModi() + "')");
			}
			Close(con);
		} catch (SQLException e) {}
	}
	
	public static void deleteMinecraftServer(String BungeeCordServername) {
		try {
			Connection con = Connect();
			if (con != null) {
				Statement stmt = con.createStatement();
				stmt.execute("DELETE FROM MinecraftServer WHERE BungeeCordServername='" + BungeeCordServername + "'");
			}
			Close(con);
		} catch (SQLException e) {}
	}
	
	public static Map<String, MinecraftServer> getMinecraftServers() {
		Map<String, MinecraftServer> MinecraftServers = new HashMap<String, MinecraftServer>();
		try {
			Connection con = Connect();
			if (con != null) {
				Statement stmt = con.createStatement();
				ResultSet rss = stmt.executeQuery("SELECT * FROM MinecraftServer");
				while (rss.next()) {
					MinecraftServers.put(rss.getString("BungeeCordServername"), new MinecraftServer(rss.getString("BungeeCordServername"), rss.getInt("Port"), rss.getString("Map"), rss.getString("Modi")));
				}
			}
			Close(con);
		} catch (SQLException e) {}
		return MinecraftServers;
	}
	
	public static void addMinecraftServerStatus(MinecraftServer mcs) {
		try {
			Connection con = Connect();
			if (con != null) {
				Statement stmt = con.createStatement();
				stmt.execute("INSERT INTO ServerStatus (BungeeCordServername, Online, Status) VALUES ('" + mcs.getBungeeCordServername() + "', '" + mcs.getOnlinePlayer() +  "', '" + mcs.getStatus() + "')");
			}
			Close(con);
		} catch (SQLException e) {}
	}

	public static void deleteMinecraftServerStatus(String BungeeCordServername) {
		try {
			Connection con = Connect();
			if (con != null) {
				Statement stmt = con.createStatement();
				stmt.execute("DELETE FROM ServerStatus WHERE BungeeCordServername='" + BungeeCordServername + "'");
			}
			Close(con);
		} catch (SQLException e) {}
	}
	
	public static JsonObject getMinecraftServerStatus(String BungeeCordServername) {
		JsonObject jsonObject = new JsonObject();
		try {
			Connection con = Connect();
			if (con != null) {
				Statement stmt = con.createStatement();
				ResultSet rss = stmt.executeQuery("SELECT * FROM ServerStatus WHERE BungeeCordServername='" + BungeeCordServername + "'");
				while (rss.next()) {
					jsonObject.addProperty("servername", rss.getString("BungeeCordServername"));
					jsonObject.addProperty("online", rss.getInt("Online"));
					jsonObject.addProperty("status", rss.getString("Status"));
				}
			}
			Close(con);
		} catch (SQLException e) {}
		return jsonObject;
	}
}