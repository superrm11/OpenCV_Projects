package visionUtility;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
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
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class VisionUtility implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	// The main original image input from the source
	public static Mat originalMat = null;

	// The capture device used to get the original image, mat
	public static VideoCapture captureDevice;

	// True if the program is using a camera as a source rather than an imported
	// image
	public static boolean isUsingCamera = false;

	// The lower window where the user chooses which operations to go where
	public static SelectOpsWindow operationsWindow;

	// Gets the dimensions of the main monitor for using relative window scaling
	// (Relative coordinates * horizontal or vertical resolution)
	public static Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

	// The output from the main "process image" function (findContours)
	public static ArrayList<MatOfPoint> arrayOfPoints;

	public static void main(String[] args) throws InterruptedException, IOException
	{
		// Load the main OpenCV libraries
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		originalMat = new Mat();

		// The image after the original image gets altered by the user's
		// operations
		Mat alteredMat = new Mat();

		// The final image that gets displayed, in it's raw pixel form
		Mat displayMat = new Mat();

		captureDevice = new VideoCapture();

		// Begin setting up the main display window
		imShow(ImShowVal.Start, null);

		arrayOfPoints = new ArrayList<MatOfPoint>();
		operationsWindow = new SelectOpsWindow();
		operationsWindow.setVisible(true);
		Thread.sleep(5000);

		ArrayList<int[]> config = new ArrayList<int[]>(); // The operation list
															// input from the
															// lower window

		boolean hasThreshold = false; // Makes sure we are actually using a
										// binary (thresholded) image before
										// finding the contours.
		while (true)
		{
			hasThreshold = false;// Reset hasThreshold to make sure the second
									// run does not cause an error

			if (isUsingCamera == true && captureDevice != null && captureDevice.isOpened())
				captureDevice.read(originalMat);

			config = operationsWindow.operations; // Importing the lower
													// window's operations
			// ONLY start processing if there is an image and operations are
			// present.
			if (config != null && originalMat != null && !originalMat.empty())
			{
				// Gets the aspect ration of the original image and resizes it
				// so that the width is always 320 pixels wide (good for lower
				// resolution monitors)
				double ratio = originalMat.width() / (double) originalMat.height();
				Imgproc.resize(originalMat, originalMat, new Size(480, (1 / ratio) * 480));

				arrayOfPoints.clear();// Get the images and points ready for
				alteredMat = originalMat.clone();// processing
				displayMat = originalMat.clone();

				// Run through each operation present and execute it on the
				// altered image
				for (int i = 0; i < config.size(); i++)
				{
					// identifier for thresholding is 3
					if (config.get(i)[0] == 3)
					{
						hasThreshold = true;
						alteredMat = threshold(alteredMat,
								new Scalar(config.get(i)[1], config.get(i)[2], config.get(i)[3]),
								new Scalar(config.get(i)[4], config.get(i)[5], config.get(i)[6]),
								new Scalar(config.get(i)[7], config.get(i)[7], config.get(i)[7]), config.get(i)[8]);

					} else if (config.get(i)[0] == 2)// Identifier for erode
														// operation is 2
					{
						alteredMat = erode(alteredMat, config.get(i)[1], config.get(i)[2]);
					} else if (config.get(i)[0] == 1)// Identifier for dilate
														// operation is 1
					{
						alteredMat = dilate(alteredMat, config.get(i)[1], config.get(i)[2]);
					} else if (config.get(i)[0] == 4)// Identifier for removing
														// small particles is 4
					{
						alteredMat = removeSmallObjects(alteredMat, config.get(i)[1]);
					}
				}
				// Will only find the contours and draw them on the final image
				// if the threshold operation has been performed
				if (hasThreshold == true)
				{
					Imgproc.findContours(alteredMat, arrayOfPoints, new Mat(), Imgproc.RETR_LIST,
							Imgproc.CHAIN_APPROX_SIMPLE);
					if (operationsWindow.chckbxOverlayImage.isSelected())
						// Draws the contours on the displayMat (which already
						// contains the original image)
						Imgproc.drawContours(displayMat, arrayOfPoints, -1, new Scalar(0, 0, 255), -1);
					else
						// Draws the contours on the alteredMat (which at this
						// point is a binary image)
						Imgproc.drawContours(alteredMat, arrayOfPoints, -1, new Scalar(255, 255, 255, 255), -1);

				}

				// If the user checks the box to overlay the image, then display
				// the displayImage
				// else, just display the altered image
				if (operationsWindow.chckbxOverlayImage.isSelected())
					imShow(ImShowVal.Refresh, convertToImage(displayMat));
				else
					imShow(ImShowVal.Refresh, convertToImage(alteredMat));
			}
			// Determines the FPS value. FPS = 1 / (millis / 1000)
			// To determine what millisecond value to set for a certain FPS, use
			// millis = (1 / FPS) * 1000
			Thread.sleep(33);// About 30 FPS
		}

	}

	/**
	 * Converts the default output from the findContours method (ArrayList of MatOfPoint) to 
	 * Java's rectangle object, in which the coordinates of the upper left corner and length and width
	 * are stored in an integer array.
	 * @param contours The input ArrayList from the findContours method
	 * @return the ArrayList of integer arrays containing rectangle information.
	 */
	public static ArrayList<int[]> toRects(ArrayList<MatOfPoint> contours)
	{
		ArrayList<int[]> output = new ArrayList<int[]>();

		for (int i = 0; i < contours.size(); i++)
		// Loops through each contour and converts it to a rectangle
		{
			Rect rect = Imgproc.boundingRect(contours.get(i));
			output.add(new int[]
			{ rect.x, rect.y, rect.width, rect.height });
		}

		return output;
	}

	/**
	 * Sorts the raw rectangle information from each given blob in descending order by area (largest is index 0)
	 * @param list the input unsorted blob information
	 * @return the sorted blob information
	 */
	public static ArrayList<int[]> sortRects(ArrayList<int[]> list)
	{
		ArrayList<int[]> sortedOutput = new ArrayList<int[]>();
		ArrayList<int[]> original = (ArrayList<int[]>) list.clone();

		while (true)
		{
			if (original.isEmpty())
				break;
			// end the loop and continue once all the values have been moved
			// over.
			int tempIndex = 0;// the current largest blob's index
			// Find the next largest
			for (int i = 0; i < original.size(); i++)
			{
				if (original.get(i)[2] * original.get(i)[3] < original.get(tempIndex)[2] * original.get(tempIndex)[3])
				{
					tempIndex = i;
				}
			}
			sortedOutput.add(list.remove(tempIndex));
		}
		return sortedOutput;
	}

	private static MatOfByte matOfByte;
	private static byte[] byteArray;
	private static InputStream in;

	/**
	 * Converts a Matrix 'Mat' into a buffered image for viewing
	 * 
	 * @param m the input matrix
	 * @return Java's default buffered image
	 */
	private static BufferedImage convertToImage(Mat m)
	{
		matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", m, matOfByte);// turn the matrix (of pixel
		byteArray = matOfByte.toArray(); // data) into a matrix of bytes
		in = new ByteArrayInputStream(byteArray);

		try
		{
			return ImageIO.read(in);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Defines states of the imShow method.
	 * @author Ryan McGee
	 *
	 */
	public static enum ImShowVal
	{
		Refresh, Start
	}

	/**
	 * Defines states for whether the input image comes from a camera or an imported image.
	 * @author Ryan McGee
	 *
	 */
	public static enum updateFrameVal
	{
		Image, Camera
	}

	private static JLabel label;
	private static ImageIcon icon;
	private static JFrame frame;
	private static JMenuBar menu;
	private static JMenu imageMenu, file;
	private static JMenuItem openImage, saveConfig, openConfig, openCamera, changeSettings;

	private static CameraSelectWindow cameraSelect = new CameraSelectWindow();

	/**
	 * Starts or refreshes the main frame showing the altered video
	 * 
	 * @param val
	 *            States whether the window is being refreshed or started from
	 *            scratch
	 * @param i
	 *            The image that is to be displayed
	 */
	public static void imShow(ImShowVal val, BufferedImage i)
	{
		switch (val)
		{
		case Start:
			// Start the frame
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// Start setting up the menu bar and the options
			menu = new JMenuBar();
			frame.setJMenuBar(menu);

			imageMenu = new JMenu("Image");
			menu.add(imageMenu);

			openCamera = new JMenuItem("Open Camera");
			imageMenu.add(openCamera);
			openCamera.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					cameraSelect.displayWindow();
					isUsingCamera = true;
				}
			});

			openImage = new JMenuItem("Open Image");

			imageMenu.add(openImage);
			openImage.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					// video.release();
					isUsingCamera = false;
					originalMat = openImage();
				}
			});

			// File menu option
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
						e1.printStackTrace();
					}
				}

			});
			changeSettings = new JMenuItem("USB Camera Settings");
			file.add(changeSettings);
			changeSettings.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (captureDevice.isOpened() == false)
						JOptionPane.showMessageDialog(null, "Please open a camera first, and retry.");
					else if (cameraSelect.isUsingUsbCam() == true)
						captureDevice.set(Highgui.CV_CAP_PROP_SETTINGS, 1);
					else
						JOptionPane.showMessageDialog(null,
								"Settings for IP cameras must be changed through the web interface.");
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
			// remove the current image displayed and replace it with the new
			// one
			// The bufferedImage is contained in a JLabel and an imageIcon
			frame.getContentPane().remove(label);
			icon.setImage(i);
			label = new JLabel(icon);
			frame.getContentPane().add(label);
			frame.pack();
			break;
		}

	}

	public static String saveName;

	/**
	 * Opens a generic open dialog and prompts for a name / lets users choose an image file from the file system.
	 * This function will recursively call itself until either the user chooses a valid image or cancels.
	 * @return the raw matrix image file chosen.
	 */
	private static Mat openImage()
	{
		JFileChooser fileChooser = new JFileChooser();
		// display the dialog; rVal is the option that the user chose: ex. yes /
		// no / cancel. This will block the code until the user chooses an
		// option.
		int rVal = fileChooser.showOpenDialog(fileChooser);
		if (rVal == JFileChooser.APPROVE_OPTION)
		{
			// IF the user chose open, it gets the file given the absolute path.
			saveName = fileChooser.getSelectedFile().getAbsolutePath();

			// If the user did not specify the file type to be a .jpg, then add
			// it on.
			if (!saveName.toLowerCase().contains(".jpg"))
				saveName = saveName.concat(".jpg");

			File file = new File(saveName);
			if (!file.exists())// IF it does not exist, then say so. and restart
								// the method
			{
				JOptionPane.showMessageDialog(fileChooser, "The File Does Not Exist!");
				openImage();
			} else
			{
				// If the file DOES exist, then open it and return that image.
				Mat m = Highgui.imread(saveName);
				if (captureDevice != null && captureDevice.isOpened())
					captureDevice.release();
				// If the camera was previously used, release it and specify
				// that it
				// will not be used from now on.
				isUsingCamera = false;
				return m;
			}
		}
		return null;
		// If, for some reason, none of this happens, just return null.
	}

	private static ObjectOutputStream oos;
	private static FileOutputStream fos;

	/**
	 * Opens a generic save window that allows the user to save a given configuration to a file for later use.
	 * @throws IOException
	 */
	private static void saveConfig() throws IOException
	{
		JFileChooser fileChooser = new JFileChooser();
		// Set the filter for the file extension to .cfg and name it
		// Configuration Files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Configuration Files", "cfg");
		fileChooser.setFileFilter(filter);

		int rVal = fileChooser.showSaveDialog(fileChooser);
		// Show the window and store what the user chose
		if (rVal == JFileChooser.APPROVE_OPTION)
		{
			// get the path of the file
			saveName = fileChooser.getSelectedFile().getAbsolutePath();
			System.out.println(saveName);
			// If the name does not already have the file extension, then add it
			// on.
			if (!saveName.toLowerCase().contains(".cfg"))
				saveName = saveName.concat(".cfg");
			// Save the file using the objectOutputStream. If it exists, then
			// ask to overwrite.
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

	/**
	 * Opens a generic open file window that allows the user to choose a configuration previously saved.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static void openConfig() throws IOException, ClassNotFoundException
	{
		// Set the file extension filter and open the window.
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File arg0)
			// Only display directories and files that have the extension .cfg
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
		// display the window and store the values of the user's choice
		int rVal = fileChooser.showOpenDialog(fileChooser);
		if (rVal == JFileChooser.APPROVE_OPTION)
		{
			// Get the path and save the files
			saveName = fileChooser.getSelectedFile().getAbsolutePath();
			if (!saveName.toLowerCase().contains(".cfg"))
				saveName = saveName.concat(".cfg");
			File file = new File(saveName);
			if (!file.exists())
			{
				// If it does not exist, say so and restart the method
				JOptionPane.showMessageDialog(fileChooser, "The File Does Not Exist!");
				openImage();
			} else
			{
				// Open the configuration
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
				Imgproc.drawContours(newAlteredMat, contours, i, new Scalar(255, 255, 255, 255), Core.FILLED);
			}
		}
		return newAlteredMat;
	}

	public static void startVideoCapture(String ip)
	{
		captureDevice = new VideoCapture(ip);
	}

	public static void startVideoCapture(int deviceNum)
	{
		captureDevice = new VideoCapture(deviceNum);
	}
}
