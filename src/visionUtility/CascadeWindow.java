package visionUtility;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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

public class CascadeWindow extends JFrame {
	/*
	 * Constant Variables
	 */
	private final int DEFAULT_WIDTH = 40, DEFAULT_HEIGHT = 40;
	private final int DEFAULT_GEN_NUM = 500;
	private final int DEFAULT_POS_IMG = 200, DEFAULT_NEG_IMG = 400;
	private final String tempNegativeTxtPath = (System.getProperty("os.name").toLowerCase().contains("windows"))
			? "%APPDATA%/v_util_cascade.txt"
			: "/tmp/v_util_cascade.txt";
	/*
	 * End Constant Variables
	 */

	/*
	 * Class Variables
	 */
	private String imgOpenPath = "", vecSavePath = "", negOpenPath_gen = "";
	private String vecOpenPath = "", trainOutPath = "", negOpenPath_train;
	private File negativeTxtFile = new File(tempNegativeTxtPath);
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
	public CascadeWindow() {
		initWindowComponents();
		createActionListeners();
		initCommandOutArea().start();
	}

	private void initWindowComponents() {
		// WINDOW INIT
		setTitle("Cascade Classifier Training");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 910, 571);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JSeparator separator = new JSeparator();
		separator.setBounds(505, 249, 365, 5);
		contentPane.add(separator);
		// END WINDOW INIT

		// BUTTON INIT
		btnSelectPositive = new JButton("Select positive");
		btnSelectPositive.setBounds(505, 47, 139, 25);
		contentPane.add(btnSelectPositive);

		btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(722, 211, 96, 25);
		contentPane.add(btnGenerate);

		btnTrainClassifier = new JButton("Train Classifier");
		btnTrainClassifier.setBounds(735, 486, 137, 25);
		contentPane.add(btnTrainClassifier);

		btnGenOutput = new JButton("Save As...");
		btnGenOutput.setBounds(505, 186, 96, 25);
		contentPane.add(btnGenOutput);

		btnGenView = new JButton("View");
		btnGenView.setBounds(818, 211, 64, 25);
		contentPane.add(btnGenView);
		btnGenView.setEnabled(false);

		btnSelNeg_Gen = new JButton("Select negatives");
		btnSelNeg_Gen.setBounds(712, 47, 158, 25);
		contentPane.add(btnSelNeg_Gen);

		btnSelNeg_Train = new JButton("Select negatives");
		btnSelNeg_Train.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSelNeg_Train.setBounds(734, 294, 148, 25);
		contentPane.add(btnSelNeg_Train);

		btnSelectVecFile = new JButton("Select Vec File");
		btnSelectVecFile.setBounds(507, 294, 127, 25);
		contentPane.add(btnSelectVecFile);

		btnTrainPreview = new JButton("Preview");
		btnTrainPreview.setBounds(634, 294, 94, 25);
		contentPane.add(btnTrainPreview);

		btnTrainOut = new JButton("Save As...");
		btnTrainOut.setBounds(507, 461, 96, 25);
		contentPane.add(btnTrainOut);
		// END BUTTON INIT

		// COMMAND TEXT AREA INIT
		commandOut = new TextArea();
		commandOut.setEditable(true);
		commandOut.setBounds(0, 0, 499, 524);
		contentPane.add(commandOut);

		// Instructions:

		commandOut.append("This part of the utility is designed to train cascade \nclassifiers. This is a type"
				+ " of object detection built into OpenCV.\n It allows the detection of symbols or shapes by using"
				+ "\nmachine learning: inputing a picture of what you want to\nsee, and recognizing it from a camera."
				+ "\n\n");

		// END COMMAND TEXT AREA INIT

