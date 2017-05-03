import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class Test_Client implements java.io.Serializable
{
	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException
	{

		ProcessBuilder pb = new ProcessBuilder("opencv_createsamples",
				" -vec \"C:/Users/Ryan McGee/Desktop/cascade_stuff/data.vec\"");
		System.out.println(output(pb.start().getInputStream()));
		// System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// VideoCapture cap = new VideoCapture();
		// cap.open(0);
		// Mat m = new Mat();
		// cap.retrieve(m);
		// Core.add(m, new Scalar(-33, -33, -33), m);
		// Core.inRange(m, new Scalar(0, 22, 0), new Scalar(31, 107, 4), m);
		// Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new
		// Size(11, 11));
		// Imgproc.dilate(m, m, element, new Point(-1, -1), 1);
		// ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		// Imgproc.findContours(m, contours, new Mat(), Imgproc.RETR_LIST,
		// Imgproc.CHAIN_APPROX_SIMPLE);
		// Imgproc.drawContours(m, contours, -1, new Scalar(200, 0, 0),
		// Core.FILLED);
		// MatOfPoint points = new MatOfPoint(contours.get(0).toArray());
		// Rect rect = Imgproc.boundingRect(points);
		//
		// Core.rectangle(m, new Point(rect.x, rect.y), new Point(rect.x +
		// rect.width, rect.y + rect.height),
		// new Scalar(200, 0, 0));
		//
		// JFrame frame = new JFrame();
		// imageIcon = new ImageIcon(convertToImage(m));
		// label = new JLabel(imageIcon);
		// frame.getContentPane().add(label);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.pack();
		// frame.setVisible(true);
		// cap.release();
	}

	private static String output(InputStream is) throws IOException
	{
		String line = null;
		String out = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try
		{
			while ((line = br.readLine()) != null)
			{
				out += line + "\n";
			}
		} finally
		{
			br.close();
		}
		return out;

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
