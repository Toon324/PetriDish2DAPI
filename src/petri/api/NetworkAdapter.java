package petri.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Provides convience methods for connecting to a specified IP address and port,
 * or hosting on the local machine IP on a given port.
 * 
 * Provides access to input and output stream for receiving and sending data
 * over the network.
 * 
 * A ConnectionListener is created to determine if any data is available on the
 * input stream. This is to prevent a system resource lag in attempting to
 * constantly read from the stream.
 * 
 * @author Cody Swendrowski
 */
public class NetworkAdapter {

	protected boolean dataAvailable;
	protected DataInputStream input;
	protected DataOutputStream output;

	/**
	 * Creates a new NetworkAdapter with no data available.
	 */
	public NetworkAdapter() {
		dataAvailable = false;
	}

	/**
	 * Called from ConnectionListener. Signals that there is data available on
	 * the input stream.
	 */
	public void dataAvailable() {
		dataAvailable = true;
	}

	/**
	 * Connects to a given IP Address and port over the local network.
	 * 
	 * @param IPAddress
	 *            The IP Address to connect to
	 * @param port
	 *            The port on the IP Address to connect to
	 * @throws IOException
	 *             If the connection fails. Usually signifies an incorrect
	 *             IPAddress and port configuration.
	 */
	public void connect(String IPAddress, int port) throws IOException {
		GameEngine.log("Connecting to " + IPAddress + ":" + port);
		Socket connection = new Socket(IPAddress, port);
		input = new DataInputStream(connection.getInputStream());
		ConnectionListener cL = new ConnectionListener(this, input);
		output = new DataOutputStream(connection.getOutputStream());
		cL.start();
		GameEngine.log("Successfully connected to host.");
	}

	/**
	 * 
	 * @param port
	 * @throws IOException
	 */
	public void host(int port) throws IOException {
		ServerSocket socket = new ServerSocket(port);
		socket.setSoTimeout(100000);
		GameEngine.log("Hosting from " + InetAddress.getLocalHost() + ":"
				+ port);
		Socket connection = socket.accept();
		input = new DataInputStream(connection.getInputStream());
		ConnectionListener cL = new ConnectionListener(this, input);
		output = new DataOutputStream(connection.getOutputStream());
		cL.start();
		GameEngine.log("Successfully connected to client");
	}

	public boolean isDataAvailable() {
		return dataAvailable;
	}

	public void clearDataAvailable() {
		dataAvailable = false;
	}

	public DataInputStream getInputStream() {
		return input;
	}

	public DataOutputStream getOutputStream() {
		return output;
	}
}
