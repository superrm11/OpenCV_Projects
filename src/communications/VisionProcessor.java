package communications;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This is the class that does the processing of the image, and can be called as
 * many times as necessary.
 * <p>
 * 		PROCESSOR COMMANDS: 
 * <br>			-1: process image and send it over the socket 
 * <br>			-2: set the threshold values from the socket 
 * <br>			-3: reserved just in case for the main socket for requesting new sockets 
 * <br>			-4: sets the operations and their parameters (such as Dilation and Erosion)
 * <br>			-5: requests the thread to continuously process images
 * <br>			-6: requests to save the raw image to the destination provided
 * <br>			-7: requests to save the processed image to the destination provided
 * <br>			-8: load a configuration file saved from the thresholdutility program
 * </p>
 * <p>
 * 		MAIN COMMUNICATION COMMANDS:
 * <br>			1: request new thread
 * <br>			2: confirmation of request (sent from client)
 * </p>
 * @author Ryan McGee
 *
 */
public class VisionProcessor extends Thread
{
	// -------------------MAIN INITIALIZATION----------------------
	private static ServerSocket mainServerSocket = null;
	private static Socket mainSocket = null;
	private static ObjectOutputStream mainOutputStream = null;
	private static ObjectInputStream mainInputStream = null;

	/**
	 * Determines each port for each VisionProcessor object created. Port 6000 reserved for accepting thread requests.
	 */
	private static int currentPort = 6000;

	/**
	 * Used only in the initialization phase, this is used for connecting to the raspberry pi's multi-thread controller.
	 */
	private static final int MAIN_INIT_PORT = currentPort;
	/**
	 * Determines whether a socket has been created with the client for multi-threading purposes.
	 */
	private volatile static boolean hasInitialized = false;

