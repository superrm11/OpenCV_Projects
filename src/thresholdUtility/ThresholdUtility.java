package thresholdUtility;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class ThresholdUtility implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	// public static VideoCapture video;
	public static Mat mat;

	public static ThresholdWindows thresholdWindows = null;

	public static SelectOpsWindow operationsWindow = new SelectOpsWindow();

	public static void main(String[] args) throws InterruptedException, IOException
	{
		// Load the main OpenCV libraries
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		mat = null;
		Mat alteredMat = new Mat();

		operationsWindow.setVisible(true);

		imShow(ImShowVal.Start, null);
		Scalar upperBoundScalar;
		Scalar lowerBoundScalar;
		Scalar brightnessScalar;
		ArrayList<MatOfPoint> arrayOfPoints = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();

		double brightnessConstant;
		// start the background processes with mundane tasks in a different
		// thread

		ArrayList<int[]> config = new ArrayList<int[]>();

		while (true)
		{
			config = operationsWindow.operations;
			if (config != null && mat != null && !mat.empty())
			{
				alteredMat = mat.clone();
				for (int i = 0; i < config.size(); i++)
				{
					if (config.get(i)[0] == 3)
					{
						Core.add(alteredMat, new Scalar(config.get(i)[7]), alteredMat);
						Core.inRange(alteredMat, new Scalar(config.get(i)[1], config.get(i)[2], config.get(i)[3]),
								new Scalar(config.get(i)[4], config.get(i)[5], config.get(i)[6]), alteredMat);
					} else if (config.get(i)[0] == 2)
					{
						Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(config.get(i)[1], config.get(i)[1]));
						Imgproc.erode(alteredMat, alteredMat, element, new Point(-1, -1), config.get(i)[2]);
					} else if (config.get(i)[0] == 1)
					{
						Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(config.get(i)[1], config.get(i)[1]));
						Imgproc.dilate(alteredMat, alteredMat, element, new Point(-1, -1), config.get(i)[2]);
					}
				}
				
				Imgproc.findContours(alteredMat, arrayOfPoints, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
				Imgproc.drawContours(alteredMat, arrayOfPoints, -1, new Scalar(255, 0 , 0), Core.FILLED);
				imShow(ImShowVal.Refresh, convertToImage(alteredMat));
			}
			// Determines the FPS value
			Thread.sleep(33);
		}

		//
	}

	private static MatOfByte matOfByte;
	private static byte[] byteArray;
	private static BufferedImage bufImage;
	private static InputStream in;

	/**
	 * Converts a Matrix 'Mat' into a buffered image for viewing
	 * 
	 * @param m
	 * @return
	 */
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

	public static enum updateFrameVal
	{
		Image, Camera
	}

	public static JLabel label;
	public static ImageIcon icon;
	public static JFrame frame;
	public static JMenuBar menu;
	public static JMenu imageMenu;
	public static JMenuItem openImage;
	public static JMenu file;
	public static JMenuItem saveConfig, openConfig;

	public static boolean isOpeningImage = false;
	public static boolean imageIsOpen = false;

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
			menu = new JMenuBar();
			frame.setJMenuBar(menu);
			imageMenu = new JMenu("Image");
			menu.add(imageMenu);
			openImage = new JMenuItem("Open Image");

			imageMenu.add(openImage);
			openImage.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					// video.release();
					mat = openImage();
				}
			});

			file = new JMenu("File");
			menu.add(file);

			saveConfig = new JMenuItem("Save Configuration");
			file.add(saveConfig);
			saveConfig.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					try
					{
						saveConfig();
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});

			openConfig = new JMenuItem("Open Configuration");
			file.add(openConfig);
			openConfig.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						openConfig();
					} catch (ClassNotFoundException | IOException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			});

			icon = new ImageIcon();
			label = new JLabel(icon);
			frame.getContentPane().add(label);
			frame.pack();
			frame.setVisible(true);
			frame.setBounds(600, 0, 400, 300);
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

	public static String saveName;

	private static Mat openImage()
	{
		JFileChooser fileChooser = new JFileChooser();

		int rVal = fileChooser.showOpenDialog(fileChooser);
		if (rVal == JFileChooser.APPROVE_OPTION)
		{
			saveName = fileChooser.getSelectedFile().getAbsolutePath();
			if (!saveName.toLowerCase().contains(".jpg"))
				saveName = saveName.concat(".jpg");
			File file = new File(saveName);
			if (!file.exists())
			{
				JOptionPane.showMessageDialog(fileChooser, "The File Does Not Exist!");
				openImage();
			} else
			{
				Mat m = Highgui.imread(saveName);
				return m;
			}
		}
		return null;
	}

	private static ObjectOutputStream oos;
	private static FileOutputStream fos;

	private static void saveConfig() throws IOException
	{
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Configuration Files", "cfg");
		fileChooser.setFileFilter(filter);

		int rVal = fileChooser.showSaveDialog(fileChooser);
		if (rVal == JFileChooser.APPROVE_OPTION)
		{
			saveName = fileChooser.getSelectedFile().getAbsolutePath();
			System.out.println(saveName);
			if (!saveName.toLowerCase().contains(".cfg"))
				saveName = saveName.concat(".cfg");

			File file = new File(saveName);
			if (!file.exists())
			{
				fos = new FileOutputStream(saveName);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(operationsWindow.operations);
				oos.flush();
				oos.close();
			} else
			{
				int result = JOptionPane.showConfirmDialog(fileChooser, "File Already Exists. Overwrite?");
				switch (result)
				{
				case JOptionPane.YES_OPTION:
					fos = new FileOutputStream(saveName);
					oos = new ObjectOutputStream(fos);
					oos.writeObject(operationsWindow.operations);
					oos.flush();
					oos.close();
					return;
				case JOptionPane.NO_OPTION:
					saveConfig();
					return;

				}
			}

		}
	}

	private static ObjectInputStream ois;
	private static FileInputStream fis;

	private static void openConfig() throws IOException, ClassNotFoundException
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter()
		{

			@Override
			public boolean accept(File arg0)
			{
				if (arg0.isDirectory())
					return true;
				else
				{
					String fileName = arg0.getName().toLowerCase();
					return fileName.endsWith(".cfg");
				}
			}

			@Override
			public String getDescription()
			{

				return "Config files (*.cfg)";
			}

		});
		int rVal = fileChooser.showOpenDialog(fileChooser);
		if (rVal == JFileChooser.APPROVE_OPTION)
		{
			saveName = fileChooser.getSelectedFile().getAbsolutePath();
			if (!saveName.toLowerCase().contains(".cfg"))
				saveName = saveName.concat(".cfg");
			File file = new File(saveName);
			if (!file.exists())
			{
				JOptionPane.showMessageDialog(fileChooser, "The File Does Not Exist!");
				openImage();
			} else
			{
				fis = new FileInputStream(saveName);
				ois = new ObjectInputStream(fis);
				ArrayList<int[]> config = (ArrayList<int[]>) ois.readObject();
				operationsWindow.setOperations(config);
				operationsWindow.operations = new ArrayList<int[]>(config);

			}
		}
	}
}
