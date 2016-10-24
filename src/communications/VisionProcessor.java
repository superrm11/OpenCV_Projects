package communications;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This is the class that does the processing of the image, and can be called as
 * many times as necessary.
 * 
 * 		PROCESSOR COMMANDS: 
 * 			-1: process image and send it over the socket 
 * 			-2: set the threshold values from the socket 
 * 			-3: reserved just in case for the main socket for requesting new sockets 
 * 			-4: sets the operations and their parameters (such as Dilation and Erosion)
 * 
 * @author Ryan McGee
 *
 */
public class VisionProcessor extends Thread {
	int port;
	public VisionProcessor(int port) {
		this.port = port;
		this.start();
	}

	private ArrayList<int[]> operations = new ArrayList<int[]>();

	boolean requestSingleProcessedImage = false;

	/**
	 * [0] = blueLowerBound 
	 * [1] = greenLowerBound 
	 * [2] = redLowerBound 
	 * [3] = blueUpperBound 
	 * [4] = greenUpperBound 
	 * [5] = redUpperBound 
	 * [6] = brightness
	 */
	static int[] thresholdValues = new int[7];

	boolean setThresholdValues = false;

	/**
	 * a two dimensional ArrayList that will store 1: the blobs and their indexes,
	 * and 2: the xy coordinates of the top left corner of each of those
	 * blobs and their width and height
	 **/
	public ArrayList<int[]> blobs = null;

	public boolean isRunningContinuously = false;

	@SuppressWarnings({ "resource", "unchecked" })
	public void run() {
		try {
			System.out.println("VisionProcessor output port: " + port);
			ServerSocket listener = new ServerSocket(port);
			System.out.println("Vision Processor Listeners Created");
			Socket socket = listener.accept();
			System.out.println("Vision Processor Sockets Created");
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			System.out.println("Vision Processor I/O streams created");
			Thread.sleep(1000);

			while (true) {
				// Requests an array of rectangles' x and y coordinates
				if (requestSingleProcessedImage) {
					oos.writeInt(-1);
					oos.flush();
					System.out.println("requested processed image");
					if (ois.available() > 0 && ois.readInt() == -5) {
						blobs = (ArrayList<int[]>) ois.readObject();
						requestSingleProcessedImage = false;
					}
				}

				if (setThresholdValues) {
					oos.writeInt(-2);
					oos.writeObject(thresholdValues);
					oos.flush();
					setThresholdValues = false;
				}

				if (sendOperations) {
					oos.writeInt(-4);
					oos.writeObject(operations);
					oos.flush();
					sendOperations = false;
				}

				if (runContinuously) {
					oos.writeInt(-5);
					oos.flush();
					isRunningContinuously = true;
					runContinuously = false;
				}

			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void loadConfig(String destination) {
		try {
			FileInputStream fis = new FileInputStream(destination);
			ObjectInputStream ois = new ObjectInputStream(fis);

			thresholdValues = (int[]) ois.readObject();

			setThresholdValues = true;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a request to process a SINGLE image ONLY if it is not continuously processing images
	 * 
	 * @author Ryan McGee
	 */
	public void requestSingleProcessedImage() {
		// if (!isRunningContinuously)
		requestSingleProcessedImage = true;
	}

	private boolean requestContinuousBlobs = false;

	public void requestContinuousBlobs() {
		if (isRunningContinuously)
			requestContinuousBlobs = true;
	}

	private boolean runContinuously = false;

	public void runContinuously() {
		runContinuously = true;
	}

	/**
	 * Gets the widest blob from the ArrayList of blobs
	 * @param blobs
	 * @return int array: The widest blob with 
	 * 			[0] = top left x coordinate, 
	 * 			[1] = y, 
	 * 			[2] = width, [3] = height
	 */
	public int[] getWidestBlob(ArrayList<int[]> blobs) {
		int[] temp = { 0, 0, 0, 0 };
		for (int i = 0; i < blobs.size(); i++) {
			if (blobs.get(i)[2] > temp[2]) {
				temp = blobs.get(i);
			}
		}
		return temp;
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
	public void setThresholdValues(int blueLowerBound, int greenLowerBound, int redLowerBound, int blueUpperBound,
			int greenUpperBound, int redUpperBound, int brightness) {

		thresholdValues[0] = blueLowerBound;
		thresholdValues[1] = greenLowerBound;
		thresholdValues[2] = redLowerBound;
		thresholdValues[3] = blueUpperBound;
		thresholdValues[4] = greenUpperBound;
		thresholdValues[5] = redUpperBound;
		thresholdValues[6] = brightness;

		setThresholdValues = true;
	}

	boolean sendOperations = false;

	public void sendOperations() {
		sendOperations = true;
	}

	public void dilate(int size, int iterations) {
		int[] dilation = { 1, size, iterations };
		operations.add(dilation);
	}

	public void erode(int size, int iterations) {
		int[] erosion = { 2, size, iterations };
		operations.add(erosion);
	}

	public boolean threshold() {
		int[] threshold = { 3 };
		operations.add(threshold);
		return true;
	}

}
