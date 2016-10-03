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

public class SlideBarTest extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SlideBarTest frame = new SlideBarTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SlideBarTest() {
		setTitle("Lower Bound");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenimage = new JMenuItem("OpenImage");
		mnFile.add(mntmOpenimage);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JSlider slider = new JSlider();
		slider.setBounds(123, 16, 200, 26);
		contentPane.add(slider);
		
		JSlider slider_1 = new JSlider();
		slider_1.setBounds(123, 71, 200, 26);
		contentPane.add(slider_1);
		
		JSlider slider_2 = new JSlider();
		slider_2.setBounds(123, 124, 200, 26);
		contentPane.add(slider_2);
		
		JLabel lblRed = new JLabel("Red");
		lblRed.setBounds(39, 16, 69, 20);
		contentPane.add(lblRed);
		
		JLabel lblGreen = new JLabel("Green");
		lblGreen.setBounds(39, 77, 69, 20);
		contentPane.add(lblGreen);
		
		JLabel lblBlue = new JLabel("Blue");
		lblBlue.setBounds(39, 124, 69, 20);
		contentPane.add(lblBlue);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(338, 16, 55, 26);
		contentPane.add(spinner);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setBounds(338, 71, 55, 26);
		contentPane.add(spinner_1);
		
		JSpinner spinner_2 = new JSpinner();
		spinner_2.setBounds(338, 124, 55, 26);
		contentPane.add(spinner_2);
		
		JSlider slider_3 = new JSlider();
		slider_3.setBounds(123, 166, 200, 26);
		contentPane.add(slider_3);
		
		JLabel lblBrightness = new JLabel("Brightness");
		lblBrightness.setBounds(39, 172, 82, 20);
		contentPane.add(lblBrightness);
		
		JSpinner spinner_3 = new JSpinner();
		spinner_3.setBounds(338, 166, 55, 26);
		contentPane.add(spinner_3);
	}
}