	/**
	 * Initializes the main sockets for communication when requesting new threads and such. This will allow the 
	 * VisionProcessor class and it's communication to be self contained, without the need for Mr. programmer to 
	 * worry about ports and sockets and things of that sort.
	 * <br>
	 * WARNING: This has a recursive call. It will attempt to reconnect if the connection failed.
	 * <br>
	 * <b> KEEP THIS OUT OF THE MAIN THREAD! </b>
	 * 
	 * @return Whether or not the connection to the Raspberry Pi was successful.
	 */
	private static boolean init()
	{
		try
		{
			// Connect to the initial server.
			// This will replace the current "RaspberryPi" class.
			// Enables communication with the Raspberry Pi for requesting new
			// threads and ports.
			mainServerSocket = new ServerSocket(MAIN_INIT_PORT);
			mainSocket = mainServerSocket.accept();
			mainOutputStream = new ObjectOutputStream(new BufferedOutputStream(mainSocket.getOutputStream()));
			mainOutputStream.flush();
			mainInputStream = new ObjectInputStream(new BufferedInputStream(mainSocket.getInputStream()));

			return true;

		} catch (IOException e)
		{
			System.out.println("Unable to connect to the vision processor. Retrying...");
		}
		// Makes sure that the program will only try to connect every 3 seconds
		// if it failed.
		try
		{
			Thread.sleep(3000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return init();
	}

	// ==================END MAIN INIT==========================

	private static void requestNewThread()
	{
		try
		{
			mainOutputStream.writeInt(1);
			mainOutputStream.flush();

			// Waits for the client to acknowledge that we have requested a new
			// thread
			while (mainInputStream.readInt() != 2)
				;

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * The port for this VisionProcessor specifically
	 */
	private int port;

	public VisionProcessor()
	{
		// Creates a temporary thread for the initial connection to the
		// Raspberry Pi. ONLY if this is the first setup.
		// This thread will terminate as soon as the pi has connected.
		if (hasInitialized == false)
		{
			(new Thread(new Runnable()
			{
				public void run()
				{
					hasInitialized = init();
				}
			})).start();
		}
		requestNewThread();
		// Adds one to the current port; port 6000 reserved for accepting thread
		// requests
		this.port = ++currentPort;
		// begin the new thread
		this.start();
	}

	private ArrayList<int[]> operations = new ArrayList<int[]>();

	boolean requestSingleProcessedImage = false;

	/**
	 * <br>[0] = blueLowerBound 
	 * <br>[1] = greenLowerBound 
	 * <br>[2] = redLowerBound 
	 * <br>[3] = blueUpperBound 
	 * <br>[4] = greenUpperBound 
	 * <br>[5] = redUpperBound 
	 * <br>[6] = brightness
	 */
	static int[] thresholdValues = new int[9];

	/**
	 * ArrayList of integer arrays, containing blob information.
	 *<br> (They are all rectangles)
	 * <p>
	 *<br> 			blobs[0] = x coordinate
	 *<br> 			blobs[1] = y coordinate
	 *<br> 			blobs[2] = width
	 *<br> 			blobs[3] = height
	 *</p>
	 **/
	public ArrayList<int[]> blobs = null;

	/**
	 * Will display true only if the program just received new blobs.
	 * You can change this if you are finished using this set of blobs
	 * and want to wait for the next to send.
	 */
	public volatile boolean blobsAreNew = false;

	@SuppressWarnings(
	{ "unchecked" })
	/**
	 * The main thread for the VisionProcessor. Will run a setup, followed by a loop. Loop will end if communications with 
	 * the client is cut off.
	 */
	public void run()
	{

		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		InputStreamReader isr = null;
		Socket socket = null;
		ServerSocket listener = null;
		while (true)
			try
			{
				System.out.println("VisionProcessor output port: " + port);
				// Server Socket listens on that specific port for incomimg
				// clients.
				listener = new ServerSocket(port);
				System.out.println("Vision Processor Listeners Created");
				// Once a client connects, it is "bound" to that
				// client.
				socket = listener.accept();
				System.out.println("Vision Processor Sockets Created");
				// binds the output stream to the same as the socket, used for
				// sending objects over it.
				oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				// Flushes the stream to ensure the client can connect
				oos.flush();
				// creates and binds the input stream to the socket to input
				// objects. OutputStream on client must be flushed before
				// connecting.
				ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

				// Creates a reader so that the input stream can be read without
				// blocking the thread.
				isr = new InputStreamReader(ois);

				System.out.println("Vision Processor I/O streams created");
				Thread.sleep(1000);
				Object o = null;

				while (true)
				{
					o = isr.getObject();
					if (blobsAreNew && o != null)
					{
						blobs = (ArrayList<int[]>) o;
					}
					if (sendOperations)
					{
						oos.writeInt(-4);
						oos.writeObject(operations);
						sendOperations = false;
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
						oos.writeInt(-1);
						// System.out.println("requested processed image");
						requestSingleProcessedImage = false;

					}

					if (stopThread)
					{
						try
						{ // Increase this time delay in case of connection
							// abort
							// error ONLY.
							Thread.sleep(500);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						break;
					}

					if (loadConfig)
					{
						oos.writeInt(-8);
						oos.writeObject(configDestination);
						System.out.println("Loading configuration from file");
						loadConfig = false;
					}
					oos.flush();

				}
			} catch (IOException | InterruptedException e)
			{
				e.printStackTrace();

			} finally
			{
				stopThread = false;
				try
				{
					if (oos != null)
						oos.close();
					if (ois != null)
						ois.close();
					if (isr != null)
						isr.close();
					if (listener != null)
						listener.close();

				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

	}

	/**
	 *  Load a configuration file saved from the Threshold Utility.
	 * @param destination @param destination the path to the file; 
	 * 			it MUST contain the FULL path, right down to the .cfg at the end.
	 */
	public void loadConfig(String destination)
	{
		this.configDestination = destination;
		loadConfig = true;
	}

	private String configDestination = "";
	private boolean loadConfig = false;

	/**
	 * Sends a request to process a SINGLE image ONLY if it is not continuously processing images
	 * 
	 */
	public void requestSingleProcessedImage()
	{
		// if (!isRunningContinuously)
		requestSingleProcessedImage = true;
	}

	/**
	 * Gets the widest blob from the ArrayList of blobs
	 * @param blobs
	 * @return int array: The widest blob with 
	 * 			[0] = top left x coordinate, 
	 * 			[1] = y, 
	 * 			[2] = width, 
	 * 			[3] = height
	 */
	public int[] getWidestBlob(ArrayList<int[]> blobs)
	{
		int[] temp =
		{ 0, 0, 0, 0 };
		if (blobs != null)
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
			int greenUpperBound, int redUpperBound, int brightness, int colorCode)
	{
		thresholdValues[0] = 3;
		thresholdValues[1] = blueLowerBound;
		thresholdValues[2] = greenLowerBound;
		thresholdValues[3] = redLowerBound;
		thresholdValues[4] = blueUpperBound;
		thresholdValues[5] = greenUpperBound;
		thresholdValues[6] = redUpperBound;
		thresholdValues[7] = brightness;
		thresholdValues[8] = colorCode;

		operations.add(thresholdValues);
	}

	public static final int BGR = 0;
	public static final int HSV = 1;

	/**
	 * Sets the threshold values in BGR format, ranging from 0-255 and brightness from -255 to 255.
	 * This will automatically set the lower and upper boundaries based on the range and input values.
	 * 
	 * @param blue Average blue value
	 * @param green Average green value
	 * @param red Average red value
	 * @param brightness brightness value, added to BGR matrix as a scalar
	 * @param percent percentage value expressed as a number between 0 and 1
	 */
	public void threshold(int blue, int green, int red, int brightness, double percent)
	{
		thresholdValues[0] = 3;
		thresholdValues[1] = (int) testForNegative(blue - (255 * percent));
		thresholdValues[2] = (int) testForNegative(green - (255 * percent));
		thresholdValues[3] = (int) testForNegative(red - (255 * percent));
		thresholdValues[4] = (int) (blue + (255 * percent));
		thresholdValues[5] = (int) (green + (255 * percent));
		thresholdValues[6] = (int) (red + (255 * percent));
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

	/**
	 * Removes objects with an area less than the size given, without
	 * altering the size of other blobs (does not use erosion).
	 * @param size size of the blobs that should be removed
	 */
	public void removeSmallObjects(int size)
	{
		int[] rmSmObs =
		{ 4, size };
		operations.add(rmSmObs);
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
	 * <p>
	 * NOTE: to stop the Vision Processor threads (in the case of enabling / disabling) the RaspberryPi object MUST call the stopAllThreads 
	 * function before the VisionProcessor object does. (It is a case of timing between sockets). If you are still getting a Connection abort:
	 * socket write error on EITHER the rio or the pi, increase the time delay in VisionProcessor.run().
	 */
	public void stopThread()
	{
		this.stopThread = true;
	}

	/**
	 * Gets an array of the information of all the blobs
	 */
	public void getParticleReport()
	{
		if (this.blobs != null)
		{
			ArrayList<int[]> blobsCopy = (ArrayList<int[]>) this.blobs.clone();
			if (blobsCopy == null)
				return;
			ParticleReport[] particles = new ParticleReport[blobsCopy.size()];
			for (int i = 0; i < blobsCopy.size(); i++)
			{
				particles[i] = new ParticleReport(blobsCopy.get(i));
			}
			this.particleReports = particles;
		}
	}

	/**
	 * Filters out all blobs that do not have the aspect ratio falling in the range of the lowerBound and upperBound specified, or the given
	 * aspect ratio give or take the tolerance times that aspect ratio.
	 * <br> NOTE: the aspect ratio is (width / height), so a value above one is wider than tall, etc.
	 * @param arg0 If AspectRatio is set to BOUNDARY, this is the lower boundary of acceptable aspect ratios.
	 *				Else if it is set to TOLERANCE, this is the desired aspect ratio.
	 * @param arg1 If AspectRatio is set to BOUNDARY, this is the upper boundary of acceptable aspect ratios.
	 * 				Else if it is set to TOLERANCE, this is the tolerance.
	 * @param a
	 * 				Whether the user wants a desired ratio and tolerance or boundaries.
	 * @param useTolerance 
	 */
	public void filterAspectRatio(double arg0, double arg1, AspectRatio a)
	{
		ArrayList<ParticleReport> filteredReports = new ArrayList<ParticleReport>();
		switch (a)
		{
		case BOUNDARY:
			for (int i = 0; i < this.particleReports.length; i++)
			{
				if (this.particleReports[i].blobAspectRatio > arg0 && this.particleReports[i].blobAspectRatio < arg1)
					filteredReports.add(this.particleReports[i]);
			}
			break;
		case TOLERANCE:
			for (int i = 0; i < this.particleReports.length; i++)
			{
				if (this.particleReports[i].blobAspectRatio > (arg0 - (arg0 * arg1))
						&& this.particleReports[i].blobAspectRatio < (arg0 + (arg0 * arg1)))
					filteredReports.add(this.particleReports[i]);
			}
		}

		this.particleReports = (ParticleReport[]) filteredReports.toArray();
	}

	public static enum AspectRatio
	{
		BOUNDARY, TOLERANCE
	}

	public ParticleReport[] particleReports;

	String destination;

	/**
	 * A simple object that can continuously read an object from an objectInputStream
	 * WITHOUT blocking the code.
	 * 
	 * @author Ryan McGee
	 *
	 */
	private class InputStreamReader extends Thread
	{
		private ObjectInputStream ois;

		public InputStreamReader(ObjectInputStream ois)
		{
			this.ois = ois;
			this.start();

		}

		public void run()
		{
			while (!closeThread)
			{
				try
				{
					o = ois.readObject();
					if (o != null)
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

		public void close()
		{
			closeThread = true;
		}

		private boolean closeThread = false;

		private volatile Object o = null;

		/**
		 * 
		 * @return the last received object from the ObjectInputStream. Will return null if no object has been found yet.
		 */
		public Object getObject()
		{
			return o;
		}

	}

	public class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport>
	{

		// TODO add missing camera code and Particle Report

		public Point topLeft;

		public Point bottemRight;

		public Point centerOfRect;

		public int rectWidth;
		public int rectHeight;

		public int rectArea;

		public int imageHeight;
		public int imageWidth;

		public double blobAspectRatio;

		public ParticleReport(int[] info)
		{
			this.topLeft = new Point(info[0], info[1]);

			this.rectWidth = info[2];
			this.rectHeight = info[3];

			this.rectArea = this.rectWidth * this.rectHeight;

			this.bottemRight = new Point(this.topLeft.x + this.rectWidth, this.topLeft.y + this.rectHeight);

			this.centerOfRect = new Point(this.topLeft.x + (int) Math.round(this.rectWidth / 2.0),
					this.topLeft.y + (int) Math.round(this.rectHeight / 2.0));

			this.blobAspectRatio = this.rectWidth / (double) this.rectHeight;

		}

		@Override
		public int compareTo(ParticleReport arg0)
		{
			if (this.rectArea > arg0.rectArea)
				return 1;
			else if (this.rectArea < arg0.rectArea)
				return -1;
			else
				return 0;
		}

		@Override
		public int compare(ParticleReport arg0, ParticleReport arg1)
		{
			if (arg0.rectArea > arg1.rectArea)
				return 1;
			else if (arg0.rectArea < arg1.rectArea)
				return -1;
			else
				return 0;
		}

	}

}
