package visionUtility;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;

public class CascadeWindow extends JFrame
{

	private JPanel contentPane;
	private JTextField genOutPrev, negativeDirPrev, trainVecPrev, trainOutPrev;

	/**
	 * Create the frame.
	 */
	public CascadeWindow()
	{
		setTitle("Cascade Classifier Training");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 741, 502);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		TextArea commandOut = new TextArea();
		commandOut.setEditable(false);
		commandOut.setBounds(0, 0, 340, 455);
		contentPane.add(commandOut);

		JSeparator separator = new JSeparator();
		separator.setBounds(346, 215, 365, 2);
		contentPane.add(separator);

		JButton btnSelectPositive = new JButton("Select positive");
		btnSelectPositive.setBounds(346, 13, 115, 25);
		contentPane.add(btnSelectPositive);

		JLabel lblPicturePrev = new JLabel("...No Picture Selected");
		lblPicturePrev.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblPicturePrev.setBackground(Color.WHITE);
		lblPicturePrev.setForeground(Color.BLACK);
		lblPicturePrev.setBounds(346, 43, 139, 96);
		contentPane.add(lblPicturePrev);

		JSpinner spinGenWidth = new JSpinner();
		spinGenWidth.setBounds(553, 87, 30, 22);
		contentPane.add(spinGenWidth);

		JSpinner spinGenHeight = new JSpinner();
		spinGenHeight.setBounds(553, 122, 30, 22);
		contentPane.add(spinGenHeight);

		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setBounds(514, 90, 56, 16);
		contentPane.add(lblWidth);

		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setBounds(512, 125, 56, 16);
		contentPane.add(lblHeight);

		JButton btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(574, 177, 85, 25);
		contentPane.add(btnGenerate);

		JButton btnTrainClassifier = new JButton("Train Classifier");
		btnTrainClassifier.setBounds(574, 417, 137, 25);
		contentPane.add(btnTrainClassifier);

		JButton btnGenOutput = new JButton("Output");
		btnGenOutput.setBounds(346, 152, 75, 25);
		contentPane.add(btnGenOutput);

		genOutPrev = new JTextField();
		genOutPrev.setEditable(false);
		genOutPrev.setBounds(346, 178, 216, 22);
		contentPane.add(genOutPrev);
		genOutPrev.setColumns(10);

		JButton btnSelectNegative = new JButton("Select negatives");
		btnSelectNegative.setBounds(553, 13, 139, 25);
		contentPane.add(btnSelectNegative);

		negativeDirPrev = new JTextField();
		negativeDirPrev.setEditable(false);
		negativeDirPrev.setBounds(553, 52, 158, 22);
		contentPane.add(negativeDirPrev);
		negativeDirPrev.setColumns(10);

		JSpinner spinGenNum = new JSpinner();
		spinGenNum.setBounds(681, 87, 30, 22);
		contentPane.add(spinGenNum);

		JLabel lblNumber = new JLabel("Number:");
		lblNumber.setBounds(628, 90, 56, 16);
		contentPane.add(lblNumber);

		JButton btnGenView = new JButton("View");
		btnGenView.setBounds(659, 177, 64, 25);
		contentPane.add(btnGenView);
		
		JCheckBox chckbxUseGeneratedImages = new JCheckBox("Use generated images (above)");
		chckbxUseGeneratedImages.setBounds(346, 226, 184, 23);
		contentPane.add(chckbxUseGeneratedImages);
		
		JButton btnSelectVecFile = new JButton("Select Vec File");
		btnSelectVecFile.setBounds(346, 256, 106, 23);
		contentPane.add(btnSelectVecFile);
		
		JButton btnTrainPreview = new JButton("Preview");
		btnTrainPreview.setBounds(451, 256, 75, 23);
		contentPane.add(btnTrainPreview);
		
		trainVecPrev = new JTextField();
		trainVecPrev.setBounds(346, 279, 184, 20);
		contentPane.add(trainVecPrev);
		trainVecPrev.setColumns(10);
		
		JButton btnTrainOut = new JButton("Output");
		btnTrainOut.setBounds(346, 395, 75, 23);
		contentPane.add(btnTrainOut);
		
		trainOutPrev = new JTextField();
		trainOutPrev.setBounds(346, 419, 216, 20);
		contentPane.add(trainOutPrev);
		trainOutPrev.setColumns(10);
		
		JSpinner spinTrainHeight = new JSpinner();
		spinTrainHeight.setBounds(594, 314, 30, 22);
		contentPane.add(spinTrainHeight);
		
		JSpinner spinTrainWidth = new JSpinner();
		spinTrainWidth.setBounds(594, 279, 30, 22);
		contentPane.add(spinTrainWidth);
		
		JLabel label = new JLabel("Width:");
		label.setBounds(555, 282, 56, 16);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Height:");
		label_1.setBounds(553, 317, 56, 16);
		contentPane.add(label_1);
		
		JLabel lblNoteWidthAnd = new JLabel("Note: Width and Height \r\nmust match the images\r\n in the .vec file!");
		lblNoteWidthAnd.setBounds(346, 371, 321, 25);
		contentPane.add(lblNoteWidthAnd);
		
		JCheckBox chckbxMatchToGenerator = new JCheckBox("Match to Generator");
		chckbxMatchToGenerator.setBounds(553, 256, 131, 23);
		contentPane.add(chckbxMatchToGenerator);
		
		JLabel lblNumberOfPosotive = new JLabel("Number of Positive samples:");
		lblNumberOfPosotive.setBounds(346, 310, 150, 14);
		contentPane.add(lblNumberOfPosotive);
		
		JSpinner spinPosSamples = new JSpinner();
		spinPosSamples.setBounds(487, 307, 56, 20);
		contentPane.add(spinPosSamples);
		
		JLabel lblNumberOfNegative = new JLabel("Number of Negative samples:");
		lblNumberOfNegative.setBounds(346, 343, 150, 14);
		contentPane.add(lblNumberOfNegative);
		
		JSpinner spinNegSamples = new JSpinner();
		spinNegSamples.setBounds(487, 340, 56, 20);
		contentPane.add(spinNegSamples);
		
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
	}
}
