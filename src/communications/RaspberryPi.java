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
 * 		COMMANDS:
 * 			-3: reserved for creating new classes on the Raspberry Pi
 * 
 * @author Ryan McGee
 *
 */
public class RaspberryPi extends Thread {
	public RaspberryPi() {
		this.start();
	}

	public int port = 2001;

	public ArrayList<Integer> ports = new ArrayList<Integer>();

	public void run() {
		try {
			ports.add(2001);
			// Create the servers for the Raspberry Pi to connect
			ServerSocket listener = new ServerSocket(2000);

			System.out.println("Rpi Listeners created");

			// Waits until the Raspberry Pi has connected
			Socket socket = listener.accept();

			System.out.println("Rpi Sockets created");

			// Creates the I/O streams for the Pi and Roborio to communicate
			// over
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

			System.out.println("Raspberry Pi Connected!");

			int command;
			while (true) {
				// If there is a queue for requesting new threads, create all of
				// them
				if (requestNewThread > 0) {
					for (int i = 0; i < requestNewThread; i++) {
						oos.writeInt(-3);
						oos.writeInt(ports.get(0));
						oos.flush();
						ports.remove(0);
						System.out.println("Thread request sent!");
					}
					requestNewThread = 0;
				}
				Thread.sleep(100);

			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
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
	public void requestNewThread() {
		requestNewThread++;
		updatePorts();
	}

	private void updatePorts() {
		port++;
		ports.add(port);
	}

}
