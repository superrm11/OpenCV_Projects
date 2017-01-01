package camera_test;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class PiCameraClient
{

	public static void main(String[] args) throws UnknownHostException, IOException
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Socket s = new Socket("192.168.1.13", 2000);
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
		oos.flush();
		Mat m = new Mat();
		MatOfByte matOfByte = new MatOfByte();
		byte[] toSend;
		VideoCapture cap = new VideoCapture();
		cap.open(0);
		while(true)
		{
//			m = Highgui.imread("/home/pi/pic.jpeg");
			cap.read(m);
			Highgui.imencode(".jpg", m, matOfByte);
			toSend = matOfByte.toArray();
			
			oos.writeObject(toSend);
			
			oos.flush();
		}
	
	}
	
	private static MatOfByte matOfByte;
	private static byte[] byteArray;
	private static BufferedImage bufImage;
	private static ByteArrayInputStream in;
	
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