		// JLABEL INIT
		lblPicturePrev = new JLabel("...No Picture Selected");
		lblPicturePrev.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblPicturePrev.setBackground(Color.WHITE);
		lblPicturePrev.setForeground(Color.BLACK);
		lblPicturePrev.setBounds(505, 77, 139, 96);
		lblPicturePrev.setLayout(new FlowLayout());
		contentPane.add(lblPicturePrev);

		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setBounds(656, 124, 56, 16);
		contentPane.add(lblWidth);

		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setBounds(656, 159, 56, 16);
		contentPane.add(lblHeight);

		JLabel lblNumber = new JLabel("Samples to");
		lblNumber.setBounds(773, 124, 70, 16);
		contentPane.add(lblNumber);

		JLabel label = new JLabel("Width:");
		label.setBounds(775, 375, 56, 16);
		contentPane.add(label);

		JLabel label_1 = new JLabel("Height:");
		label_1.setBounds(773, 410, 56, 16);
		contentPane.add(label_1);

		JLabel lblNoteWidthAnd = new JLabel("Note: Width and Height \r\nmust match the images\r\n in the .vec file!");
		lblNoteWidthAnd.setBounds(507, 439, 377, 25);
		contentPane.add(lblNoteWidthAnd);

		JLabel lblNumberOfPosotive = new JLabel("Number of Positive samples:");
		lblNumberOfPosotive.setBounds(507, 379, 169, 14);
		contentPane.add(lblNumberOfPosotive);

		JLabel lblNumberOfNegative = new JLabel("Number of Negative samples:");
		lblNumberOfNegative.setBounds(507, 412, 169, 14);
		contentPane.add(lblNumberOfNegative);

		JLabel lblIsA = new JLabel("200-400 is a good number");
		lblIsA.setForeground(Color.GRAY);
		lblIsA.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblIsA.setBounds(507, 391, 139, 14);
		contentPane.add(lblIsA);

		JLabel lblGenerallyTwiceAs = new JLabel("Generally twice as many as Positives");
		lblGenerallyTwiceAs.setForeground(Color.GRAY);
		lblGenerallyTwiceAs.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblGenerallyTwiceAs.setBounds(507, 426, 184, 14);
		contentPane.add(lblGenerallyTwiceAs);
		// END JLABEL INIT

		// SPINNER INIT
		spinGenWidth = new JSpinner();
		spinGenWidth.setBounds(698, 121, 51, 22);
		contentPane.add(spinGenWidth);
		spinGenWidth.setValue(DEFAULT_WIDTH);

		spinGenHeight = new JSpinner();
		spinGenHeight.setBounds(698, 156, 51, 22);
		contentPane.add(spinGenHeight);
		spinGenHeight.setValue(DEFAULT_HEIGHT);

		spinGenNum = new JSpinner();
		spinGenNum.setBounds(773, 156, 60, 22);
		contentPane.add(spinGenNum);
		spinGenNum.setValue(DEFAULT_GEN_NUM);

		spinTrainHeight = new JSpinner();
		spinTrainHeight.setBounds(818, 409, 52, 22);
		contentPane.add(spinTrainHeight);
		spinTrainHeight.setValue(DEFAULT_HEIGHT);

		spinTrainWidth = new JSpinner();
		spinTrainWidth.setBounds(818, 374, 52, 22);
		contentPane.add(spinTrainWidth);
		spinTrainWidth.setValue(DEFAULT_WIDTH);

		spinPosSamples = new JSpinner();
		spinPosSamples.setBounds(688, 374, 56, 20);
		contentPane.add(spinPosSamples);
		spinPosSamples.setValue(DEFAULT_POS_IMG);

		spinNegSamples = new JSpinner();
		spinNegSamples.setBounds(688, 407, 56, 20);
		contentPane.add(spinNegSamples);
		spinNegSamples.setValue(DEFAULT_NEG_IMG);
		// END SPINNER INIT

		// TEXT FIELD INIT
		genOutPrev = new JTextField();
		genOutPrev.setEditable(false);
		genOutPrev.setBounds(505, 212, 216, 22);
		contentPane.add(genOutPrev);
		genOutPrev.setColumns(10);

