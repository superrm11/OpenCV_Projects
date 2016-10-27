
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	public static VideoCapture video;
	public static Mat mat;

	public static void main(String[] args) throws InterruptedException, IOException {
		// Load the main OpenCV libraries
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		mat = new Mat();
		Mat alteredMat = new Mat();
//		video = new VideoCapture();
		// video.read(mat);
//		video.open(0);
		// open the default system camera dialogue
//		video.set(Highgui.CV_CAP_PROP_SETTINGS, 0);
		// create the frame with the upper bound values
		createSlideBarUpperBound();
		// create the frame with the lower bound values
		createSlideBarLowerBound();
		// start the main frame that will hold the video

		imShow(ImShowVal.Start, null);
		Scalar upperBoundScalar;
		Scalar lowerBoundScalar;
		Scalar brightnessScalar;
		ArrayList<MatOfPoint> arrayOfPoints = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		boolean videoIsOn = false;

		double brightnessConstant;
		// start the background processes with mundane tasks in a different
		// thread
		BackgroundProcesses b = new BackgroundProcesses();
		b.start();
		try {
			while (true) {
				arrayOfPoints.clear();
				brightnessConstant = ((int) brightnessSpinner.getValue());
				brightnessScalar = new Scalar(brightnessConstant, brightnessConstant, brightnessConstant);
				// set the lower bound scalar to the sliders in the Lower Bound
				// frame
				lowerBoundScalar = new Scalar((int) blueSpinnerLowerBound.getValue(),
						(int) greenSpinnerLowerBound.getValue(), (int) redSpinnerLowerBound.getValue());
				// set the upper bound scalar to the sliders in the Upper Bound
				// frame
				upperBoundScalar = new Scalar((int) blueSpinnerUpperBound.getValue(),
						(int) greenSpinnerUpperBound.getValue(), (int) redSpinnerUpperBound.getValue());
				try {
//					if (video.isOpened()) {
//						video.retrieve(mat);
//						Imgproc.resize(mat, mat, new Size(400, 300));
//						alteredMat = mat.clone();
//						Core.add(alteredMat, brightnessScalar, alteredMat);
//						// Threshold based on the scalar values declared
//						Core.inRange(alteredMat, lowerBoundScalar, upperBoundScalar, alteredMat);
//						Imgproc.findContours(alteredMat, arrayOfPoints, hierarchy, Imgproc.RETR_TREE,
//								Imgproc.CHAIN_APPROX_SIMPLE);
//						Imgproc.drawContours(alteredMat, arrayOfPoints, -1, new Scalar(255, 0, 0, 255), Imgproc.RETR_FLOODFILL);
//						// Refresh the frame
//						imShow(ImShowVal.Refresh, convertToImage(alteredMat));
//					} else {
						if (mat != null) {
							if (mat.size().width > 400) {
								Imgproc.resize(mat, mat, new Size(320, 240));
							}
							alteredMat = mat.clone();
							Core.add(alteredMat, brightnessScalar, alteredMat);
							// Threshold based on the scalar values declared
							Core.inRange(alteredMat, lowerBoundScalar, upperBoundScalar, alteredMat);
							imShow(ImShowVal.Refresh, convertToImage(alteredMat));
//						}
					}
				} catch (Exception e) {
					if(videoIsOn)
						e.printStackTrace();
				}

				// Determines the FPS value
				Thread.sleep(33);
				if (BackgroundProcesses.exitProgram)
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			video.release();
			System.exit(0);
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
	public static JMenu cameraMenu;
	public static JMenuItem openImage;
	public static JMenuItem camera1, camera2, camera3;
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
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			menu = new JMenuBar();
			frame.setJMenuBar(menu);
			imageMenu = new JMenu("Image");
			cameraMenu = new JMenu("Camera");
			menu.add(imageMenu);
			imageMenu.add(cameraMenu);
			openImage = new JMenuItem("Open Image");
			camera1 = new JMenuItem("camera 1 (default)");
			camera2 = new JMenuItem("camera 2");
			camera3 = new JMenuItem("camera 3");
			cameraMenu.add(camera1);
			cameraMenu.add(camera2);
			cameraMenu.add(camera3);

			camera1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
//					video.open(0);
				}
			});

			camera2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
//					video.open(1);
				}
			});

			camera3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
//					video.open(2);
				}
			});

			imageMenu.add(openImage);
			openImage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
