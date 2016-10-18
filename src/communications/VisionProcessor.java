package communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VisionProcessor {
	SearchForClient a;

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
	public void startServer(int receivePort, int sendPort) {
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
	 * Sets the threshold boundaries in BGR format, with an added Scalar for brightness
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
	public void setThresholdValues(int blueLowerBound, int greenLowerBound, int redLowerBound, int blueUpperBound,
			int greenUpperBound, int redUpperBound, int brightness) {
		
		a.blueLowerBound = blueLowerBound;
		a.greenLowerBound = greenLowerBound;
		a.redLowerBound = redLowerBound;
		a.blueUpperBound = blueUpperBound;
		a.greenUpperBound = greenUpperBound;
		a.redUpperBound = redUpperBound;
		a.brightness = brightness;
		
		a.setThresholdValues = true;
	}

	static class SearchForClient extends Thread {
		int receivePort;
		int sendPort;

		boolean requestProcessedImage = false;

		int redLowerBound = 0;
		int greenLowerBound = 0;
		int blueLowerBound = 0;
		int redUpperBound = 0;
		int greenUpperBound = 0;
		int blueUpperBound = 0;
		int brightness = 0;

		boolean setThresholdValues = false;

		// a two dimensional array that will store 1: the blobs and their
		// indexes, and 2: the xy coordinates of the bounding rectangle of each
		// of those blobs
		int[][] blobs;

		public SearchForClient(int receivePort, int sendPort) {
			this.receivePort = receivePort;
			this.sendPort = sendPort;
		}

		public void run() {
			try {
				ServerSocket receiveListener = new ServerSocket(receivePort);
				ServerSocket sendListener = new ServerSocket(sendPort);

				Socket receiveSocket = receiveListener.accept();
				Socket sendSocket = sendListener.accept();

				OutputStream os = sendSocket.getOutputStream();
				InputStream is = receiveSocket.getInputStream();

				ObjectInputStream ois = new ObjectInputStream(sendSocket.getInputStream());

				while (true) {

					if (requestProcessedImage = true) {
						os.write(-1);
						os.flush();
						blobs = (int[][]) ois.readObject();
						requestProcessedImage = false;
					}

					if (setThresholdValues) {
						os.write(-2);
						os.flush();
						setThresholdValues = false;
					}

				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}
}
