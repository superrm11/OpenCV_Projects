package thresholdUtility;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RemoveSmObsWindow implements OperationWindows
{
	private final int operationIndex;
	private int[] lastSetParams =
	{ 4, 0, 0 };

	public RemoveSmObsWindow(int operationIndex)
	{
		this.operationIndex = operationIndex;
	}

	@Override
	public int[] getParams()
	{
		return new int[]
		{ 4, (int) sizeSpinner.getValue(), (int) iterationsSpinner.getValue() };
	}

	@Override
	public void setParams(int[] params)
	{
		sizeSpinner.setValue(params[1]);
		iterationsSpinner.setValue(params[2]);
	}

	@Override
	public int getOperationIndex()
	{
		return operationIndex;
	}

	@Override
	public void displayWindows()
	{
		frame.setVisible(true);
	}

	public JFrame frame;

	private JSlider iterationsSlider;
	private JSlider sizeSlider;
	private JSpinner iterationsSpinner;
	private JSpinner sizeSpinner;

	public void createDilationWindow()
	{
		frame = new JFrame();
		frame.setTitle("Dilation");
		frame.getContentPane().setLayout(null);
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblIterations = new JLabel("Iterations");
		lblIterations.setBounds(15, 16, 69, 20);
		frame.getContentPane().add(lblIterations);

		JLabel lblSize = new JLabel("Size");
		lblSize.setBounds(15, 102, 69, 20);
		frame.getContentPane().add(lblSize);

		iterationsSlider = new JSlider();
		iterationsSlider.setBounds(15, 52, 200, 26);
		frame.getContentPane().add(iterationsSlider);

		iterationsSlider.setValue(0);

		iterationsSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				iterationsSpinner.setValue(iterationsSlider.getValue() / 10);

				lastSetParams = getParams();
				ThresholdUtility.operationsWindow.operations.set(operationIndex, getParams());
			}
		});

		sizeSlider = new JSlider();
		sizeSlider.setBounds(15, 138, 200, 26);
		frame.getContentPane().add(sizeSlider);

		sizeSlider.setValue(0);

		sizeSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				sizeSpinner.setValue(sizeSlider.getValue() / 10);

				lastSetParams = getParams();
				ThresholdUtility.operationsWindow.operations.set(operationIndex, getParams());
			}

		});

		iterationsSpinner = new JSpinner();
		iterationsSpinner.setBounds(241, 52, 56, 26);
		frame.getContentPane().add(iterationsSpinner);

		iterationsSpinner.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				iterationsSlider.setValue((int) iterationsSpinner.getValue() * 10);

				lastSetParams = getParams();
				ThresholdUtility.operationsWindow.operations.set(operationIndex, getParams());
			}
		});

		sizeSpinner = new JSpinner();
		sizeSpinner.setBounds(241, 138, 56, 26);
		frame.getContentPane().add(sizeSpinner);

		sizeSpinner.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				sizeSlider.setValue((int) sizeSpinner.getValue() * 10);

				lastSetParams = getParams();
				ThresholdUtility.operationsWindow.operations.set(operationIndex, getParams());
			}
		});

	}
}
