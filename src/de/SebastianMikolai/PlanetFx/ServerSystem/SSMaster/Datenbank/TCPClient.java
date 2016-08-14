package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Datenbank;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.google.gson.JsonObject;

public class TCPClient {
	
	public static void send(JsonObject jsonObject, int port) {
		try {
			Socket socket = new Socket("localhost", port);
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
			outToServer.writeBytes(jsonObject.toString() + '\n');
			socket.close();
		} catch (IOException e) {}
	}
}