package communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class controls the creation of new VisionProcessor classes
 * on the Raspberry Pi end of things.
 * <p>	COMMANDS:
 * <br>			-3: reserved for creating new classes on the Raspberry Pi
 * <br>			 1: reserved for stopping all threads on the Raspberry Pi
 * <br>			 2: reserved for triggering a reboot of the Raspberry Pi
 * <br>			 3: reserved for restarting the code on the Raspberry Pi.
 * 
 * 
 * 
 * @author Ryan McGee
 *
 */
public class RaspberryPi extends Thread
{
	public RaspberryPi()
	{
		this.start();
		this.requestNewThread();
	}

	public int port = 2001;

	public ArrayList<Integer> ports = new ArrayList<Integer>();

	private ServerSocket listener = null;
	private Socket socket = null;
	private ObjectOutputStream oos = null;

	private boolean hardRestart = false;

	public void run()
	{
		if (hardRestart)
		{
			ports.clear();
			this.requestNewThread();
			hardRestart = false;
		}

		try
		{
			// Create the servers for the Raspberry Pi to connect
			ports.add(2001);
			listener = new ServerSocket(2000);
			System.out.println("Rpi Listeners created");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		while (true)

			try
			{

				// Waits until the Raspberry Pi has connected
				socket = listener.accept();

				System.out.println("Rpi Sockets created");

				// Creates the I/O streams for the Pi and Roborio to communicate
				// over
				oos = new ObjectOutputStream(socket.getOutputStream());

				System.out.println("Raspberry Pi Connected!");
				isConnected = true;

				int command;

				long safeTime = System.currentTimeMillis();

				Thread.sleep(1000);
				while (true)
				{
					if (System.currentTimeMillis() - safeTime >= 500)
					{
						oos.writeInt(255);
						safeTime = System.currentTimeMillis();
					}

					// If there is a queue for requesting new threads,
					// create all of them
					if (requestNewThread > 0)
					{
						for (int i = 0; i < requestNewThread; i++)
						{
							oos.writeInt(-3);
							oos.writeInt(ports.get(0));
							ports.remove(0);
							System.out.println("Thread request sent!");
						}
						requestNewThread = 0;
					}
					if (stopAllThreads)
					{
						oos.writeInt(1);
						System.out.println("Stopping all vision threads...");
						stopAllThreads = false;
					}

					if (reboot)
					{
						oos.writeInt(2);
						System.out.println("Triggering Raspberry pi reboot...");
						reboot = false;
					}

					if (restartCode)
					{
						oos.writeInt(3);
						System.out.println("Restarting Raspberry Pi vision code...");
						restartCode = false;
					}
					oos.flush();
					Thread.sleep(1);

				}

			} catch (IOException | InterruptedException e)
			{
				e.printStackTrace();
			} finally
			{
				System.out.println("Pi has disconnected");
				isConnected = false;
				try
				{
					if (socket != null)
						socket.close();
					System.out.println("Restarting server...");
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				hasDisconnected = true;
				hardRestart = true;
			}

	}

	// The queue for setting up new threads
	int requestNewThread = 0;

	/**
	 * Requests a new thread to start, allowing the image to be processed in
	 * multiple ways.
	 * 
	 * @author Ryan McGee
	 */
	public void requestNewThread()
	{
		requestNewThread++;
		updatePorts();
	}
	
	private boolean hasDisconnected = false;
	
	public boolean hasDisconnected()
	{
		return hasDisconnected;
	}

	private void updatePorts()
	{
		port++;
		ports.add(port);
	}

	boolean stopAllThreads = false;

	/**
	 * Sends a request to stop all of the vision processing on the pi, followed by closing the sockets and I/O streams
	 *<p>
	 * NOTE: to stop the Vision Processor threads (in the case of enabling / disabling) the RaspberryPi MUST call the stopAllThreads 
	 * function before the VisionProcessor object does. (It is a case of timing between sockets). If you are still getting a Connection abort:
	 * socket write error on EITHER the rio or the pi, increase the time delay in VisionProcessor.run().
	 * 
	 */
	public void stopAllThreads()
	{
		this.stopAllThreads = true;
	}

	private boolean isConnected = false;

	/**
	 * 
	 * @return whether or not the RoboRio is connected to the Rasberry Pi
	 */
	public boolean isConnected()
	{
		return isConnected;
	}

	boolean reboot = false;

	/**
	 * Sends a request for the Raspberry Pi to completely reboot, restarting all the code.
	 * <p>
	 * <b>NOTE:</b> this will take <i>at least 30 seconds</i> to completely shut down and come
	 * back up. Only use in desperate situations.
	 */
	public void triggerReboot()
	{
		reboot = true;
	}

	boolean restartCode = false;

	/**
	 * Sends a request for the Raspberry Pi to restart it's code only, without the need to reboot.
	 * <p>
	 * DO NOT USE if you want to just start / stop the vision; only use if there is a connection 
	 * problem. If you want to start / stop the code, use stopAllThreads.
	 *</p>
	 */
	public void restartCode()
	{
		restartCode = true;
	}

}
