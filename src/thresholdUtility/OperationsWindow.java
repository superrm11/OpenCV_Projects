package thresholdUtility;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class OperationsWindow extends JFrame
{

	private JPanel contentPane;

	public static JComboBox[] operationsComboBox = new JComboBox[10];
	
	
	/**
	 * Create the frame.
	 */
	public OperationsWindow()
	{
		
		setTitle("Operations");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 653, 289);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		operationsComboBox[0] = new JComboBox();
		operationsComboBox[0].setBounds(22, 41, 93, 20);
		contentPane.add(operationsComboBox[0]);

		JLabel lblOperation = new JLabel("Operation 1");
		lblOperation.setBounds(22, 11, 67, 14);
		contentPane.add(lblOperation);

		operationsComboBox[1] = new JComboBox();
		operationsComboBox[1].setBounds(151, 41, 93, 20);
		contentPane.add(operationsComboBox[1]);

		JLabel lblOperation_1 = new JLabel("Operation 2");
		lblOperation_1.setBounds(151, 11, 67, 14);
		contentPane.add(lblOperation_1);

		operationsComboBox[2] = new JComboBox();
		operationsComboBox[2].setBounds(277, 41, 93, 20);
		contentPane.add(operationsComboBox[2]);

		JLabel lblOperation_2 = new JLabel("Operation 3\r\n");
		lblOperation_2.setBounds(277, 11, 67, 14);
		contentPane.add(lblOperation_2);

		operationsComboBox[3] = new JComboBox();
		operationsComboBox[3].setBounds(407, 41, 93, 20);
		contentPane.add(operationsComboBox[3]);

		JLabel lblOperation_3 = new JLabel("Operation 4");
		lblOperation_3.setBounds(407, 11, 67, 14);
		contentPane.add(lblOperation_3);

		operationsComboBox[4] = new JComboBox();
		operationsComboBox[4].setBounds(528, 41, 93, 20);
		contentPane.add(operationsComboBox[4]);

		JLabel lblOperation_4 = new JLabel("Operation 5");
		lblOperation_4.setBounds(528, 11, 67, 14);
		contentPane.add(lblOperation_4);

		operationsComboBox[5] = new JComboBox();
		operationsComboBox[5].setBounds(22, 148, 93, 20);
		contentPane.add(operationsComboBox[5]);

		JLabel lblOperation_5 = new JLabel("Operation 6");
		lblOperation_5.setBounds(22, 118, 67, 14);
		contentPane.add(lblOperation_5);

		operationsComboBox[6] = new JComboBox();
		operationsComboBox[6].setBounds(151, 148, 93, 20);
		contentPane.add(operationsComboBox[6]);

		JLabel lblOperation_6 = new JLabel("Operation 7");
		lblOperation_6.setBounds(151, 118, 67, 14);
		contentPane.add(lblOperation_6);

		operationsComboBox[7] = new JComboBox();
		operationsComboBox[7].setBounds(277, 148, 93, 20);
		contentPane.add(operationsComboBox[7]);

		JLabel lblOperation_7 = new JLabel("Operation 8");
		lblOperation_7.setBounds(277, 118, 67, 14);
		contentPane.add(lblOperation_7);

		operationsComboBox[8] = new JComboBox();
		operationsComboBox[8].setBounds(407, 148, 93, 20);
		contentPane.add(operationsComboBox[8]);

		JLabel lblOperation_8 = new JLabel("Operation 9");
		lblOperation_8.setBounds(407, 118, 67, 14);
		contentPane.add(lblOperation_8);

		operationsComboBox[9] = new JComboBox();
		operationsComboBox[9].setBounds(528, 148, 93, 20);
		contentPane.add(operationsComboBox[9]);

		JLabel lblOperation_9 = new JLabel("Operation 10");
		lblOperation_9.setBounds(528, 118, 67, 14);
		contentPane.add(lblOperation_9);

		JButton[] btnParams = new JButton[10];

		btnParams[0] = new JButton("Params");
		btnParams[0].setBounds(0, 83, 115, 29);
		contentPane.add(btnParams[0]);

		btnParams[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[0].getSelectedIndex())
				{
				case 1:

					break;
				case 2:

					break;
				case 3:
					ThresholdUtility.thresholdWindows = new ThresholdWindows(0);
					break;
				}
			}
		});

		btnParams[1] = new JButton("Params");
		btnParams[1].setBounds(130, 83, 115, 29);
		contentPane.add(btnParams[1]);

		btnParams[2] = new JButton("Params");
		btnParams[2].setBounds(260, 83, 115, 29);
		contentPane.add(btnParams[2]);

		btnParams[3] = new JButton("Params");
		btnParams[3].setBounds(390, 83, 115, 29);
		contentPane.add(btnParams[3]);

		btnParams[4] = new JButton("Params");
		btnParams[4].setBounds(516, 83, 115, 29);
		contentPane.add(btnParams[4]);

		btnParams[5] = new JButton("Params");
		btnParams[5].setBounds(516, 188, 115, 29);
		contentPane.add(btnParams[5]);

		btnParams[6] = new JButton("Params");
		btnParams[6].setBounds(390, 188, 115, 29);
		contentPane.add(btnParams[6]);

		btnParams[7] = new JButton("Params");
		btnParams[7].setBounds(260, 188, 115, 29);
		contentPane.add(btnParams[7]);

		btnParams[8] = new JButton("Params");
		btnParams[8].setBounds(130, 188, 115, 29);
		contentPane.add(btnParams[8]);

		btnParams[9] = new JButton("Params");
		btnParams[9].setBounds(0, 188, 115, 29);
		contentPane.add(btnParams[9]);

		for (int i = 0; i < operationsComboBox.length; i++)
		{
			operationsComboBox[i].addItem("");
			operationsComboBox[i].addItem("Dilate");
			operationsComboBox[i].addItem("Erode");
			operationsComboBox[i].addItem("Threshold");

		}

	}

	public static boolean hasThresholdOperation()
	{
		for (int i = 0; i < operations.size(); i++)
		{
			if (operations.get(i)[0] == 3)
			{
				return true;
			}
		}
		return false;
	}

	public static ArrayList<int[]> getOperations()
	{
		return operations;
	}

	private static ArrayList<int[]> operations = new ArrayList<int[]>();
}
