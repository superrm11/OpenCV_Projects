package camera_test;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import visionUtility.VisionUtility.ImShowVal;

public class PiCameraTest
{

	public static void main(String[] args) throws ClassNotFoundException
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try
		{
			ServerSocket listener = new ServerSocket(2000);
			Socket socket = listener.accept();
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			
			File f;
			Mat m;
			byte[] received;
			MatOfByte matOfByte = new MatOfByte();
			BufferedImage im = null;
			ByteArrayInputStream bais;
		
			imShow(ImShowVal.Start, null);
			while(true)
			{
				received = (byte[]) ois.readObject();
				bais = new ByteArrayInputStream(received);
				im = ImageIO.read(bais);
				imShow(ImShowVal.Refresh, im);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
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

	
	public static enum ImShowVal
	{
		Refresh, Start
	}
	public static JLabel label;
	public static ImageIcon icon;
	public static JFrame frame;

	static Dimension dimension= Toolkit.getDefaultToolkit().getScreenSize();
	/**
	 * Starts or refreshes the main frame showing the altered video
	 * 
	 * @param val
	 *            States whether the window is being refreshed or started from
	 *            scratch
	 * @param i
	 *            The image that is to be displayed
	 * @throws Exception
	 */
	public static void imShow(ImShowVal val, BufferedImage i)
	{
		switch (val)
		{
		case Start:
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			icon = new ImageIcon();
			label = new JLabel(icon);
			frame.getContentPane().add(label);
			frame.pack();
			frame.setVisible(true);
			frame.setBounds(0, 0, 1920, 1080);

			break;
		case Refresh:
			frame.getContentPane().remove(label);
			icon.setImage(i);
			label = new JLabel(icon);
			frame.getContentPane().add(label);
			frame.pack();
			break;
		}

	}
}
