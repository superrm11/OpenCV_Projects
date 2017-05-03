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

public class CascadeWindow extends JFrame
{

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

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

		TextArea command_out = new TextArea();
		command_out.setBounds(0, 0, 340, 455);
		contentPane.add(command_out);

		JSeparator separator = new JSeparator();
		separator.setBounds(346, 215, 365, 2);
		contentPane.add(separator);

		JButton btnSelectPicture = new JButton("Select positive");
		btnSelectPicture.setBounds(346, 13, 115, 25);
		contentPane.add(btnSelectPicture);

		JLabel lblnoPictureSelected = new JLabel("...No Picture Selected");
		lblnoPictureSelected.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblnoPictureSelected.setBackground(Color.WHITE);
		lblnoPictureSelected.setForeground(Color.BLACK);
		lblnoPictureSelected.setBounds(346, 43, 139, 96);
		contentPane.add(lblnoPictureSelected);

		JSpinner spinner = new JSpinner();
		spinner.setBounds(553, 87, 30, 22);
		contentPane.add(spinner);

		JSpinner spinner_1 = new JSpinner();
		spinner_1.setBounds(553, 122, 30, 22);
		contentPane.add(spinner_1);

		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setBounds(514, 90, 56, 16);
		contentPane.add(lblWidth);

		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setBounds(512, 125, 56, 16);
		contentPane.add(lblHeight);

		JButton btnCreateSamples = new JButton("Generate");
		btnCreateSamples.setBounds(574, 177, 85, 25);
		contentPane.add(btnCreateSamples);

		JButton btnTrainClassifier = new JButton("Train Classifier");
		btnTrainClassifier.setBounds(574, 417, 137, 25);
		contentPane.add(btnTrainClassifier);

		JButton btnOutput = new JButton("Output");
		btnOutput.setBounds(346, 152, 75, 25);
		contentPane.add(btnOutput);

		textField = new JTextField();
		textField.setBounds(346, 178, 216, 22);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnSelectNegative = new JButton("Select negatives");
		btnSelectNegative.setBounds(553, 13, 139, 25);
		contentPane.add(btnSelectNegative);

		textField_1 = new JTextField();
		textField_1.setBounds(553, 52, 158, 22);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		JSpinner spinner_2 = new JSpinner();
		spinner_2.setBounds(681, 87, 30, 22);
		contentPane.add(spinner_2);

		JLabel lblNumber = new JLabel("Number:");
		lblNumber.setBounds(628, 90, 56, 16);
		contentPane.add(lblNumber);

		JButton btnView = new JButton("View");
		btnView.setBounds(659, 177, 64, 25);
		contentPane.add(btnView);
	}
}
