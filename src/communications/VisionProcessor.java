package communications;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
 * 			-5: requests the thread to continuously process images
 * 			-6: requests to save the raw image to the destination provided
 * 			-7: requests to save the processed image to the destination provided
 * 
 * @author Ryan McGee
 *
 */
public class VisionProcessor extends Thread
{
	int port;

	public VisionProcessor(int port)
	{
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
	static int[] thresholdValues = new int[8];

	/**
	 * ArrayList of integer arrays, containing blob information.
	 * (They are all rectangles)
	 * 			blobs[0] = x coordinate
	 * 			blobs[1] = y coordinate
	 * 			blobs[2] = width
	 * 			blobs[3] = height
	 **/
	public ArrayList<int[]> blobs = null;

	public volatile boolean blobsAreNew = false;

	public boolean isRunningContinuously = false;

	@SuppressWarnings(
	{ "resource", "unchecked" })
	public void run()
	{
		try
		{
			System.out.println("VisionProcessor output port: " + port);
			ServerSocket listener = new ServerSocket(port);
			System.out.println("Vision Processor Listeners Created");
			Socket socket = listener.accept();
			System.out.println("Vision Processor Sockets Created");
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			oos.flush();
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

			InputStreamReader iis = new InputStreamReader(ois);

			System.out.println("Vision Processor I/O streams created");
			Thread.sleep(1000);
			boolean alreadyRequested = false;
			int command = 0;
			Object o = null;

			while (!stopThread)
			{
				o = iis.getObject();
				if (o != null && o instanceof ArrayList)
				{
					blobs = (ArrayList<int[]>) o;
				}
				if (sendOperations)
				{
					oos.writeInt(-4);
					oos.writeObject(operations);
					sendOperations = false;
				}

				if (runContinuously)
				{
					oos.writeInt(-5);
					isRunningContinuously = true;
					runContinuously = false;
				}
				if (saveRawImage)
				{
					System.out.println("Requesting to save unprocessed image");
					oos.writeInt(-6);
					oos.writeObject(destination);
					saveRawImage = false;
				}

				if (saveProcessedImage)
				{
					System.out.println("Requesting to save processed image");
					oos.writeInt(-7);
					oos.writeObject(destination);
					saveProcessedImage = false;
				}
				// Requests an array of rectangles' x and y coordinates
				if (requestSingleProcessedImage)
				{
					if (alreadyRequested == false)
					{
						oos.writeInt(-1);
//						 System.out.println("requested processed image");
						alreadyRequested = true;
					}
					requestSingleProcessedImage = false;
					alreadyRequested = false;

				}
				oos.flush();

			}

			ois.close();
			oos.close();
			socket.close();
			listener.close();
		} catch (IOException | InterruptedException e)
		{
			e.printStackTrace();

		}

	}

	/**
	 *  Load a configuration file saved from the Threshold Utility.
	 * @param destination
	 */
	public void loadConfig(String destination)
	{
		try
		{
			FileInputStream fis = new FileInputStream(destination);
			ObjectInputStream ois = new ObjectInputStream(fis);

			operations = (ArrayList<int[]>) ois.readObject();

		} catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sends a request to process a SINGLE image ONLY if it is not continuously processing images
	 * 
	 */
	public void requestSingleProcessedImage()
	{
		// if (!isRunningContinuously)
		requestSingleProcessedImage = true;
	}

	private boolean requestContinuousBlobs = false;

	/**
	 * Requests a blob from the Raspberry Pi only if it is running continuously.
	 * 
	 * EDIT: just use the requestSingleImage command. It does not take that much time.
	 * @deprecated
	 */
	public void requestContinuousBlobs()
	{
		if (isRunningContinuously)
			requestContinuousBlobs = true;
	}

	private boolean runContinuously = false;

	/**
	 * Requests that the Raspberry Pi constantly takes and processes images, discarding
	 * the results until an image request is sent.
	 * 
	 * EDIT: just use the requestSingleImage command. It does not take that much time.
	 * 
	 * @deprecated
	 */
	public void runContinuously()
	{
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
	public int[] getWidestBlob(ArrayList<int[]> blobs)
	{
		int[] temp =
		{ 0, 0, 0, 0 };
		for (int i = 0; i < blobs.size(); i++)
		{
			if (blobs.get(i)[2] > temp[2])
			{
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
	public void threshold(int blueLowerBound, int greenLowerBound, int redLowerBound, int blueUpperBound,
			int greenUpperBound, int redUpperBound, int brightness)
	{
		thresholdValues[0] = 3;
		thresholdValues[1] = blueLowerBound;
		thresholdValues[2] = greenLowerBound;
		thresholdValues[3] = redLowerBound;
		thresholdValues[4] = blueUpperBound;
		thresholdValues[5] = greenUpperBound;
		thresholdValues[6] = redUpperBound;
		thresholdValues[7] = brightness;

		operations.add(thresholdValues);
	}

	/**
	 * Sets the threshold values in BGR format, ranging from 0-255 and brightness from -255 to 255.
	 * This will automatically set the lower and upper boundaries based on the range and input values.
	 * 
	 * @param blue Average blue value
	 * @param green Average green value
	 * @param red Average red value
	 * @param brightness brightness value, added to BGR matrix as a scalar
	 * @param percentError percentage value expressed as a number between 0 and 1
	 */
	public void threshold(int blue, int green, int red, int brightness, double percentError)
	{
		thresholdValues[0] = 3;
		thresholdValues[1] = (int) testForNegative(blue - (255 * percentError));
		thresholdValues[2] = (int) testForNegative(green - (255 * percentError));
		thresholdValues[3] = (int) testForNegative(red - (255 * percentError));
		thresholdValues[4] = (int) (blue + (255 * percentError));
		thresholdValues[5] = (int) (green + (255 * percentError));
		thresholdValues[6] = (int) (red + (255 * percentError));
		thresholdValues[7] = brightness;
		operations.add(thresholdValues);
	}

	private double testForNegative(double i)
	{
		if (i < 0)
		{
			return 0.0;
		} else
		{
			return i;
		}
	}

	boolean sendOperations = false;

	/**
	 * Tells the Raspberry pi what must be done in what order, and with which parameters.
	 * This runs after operations have been set (dilation, erosion, threshold),
	 * and should NOT be run continuously.
	 */
	public void sendOperations()
	{
		sendOperations = true;
	}

	/**
	 * Dilate blobs on the image (make all blobs larger from their middle).
	 * Useful for resizing the blobs back to normal after erosion, or closing a blob.
	 * @param size How large the dilation should make the blobs
	 * @param iterations How many times the dilation should run
	 */
	public void dilate(int size, int iterations)
	{
		int[] dilation =
		{ 1, size, iterations };
		operations.add(dilation);
	}

	/**
	 * Erode the image (make all blobs smaller from their middle).
	 * Useful for removing small objects.
	 * @param size How small the erosion should make the blobs
	 * @param iterations How many times it should be eroded
	 */
	public void erode(int size, int iterations)
	{
		int[] erosion =
		{ 2, size, iterations };
		operations.add(erosion);
	}

	boolean saveRawImage = false;

	/**
	 * Requests that the Raspberry Pi saves the camera picture(WITH brightness change)
	 * before processing it. This is useful for finding threshold values.
	 * @param destination The path you want the file to save to. DO NOT add the file name or extension.
	 */
	public void saveRawImage(String destination)
	{
		this.destination = destination;
		saveRawImage = true;
	}

	boolean saveProcessedImage = false;

	/**
	 * Requests that the Raspberry Pi saves the camera picture after
	 *  processing it. This is useful for finding the correct values for aiming.
	 * @param destination The path you want the file to save to. DO NOT add the file name or extension.
	 */
	public void saveProcessedImage(String destination)
	{
		this.destination = destination;
		saveProcessedImage = true;
	}

	private boolean stopThread = false;

	/**
	 * Halts requests for images, closes listeners, sockets and I/O streams, and ends the thread.
	 * Any requests made for single images will be discarded unless creating a new object.
	 * 
	 * NOTE: to stop the Vision Processor threads (in the case of enabling / disabling) the RaspberryPi object MUST call the stopAllThreads 
	 * function before the VisionProcessor object does. (It is a case of timing between sockets). If you are still getting a Connection abort:
	 * socket write error on EITHER the rio or the pi, increase the time delay in VisionProcessor.stopThread().
	 */
	public void stopThread()
	{
		try
		{ // Increase this time delay in case of connection abort error ONLY.
			Thread.sleep(500);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		this.stopThread = true;
	}

	String destination;

	/**
	 * A simple object that can continuously read an object from an objectInputStream
	 * WITHOUT blocking the code.
	 * @author Ryan McGee
	 *
	 */
	private class InputStreamReader implements Runnable
	{
		private ObjectInputStream ois;

		public InputStreamReader(ObjectInputStream ois)
		{
			this.ois = ois;
			Thread thread = new Thread(new InputStreamReader(this.ois, true));
			thread.start();

		}
		
		public InputStreamReader(ObjectInputStream ois, boolean isRunningInternally)
		{
			this.ois = ois;
		}

		public void run()
		{
			while (true)
			{
				try
				{
					o = ois.readObject();
					blobsAreNew = true;
				} catch (ClassNotFoundException | IOException e)
				{
					break;
				}
				try
				{
					Thread.sleep(1);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		Object o = null;

		/**
		 * 
		 * @return the last received object from the ObjectInputStream. Will return null if no object has been found yet.
		 */
		public Object getObject()
		{
			return o;
		}

	}

}
