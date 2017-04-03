package communications;

public class Test_Main
{

	public static void main(String[] args) throws InterruptedException
	{

		// -------------------INIT------------------------------------
		// VisionProcessor vp = new VisionProcessor(true);
		VisionProcessor.sortBySize(new int[]
		{ 3, 4, 5, 9, 7, 8, 6, 2, 0, 1 });
		// vp.threshold(0, 0, 0, 255, 255, 255, 0, VisionProcessor.BGR);
		// vp.sendOperations();
		//
		// vp.requestSingleProcessedImage();

		// -------------------ENDINIT-----------------------------------

		// ------------------PERIODIC------------------------

		// while (true)
		// {
		// if (vp.particleReportsAreNew && vp.getParticleReports() != null)
		// {
		// System.out.println(vp.getParticleReports().length);
		// System.out.println(vp.getParticleReports()[0].rectArea);
		// vp.particleReportsAreNew = false;
		// vp.requestSingleProcessedImage();
		// }
		// }
	}

}