		negativeDirPrev = new JTextField();
		negativeDirPrev.setEditable(false);
		negativeDirPrev.setBounds(712, 86, 158, 22);
		contentPane.add(negativeDirPrev);
		negativeDirPrev.setColumns(10);

		trainVecPrev = new JTextField();
		trainVecPrev.setEditable(false);
		trainVecPrev.setBounds(507, 326, 221, 22);
		contentPane.add(trainVecPrev);
		trainVecPrev.setColumns(10);

		trainOutPrev = new JTextField();
		trainOutPrev.setEditable(false);
		trainOutPrev.setBounds(507, 488, 216, 23);
		contentPane.add(trainOutPrev);
		trainOutPrev.setColumns(10);
		// END TEXT FIELD INIT

		// CHECK BOX INIT
		chckbxUseGeneratedImages = new JCheckBox("Use generated images (above)");
		chckbxUseGeneratedImages.setBounds(507, 349, 205, 23);
		contentPane.add(chckbxUseGeneratedImages);

		chckbxMatchToGenerator = new JCheckBox("Match Size to Above");
		chckbxMatchToGenerator.setBounds(714, 349, 170, 23);
		contentPane.add(chckbxMatchToGenerator);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(734, 326, 148, 22);
		contentPane.add(textField);

		JLabel lblGenerate = new JLabel("Generate:");
		lblGenerate.setBounds(773, 140, 64, 16);
		contentPane.add(lblGenerate);

		lblTrainingTheClassifier = new JLabel("Training the Classifier");
		lblTrainingTheClassifier.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTrainingTheClassifier.setBounds(504, 257, 194, 35);
		contentPane.add(lblTrainingTheClassifier);

		lblPreparingTheSamples = new JLabel("Preparing the Samples");
		lblPreparingTheSamples.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPreparingTheSamples.setBounds(504, 13, 208, 35);
		contentPane.add(lblPreparingTheSamples);

		btnInstructSamples = new JButton("?");
		btnInstructSamples.setBounds(712, 21, 61, 20);
		contentPane.add(btnInstructSamples);

		// Buttons that display instructions

		btnInstructTrain = new JButton("?");
		btnInstructTrain.setBounds(703, 265, 70, 20);
		contentPane.add(btnInstructTrain);

