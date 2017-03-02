package visionUtility;

public interface OperationWindows
{	
	/**
	 * 
	 * @return the parameters for the given operations for saving/displaying
	 */
	public int[] getParams();
	
	/**
	 * 
	 * @param params the parameters you want the windows to start up with initially
	 */
	public void setParams(int[] params);
	
	/**
	 * 
	 * @return the index in which each operation is in.
	 */
	public int getOperationIndex();
	
	/**
	 * Displays the parameter windows
	 */
	public void displayWindows();
	
}
