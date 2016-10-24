package communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class VisionProcessorClient {

	/**
	 * This is the main class that will accept thread requests and create new
	 * Processor classes for each request.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		image = new Mat();
		cap = new VideoCapture();
		cap.open(0);
		try {
			// Set up the sockets that create the main processing sockets.
			Socket socket = new Socket(IP_ADDRESS, 2000);
			System.out.println("Set up sockets");
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("Set up I/O Streams");
			int command;
			int port = 2001;
			while (true) {

				// If there is data in the input stream, reaSd it
				if (ois.available() > 0) {
					command = ois.readInt();
					// -3 is the code for create new Processor
					if (command == -3) {
						port = ois.readInt();
						System.out.println(port);
						(new Processor(port)).start();
					}
				}
				Thread.sleep(100);
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Mat image;
	public static VideoCapture cap;

	public static Mat getCapturedImage() {
		cap.retrieve(image);
		return image;
	}

	/**
	 * This is the class that does the processing of the image, and can be
	 * called as many times as necessary.
	 * 
	 * PROCESSOR COMMANDS: -1: process image and send it over the socket -2: set
	 * the threshold values from the socket -3: reserved just in case for the
	 * main socket for requesting new sockets -4: sets the operations and their
	 * parameters (such as Dilation and Erosion)
	 * 
	 * 
	 * @author Ryan McGee
	 *
	 */
	public static class Processor extends Thread {
		int port;

		private boolean isRunningContinuously = false;

		/**
		 * @param sendPort
		 *        The port data will be received/sent from/to
		 */
		public Processor(int port) {
			this.port = port;
		}

		/**
		 * This is the method everything will be run in; It is the "main method"
		 * of new threads
		 */
		public void run() {
			ArrayList<int[]> blobs = new ArrayList<int[]>();
			try {
				// Create the sockets based on the ports given
				Socket socket = new Socket("localhost", port);
				System.out.println("Sockets created");
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
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
							command = 0;
						} else if (command == -1) {
							blobs = processImage();
							oos.writeObject(blobs);
							oos.flush();
							command = 0;
						} else if (command == -4) {
							operations = (ArrayList<int[]>) ois.readObject();
							if(operations != null){
								System.out.println("Successfully read operations arraylist");
							}else{
								System.out.println("Failed to read operations arrayList");
							}
							command = 0;
						} else if (command == -5) {
							isRunningContinuously = true;
							command = 0;
						}
					}

					if (isRunningContinuously) {
						blobs = processImage();
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
		 *            The integer array with the values: thresholdValues[0] =
		 *            blue lower boundary thresholdValues[1] = green lower
		 *            boundary thresholdValues[2] = red lower boundary
		 *            thresholdValues[3] = blue upper boundary
		 *            thresholdValues[4] = green upper boundary
		 *            thresholdValues[5] = red upper boundary thresholdValues[6]
		 *            = brightness
		 */
		private void setThresholdValues(int[] thresholdValues) {
			lowerBound.set(new double[] { (double) thresholdValues[0], (double) thresholdValues[1],
					(double) thresholdValues[2] });
			upperBound.set(new double[] { (double) thresholdValues[3], (double) thresholdValues[4],
					(double) thresholdValues[5] });
			brightness.set(new double[] { thresholdValues[6], thresholdValues[6], thresholdValues[6] });

		}

		/**
		 * Will process a mat based on the order set by the RoboRIO COMMANDS:
		 * the first integer of every int array in the ArrayList 1: dilate 2:
		 * erode
		 * 
		 * @return
		 */
		private ArrayList<int[]> processImage() {
			ArrayList<int[]> blobs = new ArrayList<int[]>();
			Mat m;

			if (operations.isEmpty()) {
				System.out.println("No operations present!");
				return null;
			}

			m = VisionProcessorClient.getCapturedImage();
			for (int i = 0; i < operations.size(); i++) {
				if (operations.get(i)[0] == 1) {
					m = dilate(m, operations.get(i)[1], operations.get(i)[2]);
				} else if (operations.get(i)[0] == 2) {
					erode(m, operations.get(i)[1], operations.get(i)[2]);
				} else if (operations.get(i)[0] == 3) {
					threshold(m, upperBound, lowerBound, brightness);
				}
			}
			ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(m, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.RETR_LIST);
			for (int i = 0; i < contours.size(); i++) {
				Rect rect = Imgproc.boundingRect(contours.get(i));
				blobs.add(new int[] { rect.x, rect.y, rect.width, rect.height });
			}
			return blobs;
		}

		private ArrayList<int[]> operations = new ArrayList<int[]>();

		private Mat dilate(Mat m, int size, int iterations) {
			System.out.println("Dilating with " + size + " size and " + iterations + " iterations");

			Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(size, size));
			Imgproc.dilate(m, m, element, new Point(-1, -1), iterations);
			return m;
		}

		private Mat erode(Mat m, int size, int iterations) {
			System.out.println("Eroding with " + size + " size and " + iterations + " iterations");
			Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(size, size));
			Imgproc.erode(m, m, element, new Point(-1, -1), iterations);
			return m;
		}

		private Mat threshold(Mat m, Scalar upperBound, Scalar lowerBound, Scalar brightness) {
			System.out.println("Thresholding image");
			Core.add(m, brightness, m);
			Core.inRange(m, lowerBound, upperBound, m);
			return m;
		}

	}

	private final static String IP_ADDRESS = "localhost";

}
