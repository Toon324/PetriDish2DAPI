package petri.api;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import xf.Actors;
import xf.ConnectionListener;

/**
 * 
 * 
 * @author Cody Swendrowski
 */
public class NetworkAdapter {

	protected boolean dataAvailable;
	protected DataInputStream input;
	protected DataOutputStream output;
	
	public NetworkAdapter() {
		dataAvailable = false;
	}

	public void dataAvailable() {
		dataAvailable = true;
	}
	
	public void connect(String IPAddress, int port) throws IOException {
		GameEngine.log("Connecting to " + IPAddress + ":" + port);
		Socket connection = new Socket(IPAddress, port);
		input = new DataInputStream(connection.getInputStream());
		ConnectionListener cL = new ConnectionListener(this, input);
		output = new DataOutputStream(connection.getOutputStream());
		cL.start();
		GameEngine.log("Successfully connected to host.");
	}
	
	public void host(int port) throws IOException {
		ServerSocket socket = new ServerSocket(port);
		socket.setSoTimeout(100000);
		GameEngine.log("Hosting from " + InetAddress.getLocalHost() + ":" + port);
		Socket connection = socket.accept();
		input = new DataInputStream(connection.getInputStream());
		ConnectionListener cL = new ConnectionListener(this, input);
		output = new DataOutputStream(connection.getOutputStream());
		cL.start();
		GameEngine.log("Successfully connected to client");
	}
	
	public void send() {
		
	}
}
