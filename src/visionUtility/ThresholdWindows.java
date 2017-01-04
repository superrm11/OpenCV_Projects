package visionUtility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.JButton;

public class ThresholdWindows implements OperationWindows
{
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
	private int thresholdType = 0;

	public static enum ColorType
	{
		HSV, BGR
	}

	public ColorType thisColorType = ColorType.BGR;

	public ThresholdWindows(int operationIndex)
	{

		createSlideBarLowerBound();
		createSlideBarUpperBound();
		this.operationIndex = operationIndex;

	}

	public int getOperationsIndex()
	{
		return operationIndex;
	}

	public int[] lastSetParams =
	{ 3, 0, 0, 0, 0, 0, 0, 0, 0 };

	public void setParams(int[] params)
	{
		blueSpinnerLowerBound.setValue(params[1]);
		greenSpinnerLowerBound.setValue(params[2]);
		redSpinnerLowerBound.setValue(params[3]);

		blueSpinnerUpperBound.setValue(params[4]);
		greenSpinnerUpperBound.setValue(params[5]);
		redSpinnerUpperBound.setValue(params[6]);

		brightnessSpinner.setValue(params[7]);
	}

	public int[] getParams()
	{
		int color;
		switch (this.thisColorType)
		{
		case HSV:
			color = 1;
			break;
		case BGR:
			color = 0;
			break;
		default:
			color = 0;
		}
		int[] params =
		{ 3, (int) blueSpinnerLowerBound.getValue(), (int) greenSpinnerLowerBound.getValue(),
				(int) redSpinnerLowerBound.getValue(), (int) blueSpinnerUpperBound.getValue(),
				(int) greenSpinnerUpperBound.getValue(), (int) redSpinnerUpperBound.getValue(),
				(int) brightnessSpinner.getValue(), color };
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

	private JLabel lblBlueLower;
	private JLabel lblGreenLower;
	private JLabel lblRedLower;

	private JButton btnToHsv;
	private JLabel lblBlue;
	private JLabel lblGreen;
	private JLabel lblRed;
	private JLabel lblBrightness;

	/**
	 * Creates the frame with the Lower Bound sliders for Thresholding
	 */
	public void createSlideBarLowerBound()
	{
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
		redSliderLowerBound.setValue(0);
		redSliderLowerBound.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				redSpinnerLowerBound.setValue((int) Math.round(redSliderLowerBound.getValue() * 2.55));
			}
		});

		greenSliderLowerBound = new JSlider();
		greenSliderLowerBound.setBounds(123, 71, 200, 26);
		contentPane.add(greenSliderLowerBound);
		greenSliderLowerBound.setValue(0);
		greenSliderLowerBound.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				greenSpinnerLowerBound.setValue((int) Math.round(greenSliderLowerBound.getValue() * 2.55));
			}
		});

		blueSliderLowerBound = new JSlider();
		blueSliderLowerBound.setBounds(123, 16, 200, 26);
		contentPane.add(blueSliderLowerBound);
		blueSliderLowerBound.setValue(0);
		blueSliderLowerBound.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				blueSpinnerLowerBound.setValue((int) Math.round(blueSliderLowerBound.getValue() * 2.55));
			}

		});

		lblBlueLower = new JLabel("Blue");
		lblBlueLower.setBounds(39, 16, 69, 20);
		contentPane.add(lblBlueLower);

		lblGreenLower = new JLabel("Green");
		lblGreenLower.setBounds(39, 77, 69, 20);
		contentPane.add(lblGreenLower);

		lblRedLower = new JLabel("Red");
		lblRedLower.setBounds(39, 124, 69, 20);
		contentPane.add(lblRedLower);

		redSpinnerLowerBound = new JSpinner();
		redSpinnerLowerBound.setBounds(338, 124, 55, 26);
		contentPane.add(redSpinnerLowerBound);

		redSpinnerLowerBound.addChangeListener(new ChangeListener()
		{

			public void stateChanged(ChangeEvent arg0)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				redSliderLowerBound.setValue((int) Math.round((int) redSpinnerLowerBound.getValue() / 2.55));
			}

		});

		greenSpinnerLowerBound = new JSpinner();
		greenSpinnerLowerBound.setBounds(338, 71, 55, 26);
		contentPane.add(greenSpinnerLowerBound);

		greenSpinnerLowerBound.addChangeListener(new ChangeListener()
		{

			public void stateChanged(ChangeEvent arg0)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				greenSliderLowerBound.setValue((int) Math.round((int) greenSpinnerLowerBound.getValue() / 2.55));
			}

		});

		blueSpinnerLowerBound = new JSpinner();
		blueSpinnerLowerBound.setBounds(338, 16, 55, 26);
		contentPane.add(blueSpinnerLowerBound);

		blueSpinnerLowerBound.addChangeListener(new ChangeListener()
		{

			public void stateChanged(ChangeEvent arg0)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				blueSliderLowerBound.setValue((int) Math.round((int) blueSpinnerLowerBound.getValue() / 2.55));
			}

		});

		frameLowerBound.addWindowListener(new WindowListener()
		{

			@Override
			public void windowActivated(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0)
			{
				frameUpperBound.dispose();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

		});

	}

	/**
	 * Creates the frame with the Upper Bound sliders for Thresholding
	 */
	public void createSlideBarUpperBound()
	{
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
		redSliderUpperBound.setValue(0);
		redSliderUpperBound.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				redSpinnerUpperBound.setValue((int) Math.round(redSliderUpperBound.getValue() * 2.55));
			}
		});

		greenSliderUpperBound = new JSlider();
		greenSliderUpperBound.setBounds(123, 71, 200, 26);
		contentPane.add(greenSliderUpperBound);
		greenSliderUpperBound.setValue(0);
		greenSliderUpperBound.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				greenSpinnerUpperBound.setValue((int) Math.round(greenSliderUpperBound.getValue() * 2.55));
			}
		});

		blueSliderUpperBound = new JSlider();
		blueSliderUpperBound.setBounds(123, 16, 200, 26);
		contentPane.add(blueSliderUpperBound);
		blueSliderUpperBound.setValue(0);
		blueSliderUpperBound.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				blueSpinnerUpperBound.setValue((int) Math.round(blueSliderUpperBound.getValue() * 2.55));
			}
		});

		lblBlue = new JLabel("Blue");
		lblBlue.setBounds(39, 16, 69, 20);
		contentPane.add(lblBlue);

		lblGreen = new JLabel("Green");
		lblGreen.setBounds(39, 77, 69, 20);
		contentPane.add(lblGreen);

		lblRed = new JLabel("Red");
		lblRed.setBounds(39, 124, 69, 20);
		contentPane.add(lblRed);

		redSpinnerUpperBound = new JSpinner();
		redSpinnerUpperBound.setBounds(338, 124, 55, 26);
		contentPane.add(redSpinnerUpperBound);
		redSpinnerUpperBound.addChangeListener(new ChangeListener()
		{

			public void stateChanged(ChangeEvent arg0)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				redSliderUpperBound.setValue((int) Math.round((int) redSpinnerUpperBound.getValue() / 2.55));
			}

		});

		greenSpinnerUpperBound = new JSpinner();
		greenSpinnerUpperBound.setBounds(338, 71, 55, 26);
		contentPane.add(greenSpinnerUpperBound);
		greenSpinnerUpperBound.addChangeListener(new ChangeListener()
		{

			public void stateChanged(ChangeEvent arg0)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				greenSliderUpperBound.setValue((int) Math.round((int) greenSpinnerUpperBound.getValue() / 2.55));
			}

		});

		blueSpinnerUpperBound = new JSpinner();
		blueSpinnerUpperBound.setBounds(338, 16, 55, 26);
		contentPane.add(blueSpinnerUpperBound);
		blueSpinnerUpperBound.addChangeListener(new ChangeListener()
		{

			public void stateChanged(ChangeEvent arg0)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				blueSliderUpperBound.setValue((int) Math.round((int) blueSpinnerUpperBound.getValue() / 2.55));
			}

		});

		brightnessSlider = new JSlider();
		brightnessSlider.setBounds(123, 166, 200, 26);
		contentPane.add(brightnessSlider);

		brightnessSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				brightnessSpinner.setValue((int) Math.round((brightnessSlider.getValue() * 2.55) - 127.5));
			}
		});

		lblBrightness = new JLabel("Brightness");
		lblBrightness.setBounds(39, 172, 82, 20);
		contentPane.add(lblBrightness);

		brightnessSpinner = new JSpinner();
		brightnessSpinner.setBounds(338, 166, 55, 26);
		contentPane.add(brightnessSpinner);

		btnToHsv = new JButton("To HSV");
		btnToHsv.setBounds(39, 215, 97, 25);
		contentPane.add(btnToHsv);
		btnToHsv.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (thresholdType >= 1)
					thresholdType = 0;
				else
					thresholdType++;

				switch (thresholdType)
				{
				case 1:
					setTo(ColorType.HSV);
					break;
				case 0:
					setTo(ColorType.BGR);
					break;
				}
			}

		});

		brightnessSpinner.addChangeListener(new ChangeListener()
		{

			public void stateChanged(ChangeEvent arg0)
			{
				lastSetParams = getParams();
				VisionUtility.operationsWindow.operations.set(operationIndex, lastSetParams);
				brightnessSlider.setValue((int) Math.round(((int) brightnessSpinner.getValue() / 2.55) + 50));
			}

		});

		frameUpperBound.addWindowListener(new WindowListener()
		{

			@Override
			public void windowActivated(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0)
			{
				frameLowerBound.dispose();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

		});
	}

	@Override
	public int getOperationIndex()
	{
		return operationIndex;
	}

	@Override
	public void displayWindows()
	{
		frameUpperBound.setVisible(true);
		frameLowerBound.setVisible(true);

		if (VisionUtility.operationsWindow.operations.get(operationIndex) != null
				&& VisionUtility.operationsWindow.operations.get(operationIndex)[0] == 3)
			lastSetParams = VisionUtility.operationsWindow.operations.get(operationIndex);
		setParams(lastSetParams);
	}

	public void setTo(ColorType type)
	{
		this.thisColorType = type;

		switch (type)
		{
		case BGR:
			btnToHsv.setText("To HSV");
			lblBlue.setText("Blue");
			lblGreen.setText("Green");
			lblRed.setText("Red");

			lblBlueLower.setText("Blue");
			lblGreenLower.setText("Green");
			lblRedLower.setText("Red");

			lblBrightness.setVisible(true);
			brightnessSlider.setVisible(true);
			brightnessSpinner.setVisible(true);
			break;
		case HSV:
			btnToHsv.setText("To BGR");
			lblBlue.setText("Hue");
			lblGreen.setText("Saturation");
			lblRed.setText("Value");

			lblBlueLower.setText("Hue");
			lblGreenLower.setText("Saturation");
			lblRedLower.setText("Value");

			brightnessSpinner.setValue(0);
			lblBrightness.setVisible(false);
			brightnessSlider.setVisible(false);
			brightnessSpinner.setVisible(false);
			break;
		}
	}
}