//					video.release();
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

	public static JSlider redSliderUpperBound;
	public static JSlider greenSliderUpperBound;
	public static JSlider blueSliderUpperBound;
	public static JSpinner redSpinnerUpperBound;
	public static JSpinner greenSpinnerUpperBound;
	public static JSpinner blueSpinnerUpperBound;
	public static JFrame frameUpperBound;

	public static JSlider brightnessSlider;
	public static JSpinner brightnessSpinner;

	/**
	 * Creates the frame with the Upper Bound sliders for Thresholding
	 */
	public static void createSlideBarUpperBound() {
		frameUpperBound = new JFrame("Upper Bound");
		frameUpperBound.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameUpperBound.setBounds(1270, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frameUpperBound.setContentPane(contentPane);
		contentPane.setLayout(null);

		redSliderUpperBound = new JSlider();
		redSliderUpperBound.setBounds(123, 124, 200, 26);
		contentPane.add(redSliderUpperBound);

		greenSliderUpperBound = new JSlider();
		greenSliderUpperBound.setBounds(123, 71, 200, 26);
		contentPane.add(greenSliderUpperBound);

		blueSliderUpperBound = new JSlider();
		blueSliderUpperBound.setBounds(123, 16, 200, 26);
		contentPane.add(blueSliderUpperBound);

		JLabel lblBlue = new JLabel("Blue");
		lblBlue.setBounds(39, 16, 69, 20);
		contentPane.add(lblBlue);

		JLabel lblGreen = new JLabel("Green");
		lblGreen.setBounds(39, 77, 69, 20);
		contentPane.add(lblGreen);

		JLabel lblRed = new JLabel("Red");
		lblRed.setBounds(39, 124, 69, 20);
		contentPane.add(lblRed);

		redSpinnerUpperBound = new JSpinner();
		redSpinnerUpperBound.setBounds(338, 124, 55, 26);
		contentPane.add(redSpinnerUpperBound);
		redSpinnerUpperBound.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				redSliderUpperBound.setValue((int) Math.round((int) redSpinnerUpperBound.getValue() / 2.55));
			}

		});

		greenSpinnerUpperBound = new JSpinner();
		greenSpinnerUpperBound.setBounds(338, 71, 55, 26);
		contentPane.add(greenSpinnerUpperBound);
		greenSpinnerUpperBound.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				greenSliderUpperBound.setValue((int) Math.round((int) greenSpinnerUpperBound.getValue() / 2.55));
			}

		});

		blueSpinnerUpperBound = new JSpinner();
		blueSpinnerUpperBound.setBounds(338, 16, 55, 26);
		contentPane.add(blueSpinnerUpperBound);
		blueSpinnerUpperBound.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				blueSliderUpperBound.setValue((int) Math.round((int) blueSpinnerUpperBound.getValue() / 2.55));
			}

		});

		brightnessSlider = new JSlider();
		brightnessSlider.setBounds(123, 166, 200, 26);
		contentPane.add(brightnessSlider);

		JLabel lblBrightness = new JLabel("Brightness");
		lblBrightness.setBounds(39, 172, 82, 20);
		contentPane.add(lblBrightness);

		brightnessSpinner = new JSpinner();
		brightnessSpinner.setBounds(338, 166, 55, 26);
		contentPane.add(brightnessSpinner);

		brightnessSpinner.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				brightnessSlider.setValue((int) Math.round(((int) brightnessSpinner.getValue() / 2.55) + 50));
			}

		});
		frameUpperBound.setVisible(true);
	}

	public static JSlider redSliderLowerBound;
	public static JSlider greenSliderLowerBound;
	public static JSlider blueSliderLowerBound;
	public static JSpinner redSpinnerLowerBound;
	public static JSpinner greenSpinnerLowerBound;
	public static JSpinner blueSpinnerLowerBound;
	public static JFrame frameLowerBound;

	public static JRadioButton crossButtonDilation;
	public static JRadioButton ellipseButtonDilation;
	public static JRadioButton recButtonDilation;
	public static JRadioButton crossButtonErosion;
	public static JRadioButton ellipseButtonErosion;
	public static JRadioButton recButtonErosion;
	public static JSpinner dilationSpinner;
	public static JSpinner erosionSpinner;

	
	/**
	 * Creates the frame with the Lower Bound sliders for Thresholding
	 */
	public static void createSlideBarLowerBound() {
		frameLowerBound = new JFrame("Lower Bound");
		frameLowerBound.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameLowerBound.setSize(400, 300);
		frameLowerBound.setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frameLowerBound.setContentPane(contentPane);
		contentPane.setLayout(null);

		redSliderLowerBound = new JSlider();
		redSliderLowerBound.setBounds(123, 124, 200, 26);
		contentPane.add(redSliderLowerBound);

		greenSliderLowerBound = new JSlider();
		greenSliderLowerBound.setBounds(123, 71, 200, 26);
		contentPane.add(greenSliderLowerBound);

		blueSliderLowerBound = new JSlider();
		blueSliderLowerBound.setBounds(123, 16, 200, 26);
		contentPane.add(blueSliderLowerBound);

		JLabel lblBlue = new JLabel("Blue");
		lblBlue.setBounds(39, 16, 69, 20);
		contentPane.add(lblBlue);

		JLabel lblGreen = new JLabel("Green");
		lblGreen.setBounds(39, 77, 69, 20);
		contentPane.add(lblGreen);

		JLabel lblRed = new JLabel("Red");
		lblRed.setBounds(39, 124, 69, 20);
		contentPane.add(lblRed);

		redSpinnerLowerBound = new JSpinner();
		redSpinnerLowerBound.setBounds(338, 124, 55, 26);
		contentPane.add(redSpinnerLowerBound);

		redSpinnerLowerBound.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				redSliderLowerBound.setValue((int) Math.round((int) redSpinnerLowerBound.getValue() / 2.55));
			}

		});

		greenSpinnerLowerBound = new JSpinner();
		greenSpinnerLowerBound.setBounds(338, 71, 55, 26);
		contentPane.add(greenSpinnerLowerBound);

		greenSpinnerLowerBound.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				greenSliderLowerBound.setValue((int) Math.round((int) greenSpinnerLowerBound.getValue() / 2.55));
			}

		});

		blueSpinnerLowerBound = new JSpinner();
		blueSpinnerLowerBound.setBounds(338, 16, 55, 26);
		contentPane.add(blueSpinnerLowerBound);

		blueSpinnerLowerBound.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				blueSliderLowerBound.setValue((int) Math.round((int) blueSpinnerLowerBound.getValue() / 2.55));
			}

		});

		frameLowerBound.setVisible(true);
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
				int[] config = { (int) blueSpinnerLowerBound.getValue(), (int) greenSpinnerLowerBound.getValue(),
						(int) redSpinnerLowerBound.getValue(), (int) blueSpinnerUpperBound.getValue(),
						(int) greenSpinnerUpperBound.getValue(), (int) redSpinnerUpperBound.getValue(),
						(int) brightnessSpinner.getValue() };
				fos = new FileOutputStream(saveName);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(config);
				oos.flush();
				oos.close();
			} else {
				int result = JOptionPane.showConfirmDialog(fileChooser, "File Already Exists. Overwrite?");
				switch (result) {
				case JOptionPane.YES_OPTION:
					int[] config = { (int) redSpinnerLowerBound.getValue(), (int) greenSpinnerLowerBound.getValue(),
							(int) blueSpinnerLowerBound.getValue(), (int) redSpinnerUpperBound.getValue(),
							(int) greenSpinnerUpperBound.getValue(), (int) blueSpinnerUpperBound.getValue(),
							(int) brightnessSpinner.getValue() };
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
				blueSpinnerLowerBound.setValue(config[0]);
				greenSpinnerLowerBound.setValue(config[1]);
				redSpinnerLowerBound.setValue(config[2]);
				blueSpinnerUpperBound.setValue(config[3]);
				greenSpinnerUpperBound.setValue(config[4]);
				redSpinnerUpperBound.setValue(config[5]);
				brightnessSpinner.setValue(config[6]);
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
			redSliderLowerBound.setValue(0);
			greenSliderLowerBound.setValue(0);
			blueSliderLowerBound.setValue(0);
			redSliderUpperBound.setValue(0);
			greenSliderUpperBound.setValue(0);
			blueSliderUpperBound.setValue(0);

			while (!exitProgram) {

				if (redSliderLowerBound.getValueIsAdjusting())
					redSpinnerLowerBound.setValue((int) Math.round(redSliderLowerBound.getValue() * 2.55));
				if (greenSliderLowerBound.getValueIsAdjusting())
					greenSpinnerLowerBound.setValue((int) Math.round(greenSliderLowerBound.getValue() * 2.55));
				if (blueSliderLowerBound.getValueIsAdjusting())
					blueSpinnerLowerBound.setValue((int) Math.round(blueSliderLowerBound.getValue() * 2.55));
				if (redSliderUpperBound.getValueIsAdjusting())
					redSpinnerUpperBound.setValue((int) Math.round(redSliderUpperBound.getValue() * 2.55));
				if (greenSliderUpperBound.getValueIsAdjusting())
					greenSpinnerUpperBound.setValue((int) Math.round(greenSliderUpperBound.getValue() * 2.55));
				if (blueSliderUpperBound.getValueIsAdjusting())
					blueSpinnerUpperBound.setValue((int) Math.round(blueSliderUpperBound.getValue() * 2.55));
				if (brightnessSlider.getValueIsAdjusting())
					brightnessSpinner.setValue((int) Math.round((brightnessSlider.getValue() - 50) * 2.55));

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
