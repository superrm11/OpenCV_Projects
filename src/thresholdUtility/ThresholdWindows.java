package thresholdUtility;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ThresholdWindows {
	public JSlider redSliderUpperBound;
	public JSlider greenSliderUpperBound;
	public JSlider blueSliderUpperBound;
	public JSpinner redSpinnerUpperBound;
	public JSpinner greenSpinnerUpperBound;
	public JSpinner blueSpinnerUpperBound;
	public JFrame frameUpperBound;

	public JSlider brightnessSlider;
	public JSpinner brightnessSpinner;

	private final int operationIndex;

	public ThresholdWindows(int operationIndex) {
		this.operationIndex = operationIndex;
		createSlideBarUpperBound();
		createSlideBarLowerBound();
	}

	public int getOperationsIndex() {
		return operationIndex;
	}

	public int[] getParams() {
		int[] params = { (int) blueSpinnerLowerBound.getValue(), (int) greenSpinnerLowerBound.getValue(),
				(int) redSpinnerLowerBound.getValue(), (int) blueSpinnerUpperBound.getValue(),
				(int) greenSpinnerUpperBound.getValue(), (int) redSpinnerUpperBound.getValue(),
				(int) brightnessSpinner.getValue() };
		return params;
	}

	public JSlider redSliderLowerBound;
	public JSlider greenSliderLowerBound;
	public JSlider blueSliderLowerBound;
	public JSpinner redSpinnerLowerBound;
	public JSpinner greenSpinnerLowerBound;
	public JSpinner blueSpinnerLowerBound;
	public JFrame frameLowerBound;

	public JRadioButton crossButtonDilation;
	public JRadioButton ellipseButtonDilation;
	public JRadioButton recButtonDilation;
	public JRadioButton crossButtonErosion;
	public JRadioButton ellipseButtonErosion;
	public JRadioButton recButtonErosion;
	public JSpinner dilationSpinner;
	public JSpinner erosionSpinner;

	/**
	 * Creates the frame with the Lower Bound sliders for Thresholding
	 */
	public void createSlideBarLowerBound() {
		frameLowerBound = new JFrame("Lower Bound");
		frameLowerBound.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameLowerBound.setUndecorated(true);
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

	/**
	 * Creates the frame with the Upper Bound sliders for Thresholding
	 */
	public void createSlideBarUpperBound() {
		frameUpperBound = new JFrame("Upper Bound");
		frameUpperBound.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameUpperBound.setUndecorated(true);
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

}
