package visionUtility;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RemoveSmObsWindow implements OperationWindows
{
	private final int operationIndex;

	public RemoveSmObsWindow(int operationIndex)
	{
		createSmObsWindow();
		this.operationIndex = operationIndex;
	}

	@Override
	public int[] getParams()
	{
		return new int[]
		{ 4, (int) sizeSpinner.getValue()};
	}

	@Override
	public void setParams(int[] params)
	{
		sizeSpinner.setValue(params[1]);
	}

	@Override
	public int getOperationIndex()
	{
		return operationIndex;
	}

	@Override
	public void displayWindows()
	{
		this.frame.setVisible(true);
	}

	public JFrame frame;

	private JSlider sizeSlider;
	private JSpinner sizeSpinner;

	public void createSmObsWindow()
	{
		frame = new JFrame();
		frame.setTitle("Remove Small Objects");
		frame.getContentPane().setLayout(null);
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblSize = new JLabel("Size");
		lblSize.setBounds(15, 102, 69, 20);
		frame.getContentPane().add(lblSize);

		sizeSlider = new JSlider();
		sizeSlider.setBounds(15, 138, 200, 26);
		frame.getContentPane().add(sizeSlider);

		sizeSlider.setValue(0);

		sizeSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				sizeSpinner.setValue(sizeSlider.getValue());
				VisionUtility.operationsWindow.operations.set(operationIndex, getParams());
			}

		});

		sizeSpinner = new JSpinner();
		sizeSpinner.setBounds(241, 138, 56, 26);
		frame.getContentPane().add(sizeSpinner);

		sizeSpinner.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				sizeSlider.setValue((int) sizeSpinner.getValue());
				VisionUtility.operationsWindow.operations.set(operationIndex, getParams());
			}
		});

	}
}
