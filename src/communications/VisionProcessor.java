package communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VisionProcessor {
	private static SearchForClient a;

	public static void main(String[] args) {
		startServer(9090, 9091);

		setThresholdValues(255, 255, 255, 255, 255, 255, 255);
	}

	/**
	 * Starts the server wait for the vision processing client to connect.
	 * 
	 * @author Ryan McGee
	 * 
	 * @param receivePort
	 *            The port that will receive data from the client.
	 * @param sendPort
	 *            The port that will send data to the client.
	 */
	public static void startServer(int receivePort, int sendPort) {
		a = new SearchForClient(receivePort, sendPort);
		a.start();
	}

	/**
	 * Sends a request to process an image
	 * 
	 * @author Ryan McGee
	 */
	public void requestProcessedImage() {
		a.requestProcessedImage = true;
	}

	/**
	 * Sets the threshold boundaries in BGR format, with an added Scalar for
	 * brightness
	 * 
	 * @param blueLowerBound
	 * @param greenLowerBound
	 * @param redLowerBound
	 * @param blueUpperBound
	 * @param greenUpperBound
	 * @param redUpperBound
	 * @param brightness
	 * 
	 * @author Ryan McGee
	 */
	public static void setThresholdValues(int blueLowerBound, int greenLowerBound, int redLowerBound,
			int blueUpperBound, int greenUpperBound, int redUpperBound, int brightness) {

		a.thresholdValues[0] = blueLowerBound;
		a.thresholdValues[1] = greenLowerBound;
		a.thresholdValues[2] = redLowerBound;
		a.thresholdValues[3] = blueUpperBound;
		a.thresholdValues[4] = greenUpperBound;
		a.thresholdValues[5] = redUpperBound;
		a.thresholdValues[6] = brightness;

		a.setThresholdValues = true;
	}

	static class SearchForClient extends Thread {
		int receivePort;
		int sendPort;

		boolean requestProcessedImage = false;

		/**
		 * [0] = blueLowerBound [1] = greenLowerBound [2] = redLowerBound [3] =
		 * blueUpperBound [4] = greenUpperBound [5] = redUpperBound [6] =
		 * brightness
		 */
		int[] thresholdValues = new int[7];

		boolean setThresholdValues = false;

		/**
		 * a two dimensional array that will store 1: the blobs and their
		 * indexes, and 2: the xy coordinates of the bounding rectangle of each
		 * of those blobs
		 **/
		int[][] blobs = null;

		public SearchForClient(int receivePort, int sendPort) {
			this.receivePort = receivePort;
			this.sendPort = sendPort;
		}

		public void run() {
			try {
				ServerSocket receiveListener = new ServerSocket(receivePort);
				ServerSocket sendListener = new ServerSocket(sendPort);
				System.out.println("Listeners Created");
				Socket receiveSocket = receiveListener.accept();
				Socket sendSocket = sendListener.accept();
				System.out.println("sockets created");
				ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(receiveSocket.getInputStream());

				System.out.println("I/O streams created");
				Thread.sleep(1000);

				while (true) {
					// Requests an array of rectangles' x and y coordinates
					if (requestProcessedImage = true) {
						oos.writeByte(-1);
						oos.flush();
						System.out.println("requested processed image");
						// If the input stream has data, write it to blobs
						if (ois.available() > 0) {
							blobs = (int[][]) ois.readObject();
							requestProcessedImage = false;
						}
					}
					
					if (setThresholdValues) {
						oos.writeByte(-2);
						oos.flush();
						oos.writeObject(thresholdValues);
						oos.flush();
						setThresholdValues = false;
					}

				}
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
