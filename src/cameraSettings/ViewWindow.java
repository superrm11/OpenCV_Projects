package cameraSettings;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class ViewWindow extends JFrame
{

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ViewWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private boolean isClosing = false;

	public boolean isClosing()
	{
		return isClosing;
	}

	/**
	 * Converts a Matrix 'Mat' into a buffered image for viewing
	 * 
	 * @param m
	 * @return
	 */
	private BufferedImage convertToImage(Mat m)
	{

		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", m, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
		try
		{
			bufImage = ImageIO.read(in);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return bufImage;

	}

	public void refresh(Mat m)
	{
		ImageIcon image = new ImageIcon(convertToImage(m));
		JLabel label = new JLabel(image);
		this.getContentPane().removeAll();
		this.getContentPane().add(label);
		this.pack();

	}

}
