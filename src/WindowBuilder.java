import java.awt.BorderLayout;
import java.awt.EventQueue;

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

public class WindowBuilder extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowBuilder frame = new WindowBuilder();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static JComboBox[] operations = new JComboBox[10];
	
	/**
	 * Create the frame.
	 */
	public WindowBuilder() {
		setTitle("Lower Bound");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 653, 228);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		operations[0] = new JComboBox();
		operations[0].setBounds(22, 41, 93, 20);
		contentPane.add(operations[0]);
		
		JLabel lblOperation = new JLabel("Operation 1");
		lblOperation.setBounds(22, 11, 67, 14);
		contentPane.add(lblOperation);
		
		operations[1] = new JComboBox();
		operations[1].setBounds(151, 41, 93, 20);
		contentPane.add(operations[1]);
		
		operations[2] = new JLabel("Operation 2");
		operations[2].setBounds(151, 11, 67, 14);
		contentPane.add(operations[2]);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(277, 41, 93, 20);
		contentPane.add(comboBox_2);
		
		JLabel lblOperation_2 = new JLabel("Operation 3\r\n");
		lblOperation_2.setBounds(277, 11, 67, 14);
		contentPane.add(lblOperation_2);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setBounds(407, 41, 93, 20);
		contentPane.add(comboBox_3);
		
		JLabel lblOperation_3 = new JLabel("Operation 4");
		lblOperation_3.setBounds(407, 11, 67, 14);
		contentPane.add(lblOperation_3);
		
		JComboBox comboBox_4 = new JComboBox();
		comboBox_4.setBounds(528, 41, 93, 20);
		contentPane.add(comboBox_4);
		
		JLabel lblOperation_4 = new JLabel("Operation 5");
		lblOperation_4.setBounds(528, 11, 67, 14);
		contentPane.add(lblOperation_4);
		
		JComboBox comboBox_5 = new JComboBox();
		comboBox_5.setBounds(22, 148, 93, 20);
		contentPane.add(comboBox_5);
		
		JLabel lblOperation_5 = new JLabel("Operation 6");
		lblOperation_5.setBounds(22, 118, 67, 14);
		contentPane.add(lblOperation_5);
		
		JComboBox comboBox_6 = new JComboBox();
		comboBox_6.setBounds(151, 148, 93, 20);
		contentPane.add(comboBox_6);
		
		JLabel lblOperation_6 = new JLabel("Operation 7");
		lblOperation_6.setBounds(151, 118, 67, 14);
		contentPane.add(lblOperation_6);
		
		JComboBox comboBox_7 = new JComboBox();
		comboBox_7.setBounds(277, 148, 93, 20);
		contentPane.add(comboBox_7);
		
		JLabel lblOperation_7 = new JLabel("Operation 8");
		lblOperation_7.setBounds(277, 118, 67, 14);
		contentPane.add(lblOperation_7);
		
		JComboBox comboBox_8 = new JComboBox();
		comboBox_8.setBounds(407, 148, 93, 20);
		contentPane.add(comboBox_8);
		
		JLabel lblOperation_8 = new JLabel("Operation 9");
		lblOperation_8.setBounds(407, 118, 67, 14);
		contentPane.add(lblOperation_8);
		
		JComboBox comboBox_9 = new JComboBox();
		comboBox_9.setBounds(528, 148, 93, 20);
		contentPane.add(comboBox_9);
		
		JLabel lblOperation_9 = new JLabel("Operation 10");
		lblOperation_9.setBounds(528, 118, 67, 14);
		contentPane.add(lblOperation_9);
	}
}
