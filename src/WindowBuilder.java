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

	/**
	 * Create the frame.
	 */
	public WindowBuilder() {
		setTitle("Lower Bound");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		
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
		
		JSpinner erosionSpinner = new JSpinner();
		erosionSpinner.setBounds(15, 206, 55, 26);
		contentPane.add(erosionSpinner);
		
		JSpinner dilationSpinner = new JSpinner();
		dilationSpinner.setBounds(181, 205, 55, 26);
		contentPane.add(dilationSpinner);
		
		JLabel lblErosion = new JLabel("Erosion");
		lblErosion.setBounds(49, 172, 69, 20);
		contentPane.add(lblErosion);
		
		JRadioButton recButtonErosion = new JRadioButton("Rec");
		recButtonErosion.setBounds(81, 205, 69, 29);
		contentPane.add(recButtonErosion);
		
		JRadioButton ellipseButtonErosion = new JRadioButton("Ellipse");
		ellipseButtonErosion.setBounds(81, 242, 83, 29);
		contentPane.add(ellipseButtonErosion);
		
		JRadioButton crossButtonErosion = new JRadioButton("Cross");
		crossButtonErosion.setBounds(81, 279, 83, 29);
		contentPane.add(crossButtonErosion);
		
		JLabel lblDilation = new JLabel("Dilation");
		lblDilation.setBounds(224, 172, 69, 20);
		contentPane.add(lblDilation);
		
		JLabel lblIterations = new JLabel("Iterations");
		lblIterations.setBounds(1, 230, 69, 20);
		contentPane.add(lblIterations);
		
		JLabel label = new JLabel("Iterations");
		label.setBounds(167, 230, 69, 20);
		contentPane.add(label);
		
		JRadioButton recButtonDilation = new JRadioButton("Rec");
		recButtonDilation.setBounds(259, 205, 69, 29);
		contentPane.add(recButtonDilation);
		
		JRadioButton ellipseButtonDilation = new JRadioButton("Ellipse");
		ellipseButtonDilation.setBounds(259, 242, 83, 29);
		contentPane.add(ellipseButtonDilation);
		
		JRadioButton crossButtonDilation = new JRadioButton("Cross");
		crossButtonDilation.setBounds(259, 279, 83, 29);
		contentPane.add(crossButtonDilation);
	}
}
