
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class ThresholdUtility implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws InterruptedException, IOException {
		// Load the main OpenCV libraries
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat mat = new Mat();
		VideoCapture video = new VideoCapture();
		// Open the camera on the default port
		video.open(0);
		// put the read image into Matrix mat
		video.read(mat);
		// open the default system camera dialogue
		video.set(Highgui.CV_CAP_PROP_SETTINGS, 0);
		// create the frame with the upper bound values
		createSlideBarUpperBound();
		// create the frame with the lower bound values
		createSlideBarLowerBound();
		// start the main frame that will hold the video

		imShow(imShowValue.Start, convertToImage(mat));
		Scalar upperBoundScalar;
		Scalar lowerBoundScalar;
		Scalar brightnessScalar;

		double brightnessConstant;
		// start the background processes with mundane tasks in a different
		// thread
		BackgroundProcesses b = new BackgroundProcesses();
		b.start();
		try {
			while (true) {
				brightnessConstant = (brightnessSlider.getValue() - 50) * 2.55;
				brightnessScalar = new Scalar(brightnessConstant, brightnessConstant, brightnessConstant);
				// set the lower bound scalar to the sliders in the Lower Bound
				// frame
				lowerBoundScalar = new Scalar(blueSliderLowerBound.getValue() * 2.55,
						greenSliderLowerBound.getValue() * 2.55, redSliderLowerBound.getValue() * 2.55);
				// set the upper bound scalar to the sliders in the Upper Bound
				// frame
				upperBoundScalar = new Scalar(blueSliderUpperBound.getValue() * 2.55,
						greenSliderUpperBound.getValue() * 2.55, redSliderUpperBound.getValue() * 2.55);
				video.read(mat);
				Core.add(mat, brightnessScalar, mat);
				// Threshold based on the scalar values declared
				Core.inRange(mat, lowerBoundScalar, upperBoundScalar, mat);
				// Refresh the frame
				imShow(imShowValue.Refresh, convertToImage(mat));
				// Determines the FPS value
				Thread.sleep(33);
				if (BackgroundProcesses.exitProgram)
					break;
			}
		} finally {
			video.release();
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

	public static enum imShowValue {
		Refresh, Start
	}

	public static JLabel label;
	public static ImageIcon icon;
	public static JFrame frame;
	public static JMenuBar menu;
	public static JMenu imageMenu;
	public static JMenuItem openImage;
	
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
 	public static void imShow(imShowValue val, BufferedImage i) {
		switch (val) {
		case Start:
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.setBounds(600, 0, 400, 300);
			menu = new JMenuBar();
			frame.setJMenuBar(menu);
			imageMenu = new JMenu("Image");
			menu.add(imageMenu);
			openImage = new JMenuItem("Open Image");
			imageMenu.add(openImage);
			openImage.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					isOpeningImage = true;
				}
			});
			icon = new ImageIcon(i);
			label = new JLabel(icon);
			frame.getContentPane().add(label);
			frame.pack();
			frame.setVisible(true);
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.out.println("imShowAltered");
					BackgroundProcesses.exitProgram = true;
				}
			});
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
				redSliderLowerBound.setValue((int) Math.round((int)redSpinnerLowerBound.getValue() / 2.55));
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
	public static Mat openedImage;
	@SuppressWarnings("unused")
	private static void beginOpening() {
		JFileChooser fileChooser = new JFileChooser();

		int rVal = fileChooser.showOpenDialog(fileChooser);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			saveName = fileChooser.getSelectedFile().getAbsolutePath();
			if (!saveName.toLowerCase().contains(".jpg"))
				saveName = saveName.concat(".jpg");
			File file = new File(saveName);
			if (!file.exists()) {
				JOptionPane.showMessageDialog(fileChooser, "The File Does Not Exist!");
				beginOpening();
			} else {
				openedImage = Highgui.imread("saveName");
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

				if (ThresholdUtility.redSliderLowerBound.getValueIsAdjusting())
					ThresholdUtility.redSpinnerLowerBound.setValue((int) Math.round(ThresholdUtility.redSliderLowerBound.getValue() * 2.55));
				if (ThresholdUtility.greenSliderLowerBound.getValueIsAdjusting())
					ThresholdUtility.greenSpinnerLowerBound
							.setValue((int) Math.round(ThresholdUtility.greenSliderLowerBound.getValue() * 2.55));
				if (ThresholdUtility.blueSliderLowerBound.getValueIsAdjusting())
					ThresholdUtility.blueSpinnerLowerBound.setValue((int) Math.round(ThresholdUtility.blueSliderLowerBound.getValue() * 2.55));
				if (ThresholdUtility.redSliderUpperBound.getValueIsAdjusting())
					ThresholdUtility.redSpinnerUpperBound.setValue((int) Math.round(ThresholdUtility.redSliderUpperBound.getValue() * 2.55));
				if (ThresholdUtility.greenSliderUpperBound.getValueIsAdjusting())
					ThresholdUtility.greenSpinnerUpperBound
							.setValue((int) Math.round(ThresholdUtility.greenSliderUpperBound.getValue() * 2.55));
				if (ThresholdUtility.blueSliderUpperBound.getValueIsAdjusting())
					ThresholdUtility.blueSpinnerUpperBound.setValue((int) Math.round(ThresholdUtility.blueSliderUpperBound.getValue() * 2.55));
				if (ThresholdUtility.brightnessSlider.getValueIsAdjusting())
					ThresholdUtility.brightnessSpinner.setValue((int) Math.round((ThresholdUtility.brightnessSlider.getValue() - 50) * 2.55));
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