		// END CHECK BOX INIT
	}

	private void createActionListeners() {
		// SPINNER LISTENERS
		spinGenHeight.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (chckbxMatchToGenerator.isSelected())
					spinTrainHeight.setValue(spinGenHeight.getValue());
			}
		});

		spinGenWidth.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (chckbxMatchToGenerator.isSelected())
					spinTrainWidth.setValue(spinGenWidth.getValue());
			}
		});
		// END SPINNER LISTENERS

		// CHECK BOX LISTENERS
		chckbxMatchToGenerator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxMatchToGenerator.isSelected()) {
					spinTrainWidth.setValue(spinGenWidth.getValue());
					spinTrainHeight.setValue(spinGenHeight.getValue());
				}
			}
		});
		chckbxUseGeneratedImages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxUseGeneratedImages.isSelected()) {
					vecOpenPath = vecSavePath;
					trainVecPrev.setText(vecSavePath);
					btnSelectVecFile.setEnabled(false);
				} else {
					btnSelectVecFile.setEnabled(true);
					trainVecPrev.setText(" ");
					vecOpenPath = "";
				}
			}
		});
		// END CHECK BOX LISTENERS

		// BUTTON LISTENERS
		btnInstructSamples.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commandOut.append("\n1: Input a picture of what you want to track using \"Select Positive\""
						+ "\n2: Input multiple pictures of a background / what you "
						+ "\n   DON'T want to track using \"Select Negatives\""
						+ "\n3: Match the width / height to the aspect ratio of the picture, but \n"
						+ "   remember a lower resolution means faster processing, but a lower success rate."
						+ "\n4: Select how many samples will be generated. Samples are generated "
						+ "\n   to emulate different angles the camera may see the positive at, so a "
						+ "\n   higher number is generally better."
						+ "\n5: \"Save As...\" will prompt you to choose the location where the sample "
						+ "\n   file .vec will be stored."
						+ "\n6: Click \"Generate\" to create and save the file. Use \"View\" to see the output.\n");
			}

		});

		btnInstructTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commandOut.append("\nTraining a classifier requires samples inside of a .vec file. If you\n"
						+ "want to simply use the one you created above, just select \"User Generated Images\".\n"
						+ "1: Use \"Select Vec File\" to choose your samples. You can preview them if you wish.\n"
						+ "2: Select multiple negative images so the program has something to compare against\n"
						+ "3: Make sure the width / height match those in the .vec file\n"
						+ "4: Select the number of positive / negative samples to use: The more samples, the more\n"
						+ "   accurate the detection will work (but with performance impacts)."
						+ "5: Choose the final classifier path, and save the .xml file.\n"
						+ "6: Move the classifier to the RIO, and setup the code to import said file.");
			}
		});

		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createSamples = true;
			}
		});

		btnTrainClassifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trainCascade = true;
			}
		});

		btnSelectPositive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = open(ExtensionType.kFile, new String[] { "JPG Image Files", "PNG Image Files" },
						new String[] { "jpg", "png" });
				if (path != null) {
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

		btnSelNeg_Gen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] paths = openMulti(new String[] { "PNG Files", "JPEG Files" }, new String[] { "png", "jpeg" });
				if (paths != null && paths.length > 0) {
					negativeDirPrev.setText("");
					for (String str : paths)
						negativeDirPrev.setText(negativeDirPrev.getText() + "\"" + str + "\",");
					try {
						PrintWriter txtWrite = new PrintWriter(negativeTxtFile);
						for(String str : paths)
							txtWrite.println(str);
						txtWrite.flush();
						txtWrite.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					
					negOpenPath_gen = tempNegativeTxtPath;
				}
			}
		});

		btnGenOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = save(ExtensionType.kFile, "Image Data File", "vec");
				if (path != null) {
					vecSavePath = path;
					genOutPrev.setText(path);
				}
			}
		});

		btnGenView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayImgs_Gen = true;
			}
		});

		btnSelectVecFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = open(ExtensionType.kFile, "Image Data Files", "vec");
				if (path != null) {
					vecOpenPath = path;
					trainVecPrev.setText(path);
				}
			}
		});

		btnTrainPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayImgs_Train = true;
			}
		});

		btnTrainOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = save(ExtensionType.kDirectory, "", "");
				if (path != null) {
					trainOutPath = path;
					trainOutPrev.setText(path);
					System.out.println(path);
				}
			}
		});
		// END BUTTON LISTENERS
	}

	private String createSamples(int numPics, int width, int height, String vecSavePath, String negPath,
			String imgPath) {
		return "opencv_createsamples -img \"" + imgPath + "\" -vec \"" + vecSavePath + "\" -bg \"" + negPath
				+ "\" -num " + numPics + " -w " + width + " -h " + height;
	}

	private String trainCascade(int numPos, int numNeg, int width, int height, String xmlSavePath, String vecOpenPath,
			String negPath) {
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
	private JButton btnInstructTrain;
	private JButton btnInstructSamples;

	private Thread initCommandOutArea() {
		return new Thread(new Runnable() {
			public void run() {
				Process currentProcess = null;
				String currentCommand = "";
				boolean isProcessing = false;
				while (true) {
					if (createSamples) {
						createSamples = false;
						commandOut.setText(" ");
						commandOut.append("Creating Samples...\nPlease wait...\n");
						currentCommand = createSamples((int) spinGenNum.getValue(), (int) spinGenWidth.getValue(),
								(int) spinGenHeight.getValue(), vecSavePath, negOpenPath_gen, imgOpenPath);
						btnGenerate.setEnabled(false);
						btnTrainClassifier.setEnabled(false);
						isProcessing = true;
					} else if (trainCascade) {
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
					} else if (displayImgs_Gen) {
						displayImgs_Gen = false;
						commandOut.setText(" ");
						commandOut.append(
								"Displaying Vec File...\nTo change images, press any key.\nTo exit, press escape.\n");
						currentCommand = "opencv_createsamples -vec \"" + vecSavePath + "\" -w "
								+ (int) spinGenWidth.getValue() + " -h " + (int) spinGenHeight.getValue();
						commandOut.append(currentCommand + "\n");
						isProcessing = true;
					} else if (displayImgs_Train) {
						displayImgs_Train = false;
						commandOut.setText(" ");
						commandOut.append(
								"Displaying Vec File...\nTo change images, press any key.\nTo exit, press escape.\n");
						currentCommand = "opencv_createsamples -vec \"" + vecOpenPath + "\" -w "
								+ (int) spinGenWidth.getValue() + " -h " + (int) spinGenHeight.getValue();
						commandOut.append(currentCommand + "\n");
						isProcessing = true;
					}

					if (isProcessing) {
						try {
							currentProcess = Runtime.getRuntime().exec(currentCommand);
						} catch (IOException e1) {
							isProcessing = false;
							e1.printStackTrace();
							commandOut.append("Failed to run command!\n");
							continue;

						}
						commandOut.append("Generated Command:\n" + currentCommand + "\n");
						String line = null;
						BufferedReader br = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()));
						try {
							while ((line = br.readLine()) != null) {
								commandOut.append(line + "\n");
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}

							btnGenerate.setEnabled(true);
							btnTrainClassifier.setEnabled(true);
							isProcessing = false;
						}
					}

					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private String save(ExtensionType type, String[] description, String[] extension) {
		if (description.length != extension.length)
			return null;

		JFileChooser chooser = new JFileChooser();
		if (type == ExtensionType.kFile) {
			chooser.setFileFilter(new FileNameExtensionFilter(description[0], extension[0]));
			for (int i = 1; i < description.length; i++)
				chooser.addChoosableFileFilter(new FileNameExtensionFilter(description[i], extension[i]));

			if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				return (chooser.getSelectedFile().getAbsolutePath()
						.endsWith("." + ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0])
								? chooser.getSelectedFile().getAbsolutePath()
								: chooser.getSelectedFile().getAbsolutePath() + "."
										+ ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0]);
		} else if (type == ExtensionType.kDirectory) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	private String save(ExtensionType type, String description, String extension) {
		return this.save(type, new String[] { description }, new String[] { extension });
	}

	private String open(ExtensionType type, String[] description, String[] extension) {
		if (description.length != extension.length)
			return null;

		JFileChooser chooser = new JFileChooser();
		if (type == ExtensionType.kFile) {
			chooser.setFileFilter(new FileNameExtensionFilter(description[0], extension[0]));
			for (int i = 1; i < description.length; i++)
				chooser.addChoosableFileFilter(new FileNameExtensionFilter(description[i], extension[i]));

			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				return (chooser.getSelectedFile().getAbsolutePath()
						.endsWith("." + ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0])
								? chooser.getSelectedFile().getAbsolutePath()
								: chooser.getSelectedFile().getAbsolutePath() + "."
										+ ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0]);
		} else if (type == ExtensionType.kDirectory) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	private String open(ExtensionType type, String description, String extension) {
		return this.open(type, new String[] { description }, new String[] { extension });
	}

	private String[] openMulti(String[] description, String[] extension) {
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

	private static enum ExtensionType {
		kDirectory, kFile
	}

	public void displayWindow() {
		this.setVisible(true);
	}
}
