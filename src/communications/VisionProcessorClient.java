package communications;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.opencv.core.Scalar;

public class VisionProcessorClient {
	public static void main(String[] args) {
		try {
			Socket sendSocket = new Socket("localhost", 9090);
			Socket receiveSocket = new Socket("localhost", 9091);
			System.out.println("Sockets created");
			ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(receiveSocket.getInputStream());
			System.out.println("I/O streams created");
			byte command = 0;
			while (true) {
				// If thte input stream is available, 
				if (ois.available() > 0) {
					command = ois.readByte();
					if (command == -2) {
						setThresholdValues((int[]) ois.readObject());
						System.out.println("Set th vals to " + lowerBound);
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

	private static void setThresholdValues(int[] thresholdValues) {
		lowerBound.set(
				new double[] { (double) thresholdValues[0], (double) thresholdValues[1], (double) thresholdValues[2] });
		upperBound.set(
				new double[] { (double) thresholdValues[3], (double) thresholdValues[4], (double) thresholdValues[5] });
		brightness.set(new double[] { thresholdValues[6], thresholdValues[6], thresholdValues[6] });

	}

	// TODO implement the processImage method based on the given functionalities
	// and order of operations.
	private static int[][] processImage() {
		int[][] blobs = null;

		return blobs;
	}

}
