package communications;

public class Test_Main
{

	public static void main(String[] args) throws InterruptedException
	{

		// -------------------INIT------------------------------------
		RaspberryPi rPi = new RaspberryPi();
		VisionProcessor vp = new VisionProcessor(rPi);

		vp.threshold(71, 102, 79, 112, 145, 148, 0, VisionProcessor.BGR);
		vp.dilate(2, 2);
		vp.sendOperations();

		vp.saveRawImage("/home/pi/pics");
		vp.saveProcessedImage("/home/pi/pics");
		vp.requestSingleProcessedImage();
		// -------------------ENDINIT-----------------------------------

		// ------------------PERIODIC------------------------
		while (true)
		{
			if (vp.blobsAreNew && vp.blobs != null)
			{
				vp.getParticleReport();
				if (vp.particleReports.length > 0)
					System.out.println("Center x: " + vp.particleReports[0].centerOfRect.getX());
				vp.requestSingleProcessedImage();
				vp.blobsAreNew = false;
			}
		}

	}

}
