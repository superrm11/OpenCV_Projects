package cameraSettings;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import cameraSettings.ChangeCameraSettings.Resolution;

public class ViewWindow extends JFrame
{

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ViewWindow(Resolution res)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.addWindowListener(new WindowListener()
		{

			@Override
			public void windowOpened(WindowEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e)
			{
				isClosing = true;
				dispose();
			}

			@Override
			public void windowClosed(WindowEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e)
			{
				// TODO Auto-generated method stub

			}

		});
		this.setResolution(res);
	}

	private boolean isClosing = false;

	public boolean isClosing()
	{
		return isClosing;
	}

	public void refreshWindow(Mat m)
	{
		ImageIcon image = new ImageIcon(convertToImage(m));

	}

	/**
	 * Converts a Matrix 'Mat' into a buffered image for viewing
	 * 
	 * @param m
	 * @return
	 */
	private static BufferedImage convertToImage(Mat m)
	{

		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", m, matOfByte);
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

	private Resolution viewRes = Resolution.k640x480;

	public void setResolution(Resolution res)
	{
		this.viewRes = res;
	}

}
