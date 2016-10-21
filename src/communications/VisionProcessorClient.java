package communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.opencv.core.Scalar;

public class VisionProcessorClient {
	/**
	 * This is the main class that will accept thread requests and create new
	 * Processor classes for each request.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Set up the sockets that create the main processing sockets.
			Socket receiveSocket = new Socket(IP_ADDRESS, 2000);
			Socket sendSocket = new Socket(IP_ADDRESS, 2001);
			System.out.println("Set up sockets");
			ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(receiveSocket.getInputStream());
			System.out.println("Set up I/O Streams");
			int command;
			int port = 2002;
			while (true) {

				// If there is data in the input stream, reaSd it
				if (ois.available() > 0) {
					command = ois.readInt();
					System.out.println(command);
					// -3 is the code for create new Processor
					if (command == -3) {
						System.out.println("Command is -3");
						port = ois.readInt();
						System.out.println(port);
						(new Processor(port + 1, port)).start();
					}
				}
				Thread.sleep(100);
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is the class that does the processing of the image, and can be
	 * called as many times as necessary.
	 * 
	 * PROCESSOR COMMANDS: 
	 * 			-1: process image and send it over the socket 
	 * 			-2: set	the threshold values from the socket 
	 * 			-3: reserved just in case for the main socket 
	 * 				for requesting new sockets
	 * 
	 * 
	 * @author Ryan McGee
	 *
	 */
	public static class Processor extends Thread {
		int sendPort;
		int receivePort;

		/**
		 * @param sendPort
		 *            The port data will be sent over
		 * @param receivePort
		 *            The port data will be received from
		 */
		public Processor(int sendPort, int receivePort) {
			this.sendPort = sendPort;
			this.receivePort = receivePort;
		}

		/**
		 * This is the method everything will be run in; It is the "main method"
		 * of new threads
		 */
		public void run() {
			try {
				// Create the sockets based on the ports given
				Socket sendSocket = new Socket("localhost", sendPort);
				Socket receiveSocket = new Socket("localhost", receivePort);
				System.out.println("Sockets created");
				ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(receiveSocket.getInputStream());
				System.out.println("I/O streams created");
				int command = 0;
				while (true) {
					// If the input stream is available, read the command
					if (ois.available() > 0) {
						command = ois.readInt();
						if (command == -2) {
							setThresholdValues((int[]) ois.readObject());
							System.out.println("Set Lower bound to " + lowerBound);
							System.out.println("Set Upper bound to " + upperBound);
							System.out.println("Set brightness to " + brightness);
						} else if (command == -1) {
							processImage();
						}
					}

				}

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		private static Scalar lowerBound = new Scalar(0, 0, 0);
		private static Scalar upperBound = new Scalar(0, 0, 0);
		private static Scalar brightness = new Scalar(0, 0, 0);

		/**
		 * Sets the threshold scalars based on the integer array given
		 * 
		 * @param thresholdValues
		 *            The integer array with the values:
		 *            thresholdValues[0] = blue lower boundary
		 *            thresholdValues[1] = green lower boundary
		 *            thresholdValues[2] = red lower boundary
		 *            thresholdValues[3] = blue upper boundary
		 *            thresholdValues[4] = green upper boundary
		 *            thresholdValues[5] = red upper boundary
		 *            thresholdValues[6] = brightness
		 */
		private static void setThresholdValues(int[] thresholdValues) {
			lowerBound.set(new double[] { (double) thresholdValues[0], (double) thresholdValues[1],
					(double) thresholdValues[2] });
			upperBound.set(new double[] { (double) thresholdValues[3], (double) thresholdValues[4],
					(double) thresholdValues[5] });
			brightness.set(new double[] { thresholdValues[6], thresholdValues[6], thresholdValues[6] });

		}

		// TODO implement the processImage method based on the given
		// functionalities
		// and order of operations.
		private static int[][] processImage() {
			int[][] blobs = null;

			return blobs;
		}
	}

	private final static String IP_ADDRESS = "localhost";

}
