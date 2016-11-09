package thresholdUtility;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;

public class DilateWindow implements OperationWindows
{
	private int operationIndex;
	public DilateWindow(int operationIndex){
		this.operationIndex = operationIndex;
	}
	
	private int[] lastSetParams = {1, 0, 0};
	
	public JFrame frmDilation;
	
	private JSlider iterationsSlider;
	private JSlider sizeSlider;
	private JSpinner iterationsSpinner;
	private JSpinner sizeSpinner;
	public void createDilationWindow(){
		frmDilation = new JFrame();
		frmDilation.setTitle("Dilation");
		frmDilation.getContentPane().setLayout(null);
		
		JLabel lblIterations = new JLabel("Iterations");
		lblIterations.setBounds(15, 16, 69, 20);
		frmDilation.getContentPane().add(lblIterations);
		
		JLabel lblSize = new JLabel("Size");
		lblSize.setBounds(15, 102, 69, 20);
		frmDilation.getContentPane().add(lblSize);
		
		iterationsSlider = new JSlider();
		iterationsSlider.setBounds(15, 52, 200, 26);
		frmDilation.getContentPane().add(iterationsSlider);
		
		sizeSlider = new JSlider();
		sizeSlider.setBounds(15, 138, 200, 26);
		frmDilation.getContentPane().add(sizeSlider);
		
		iterationsSpinner = new JSpinner();
		iterationsSpinner.setBounds(241, 52, 56, 26);
		frmDilation.getContentPane().add(iterationsSpinner);
		
		sizeSpinner = new JSpinner();
		sizeSpinner.setBounds(241, 138, 56, 26);
		frmDilation.getContentPane().add(sizeSpinner);
		frmDilation.setVisible(true);
		
		frmDilation.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0)
			{	
			}

			@Override
			public void windowClosed(WindowEvent arg0)
			{
				
			}

			@Override
			public void windowClosing(WindowEvent arg0)
			{	
			}

			@Override
			public void windowDeactivated(WindowEvent arg0)
			{
			}

			@Override
			public void windowDeiconified(WindowEvent arg0)
			{
			}

			@Override
			public void windowIconified(WindowEvent arg0)
			{	
			}

			@Override
			public void windowOpened(WindowEvent arg0)
			{
			}
			
		});
		
	}

	@Override
	public int[] getParams()
	{
		return null;
	}

	@Override
	public void setParams(int[] params)
	{
		
	}

	@Override
	public int getOperationIndex()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void displayWindows()
	{
		frmDilation.setVisible(true);
	}
}
