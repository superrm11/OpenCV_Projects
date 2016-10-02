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

public class Test_Client implements java.io.Serializable{
	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		long len;
		Mat mat = new Mat();
		BufferedImage bufImage;
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		imageIcon = new ImageIcon();
		label = new JLabel(imageIcon);
		frame.getContentPane().add(label);
		frame.setVisible(true);
		
		ObjectInputStream ois;
		Socket socket = new Socket("localhost", 9090);
		ois = new ObjectInputStream(socket.getInputStream());

		while (true) {

			Thread.sleep(30);
		}
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
