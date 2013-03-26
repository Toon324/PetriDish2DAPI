package petri.api;

import java.io.DataInputStream;
import java.io.IOException;

public class ConnectionListener extends Thread {

	private NetworkAdapter adapter;
	private DataInputStream input;
	
	public ConnectionListener(NetworkAdapter adapt, DataInputStream in)
	{
		adapter = adapt;
		input = in;
	}
	
	public void run()
	{
		while (true)
		{
			try {
				if (input.available() > 0)
				{
					adapter.dataAvailable();
				}
			} catch (IOException e) {
				GameEngine.log(e.toString());
			}
		}
	}
}
