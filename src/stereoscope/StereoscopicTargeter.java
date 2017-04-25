package stereoscope;

import communications.VisionProcessor;
import communications.VisionProcessor.ParticleReport;

/**
 * A class that allows the use of two cameras connected to the raspberry pi to be used together for finding distance
 * as well as general targeting. It is important to note that when using for 2D processing (eg. targeting left and
 * right/up and down), do not create another VisionProcessor object. This class takes the average of the two and uses
 * that data.
 * @author Ryan McGee
 *
 */
public class StereoscopicTargeter
{

	private VisionProcessor camera1, camera2;

	private boolean isCamera1Left = true;

	/**
	 * Creates a stereoscopic camera system where you have 2 cameras at a given distance apart from eachother and 
	 * the program can use trigonometry to figure out distance and angles from a given target.
	 * <br><b>IMPORTANT:</b> both the cameras <b>MUST</b> be the same brand and model, and <b>MUST</b> have ALL
	 * the same settings from brightness to contrast to white balance, etc to work properly. If not, initialization and 
	 * distance and general use for targeting may or may not work (most likely not).
	 * @param isEnabled
	 */
	public StereoscopicTargeter(boolean isEnabled)
	{
		camera1 = new VisionProcessor(isEnabled);
		camera2 = new VisionProcessor(isEnabled);
	}

	private boolean hasInitialized = false;
	private boolean isFirstInitRun = false;

	/**
	 * This function looks at the largest of both blobs and determines if camera1's largest blob is farther left 
	 * than that of camera2's. If it is, then we know that the camera1 is on the left.
	 * <br> This function is designed to run the init code in robotPeriodic until it has finished initializing
	 * @return Whether or not the init function has finished.
	 */
	private boolean init()
	{
		// IF we have already initialized, do nothing.
		if (this.hasInitialized == true)
			return true;

		if (this.isFirstInitRun == true)
		{
			camera1.requestSingleProcessedImage();
			camera2.requestSingleProcessedImage();
			this.isFirstInitRun = false;
		}

		// IF camera1 and camera2 have particles, and camera1's biggest particle
		// is farther left than camera2's it is the left camera.
		if (camera1.getParticleReports() != null && camera2.getParticleReports() != null
				&& camera1.getParticleReports().length > 0 && camera2.getParticleReports().length > 0)
		{
			// Because the camera system works on the Cartesian system, farther
			// left means a lesser X.
			this.isCamera1Left = (camera1.getParticleReports()[0].centerOfRect
					.getX() < camera2.getParticleReports()[0].centerOfRect.getX());
			// We have finished the initialization phase.
			this.hasInitialized = true;
			return true;
		}

		return false;
	}

	/**
	 * This function is designed to be implemented in the robot.periodic method, as it must be run multiple times to be
	 * reliable at initializing correctly.
	 */
	public void periodic()
	{
		if (this.hasInitialized == false)
			this.init();
	}

	public double getDistanceFromLargestBlob()
	{
		return 0.0;
	}

	public double getYawAngle(ParticleReport p)
	{
		return 0.0;
	}

}
