package communications;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class VisionProcessorClient
{
	public static void restartCode()
	{
		stopAllThreads = true;
		if (cap.isOpened())
			cap.release();

		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		stopAllThreads = false;
		try
		{
			main(args);
		} catch (InterruptedException | IOException e)
		{
			e.printStackTrace();
		}

	}

	static String[] args = null;

	/**
	 * This is the main class that will accept thread requests and create new
	 * Processor classes for each request.
	 * 
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException
	{
		VisionProcessorClient.args = args;

		String ip = "";
		try
		{
			// Names log files by date and time
			Date d = Calendar.getInstance().getTime();
			String date = d.toString().replaceAll(" ", "_").replaceAll(":", ";");
			File logFile = new File("/home/pi/logs/log_" + date + ".txt");
			logFile.createNewFile();
			PrintStream outputStream = new PrintStream(logFile);
			// Sets the system.out stream to a file
			System.setOut(outputStream);
			// inputs the server ip from a text file named ip.txt . If this file
			// does not exist,
			// set the ip to be on the local machine. Useful for testing.
			FileReader fr = new FileReader("/home/pi/ip.txt");
			BufferedReader bfr = new BufferedReader(fr);
			ip = bfr.readLine();
			IP_ADDRESS = ip;
		} catch (IOException e1)
		{
			IP_ADDRESS = "localhost";
		}
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		image = new Mat();
		cap = new VideoCapture();
		// The ip of the camera image stream
		while (cap.isOpened() == false)
			cap.open(0);// "http://10.3.39.11/mjpg/video.mjpg");
		System.out.println("Camera is enabled? >" + cap.isOpened());

		int command;
		int port = 2001;

		ObjectInputStream ois = null;
		Socket socket = null;
		while (!stopAllThreads)
		{
			try
			{
				// Set up the sockets that create the main processing sockets.
				socket = new Socket(IP_ADDRESS, 2000);
				System.out.println("Set up socket");

				ois = new ObjectInputStream(socket.getInputStream());

				System.out.println("Set up I/O Streams");

				break;
			} catch (IOException e)
			{
				System.out.println("Unable to connect to " + IP_ADDRESS + "... Retrying...");
				Thread.sleep(1000);
				continue;
			}
		}

		try
		{
			long safeTime = System.currentTimeMillis();
			while (!stopAllThreads)
			{
				if (System.currentTimeMillis() - safeTime > 4000)
				{
					System.out.println("Timed out! Restarting...");
					restartCode();
				}

				// If there is data in the input stream, read it
				if (ois.available() > 0)
				{
					command = ois.readInt();
					// -3 is the code for create new Processor
					if (command == -3)
					{
						port = ois.readInt();
						System.out.println(port);
						(new Processor(port)).start();
					} else if (command == 1)
					// The code for stopping all threads is 1
					{
						stopAllThreads = true;
						System.out.println("Stopping all threads...");
					} else if (command == 2)
					{
						System.out.println("Triggering reboot...");
						Runtime.getRuntime().exec("sudo reboot");
					} else if (command == 255)
					{
						safeTime = System.currentTimeMillis();
					} else
					{
						command = 0;
					}
				}
				Thread.sleep(100);
			}

		} catch (IOException | InterruptedException e)
		{
			e.printStackTrace();

		} finally
		{
			if (ois != null)
				ois.close();
			if (socket != null)
				socket.close();
			restartCode();
		}
	}

	public static Mat image;
	public static VideoCapture cap;

	public static Mat getCapturedImage()
	{
		if (cap.isOpened() == true)
		{
			cap.grab();
			cap.retrieve(image);
			return image;
		} else
		{
			return null;
		}
	}

	public static boolean stopAllThreads = false;

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
	 * @author Ryan McGee
	 *
	 */
	public static class Processor extends Thread
	{
		int port;

		private boolean isRunningContinuously = false;

		/**
		 * @param port
		 *        The port data will be received/sent from/to
		 */
		public Processor(int port)
		{
			VisionProcessorClient.stopAllThreads = false;
			this.port = port;
		}

		/**
		 * This is the method everything will be run in; It is the "main method"
		 * of new threads
		 */
		public void run()
		{
			ArrayList<int[]> blobs = new ArrayList<int[]>();
			ObjectOutputStream oos = null;
			ObjectInputStream ois = null;
			Socket socket = null;
			try
			{
				// Create the sockets based on the ports given
				socket = new Socket(IP_ADDRESS, port);
				System.out.println("Sockets created");
				oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				oos.flush();
				ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				System.out.println("I/O streams created");
				int command = 0;
				while (!VisionProcessorClient.stopAllThreads && !socket.isClosed())
				{
					if (ois.available() > 0)
					{
						command = ois.readInt();
					}

					if (command == -1)
					{
						System.out.println("Received process image command");
						blobs = processImage();
						if (blobs != null)
						{
							oos.writeObject(blobs);
							blobs = null;
						} else
							System.out.println("blobs are null!");
						command = 0;
					} else if (command == -4)
					{
						System.out.println("Received set operations command");
						operations = (ArrayList<int[]>) ois.readObject();
						if (operations != null)
						{
							System.out.println("Successfully read operations arraylist");
						} else
						{
							System.out.println("Failed to read operations arrayList");
						}
						command = 0;
					} else if (command == -5)
					{
						System.out.println("Received run continuously command");
						isRunningContinuously = true;
						command = 0;
					} else if (command == -6)
					{
						destination = (String) ois.readObject();
						System.out.println(destination);
						saveRawImage = true;
						command = 0;
					} else if (command == -7)
					{
						destination = (String) ois.readObject();
						System.out.println(destination);
						saveProcessedImage = true;
						command = 0;
					} else if (command == -8)
					{
						loadConfig((String) ois.readObject());
					} else
					{
						command = 0;
					}

					if (isRunningContinuously)
					{
						blobs = processImage();
					}
					oos.flush();

					Thread.sleep(2);

				}

			} catch (IOException |

					ClassNotFoundException | InterruptedException e)
			{
				e.printStackTrace();
				restartCode();
			} finally
			{
				try
				{
					if (ois != null)
						ois.close();
					if (oos != null)
						oos.close();
					if (socket != null)
						socket.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}

			}
		}

		private Scalar lowerBound = new Scalar(0, 0, 0);
		private Scalar upperBound = new Scalar(0, 0, 0);
		private Scalar brightness = new Scalar(0, 0, 0);

		private int brightness_value = 0;

		boolean saveRawImage = false;
		boolean saveProcessedImage = false;
		int fileNumber = 0;
		String destination;

		/**
		 * Will process a mat based on the order set by the RoboRIO
		 * <p> COMMANDS:
		 * <br>the first integer of every int array in the ArrayList
		 * <br> 1: dilate
		 * <br> 2: erode 
		 * <br> 3: threshold
		 * </p>
		 * 
		 * @return ArrayList of integer arrays, containing blob information.
		 *<br> 			blobs[0] = x coordinate
		 *<br> 			blobs[1] = y coordinate
		 *<br> 			blobs[2] = width
		 *<br> 			blobs[3] = height
		 */
		private ArrayList<int[]> processImage()
		{
			long startTime = System.currentTimeMillis();
			ArrayList<int[]> blobs = new ArrayList<int[]>();
			Mat m;

			// Makes sure the operations array list has been received
			if (operations.isEmpty())
			{
				System.out.println("No operations present!");
				return null;
			}

			// Makes sure the camera is opened and has returned an image to the
			// processor
			m = VisionProcessorClient.getCapturedImage();
			if (m == null)
			{
				return null;
			}

			// Saves an image before processing based on the destination given.
			// If the processor is restarted, images WILL overwrite each other.
			if (saveRawImage)
			{
				Mat m1 = m.clone();
				if (destination.charAt(destination.length() - 1) == '/')
					Highgui.imwrite(new String(destination + "rawImage_" + fileNumber + ".jpg"), m1);
				else
					Highgui.imwrite(new String(destination + "/rawImage_" + fileNumber + ".jpg"), m1);
				saveRawImage = false;
			}

			// Loops through the operations list and applies the operation to
			// the image
			for (int i = 0; i < operations.size(); i++)
			{
				if (operations.get(i)[0] == 1)
				{
					m = dilate(m, operations.get(i)[1], operations.get(i)[2]);
				} else if (operations.get(i)[0] == 2)
				{
					m = erode(m, operations.get(i)[1], operations.get(i)[2]);
				} else if (operations.get(i)[0] == 3)
				{
					m = threshold(m, new Scalar(operations.get(i)[1], operations.get(i)[2], operations.get(i)[3]),
							new Scalar(operations.get(i)[4], operations.get(i)[5], operations.get(i)[6]),
							new Scalar(operations.get(i)[7], operations.get(i)[7], operations.get(i)[7]),
							operations.get(i)[8]);
				} else if (operations.get(i)[0] == 4)
				{
					m = removeSmallObjects(m, operations.get(i)[1]);
				}
			}

			// Finds the contours of the image
			ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(m, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
			for (int i = 0; i < contours.size(); i++)
			{
				// Gets bounding rectangles for each contour and stores into an
				// ArrayList
				Rect rect = Imgproc.boundingRect(contours.get(i));
				blobs.add(new int[]
				{ rect.x, rect.y, rect.width, rect.height });
				System.out.println("width of blob " + i + ": " + blobs.get(i)[2]);

			}

			// Saves an image after processing to view results.
			// Images WILL be overwritten if processor is restarted
			if (saveProcessedImage)
			{
				Imgproc.drawContours(m, contours, -1, new Scalar(200, 0, 0), Core.FILLED);
				for (int i = 0; i < blobs.size(); i++)
				{
					Core.rectangle(m, new Point(blobs.get(i)[0], blobs.get(i)[1]),
							new Point(blobs.get(i)[0] + blobs.get(i)[2], blobs.get(i)[1] + blobs.get(i)[3]),
							new Scalar(200, 0, 0));
				}
				if (destination.charAt(destination.length() - 1) == '/')
				{
					Highgui.imwrite(new String(destination + "processedImage_" + fileNumber + ".jpg"), m);
				} else
				{
					Highgui.imwrite(new String(destination + "/processedImage_" + fileNumber + ".jpg"), m);
				}
				saveProcessedImage = false;
			}
			// records the amount of time it took to process the image
			long endTime = System.currentTimeMillis();
			System.out.println("Time it took: " + (endTime - startTime));
			return blobs;
		}

		private ArrayList<int[]> operations = new ArrayList<int[]>();

		/**
		 * Dilates the blobs (all blobs grow)
		 * @param m input matrix (image)
		 * @param size how big the blobs should be dilated
		 * @param iterations how many times the blobs should be dilated
		 * @return the matrix after dilating the blobs
		 */
		private Mat dilate(Mat m, int size, int iterations)
		{
			Mat alteredMat = new Mat();
			alteredMat = m.clone();
			if (size < 1)
				return alteredMat;
			System.out.println("Dilating with " + size + " size and " + iterations + " iterations");

			Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(size, size));
			Imgproc.dilate(alteredMat, alteredMat, element, new Point(-1, -1), iterations);
			return alteredMat;
		}

		/**
		 * Erodes all blobs (all blobs shrink)
		 * @param m input matrix (image)
		 * @param size how small the blobs should shrink
		 * @param iterations how many times the blobs should be eroded
		 * @return the matrix after eroding the blobs
		 */
		private Mat erode(Mat m, int size, int iterations)
		{
			Mat alteredMat = new Mat();
			alteredMat = m.clone();
			if (size < 1)
				return alteredMat;
			System.out.println("Eroding with " + size + " size and " + iterations + " iterations");
			Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(size, size));
			Imgproc.erode(alteredMat, alteredMat, element, new Point(-1, -1), iterations);
			return alteredMat;
		}

		/**
		 * Thresholds the image based on the scalar values
		 * @param m input matrix, or image
		 * @param upperBound the scalar for the upper boundary BGR values
		 * @param lowerBound the scalar for the loser boundary BGR values
		 * @param brightness the scalar that is added to the matrix before thresholding.
		 * 			if the brightness is negative, that amount will be subtracted, therefore
		 * 			lowering the brightness.
		 * @return the matrix after thresholding the blobs
		 */
		private Mat threshold(Mat m, Scalar lowerBound, Scalar upperBound, Scalar brightness, int colorCode)
		{
			Mat alteredMat = new Mat();
			alteredMat = m.clone();
			System.out.println("Thresholding image");
			Core.add(alteredMat, brightness, alteredMat);
			if (colorCode == 1)
				Imgproc.cvtColor(alteredMat, alteredMat, Imgproc.COLOR_BGR2HSV);
			Core.inRange(alteredMat, lowerBound, upperBound, alteredMat);
			return alteredMat;
		}

		/** This is to remove small blobs that we don't want to see, without affecting size of other blobs.
		 * @param m input matrix (image)
		 * @param size how large the blobs are that you want to remove
		 * @return matrix (image) after removing the objects
		 */
		@SuppressWarnings("unused")
		private static Mat removeSmallObjects(Mat m, int size)
		{
			Mat alteredMat = m.clone();
			if (size < 1)
				return alteredMat;
			ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(alteredMat, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
			Mat newAlteredMat = Mat.zeros(alteredMat.rows(), alteredMat.cols(), alteredMat.type());
			ArrayList<int[]> rects = toRects(contours);
			for (int i = 0; i < contours.size(); i++)
			{
				if (rects.get(i)[2] * rects.get(i)[3] > (size * 20))
				{
					Imgproc.drawContours(newAlteredMat, contours, i, new Scalar(255, 255, 255, 255), Core.FILLED);
				}
			}
			return newAlteredMat;
		}

		private static ArrayList<int[]> toRects(ArrayList<MatOfPoint> contours)
		{
			ArrayList<int[]> output = new ArrayList<int[]>();

			for (int i = 0; i < contours.size(); i++)
			{
				Rect rect = Imgproc.boundingRect(contours.get(i));
				output.add(new int[]
				{ rect.x, rect.y, rect.width, rect.height });
			}

			return output;
		}

		/**
		 * Opens the .cfg file saved by the thresholdutility program
		 * @param destination the path to the file; it MUST contain the FULL path, right down to the .cfg at the end.
		 */
		private void loadConfig(String destination)
		{
			try
			{
				FileInputStream fis = new FileInputStream(destination);
				ObjectInputStream ois = new ObjectInputStream(fis);

				operations = (ArrayList<int[]>) ois.readObject();
			} catch (IOException | ClassNotFoundException e)
			{
				System.out.println("Unable to open the config file!");
				return;
			}
			System.out.println("Successfully read config file!");
		}
	}

	/**
	 * The location the server is being hosted on.
	 */
	private static String IP_ADDRESS;

}
