package visionUtility;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
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
import java.util.List;

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

public class VisionUtility implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	// public static VideoCapture video;
	public static Mat mat;

	public static ThresholdWindows thresholdWindows = null;

	public static SelectOpsWindow operationsWindow;

	public static Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

	public static ArrayList<MatOfPoint> arrayOfPoints;

	public static void main(String[] args) throws InterruptedException, IOException
	{
		// Load the main OpenCV libraries
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		mat = null;
		Mat alteredMat = new Mat();
		Mat displayMat = new Mat();

		imShow(ImShowVal.Start, null);
		arrayOfPoints = new ArrayList<MatOfPoint>();
		operationsWindow = new SelectOpsWindow();
		operationsWindow.setVisible(true);
		Thread.sleep(5000);

		ArrayList<int[]> config = new ArrayList<int[]>();

		boolean hasThreshold;
		while (true)
		{
			hasThreshold = false;
			config = operationsWindow.operations;
			if (config != null && mat != null && !mat.empty())

			{
				double ratio = mat.width() / (double) mat.height();
				Imgproc.resize(mat, mat, new Size(320, (1 / ratio) * 320));
				arrayOfPoints.clear();
				alteredMat = mat.clone();
				displayMat = mat.clone();
				for (int i = 0; i < config.size(); i++)
				{
					if (config.get(i)[0] == 3)
					{
						hasThreshold = true;
						alteredMat = threshold(alteredMat,
								new Scalar(config.get(i)[1], config.get(i)[2], config.get(i)[3]),
								new Scalar(config.get(i)[4], config.get(i)[5], config.get(i)[6]),
								new Scalar(config.get(i)[7], config.get(i)[7], config.get(i)[7]), config.get(i)[8]);

					} else if (config.get(i)[0] == 2)
					{
						alteredMat = erode(alteredMat, config.get(i)[1], config.get(i)[2]);
					} else if (config.get(i)[0] == 1)
					{
						alteredMat = dilate(alteredMat, config.get(i)[1], config.get(i)[2]);
					} else if (config.get(i)[0] == 4)
					{
						alteredMat = removeSmallObjects(alteredMat, config.get(i)[1]);
					}
				}
				if (hasThreshold == true)
				{
					Imgproc.findContours(alteredMat, arrayOfPoints, new Mat(), Imgproc.RETR_LIST,
							Imgproc.CHAIN_APPROX_SIMPLE);
					if (operationsWindow.chckbxOverlayImage.isSelected())
						Imgproc.drawContours(displayMat, arrayOfPoints, -1, new Scalar(0, 0, 255), -1);
					else
						Imgproc.drawContours(alteredMat, arrayOfPoints, -1, new Scalar(255,255,255,255), -1);

				}
				hasThreshold = false;

				if (operationsWindow.chckbxOverlayImage.isSelected())
					imShow(ImShowVal.Refresh, convertToImage(displayMat));
				else
					imShow(ImShowVal.Refresh, convertToImage(alteredMat));
			}
			// Determines the FPS value
			Thread.sleep(33);
		}

		//
	}

	public static ArrayList<int[]> toRects(ArrayList<MatOfPoint> contours)
	{
		ArrayList<int[]> output = new ArrayList<int[]>();

		for (int i = 0; i < contours.size(); i++)
		{
			Rect rect = Imgproc.boundingRect(contours.get(i));
			output.add(new int[]
			{ rect.x, rect.y, rect.width, rect.height });
		}

		return output;
	}
	
	public static ArrayList<int[]> sortRects(ArrayList<int[]> list)
	{
		ArrayList<int[]> sortedOutput = new ArrayList<int[]>();

		while (true)
		{
			if (list.isEmpty())
				break;
			int tempIndex = 0;
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i)[2] * list.get(i)[3] < list.get(tempIndex)[2] * list.get(tempIndex)[3])
				{
					tempIndex = i;
				}
			}
			sortedOutput.add(list.get(tempIndex));
			list.remove(tempIndex);
		}
		return sortedOutput;
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
			frame.setBounds((int) Math.round(0.32421875 * dimension.getWidth()),
					(int) Math.round(0.146484375 * dimension.getHeight()), 400, 300);

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

	/**
	 * Dilates the blobs (all blobs grow)
	 * @param m input matrix (image)
	 * @param size how big the blobs should be dilated
	 * @param iterations how many times the blobs should be dilated
	 * @return the matrix after dilating the blobs
	 */
	private static Mat dilate(Mat m, int size, int iterations)
	{
		Mat alteredMat = new Mat();
		alteredMat = m.clone();
		if (size < 1)
			return alteredMat;

		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(size, size));
		Imgproc.dilate(alteredMat, alteredMat, element, new Point(-1, -1), iterations);
		return alteredMat;
	}

	/**
	 * Erodes all blobs (all blobs shrink)
	 * @param m input matrix (image)
	 * @param size how small the blobs should shrink
	 * @param iterations how many times the blobs should be eroded
	 * @return the matrix after eroding the blobs
	 */
	private static Mat erode(Mat m, int size, int iterations)
	{
		Mat alteredMat = new Mat();
		alteredMat = m.clone();
		if (size < 1)
			return alteredMat;
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(size, size));
		Imgproc.erode(alteredMat, alteredMat, element, new Point(-1, -1), iterations);
		return alteredMat;
	}

	/**
	 * Thresholds the image based on the scalar values
	 * @param m input matrix, or image
	 * @param upperBound the scalar for the upper boundary BGR values
	 * @param lowerBound the scalar for the loser boundary BGR values
	 * @param brightness the scalar that is added to the matrix before thresholding.
	 * 			if the brightness is negative, that amount will be subtracted, therefore
	 * 			lowering the brightness.
	 * @return the matrix after thresholding the blobs
	 */
	private static Mat threshold(Mat m, Scalar lowerBound, Scalar upperBound, Scalar brightness, int colorCode)
	{
		Mat alteredMat = new Mat();
		alteredMat = m.clone();
		Core.add(alteredMat, brightness, alteredMat);
		if (colorCode == 1)
			Imgproc.cvtColor(alteredMat, alteredMat, Imgproc.COLOR_BGR2HSV);
		Core.inRange(alteredMat, lowerBound, upperBound, alteredMat);
		return alteredMat;
	}

	/** @WIP THIS MAY OR MAY NOT WORK RIGHT NOW!
	 * This is to remove small blobs that we don't want to see, without affecting size of other blobs.
	 * @param m input matrix (image)
	 * @param size how large the blobs are that you want to remove
	 * @param iterations how many times you want to run it
	 * @return matrix (image) after removing the objects
	 */
	@SuppressWarnings("unused")
	private static Mat removeSmallObjects(Mat m, int size)
	{
		Mat alteredMat = m.clone();
		if (size < 1)
			return alteredMat;
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(alteredMat, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		Mat newAlteredMat = Mat.zeros(alteredMat.rows(), alteredMat.cols(), alteredMat.type());
		ArrayList<int[]> rects = toRects(contours);
		for (int i = 0; i < contours.size(); i++)
		{
			if (rects.get(i)[2] * rects.get(i)[3] > (size * 20))
			{
				Imgproc.drawContours(newAlteredMat, contours, i, new Scalar(255,255,255,255), Core.FILLED);
			}
		}
		return newAlteredMat;
	}
}
