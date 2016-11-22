package thresholdUtility;

public class RemoveSmObsWindow implements OperationWindows
{
	private final int operationIndex;

	public RemoveSmObsWindow(int operationIndex)
	{
		this.operationIndex = operationIndex;
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
		return operationIndex;
	}

	@Override
	public void displayWindows()
	{

	}
	
	private void createWindow()
	{
		
	}
}
