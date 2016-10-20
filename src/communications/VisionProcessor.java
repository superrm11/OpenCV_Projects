package communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VisionProcessor extends Thread{
	int receivePort;
	int sendPort;

	public VisionProcessor(int sendPort, int receivePort) {
		this.receivePort = receivePort;
		this.sendPort = sendPort;
		this.start();
	}
	
	boolean requestProcessedImage = false;

	/**
	 * [0] = blueLowerBound [1] = greenLowerBound [2] = redLowerBound [3] =
	 * blueUpperBound [4] = greenUpperBound [5] = redUpperBound [6] =
	 * brightness
	 */
	static int[] thresholdValues = new int[7];

	boolean setThresholdValues = false;

	/**
	 * a two dimensional array that will store 1: the blobs and their
	 * indexes, and 2: the xy coordinates of the bounding rectangle of each
	 * of those blobs
	 **/
	int[][] blobs = null;

	public void run(){
		try {
			ServerSocket receiveListener = new ServerSocket(receivePort);
			ServerSocket sendListener = new ServerSocket(sendPort);
			System.out.println("Vision Processor Listeners Created");
			Socket receiveSocket = receiveListener.accept();
			Socket sendSocket = sendListener.accept();
			System.out.println("Vision Processor Sockets Created");
			ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(receiveSocket.getInputStream());

			System.out.println("Vision Processor I/O streams created");
			Thread.sleep(1000);

			while (true) {
				// Requests an array of rectangles' x and y coordinates
				if (requestProcessedImage == true) {
					oos.writeInt(-1);
					oos.flush();
					System.out.println("requested processed image");
					// If the input stream has data, write it to blobs
					if (ois.available() > 0) {
						blobs = (int[][]) ois.readObject();
						requestProcessedImage = false;
					}
				}
				
				if (setThresholdValues) {
					oos.writeInt(-2);
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

	/**
	 * Sends a request to process an image
	 * 
	 * @author Ryan McGee
	 */
	public void requestProcessedImage() {
		requestProcessedImage = true;
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

		thresholdValues[0] = blueLowerBound;
		thresholdValues[1] = greenLowerBound;
		thresholdValues[2] = redLowerBound;
		thresholdValues[3] = blueUpperBound;
		thresholdValues[4] = greenUpperBound;
		thresholdValues[5] = redUpperBound;
		thresholdValues[6] = brightness;
	}

}
