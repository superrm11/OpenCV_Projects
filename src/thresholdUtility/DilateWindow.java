package thresholdUtility;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DilateWindow implements OperationWindows
{
	private int operationIndex;

	public DilateWindow(int operationIndex)
	{
		this.operationIndex = operationIndex;
		createDilationWindow();
	}

	private int[] lastSetParams =
	{ 1, 0, 0 };

	public JFrame frmDilation;

	private JSlider iterationsSlider;
	private JSlider sizeSlider;
	private JSpinner iterationsSpinner;
	private JSpinner sizeSpinner;

	public void createDilationWindow()
	{
		frmDilation = new JFrame();
		frmDilation.setTitle("Dilation");
		frmDilation.getContentPane().setLayout(null);
		frmDilation.setSize(400, 300);
		frmDilation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblIterations = new JLabel("Iterations");
		lblIterations.setBounds(15, 16, 69, 20);
		frmDilation.getContentPane().add(lblIterations);

		JLabel lblSize = new JLabel("Size");
		lblSize.setBounds(15, 102, 69, 20);
		frmDilation.getContentPane().add(lblSize);

		iterationsSlider = new JSlider();
		iterationsSlider.setBounds(15, 52, 200, 26);
		frmDilation.getContentPane().add(iterationsSlider);

		iterationsSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				iterationsSpinner.setValue(iterationsSlider.getValue() / 10);
			}
		});

		sizeSlider = new JSlider();
		sizeSlider.setBounds(15, 138, 200, 26);
		frmDilation.getContentPane().add(sizeSlider);

		sizeSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				sizeSpinner.setValue(sizeSlider.getValue() / 10);
			}

		});

		iterationsSpinner = new JSpinner();
		iterationsSpinner.setBounds(241, 52, 56, 26);
		frmDilation.getContentPane().add(iterationsSpinner);

		iterationsSpinner.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				iterationsSlider.setValue((int) iterationsSpinner.getValue() * 10);
			}
		});

		sizeSpinner = new JSpinner();
		sizeSpinner.setBounds(241, 138, 56, 26);
		frmDilation.getContentPane().add(sizeSpinner);
		frmDilation.setVisible(true);

		sizeSpinner.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				sizeSlider.setValue((int) sizeSpinner.getValue() * 10);
			}
		});

		frmDilation.addWindowListener(new WindowListener()
		{

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
				lastSetParams = getParams();
				ThresholdUtility.operationsWindow.operations.set(operationIndex, getParams());
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
				if(ThresholdUtility.operationsWindow.operations.get(0)[0] == 1){
					
				}
			}

		});

	}

	@Override
	public int[] getParams()
	{
		return new int[]
		{ 1, (int) sizeSpinner.getValue(), (int) iterationsSpinner.getValue() };
	}

	@Override
	public void setParams(int[] params)
	{
		sizeSpinner.setValue(params[1]);
		sizeSlider.setValue(params[1] / 10);
		iterationsSpinner.setValue(params[2]);
		iterationsSlider.setValue(params[2] / 10);
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
