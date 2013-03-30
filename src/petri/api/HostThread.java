package petri.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Cody Swendrowski
 */
public final class HostThread extends Thread {

	private DataInputStream input;
	private DataOutputStream output;
	private NetworkAdapter adapter;
	private ServerSocket socket;

	/**
	 * @param socket
	 * 
	 */
	public HostThread(NetworkAdapter net, ServerSocket sock) {
		adapter = net;
		socket = sock;
	}

	@Override
	public void run() {
		try {
			Socket connection = socket.accept();
			input = new DataInputStream(connection.getInputStream());
			output = new DataOutputStream(connection.getOutputStream());
			adapter.connectionAvailable();
		} catch (IOException e) {
			GameEngine.log(e.getMessage());
		}
	}

	public DataInputStream getInputStream() {
		return input;
	}
	
	public DataOutputStream getOutputStream() {
		return output;
	}

}
