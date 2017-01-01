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

	/**
	 * This is the main class that will accept thread requests and create new
	 * Processor classes for each request.
	 * 
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException
	{

		String ip = "";
		try
		{
			Date d = Calendar.getInstance().getTime();
			String date = d.toString().replaceAll(" ", "_").replaceAll(":", ";");
			File logFile = new File("/home/pi/logs/log_" + date + ".txt");
			logFile.createNewFile();
			PrintStream outputStream = new PrintStream(logFile);

			System.setOut(outputStream);

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
		cap.open("http://Ryan-Laptop.mshome.net:8000/video.mjpg");//0);//"http://10.3.39.11/mjpg/video.mjpg");
		System.out.println(cap.isOpened());

		int command;
		int port = 2001;

		ObjectInputStream ois;
		while (true)
		{
			try
			{
				// Set up the sockets that create the main processing sockets.
				Socket socket = new Socket(IP_ADDRESS, 2000);
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
			while (true)
			{

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
					} else if (command == 3)
					{
						System.out.println("Restarting program...");
						Runtime.getRuntime().exec("java -jar -Xmx900M -Xms900M /home/pi/RpiTest.jar");
						System.exit(0);
					}
				}
				Thread.sleep(100);
			}

		} catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
			System.exit(1);
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
			try
			{
				// Create the sockets based on the ports given
				Socket socket = new Socket(IP_ADDRESS, port);
				System.out.println("Sockets created");
				ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				oos.flush();
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				System.out.println("I/O streams created");
				int command = 0;
				while (!VisionProcessorClient.stopAllThreads)
				{
					if (ois.available() > 0)
					{
						command = ois.readInt();
					}

					if (command == -1)
					{
						System.out.println("Received process image command");
						if (!isProcessingImage)
							blobs = processImage();
						if (blobs != null)
						{
							oos.writeObject(blobs);
						} else
							System.out.println("blobs are null!");
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
					}

					if (isRunningContinuously)
					{
						blobs = processImage();
					}
					oos.flush();

				}

				ois.close();
				oos.close();
				socket.close();

			} catch (IOException |

					ClassNotFoundException e)
			{
				e.printStackTrace();
				System.exit(1);
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
		boolean isProcessingImage = false;

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
			isProcessingImage = true;
			ArrayList<int[]> blobs = new ArrayList<int[]>();
			Mat m;

			if (operations.isEmpty())
			{
				System.out.println("No operations present!");
				return null;
			}

			m = VisionProcessorClient.getCapturedImage();
			if (m == null)
			{
				return null;
			}

			if (saveRawImage)
			{
				Mat m1 = m.clone();
				if (destination.charAt(destination.length() - 1) == '/')
					Highgui.imwrite(new String(destination + "rawImage_" + fileNumber + ".jpg"), m1);
				else
					Highgui.imwrite(new String(destination + "/rawImage_" + fileNumber + ".jpg"), m1);
				saveRawImage = false;
			}

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
							new Scalar(operations.get(i)[7], operations.get(i)[7], operations.get(i)[7]), operations.get(i)[8]);
				}
			}
			ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(m, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
			for (int i = 0; i < contours.size(); i++)
			{
				Rect rect = Imgproc.boundingRect(contours.get(i));
				blobs.add(new int[]
				{ rect.x, rect.y, rect.width, rect.height });
				System.out.println("width of blob " + i + ": " + blobs.get(i)[2]);

			}

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
			isProcessingImage = false;
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
			if(colorCode == 1)
				Imgproc.cvtColor(alteredMat, alteredMat, Imgproc.COLOR_BGR2HSV);
			Core.inRange(alteredMat, lowerBound, upperBound, alteredMat);
			return alteredMat;
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
