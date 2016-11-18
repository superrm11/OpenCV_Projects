package thresholdUtility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class SelectOpsWindow extends JFrame
{


	private JPanel contentPane;

	public static JComboBox[] operationsComboBox = new JComboBox[10];

	public static OperationWindows[] operationWindows = new OperationWindows[10];

	private JButton[] btnParams = new JButton[10];

	/**
	 * Create the frame.
	 */
	public SelectOpsWindow()
	{
		for (int i = 0; i < 10; i++)
		{
			operations.add(new int[1]);
		}

		setTitle("Operations");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 653, 289);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		operationsComboBox[0] = new JComboBox<Object>();
		operationsComboBox[0].setBounds(22, 41, 93, 20);
		contentPane.add(operationsComboBox[0]);

		JLabel lblOperation = new JLabel("Operation 1");
		lblOperation.setBounds(22, 11, 67, 14);
		contentPane.add(lblOperation);

		operationsComboBox[1] = new JComboBox<Object>();
		operationsComboBox[1].setBounds(151, 41, 93, 20);
		contentPane.add(operationsComboBox[1]);

		JLabel lblOperation_1 = new JLabel("Operation 2");
		lblOperation_1.setBounds(151, 11, 67, 14);
		contentPane.add(lblOperation_1);

		operationsComboBox[2] = new JComboBox<Object>();
		operationsComboBox[2].setBounds(277, 41, 93, 20);
		contentPane.add(operationsComboBox[2]);

		JLabel lblOperation_2 = new JLabel("Operation 3\r\n");
		lblOperation_2.setBounds(277, 11, 67, 14);
		contentPane.add(lblOperation_2);

		operationsComboBox[3] = new JComboBox<Object>();
		operationsComboBox[3].setBounds(407, 41, 93, 20);
		contentPane.add(operationsComboBox[3]);

		JLabel lblOperation_3 = new JLabel("Operation 4");
		lblOperation_3.setBounds(407, 11, 67, 14);
		contentPane.add(lblOperation_3);

		operationsComboBox[4] = new JComboBox<Object>();
		operationsComboBox[4].setBounds(528, 41, 93, 20);
		contentPane.add(operationsComboBox[4]);

		JLabel lblOperation_4 = new JLabel("Operation 5");
		lblOperation_4.setBounds(528, 11, 67, 14);
		contentPane.add(lblOperation_4);

		operationsComboBox[5] = new JComboBox<Object>();
		operationsComboBox[5].setBounds(22, 148, 93, 20);
		contentPane.add(operationsComboBox[5]);

		JLabel lblOperation_5 = new JLabel("Operation 6");
		lblOperation_5.setBounds(22, 118, 67, 14);
		contentPane.add(lblOperation_5);

		operationsComboBox[6] = new JComboBox<Object>();
		operationsComboBox[6].setBounds(151, 148, 93, 20);
		contentPane.add(operationsComboBox[6]);

		JLabel lblOperation_6 = new JLabel("Operation 7");
		lblOperation_6.setBounds(151, 118, 67, 14);
		contentPane.add(lblOperation_6);

		operationsComboBox[7] = new JComboBox<Object>();
		operationsComboBox[7].setBounds(277, 148, 93, 20);
		contentPane.add(operationsComboBox[7]);

		JLabel lblOperation_7 = new JLabel("Operation 8");
		lblOperation_7.setBounds(277, 118, 67, 14);
		contentPane.add(lblOperation_7);

		operationsComboBox[8] = new JComboBox<Object>();
		operationsComboBox[8].setBounds(407, 148, 93, 20);
		contentPane.add(operationsComboBox[8]);

		JLabel lblOperation_8 = new JLabel("Operation 9");
		lblOperation_8.setBounds(407, 118, 67, 14);
		contentPane.add(lblOperation_8);

		operationsComboBox[9] = new JComboBox<Object>();
		operationsComboBox[9].setBounds(528, 148, 93, 20);
		contentPane.add(operationsComboBox[9]);

		JLabel lblOperation_9 = new JLabel("Operation 10");
		lblOperation_9.setBounds(528, 118, 67, 14);
		contentPane.add(lblOperation_9);

		btnParams[0] = new JButton("Params");
		btnParams[0].setBounds(0, 83, 115, 29);
		contentPane.add(btnParams[0]);

		btnParams[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[0].getSelectedIndex())
				{
				case 0:
					operationWindows[0] = null;
					ThresholdUtility.operationsWindow.operations.set(0, new int[1]);
					break;
				case 1:
					if (operationWindows[0] == null
							|| !operationWindows[0].getClass().getName().contains("DilateWindow"))
					{
						operationWindows[0] = new DilateWindow(0);
					}
					operationWindows[0].displayWindows();
					break;
				case 2:
					if (operationWindows[0] == null
							|| !operationWindows[0].getClass().getName().contains("ErodeWindow"))
					{
						operationWindows[0] = new ErodeWindow(0);
					}
					operationWindows[0].displayWindows();
					break;
				case 3:
					if (operationWindows[0] == null
							|| !operationWindows[0].getClass().getName().contains("ThresholdWindows"))
					{
						operationWindows[0] = new ThresholdWindows(0);
					}
					operationWindows[0].displayWindows();

					break;
				}
			}
		});

		btnParams[1] = new JButton("Params");
		btnParams[1].setBounds(130, 83, 115, 29);
		contentPane.add(btnParams[1]);
		
		btnParams[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[1].getSelectedIndex())
				{
				case 0:
					operationWindows[1] = null;
					ThresholdUtility.operationsWindow.operations.set(1, new int[1]);
					break;
				case 1:
					if (operationWindows[1] == null
							|| !operationWindows[1].getClass().getName().contains("DilateWindow"))
					{
						operationWindows[1] = new DilateWindow(1);
					}
					operationWindows[1].displayWindows();
					break;
				case 2:
					if (operationWindows[1] == null
							|| !operationWindows[1].getClass().getName().contains("ErodeWindow"))
					{
						operationWindows[1] = new ErodeWindow(1);
					}
					operationWindows[1].displayWindows();
					break;
				case 3:
					if (operationWindows[1] == null
							|| !operationWindows[1].getClass().getName().contains("ThresholdWindows"))
					{
						operationWindows[1] = new ThresholdWindows(1);
					}
					operationWindows[1].displayWindows();

					break;
				}
			}
		});

		btnParams[2] = new JButton("Params");
		btnParams[2].setBounds(260, 83, 115, 29);
		contentPane.add(btnParams[2]);
		
		btnParams[2].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[2].getSelectedIndex())
				{
				case 0:
					operationWindows[2] = null;
					ThresholdUtility.operationsWindow.operations.set(2, new int[1]);
					break;
				case 1:
					if (operationWindows[2] == null
							|| !operationWindows[2].getClass().getName().contains("DilateWindow"))
					{
						operationWindows[2] = new DilateWindow(2);
					}
					operationWindows[2].displayWindows();
					break;
				case 2:
					if (operationWindows[2] == null
							|| !operationWindows[2].getClass().getName().contains("ErodeWindow"))
					{
						operationWindows[2] = new ErodeWindow(2);
					}
					operationWindows[2].displayWindows();
					break;
				case 3:
					if (operationWindows[2] == null
							|| !operationWindows[2].getClass().getName().contains("ThresholdWindows"))
					{
						operationWindows[2] = new ThresholdWindows(2);
					}
					operationWindows[2].displayWindows();

					break;
				}
			}
		});

		btnParams[3] = new JButton("Params");
		btnParams[3].setBounds(390, 83, 115, 29);
		contentPane.add(btnParams[3]);
		
		btnParams[3].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[3].getSelectedIndex())
				{
				case 0:
					operationWindows[3] = null;
					ThresholdUtility.operationsWindow.operations.set(3, new int[1]);
					break;
				case 1:
					if (operationWindows[3] == null
							|| !operationWindows[3].getClass().getName().contains("DilateWindow"))
					{
						operationWindows[3] = new DilateWindow(3);
					}
					operationWindows[3].displayWindows();
					break;
				case 2:
					if (operationWindows[3] == null
							|| !operationWindows[3].getClass().getName().contains("ErodeWindow"))
					{
						operationWindows[3] = new ErodeWindow(3);
					}
					operationWindows[3].displayWindows();
					break;
				case 3:
					if (operationWindows[3] == null
							|| !operationWindows[3].getClass().getName().contains("ThresholdWindows"))
					{
						operationWindows[3] = new ThresholdWindows(3);
					}
					operationWindows[3].displayWindows();

					break;
				}
			}
		});

		btnParams[4] = new JButton("Params");
		btnParams[4].setBounds(516, 83, 115, 29);
		contentPane.add(btnParams[4]);
		
		btnParams[4].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[4].getSelectedIndex())
				{
				case 0:
					operationWindows[4] = null;
					ThresholdUtility.operationsWindow.operations.set(4, new int[1]);
					break;
				case 1:
					if (operationWindows[4] == null
							|| !operationWindows[4].getClass().getName().contains("DilateWindow"))
					{
						operationWindows[4] = new DilateWindow(4);
					}
					operationWindows[4].displayWindows();
					break;
				case 2:
					if (operationWindows[4] == null
							|| !operationWindows[4].getClass().getName().contains("ErodeWindow"))
					{
						operationWindows[4] = new ErodeWindow(4);
					}
					operationWindows[4].displayWindows();
					break;
				case 3:
					if (operationWindows[4] == null
							|| !operationWindows[4].getClass().getName().contains("ThresholdWindows"))
					{
						operationWindows[4] = new ThresholdWindows(4);
					}
					operationWindows[4].displayWindows();

					break;
				}
			}
		});

		btnParams[5] = new JButton("Params");
		btnParams[5].setBounds(0, 188, 115, 29);
		contentPane.add(btnParams[5]);
		
		btnParams[5].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[5].getSelectedIndex())
				{
				case 0:
					operationWindows[5] = null;
					ThresholdUtility.operationsWindow.operations.set(5, new int[1]);
					break;
				case 1:
					if (operationWindows[5] == null
							|| !operationWindows[5].getClass().getName().contains("DilateWindow"))
					{
						operationWindows[5] = new DilateWindow(5);
					}
					operationWindows[5].displayWindows();
					break;
				case 2:
					if (operationWindows[5] == null
							|| !operationWindows[5].getClass().getName().contains("ErodeWindow"))
					{
						operationWindows[5] = new ErodeWindow(5);
					}
					operationWindows[5].displayWindows();
					break;
				case 3:
					if (operationWindows[5] == null
							|| !operationWindows[5].getClass().getName().contains("ThresholdWindows"))
					{
						operationWindows[5] = new ThresholdWindows(5);
					}
					operationWindows[5].displayWindows();

					break;
				}
			}
		});

		btnParams[6] = new JButton("Params");
		btnParams[6].setBounds(130, 188, 115, 29);
		contentPane.add(btnParams[6]);
		
		btnParams[6].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[6].getSelectedIndex())
				{
				case 0:
					operationWindows[6] = null;
					ThresholdUtility.operationsWindow.operations.set(6, new int[1]);
					break;
				case 1:
					if (operationWindows[6] == null
							|| !operationWindows[6].getClass().getName().contains("DilateWindow"))
					{
						operationWindows[6] = new DilateWindow(6);
					}
					operationWindows[6].displayWindows();
					break;
				case 2:
					if (operationWindows[6] == null
							|| !operationWindows[6].getClass().getName().contains("ErodeWindow"))
					{
						operationWindows[6] = new ErodeWindow(6);
					}
					operationWindows[6].displayWindows();
					break;
				case 3:
					if (operationWindows[6] == null
							|| !operationWindows[6].getClass().getName().contains("ThresholdWindows"))
					{
						operationWindows[6] = new ThresholdWindows(6);
					}
					operationWindows[6].displayWindows();

					break;
				}
			}
		});

		btnParams[7] = new JButton("Params");
		btnParams[7].setBounds(260, 188, 115, 29);
		contentPane.add(btnParams[7]);
		
		btnParams[7].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[7].getSelectedIndex())
				{
				case 0:
					operationWindows[7] = null;
					ThresholdUtility.operationsWindow.operations.set(7, new int[1]);
					break;
				case 1:
					if (operationWindows[7] == null
							|| !operationWindows[7].getClass().getName().contains("DilateWindow"))
					{
						operationWindows[7] = new DilateWindow(7);
					}
					operationWindows[7].displayWindows();
					break;
				case 2:
					if (operationWindows[7] == null
							|| !operationWindows[7].getClass().getName().contains("ErodeWindow"))
					{
						operationWindows[7] = new ErodeWindow(7);
					}
					operationWindows[7].displayWindows();
					break;
				case 3:
					if (operationWindows[7] == null
							|| !operationWindows[7].getClass().getName().contains("ThresholdWindows"))
					{
						operationWindows[7] = new ThresholdWindows(7);
					}
					operationWindows[7].displayWindows();

					break;
				}
			}
		});

		btnParams[8] = new JButton("Params");
		btnParams[8].setBounds(390, 188, 115, 29);
		contentPane.add(btnParams[8]);
		
		btnParams[8].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[8].getSelectedIndex())
				{
				case 0:
					operationWindows[8] = null;
					ThresholdUtility.operationsWindow.operations.set(8, new int[1]);
					break;
				case 1:
					if (operationWindows[8] == null
							|| !operationWindows[8].getClass().getName().contains("DilateWindow"))
					{
						operationWindows[8] = new DilateWindow(8);
					}
					operationWindows[8].displayWindows();
					break;
				case 2:
					if (operationWindows[8] == null
							|| !operationWindows[8].getClass().getName().contains("ErodeWindow"))
					{
						operationWindows[8] = new ErodeWindow(8);
					}
					operationWindows[8].displayWindows();
					break;
				case 3:
					if (operationWindows[8] == null
							|| !operationWindows[8].getClass().getName().contains("ThresholdWindows"))
					{
						operationWindows[8] = new ThresholdWindows(8);
					}
					operationWindows[8].displayWindows();

					break;
				}
			}
		});

		btnParams[9] = new JButton("Params");
		btnParams[9].setBounds(516, 188, 115, 29);
		contentPane.add(btnParams[9]);
		
		btnParams[9].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				switch (operationsComboBox[9].getSelectedIndex())
				{
				case 0:
					operationWindows[9] = null;
					ThresholdUtility.operationsWindow.operations.set(9, new int[1]);
					break;
				case 1:
					if (operationWindows[9] == null
							|| !operationWindows[9].getClass().getName().contains("DilateWindow"))
					{
						operationWindows[9] = new DilateWindow(9);
					}
					operationWindows[9].displayWindows();
					break;
				case 2:
					if (operationWindows[9] == null
							|| !operationWindows[9].getClass().getName().contains("ErodeWindow"))
					{
						operationWindows[9] = new ErodeWindow(9);
					}
					operationWindows[9].displayWindows();
					break;
				case 3:
					if (operationWindows[9] == null
							|| !operationWindows[9].getClass().getName().contains("ThresholdWindows"))
					{
						operationWindows[9] = new ThresholdWindows(9);
					}
					operationWindows[9].displayWindows();

					break;
				}
			}
		});

		for (int i = 0; i < operationsComboBox.length; i++)
		{
			operationsComboBox[i].addItem("");
			operationsComboBox[i].addItem("Dilate");
			operationsComboBox[i].addItem("Erode");
			operationsComboBox[i].addItem("Threshold");

		}

	}

	public void setOperations(ArrayList<int[]> al)
	{
		for (int i = 0; i < al.size(); i++)
		{
			if (al.get(i)[0] == 3)
			{
				if (operationWindows[i] == null || !operationWindows[i].getClass().getName().contains("Threshold"))
				{
					operationWindows[i] = new ThresholdWindows(i);
					operationsComboBox[i].setSelectedIndex(3);
				}
				operationWindows[i].setParams(al.get(i));
			} else if (al.get(i)[0] == 2)
			{
				if (operationWindows[i] == null || !operationWindows[i].getClass().getName().contains("Erode"))
				{
					operationWindows[i] = new ErodeWindow(i);
					operationsComboBox[i].setSelectedIndex(2);
				}
				operationWindows[i].setParams(al.get(i));
			} else if (al.get(i)[0] == 1)
			{
				if (operationWindows[i] == null || !operationWindows[i].getClass().getName().contains("Dilate"))
				{
					operationWindows[i] = new DilateWindow(i);
					operationsComboBox[i].setSelectedIndex(1);
				}
				operationWindows[i].setParams(al.get(i));
			}
		}
	}

	public ArrayList<int[]> operations = new ArrayList<int[]>();
}
