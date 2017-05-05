package visionUtility;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CascadeWindow extends JFrame
{
	/*
	 * Constant Variables
	 */
	private final int DEFAULT_WIDTH = 40, DEFAULT_HEIGHT = 40;
	private final int DEFAULT_GEN_NUM = 500;
	private final int DEFAULT_POS_IMG = 200, DEFAULT_NEG_IMG = 400;
	/*
	 * End Constant Variables
	 */

	/*
	 * Class Variables
	 */
	private String vecSavePath, vecOpenPath;
	private String dataSavePath;

	/*
	 * End Class Variables
	 */

	/*
	 * Window Variables
	 */
	private JPanel contentPane;
	// TEXTFIELDS
	private JTextField genOutPrev, negativeDirPrev, trainVecPrev, trainOutPrev;
	// BUTTONS
	private JButton btnSelectPositive, btnGenerate, btnTrainClassifier, btnGenOutput, btnSelectNegative, btnGenView,
			btnSelectVecFile, btnTrainPreview, btnTrainOut;
	// SPINNERS
	private JSpinner spinGenNum, spinTrainHeight, spinTrainWidth, spinPosSamples, spinNegSamples, spinGenWidth,
			spinGenHeight;
	// CHECK BOXES
	private JCheckBox chckbxUseGeneratedImages, chckbxMatchToGenerator;
	// PICTURE PREVIEW
	private JLabel lblPicturePrev;
	// COMMAND OUTPUT TEXT AREA
	private TextArea commandOut;
	/*
	 * End Window Variables
	 */

	/**
	 * Create the frame.
	 */
	public CascadeWindow()
	{
		initWindowComponents();
		createActionListeners();
		initCommandOutArea().start();
	}

	private void initWindowComponents()
	{
		// WINDOW INIT
		setTitle("Cascade Classifier Training");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 741, 502);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JSeparator separator = new JSeparator();
		separator.setBounds(346, 215, 365, 2);
		contentPane.add(separator);
		// END WINDOW INIT

		// BUTTON INIT
		btnSelectPositive = new JButton("Select positive");
		btnSelectPositive.setBounds(346, 13, 139, 25);
		contentPane.add(btnSelectPositive);

		btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(563, 177, 96, 25);
		contentPane.add(btnGenerate);

		btnTrainClassifier = new JButton("Train Classifier");
		btnTrainClassifier.setBounds(574, 417, 137, 25);
		contentPane.add(btnTrainClassifier);

		btnGenOutput = new JButton("Output");
		btnGenOutput.setBounds(346, 152, 75, 25);
		contentPane.add(btnGenOutput);

		btnGenView = new JButton("View");
		btnGenView.setBounds(659, 177, 64, 25);
		contentPane.add(btnGenView);

		btnSelectNegative = new JButton("Select negatives");
		btnSelectNegative.setBounds(553, 13, 158, 25);
		contentPane.add(btnSelectNegative);

		btnSelectVecFile = new JButton("Select Vec File");
		btnSelectVecFile.setBounds(346, 256, 139, 23);
		contentPane.add(btnSelectVecFile);

		btnTrainPreview = new JButton("Preview");
		btnTrainPreview.setBounds(474, 256, 94, 23);
		contentPane.add(btnTrainPreview);

		btnTrainOut = new JButton("Output");
		btnTrainOut.setBounds(346, 395, 75, 23);
		contentPane.add(btnTrainOut);
		// END BUTTON INIT

		// COMMAND TEXT AREA INIT
		commandOut = new TextArea();
		commandOut.setEditable(false);
		commandOut.setBounds(0, 0, 340, 455);
		contentPane.add(commandOut);
		// END COMMAND TEXT AREA INIT

		// JLABEL INIT
		lblPicturePrev = new JLabel("...No Picture Selected");
		lblPicturePrev.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblPicturePrev.setBackground(Color.WHITE);
		lblPicturePrev.setForeground(Color.BLACK);
		lblPicturePrev.setBounds(346, 43, 139, 96);
		contentPane.add(lblPicturePrev);

		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setBounds(497, 90, 56, 16);
		contentPane.add(lblWidth);

		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setBounds(497, 125, 56, 16);
		contentPane.add(lblHeight);

		JLabel lblNumber = new JLabel("Number:");
		lblNumber.setBounds(595, 90, 56, 16);
		contentPane.add(lblNumber);

		JLabel label = new JLabel("Width:");
		label.setBounds(614, 290, 56, 16);
		contentPane.add(label);

		JLabel label_1 = new JLabel("Height:");
		label_1.setBounds(612, 325, 56, 16);
		contentPane.add(label_1);

		JLabel lblNoteWidthAnd = new JLabel("Note: Width and Height \r\nmust match the images\r\n in the .vec file!");
		lblNoteWidthAnd.setBounds(346, 370, 377, 25);
		contentPane.add(lblNoteWidthAnd);

		JLabel lblNumberOfPosotive = new JLabel("Number of Positive samples:");
		lblNumberOfPosotive.setBounds(346, 310, 169, 14);
		contentPane.add(lblNumberOfPosotive);

		JLabel lblNumberOfNegative = new JLabel("Number of Negative samples:");
		lblNumberOfNegative.setBounds(346, 343, 169, 14);
		contentPane.add(lblNumberOfNegative);

		JLabel lblIsA = new JLabel("200-400 is a good number");
		lblIsA.setForeground(Color.GRAY);
		lblIsA.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblIsA.setBounds(346, 322, 139, 14);
		contentPane.add(lblIsA);

		JLabel lblGenerallyTwiceAs = new JLabel("Generally twice as many as Positives");
		lblGenerallyTwiceAs.setForeground(Color.GRAY);
		lblGenerallyTwiceAs.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblGenerallyTwiceAs.setBounds(346, 357, 184, 14);
		contentPane.add(lblGenerallyTwiceAs);
		// END JLABEL INIT

		// SPINNER INIT
		spinGenWidth = new JSpinner();
		spinGenWidth.setBounds(539, 87, 51, 22);
		contentPane.add(spinGenWidth);
		spinGenWidth.setValue(DEFAULT_WIDTH);

		spinGenHeight = new JSpinner();
		spinGenHeight.setBounds(539, 122, 51, 22);
		contentPane.add(spinGenHeight);
		spinGenHeight.setValue(DEFAULT_HEIGHT);

		spinGenNum = new JSpinner();
		spinGenNum.setBounds(651, 87, 60, 22);
		contentPane.add(spinGenNum);
		spinGenNum.setValue(DEFAULT_GEN_NUM);

		spinTrainHeight = new JSpinner();
		spinTrainHeight.setBounds(659, 323, 52, 22);
		contentPane.add(spinTrainHeight);
		spinTrainHeight.setValue(DEFAULT_HEIGHT);

		spinTrainWidth = new JSpinner();
		spinTrainWidth.setBounds(659, 288, 52, 22);
		contentPane.add(spinTrainWidth);
		spinTrainWidth.setValue(DEFAULT_WIDTH);

		spinPosSamples = new JSpinner();
		spinPosSamples.setBounds(527, 305, 56, 20);
		contentPane.add(spinPosSamples);
		spinPosSamples.setValue(DEFAULT_POS_IMG);

		spinNegSamples = new JSpinner();
		spinNegSamples.setBounds(527, 338, 56, 20);
		contentPane.add(spinNegSamples);
		spinNegSamples.setValue(DEFAULT_NEG_IMG);
		// END SPINNER INIT

		// TEXT FIELD INIT
		genOutPrev = new JTextField();
		genOutPrev.setEditable(false);
		genOutPrev.setBounds(346, 178, 216, 22);
		contentPane.add(genOutPrev);
		genOutPrev.setColumns(10);

		negativeDirPrev = new JTextField();
		negativeDirPrev.setEditable(false);
		negativeDirPrev.setBounds(553, 52, 158, 22);
		contentPane.add(negativeDirPrev);
		negativeDirPrev.setColumns(10);

		trainVecPrev = new JTextField();
		trainVecPrev.setEditable(false);
		trainVecPrev.setBounds(346, 279, 221, 20);
		contentPane.add(trainVecPrev);
		trainVecPrev.setColumns(10);

		trainOutPrev = new JTextField();
		trainOutPrev.setEditable(false);
		trainOutPrev.setBounds(346, 419, 216, 20);
		contentPane.add(trainOutPrev);
		trainOutPrev.setColumns(10);
		// END TEXT FIELD INIT

		// CHECK BOX INIT
		chckbxUseGeneratedImages = new JCheckBox("Use generated images (above)");
		chckbxUseGeneratedImages.setBounds(346, 226, 282, 23);
		contentPane.add(chckbxUseGeneratedImages);

		chckbxMatchToGenerator = new JCheckBox("Match to Generator");
		chckbxMatchToGenerator.setBounds(574, 256, 158, 23);
		contentPane.add(chckbxMatchToGenerator);
		// END CHECK BOX INIT
	}

	private void createActionListeners()
	{
		// SPINNER LISTENERS
		spinGenHeight.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				if (chckbxMatchToGenerator.isSelected())
					spinTrainHeight.setValue(spinGenHeight.getValue());
			}
		});

		spinGenWidth.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				if (chckbxMatchToGenerator.isSelected())
					spinTrainWidth.setValue(spinGenWidth.getValue());
			}
		});
		// END SPINNER LISTENERS

		// CHECK BOX LISTENERS
		chckbxMatchToGenerator.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (chckbxMatchToGenerator.isSelected())
				{
					spinTrainWidth.setValue(spinGenWidth.getValue());
					spinTrainHeight.setValue(spinGenHeight.getValue());
				}
			}
		});
		// END CHECK BOX LISTENERS

		// BUTTON LISTENERS
		btnGenerate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				createSamples = true;
			}
		});

		btnTrainClassifier.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				trainCascade = true;
			}
		});

		// END BUTTON LISTENERS
	}

	private Process createSamples(int numPics, int width, int height, String vecSavePath, String negPath,
			String imgPath)
	{
		ProcessBuilder out = new ProcessBuilder("opencv_createsamples", "-img " + imgPath,
				"-vec \"" + vecSavePath + "\"", "-bg \"" + negPath + "\"", "-numPics " + numPics, "-w " + width,
				"-h " + height);
		try
		{
			return out.start();
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private boolean createSamples = false;
	private boolean trainCascade = false;

	private Thread initCommandOutArea()
	{
		return new Thread(new Runnable()
		{
			public void run()
			{
				while (true)
				{
					if (createSamples)
					{
						createSamples = false;
						commandOut.setText("");
						commandOut.append("Creating Samples...");
					} else if (trainCascade)
					{
						trainCascade = false;
						commandOut.setText("");
						commandOut.append("Training Cascade...");
					}
				}
			}
		});
	}

	public void displayWindow()
	{
		this.setVisible(true);
	}

}
