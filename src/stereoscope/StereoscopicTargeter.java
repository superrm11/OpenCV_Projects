package stereoscope;

import communications.VisionProcessor;

public class StereoscopicTargeter extends VisionProcessor
{

	public StereoscopicTargeter(boolean isEnabled)
	{
		super(isEnabled);
		super.requestNewThread();
		super.requestNewThread();
	}

	public double getDistanceFromLargestBlob()
	{

		return 0.0;
	}

}
