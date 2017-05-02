package haar_test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class HaarTest
{

	public static void main(String[] args)
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		VideoCapture cap = new VideoCapture(0);
		Mat m = new Mat();
		cap.read(m);

		Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2GRAY);
		// Imgproc.equalizeHist(m, m);

		JFrame frame = new JFrame();
		imageIcon = new ImageIcon(convertToImage(m));
		label = new JLabel(imageIcon);
		frame.getContentPane().add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		CascadeClassifier detector = new CascadeClassifier();
		if (!detector.load("C:/OpenCV/opencv/sources/data/lbpcascades/lbpcascade_frontalface.xml"))
			System.out.println("Failed to load!");
		MatOfRect rects = new MatOfRect();
		Rect[] newRects;

		// detector.detectMultiScale(m, rects);
		// newRects = rects.toArray();
		// System.out.println(newRects.length);
		//
		// for (int i = 0; i < newRects.length; i++)
		// {
		// Core.rectangle(m, new Point(newRects[i].x, newRects[i].y),
		// new Point(newRects[i].x + newRects[i].width, newRects[i].y +
		// newRects[i].height),
		// new Scalar(0, 255, 0));
		// }
		// frame.remove(label);
		// frame.getContentPane().add(new JLabel(new
		// ImageIcon(convertToImage(m))));
		// frame.pack();
		// cap.release();

		while (true)
		{
			cap.read(m);
			// Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2GRAY);
			// Imgproc.equalizeHist(m, m);
			detector.detectMultiScale(m, rects);
			newRects = rects.toArray();
			for (int i = 0; i < newRects.length; i++)
			{
				Core.rectangle(m, new Point(newRects[i].x, newRects[i].y),
						new Point(newRects[i].x + newRects[i].width, newRects[i].y + newRects[i].height),
						new Scalar(0, 255, 0));
			}
			frame.getContentPane().remove(label);
			imageIcon.setImage(convertToImage(m));
			label = new JLabel(imageIcon);
			frame.getContentPane().add(label);
			frame.pack();
		}

	}

	private static JLabel label;
	private static ImageIcon imageIcon;
	private static MatOfByte matOfByte;
	private static byte[] byteArray;
	private static BufferedImage bufImage;
	private static InputStream in;

	private static BufferedImage convertToImage(Mat m)
	{
		matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", m, matOfByte);
		byteArray = matOfByte.toArray();
		bufImage = null;
		in = new ByteArrayInputStream(byteArray);
		try
		{
			bufImage = ImageIO.read(in);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return bufImage;

	}

}
