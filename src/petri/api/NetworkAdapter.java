package petri.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Provides convenience method for connecting to a specified IP address and port,
 * or hosting on the IP on a given port.
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
	 * Connects to a given IP Address and port over the network.
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
	 * Hosts a connection on the given port. To be able to properly connect
	 * outside of the LAN, the end-user much port forward this port to be able
	 * to communicate. Hamachi is a third-party program that can bypass this
	 * restriction, allowing another end-user to connect to the host without
	 * port-forwarding.
	 * 
	 * @param port
	 *            The port to host on
	 * @throws IOException
	 *             If the connection fails. Usually signifies a taken-port or
	 *             lack of host rights.
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

	/**
	 * Returns true if there is data available in the input stream. Used to
	 * prevent constant attempts to read an empty input stream.
	 * 
	 * @return true if data is available in the input stream.
	 */
	public boolean isDataAvailable() {
		return dataAvailable;
	}

	/**
	 * Called after user handles all data available in the input stream. Sets
	 * dataAvailable to false until new data retriggers it to true.
	 */
	public void clearDataAvailable() {
		dataAvailable = false;
	}

	/**
	 * Returns the InputStream. Used to read data sent over the network.
	 * @return input
	 */
	public DataInputStream getInputStream() {
		return input;
	}

	/**
	 * Returns the OutputStream. Used to send data over the network.
	 * @return output
	 */
	public DataOutputStream getOutputStream() {
		return output;
	}
}
