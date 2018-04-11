package visionUtility;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

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
	private String imgOpenPath = "", negOpenPath_gen = "", vecSavePath = "";
	private String vecOpenPath = "", trainOutPath = "", negOpenPath_train;
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
	private JButton btnSelectPositive, btnGenerate, btnTrainClassifier, btnGenOutput, btnSelNeg_Gen, btnGenView,
			btnSelectVecFile, btnTrainPreview, btnTrainOut, btnSelNeg_Train;
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
		setBounds(100, 100, 741, 571);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JSeparator separator = new JSeparator();
		separator.setBounds(346, 249, 365, 5);
		contentPane.add(separator);
		// END WINDOW INIT

		// BUTTON INIT
		btnSelectPositive = new JButton("Select positive");
		btnSelectPositive.setBounds(346, 47, 139, 25);
		contentPane.add(btnSelectPositive);

		btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(563, 211, 96, 25);
		contentPane.add(btnGenerate);

		btnTrainClassifier = new JButton("Train Classifier");
		btnTrainClassifier.setBounds(574, 486, 137, 25);
		contentPane.add(btnTrainClassifier);

		btnGenOutput = new JButton("Save As...");
		btnGenOutput.setBounds(346, 186, 96, 25);
		contentPane.add(btnGenOutput);

		btnGenView = new JButton("View");
		btnGenView.setBounds(659, 211, 64, 25);
		contentPane.add(btnGenView);
		btnGenView.setEnabled(false);

		btnSelNeg_Gen = new JButton("Select negatives");
		btnSelNeg_Gen.setBounds(553, 47, 158, 25);
		contentPane.add(btnSelNeg_Gen);

		btnSelNeg_Train = new JButton("Select negatives");
		btnSelNeg_Train.setBounds(573, 294, 148, 25);
		contentPane.add(btnSelNeg_Train);

		btnSelectVecFile = new JButton("Select Vec File");
		btnSelectVecFile.setBounds(346, 294, 127, 25);
		contentPane.add(btnSelectVecFile);

		btnTrainPreview = new JButton("Preview");
		btnTrainPreview.setBounds(473, 294, 94, 25);
		contentPane.add(btnTrainPreview);

		btnTrainOut = new JButton("Save As...");
		btnTrainOut.setBounds(346, 461, 96, 25);
		contentPane.add(btnTrainOut);
		// END BUTTON INIT

		// COMMAND TEXT AREA INIT
		commandOut = new TextArea();
		commandOut.setEditable(true);
		commandOut.setBounds(0, 0, 340, 524);
		contentPane.add(commandOut);
		// END COMMAND TEXT AREA INIT

		// JLABEL INIT
		lblPicturePrev = new JLabel("...No Picture Selected");
		lblPicturePrev.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblPicturePrev.setBackground(Color.WHITE);
		lblPicturePrev.setForeground(Color.BLACK);
		lblPicturePrev.setBounds(346, 77, 139, 96);
		lblPicturePrev.setLayout(new FlowLayout());
		contentPane.add(lblPicturePrev);

		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setBounds(497, 124, 56, 16);
		contentPane.add(lblWidth);

		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setBounds(497, 159, 56, 16);
		contentPane.add(lblHeight);

		JLabel lblNumber = new JLabel("Samples to");
		lblNumber.setBounds(614, 124, 70, 16);
		contentPane.add(lblNumber);

		JLabel label = new JLabel("Width:");
		label.setBounds(614, 381, 56, 16);
		contentPane.add(label);

		JLabel label_1 = new JLabel("Height:");
		label_1.setBounds(612, 416, 56, 16);
		contentPane.add(label_1);

		JLabel lblNoteWidthAnd = new JLabel("Note: Width and Height \r\nmust match the images\r\n in the .vec file!");
		lblNoteWidthAnd.setBounds(346, 439, 377, 25);
		contentPane.add(lblNoteWidthAnd);

		JLabel lblNumberOfPosotive = new JLabel("Number of Positive samples:");
		lblNumberOfPosotive.setBounds(346, 379, 169, 14);
		contentPane.add(lblNumberOfPosotive);

		JLabel lblNumberOfNegative = new JLabel("Number of Negative samples:");
		lblNumberOfNegative.setBounds(346, 412, 169, 14);
		contentPane.add(lblNumberOfNegative);

		JLabel lblIsA = new JLabel("200-400 is a good number");
		lblIsA.setForeground(Color.GRAY);
		lblIsA.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblIsA.setBounds(346, 391, 139, 14);
		contentPane.add(lblIsA);

		JLabel lblGenerallyTwiceAs = new JLabel("Generally twice as many as Positives");
		lblGenerallyTwiceAs.setForeground(Color.GRAY);
		lblGenerallyTwiceAs.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblGenerallyTwiceAs.setBounds(346, 426, 184, 14);
		contentPane.add(lblGenerallyTwiceAs);
		// END JLABEL INIT

		// SPINNER INIT
		spinGenWidth = new JSpinner();
		spinGenWidth.setBounds(539, 121, 51, 22);
		contentPane.add(spinGenWidth);
		spinGenWidth.setValue(DEFAULT_WIDTH);

		spinGenHeight = new JSpinner();
		spinGenHeight.setBounds(539, 156, 51, 22);
		contentPane.add(spinGenHeight);
		spinGenHeight.setValue(DEFAULT_HEIGHT);

		spinGenNum = new JSpinner();
		spinGenNum.setBounds(614, 156, 60, 22);
		contentPane.add(spinGenNum);
		spinGenNum.setValue(DEFAULT_GEN_NUM);

		spinTrainHeight = new JSpinner();
		spinTrainHeight.setBounds(659, 414, 52, 22);
		contentPane.add(spinTrainHeight);
		spinTrainHeight.setValue(DEFAULT_HEIGHT);

		spinTrainWidth = new JSpinner();
		spinTrainWidth.setBounds(659, 379, 52, 22);
		contentPane.add(spinTrainWidth);
		spinTrainWidth.setValue(DEFAULT_WIDTH);

		spinPosSamples = new JSpinner();
		spinPosSamples.setBounds(527, 374, 56, 20);
		contentPane.add(spinPosSamples);
		spinPosSamples.setValue(DEFAULT_POS_IMG);

		spinNegSamples = new JSpinner();
		spinNegSamples.setBounds(527, 407, 56, 20);
		contentPane.add(spinNegSamples);
		spinNegSamples.setValue(DEFAULT_NEG_IMG);
		// END SPINNER INIT

		// TEXT FIELD INIT
		genOutPrev = new JTextField();
		genOutPrev.setEditable(false);
		genOutPrev.setBounds(346, 212, 216, 22);
		contentPane.add(genOutPrev);
		genOutPrev.setColumns(10);

		negativeDirPrev = new JTextField();
		negativeDirPrev.setEditable(false);
		negativeDirPrev.setBounds(553, 86, 158, 22);
		contentPane.add(negativeDirPrev);
		negativeDirPrev.setColumns(10);

		trainVecPrev = new JTextField();
		trainVecPrev.setEditable(false);
		trainVecPrev.setBounds(346, 326, 221, 22);
		contentPane.add(trainVecPrev);
		trainVecPrev.setColumns(10);

		trainOutPrev = new JTextField();
		trainOutPrev.setEditable(false);
		trainOutPrev.setBounds(346, 488, 216, 23);
		contentPane.add(trainOutPrev);
		trainOutPrev.setColumns(10);
		// END TEXT FIELD INIT

		// CHECK BOX INIT
		chckbxUseGeneratedImages = new JCheckBox("Use generated images (above)");
		chckbxUseGeneratedImages.setBounds(346, 349, 205, 23);
		contentPane.add(chckbxUseGeneratedImages);

		chckbxMatchToGenerator = new JCheckBox("Match Size to Generator");
		chckbxMatchToGenerator.setBounds(553, 349, 170, 23);
		contentPane.add(chckbxMatchToGenerator);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(573, 326, 148, 22);
		contentPane.add(textField);

		JLabel lblGenerate = new JLabel("Generate:");
		lblGenerate.setBounds(614, 140, 64, 16);
		contentPane.add(lblGenerate);

		lblTrainingTheClassifier = new JLabel("Training the Classifier");
		lblTrainingTheClassifier.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTrainingTheClassifier.setBounds(345, 257, 257, 35);
		contentPane.add(lblTrainingTheClassifier);

		lblPreparingTheSamples = new JLabel("Preparing the Samples");
		lblPreparingTheSamples.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPreparingTheSamples.setBounds(345, 13, 257, 35);
		contentPane.add(lblPreparingTheSamples);

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
		chckbxUseGeneratedImages.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (chckbxUseGeneratedImages.isSelected())
				{
					vecOpenPath = vecSavePath;
					trainVecPrev.setText(vecSavePath);
					btnSelectVecFile.setEnabled(false);
				} else
				{
					btnSelectVecFile.setEnabled(true);
					trainVecPrev.setText(" ");
					vecOpenPath = "";
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

		btnSelectPositive.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String path = open(ExtensionType.kFile, new String[]
				{ "JPG Image Files", "PNG Image Files" }, new String[]
				{ "jpg", "png" });
				if (path != null)
				{
					imgOpenPath = path;
					Mat img = Highgui.imread(path);
					Imgproc.resize(img, img,
							(img.size().width / img.size().height > 1)
									? new Size(139, 96 * (img.size().height / img.size().width))
									: new Size(139 * (img.size().width / img.size().height), 96));
					lblPicturePrev.setIcon(new ImageIcon(VisionUtility.convertToImage(img)));
					lblPicturePrev.setText("");
				}
			}
		});

		btnSelNeg_Gen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String path = open(ExtensionType.kFile, "Text Files", "txt");
				if (path != null)
				{
					negativeDirPrev.setText(path);
					negOpenPath_gen = path;
				}
			}
		});

		btnGenOutput.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String path = save(ExtensionType.kFile, "Image Data File", "vec");
				if (path != null)
				{
					vecSavePath = path;
					genOutPrev.setText(path);
				}
			}
		});

		btnGenView.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				displayImgs_Gen = true;
			}
		});

		btnSelectVecFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String path = open(ExtensionType.kFile, "Image Data Files", "vec");
				if (path != null)
				{
					vecOpenPath = path;
					trainVecPrev.setText(path);
				}
			}
		});

		btnTrainPreview.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				displayImgs_Train = true;
			}
		});

		btnTrainOut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String path = save(ExtensionType.kDirectory, "", "");
				if (path != null)
				{
					trainOutPath = path;
					trainOutPrev.setText(path);
					System.out.println(path);
				}
			}
		});
		// END BUTTON LISTENERS
	}

	private String createSamples(int numPics, int width, int height, String vecSavePath, String negPath, String imgPath)
	{
		return "opencv_createsamples -img \"" + imgPath + "\" -vec \"" + vecSavePath + "\" -bg \"" + negPath
				+ "\" -num " + numPics + " -w " + width + " -h " + height;
	}

	private String trainCascade(int numPos, int numNeg, int width, int height, String xmlSavePath, String vecOpenPath,
			String negPath)
	{
		return "opencv_traincascade -vec \"" + vecOpenPath + "\" -data \"" + xmlSavePath + "\" -bg \"" + negPath
				+ "\" -w " + width + " -h " + height + " -numPos " + numPos + " -numNeg " + numNeg;
	}

	private boolean createSamples = false;
	private boolean trainCascade = false;
	private boolean displayImgs_Gen = false;
	private boolean displayImgs_Train = false;
	private JTextField textField;
	private JLabel lblTrainingTheClassifier;
	private JLabel lblPreparingTheSamples;

	private Thread initCommandOutArea()
	{
		return new Thread(new Runnable()
		{
			public void run()
			{
				Process currentProcess = null;
				String currentCommand = "";
				boolean isProcessing = false;
				while (true)
				{
					if (createSamples)
					{
						createSamples = false;
						commandOut.setText(" ");
						commandOut.append("Creating Samples...\nPlease wait...\n");
						currentCommand = createSamples((int) spinGenNum.getValue(), (int) spinGenWidth.getValue(),
								(int) spinGenHeight.getValue(), vecSavePath, negOpenPath_gen, imgOpenPath);
						btnGenerate.setEnabled(false);
						btnTrainClassifier.setEnabled(false);
						isProcessing = true;
					} else if (trainCascade)
					{
						trainCascade = false;
						commandOut.setText(" ");
						commandOut.append("Training Cascade...\nPlease wait: this may take a few minutes...\n");
						currentCommand = trainCascade((int) spinPosSamples.getValue(), (int) spinNegSamples.getValue(),
								(int) spinTrainWidth.getValue(), (int) spinTrainHeight.getValue(), trainOutPath,
								vecOpenPath, negOpenPath_gen);
						System.out.println(currentCommand);
						isProcessing = true;
						btnGenerate.setEnabled(false);
						btnTrainClassifier.setEnabled(false);
					} else if (displayImgs_Gen)
					{
						displayImgs_Gen = false;
						commandOut.setText(" ");
						commandOut.append(
								"Displaying Vec File...\nTo change images, press any key.\nTo exit, press escape.\n");
						currentCommand = "opencv_createsamples -vec \"" + vecSavePath + "\" -w "
								+ (int) spinGenWidth.getValue() + " -h " + (int) spinGenHeight.getValue();
						commandOut.append(currentCommand + "\n");
						isProcessing = true;
					} else if (displayImgs_Train)
					{
						displayImgs_Train = false;
						commandOut.setText(" ");
						commandOut.append(
								"Displaying Vec File...\nTo change images, press any key.\nTo exit, press escape.\n");
						currentCommand = "opencv_createsamples -vec \"" + vecOpenPath + "\" -w "
								+ (int) spinGenWidth.getValue() + " -h " + (int) spinGenHeight.getValue();
						commandOut.append(currentCommand + "\n");
						isProcessing = true;
					}

					if (isProcessing)
					{
						try
						{
							currentProcess = Runtime.getRuntime().exec(currentCommand);
						} catch (IOException e1)
						{
							isProcessing = false;
							e1.printStackTrace();
							commandOut.append("Failed to run command!\n");
							continue;

						}
						commandOut.append("Generated Command:\n" + currentCommand + "\n");
						String line = null;
						BufferedReader br = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()));
						try
						{
							while ((line = br.readLine()) != null)
							{
								commandOut.append(line + "\n");
							}
						} catch (IOException e)
						{
							e.printStackTrace();
						} finally
						{
							try
							{
								br.close();
							} catch (IOException e)
							{
								e.printStackTrace();
							}

							btnGenerate.setEnabled(true);
							btnTrainClassifier.setEnabled(true);
							isProcessing = false;
						}
					}

					try
					{
						Thread.sleep(50);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
	}

	private String save(ExtensionType type, String[] description, String[] extension)
	{
		if (description.length != extension.length)
			return null;

		JFileChooser chooser = new JFileChooser();
		if (type == ExtensionType.kFile)
		{
			chooser.setFileFilter(new FileNameExtensionFilter(description[0], extension[0]));
			for (int i = 1; i < description.length; i++)
				chooser.addChoosableFileFilter(new FileNameExtensionFilter(description[i], extension[i]));

			if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				return (chooser.getSelectedFile().getAbsolutePath()
						.endsWith("." + ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0])
								? chooser.getSelectedFile().getAbsolutePath()
								: chooser.getSelectedFile().getAbsolutePath() + "."
										+ ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0]);
		} else if (type == ExtensionType.kDirectory)
		{
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	private String save(ExtensionType type, String description, String extension)
	{
		return this.save(type, new String[]
		{ description }, new String[]
		{ extension });
	}

	private String open(ExtensionType type, String[] description, String[] extension)
	{
		if (description.length != extension.length)
			return null;

		JFileChooser chooser = new JFileChooser();
		if (type == ExtensionType.kFile)
		{
			chooser.setFileFilter(new FileNameExtensionFilter(description[0], extension[0]));
			for (int i = 1; i < description.length; i++)
				chooser.addChoosableFileFilter(new FileNameExtensionFilter(description[i], extension[i]));

			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				return (chooser.getSelectedFile().getAbsolutePath()
						.endsWith("." + ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0])
								? chooser.getSelectedFile().getAbsolutePath()
								: chooser.getSelectedFile().getAbsolutePath() + "."
										+ ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0]);
		} else if (type == ExtensionType.kDirectory)
		{
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	private String open(ExtensionType type, String description, String extension)
	{
		return this.open(type, new String[]
		{ description }, new String[]
		{ extension });
	}

	private String[] openMulti(String[] description, String[] extension)
	{
		if (description.length != extension.length)
			return null;

		String[] output = null;
		File[] chosenFiles = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileFilter(new FileNameExtensionFilter(description[0], extension[0]));
		for (int i = 1; i < description.length; i++)
			chooser.addChoosableFileFilter(new FileNameExtensionFilter(description[i], extension[i]));

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			chosenFiles = chooser.getSelectedFiles();
		output = new String[chosenFiles.length];
		for (int i = 0; i < chosenFiles.length; i++)
			output[i] = chosenFiles[i].getAbsolutePath();
		return output;
	}

	private static enum ExtensionType
	{
		kDirectory, kFile
	}

	public void displayWindow()
	{
		this.setVisible(true);
	}
}
