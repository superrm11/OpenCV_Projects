import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.SocketAddress;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class Test_Client implements java.io.Serializable {
	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoCapture cap = new VideoCapture();
		cap.open(0);
		Mat m = new Mat();
		cap.retrieve(m);
		Core.add(m, new Scalar(-33, -33, -33), m);
		Core.inRange(m, new Scalar(0, 22, 0), new Scalar(31, 107, 4), m);
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(11,11));
		Imgproc.dilate(m, m, element, new Point(-1,-1), 1);
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(m, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		Imgproc.drawContours(m, contours, -1, new Scalar(200, 0, 0), Core.FILLED);
		MatOfPoint points = new MatOfPoint(contours.get(0).toArray());
		Rect rect = Imgproc.boundingRect(points);
		
		Core.rectangle(m, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(200, 0, 0));

		JFrame frame = new JFrame();
		imageIcon = new ImageIcon(convertToImage(m));
		label = new JLabel(imageIcon);
		frame.getContentPane().add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		cap.release();
	}

	private static JLabel label;
	private static ImageIcon imageIcon;
	private static MatOfByte matOfByte;
	private static byte[] byteArray;
	private static BufferedImage bufImage;
	private static InputStream in;

	private static BufferedImage convertToImage(Mat m) {
		matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", m, matOfByte);
		byteArray = matOfByte.toArray();
		bufImage = null;
		in = new ByteArrayInputStream(byteArray);
		try {
			bufImage = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufImage;

	}
}
