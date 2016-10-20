package communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class RaspberryPi extends Thread {
	String ipAddress;

	public RaspberryPi(String ipAddress) {
		this.ipAddress = ipAddress;
		this.start();
	}

	public int receivePort = 2003;
	public int sendPort = 2002;
	
	public ArrayList<Integer> ports	= new ArrayList<Integer>();

	public void run() {
		try {
			ports.add(sendPort);
			ServerSocket sendListener = new ServerSocket(2000);
			ServerSocket receiveListener = new ServerSocket(2001);

			System.out.println("Rpi Listeners created");

			Socket sendSocket = sendListener.accept();
			Socket receiveSocket = receiveListener.accept();

			System.out.println("Rpi Sockets created");

			ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(receiveSocket.getInputStream());

			System.out.println("Raspberry Pi Connected!");

			int command;
			while (true) {
				if (ois.available() > 0) {
					command = ois.readInt();
					System.out.println(command);
					// The command code for requesting a new thread is -3.
					
				}
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
		System.out.println(sendPort);
	}

	public int getSendPort() {
		return sendPort;
	}

	public int getReceivePort() {
		return receivePort;
	}
	
	public void updatePorts() {
		sendPort +=2;
		receivePort +=2;
		ports.add(sendPort);
	}

}
