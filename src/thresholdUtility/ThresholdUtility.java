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

public class ThresholdUtility implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// public static VideoCapture video;
	public static Mat mat;

	public static void main(String[] args) throws InterruptedException, IOException {
		// Load the main OpenCV libraries
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		mat = null;
		Mat alteredMat = new Mat();
		
		OperationsWindow operationsWindow = new OperationsWindow();
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
		BackgroundProcesses b = new BackgroundProcesses();
		b.start();

		while (true) {
			arrayOfPoints.clear();
			brightnessConstant = ((int) ThresholdWindows.brightnessSpinner.getValue());
			brightnessScalar = new Scalar(brightnessConstant, brightnessConstant, brightnessConstant);
			// set the lower bound scalar to the sliders in the Lower Bound
			// frame
			lowerBoundScalar = new Scalar((int) ThresholdWindows.blueSpinnerLowerBound.getValue(),
					(int) ThresholdWindows.greenSpinnerLowerBound.getValue(), (int) ThresholdWindows.redSpinnerLowerBound.getValue());
			// set the upper bound scalar to the sliders in the Upper Bound
			// frame
			upperBoundScalar = new Scalar((int) ThresholdWindows.blueSpinnerUpperBound.getValue(),
					(int) ThresholdWindows.greenSpinnerUpperBound.getValue(), (int) ThresholdWindows.redSpinnerUpperBound.getValue());
			// if (video.isOpened()) {
			// video.retrieve(mat);
			// Imgproc.resize(mat, mat, new Size(400, 300));
			// alteredMat = mat.clone();
			// Core.add(alteredMat, brightnessScalar, alteredMat);
			// // Threshold based on the scalar values declared
			// Core.inRange(alteredMat, lowerBoundScalar,
			// upperBoundScalar, alteredMat);
			// Imgproc.findContours(alteredMat, arrayOfPoints,
			// hierarchy, Imgproc.RETR_TREE,
			// Imgproc.CHAIN_APPROX_SIMPLE);
			// Imgproc.drawContours(alteredMat, arrayOfPoints, -1, new
			// Scalar(255, 0, 0, 255), Imgproc.RETR_FLOODFILL);
			// // Refresh the frame
			// imShow(ImShowVal.Refresh, convertToImage(alteredMat));
			// } else {
			if (mat != null) {
				if (mat.size().width > 320) {
					Imgproc.resize(mat, mat, new Size(320, 240));
				}
				alteredMat = mat.clone();
				Core.add(alteredMat, brightnessScalar, alteredMat);
				// Threshold based on the scalar values declared
				Core.inRange(alteredMat, lowerBoundScalar, upperBoundScalar, alteredMat);
				imShow(ImShowVal.Refresh, convertToImage(alteredMat));
				// }
			}

			// Determines the FPS value
			Thread.sleep(33);
			if (BackgroundProcesses.exitProgram)
				break;
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

	public static enum ImShowVal {
		Refresh, Start
	}

	public static enum updateFrameVal {
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
	public static void imShow(ImShowVal val, BufferedImage i) {
		switch (val) {
		case Start:
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			menu = new JMenuBar();
			frame.setJMenuBar(menu);
			imageMenu = new JMenu("Image");
			menu.add(imageMenu);
			openImage = new JMenuItem("Open Image");

			imageMenu.add(openImage);
			openImage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// video.release();
					mat = openImage();
				}
			});

			file = new JMenu("File");
			menu.add(file);

			saveConfig = new JMenuItem("Save Configuration");
			file.add(saveConfig);
			saveConfig.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						saveConfig();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});

			openConfig = new JMenuItem("Open Configuration");
			file.add(openConfig);
			openConfig.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						openConfig();
					} catch (ClassNotFoundException | IOException e1) {
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
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					BackgroundProcesses.exitProgram = true;
				}
			});
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

	private static Mat openImage() {
		JFileChooser fileChooser = new JFileChooser();

		int rVal = fileChooser.showOpenDialog(fileChooser);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			saveName = fileChooser.getSelectedFile().getAbsolutePath();
			if (!saveName.toLowerCase().contains(".jpg"))
				saveName = saveName.concat(".jpg");
			File file = new File(saveName);
			if (!file.exists()) {
				JOptionPane.showMessageDialog(fileChooser, "The File Does Not Exist!");
				openImage();
			} else {
				Mat m = Highgui.imread(saveName);
				return m;
			}
		}
		return null;
	}

	private static ObjectOutputStream oos;
	private static FileOutputStream fos;

	private static void saveConfig() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Configuration Files", "cfg");
		fileChooser.setFileFilter(filter);

		int rVal = fileChooser.showSaveDialog(fileChooser);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			saveName = fileChooser.getSelectedFile().getAbsolutePath();
			System.out.println(saveName);
			if (!saveName.toLowerCase().contains(".cfg"))
				saveName = saveName.concat(".cfg");

			File file = new File(saveName);
			if (!file.exists()) {
				int[] config = { (int) ThresholdWindows.blueSpinnerLowerBound.getValue(), (int) ThresholdWindows.greenSpinnerLowerBound.getValue(),
						(int) ThresholdWindows.redSpinnerLowerBound.getValue(), (int) ThresholdWindows.blueSpinnerUpperBound.getValue(),
						(int) ThresholdWindows.greenSpinnerUpperBound.getValue(), (int) ThresholdWindows.redSpinnerUpperBound.getValue(),
						(int) ThresholdWindows.brightnessSpinner.getValue() };
				fos = new FileOutputStream(saveName);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(config);
				oos.flush();
				oos.close();
			} else {
				int result = JOptionPane.showConfirmDialog(fileChooser, "File Already Exists. Overwrite?");
				switch (result) {
				case JOptionPane.YES_OPTION:
					int[] config = { (int) ThresholdWindows.redSpinnerLowerBound.getValue(), (int) ThresholdWindows.greenSpinnerLowerBound.getValue(),
							(int) ThresholdWindows.blueSpinnerLowerBound.getValue(), (int) ThresholdWindows.redSpinnerUpperBound.getValue(),
							(int) ThresholdWindows.greenSpinnerUpperBound.getValue(), (int) ThresholdWindows.blueSpinnerUpperBound.getValue(),
							(int) ThresholdWindows.brightnessSpinner.getValue() };
					fos = new FileOutputStream(saveName);
					oos = new ObjectOutputStream(fos);
					oos.writeObject(config);
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

	private static void openConfig() throws IOException, ClassNotFoundException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				if (arg0.isDirectory())
					return true;
				else {
					String fileName = arg0.getName().toLowerCase();
					return fileName.endsWith(".cfg");
				}
			}

			@Override
			public String getDescription() {

				return "Config files (*.cfg)";
			}

		});
		int rVal = fileChooser.showOpenDialog(fileChooser);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			saveName = fileChooser.getSelectedFile().getAbsolutePath();
			if (!saveName.toLowerCase().contains(".cfg"))
				saveName = saveName.concat(".cfg");
			File file = new File(saveName);
			if (!file.exists()) {
				JOptionPane.showMessageDialog(fileChooser, "The File Does Not Exist!");
				openImage();
			} else {
				fis = new FileInputStream(saveName);
				ois = new ObjectInputStream(fis);
				int[] config = (int[]) ois.readObject();
				ThresholdWindows.blueSpinnerLowerBound.setValue(config[0]);
				ThresholdWindows.greenSpinnerLowerBound.setValue(config[1]);
				ThresholdWindows.redSpinnerLowerBound.setValue(config[2]);
				ThresholdWindows.blueSpinnerUpperBound.setValue(config[3]);
				ThresholdWindows.greenSpinnerUpperBound.setValue(config[4]);
				ThresholdWindows.redSpinnerUpperBound.setValue(config[5]);
				ThresholdWindows.brightnessSpinner.setValue(config[6]);
			}
		}
	}

	/**
	 * 
	 * Conatains mundane tasks
	 *
	 */
	public static class BackgroundProcesses extends Thread {

		public static boolean exitProgram = false;

		public void run() {
			ThresholdWindows.redSliderLowerBound.setValue(0);
			ThresholdWindows.greenSliderLowerBound.setValue(0);
			ThresholdWindows.blueSliderLowerBound.setValue(0);
			ThresholdWindows.redSliderUpperBound.setValue(0);
			ThresholdWindows.greenSliderUpperBound.setValue(0);
			ThresholdWindows.blueSliderUpperBound.setValue(0);

			while (!exitProgram) {

				if (ThresholdWindows.redSliderLowerBound.getValueIsAdjusting())
					ThresholdWindows.redSpinnerLowerBound.setValue((int) Math.round(ThresholdWindows.redSliderLowerBound.getValue() * 2.55));
				if (ThresholdWindows.greenSliderLowerBound.getValueIsAdjusting())
					ThresholdWindows.greenSpinnerLowerBound.setValue((int) Math.round(ThresholdWindows.greenSliderLowerBound.getValue() * 2.55));
				if (ThresholdWindows.blueSliderLowerBound.getValueIsAdjusting())
					ThresholdWindows.blueSpinnerLowerBound.setValue((int) Math.round(ThresholdWindows.blueSliderLowerBound.getValue() * 2.55));
				if (ThresholdWindows.redSliderUpperBound.getValueIsAdjusting())
					ThresholdWindows.redSpinnerUpperBound.setValue((int) Math.round(ThresholdWindows.redSliderUpperBound.getValue() * 2.55));
				if (ThresholdWindows.greenSliderUpperBound.getValueIsAdjusting())
					ThresholdWindows.greenSpinnerUpperBound.setValue((int) Math.round(ThresholdWindows.greenSliderUpperBound.getValue() * 2.55));
				if (ThresholdWindows.blueSliderUpperBound.getValueIsAdjusting())
					ThresholdWindows.blueSpinnerUpperBound.setValue((int) Math.round(ThresholdWindows.blueSliderUpperBound.getValue() * 2.55));
				if (ThresholdWindows.brightnessSlider.getValueIsAdjusting())
					ThresholdWindows.brightnessSpinner.setValue((int) Math.round((ThresholdWindows.brightnessSlider.getValue() - 50) * 2.55));

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
