package communications;

import java.util.ArrayList;
import java.util.Timer;

public class Test_Main
{

	public static void main(String[] args) throws InterruptedException
	{
		// -------------------INIT------------------------------------
		RaspberryPi rPi = new RaspberryPi();
		VisionProcessor vp = new VisionProcessor(rPi.port);
		rPi.requestNewThread();

		vp.threshold(0, 77, 0, 87, 255, 94, 0);
		vp.erode(3, 1);
		vp.dilate(3, 2);
		vp.sendOperations();

		vp.saveRawImage("/home/pi/pics");
		vp.saveProcessedImage("/home/pi/pics");

		long startTime = System.currentTimeMillis();
		long endTime;
		vp.requestSingleProcessedImage();
		while(true)
		{
			if(vp.blobsAreNew)
			{
				endTime = System.currentTimeMillis();
				System.out.println("Time it took: " + (endTime - startTime) + " millis");
				startTime = System.currentTimeMillis();
				vp.requestSingleProcessedImage();
				vp.blobsAreNew = false;
			}
			Thread.sleep(1);
			
		}
//		for (int i = 0; i < vp.blobs.size(); i++)
//		{
//			System.out.println("X: " + vp.blobs.get(i)[0] + "\tY: " + vp.blobs.get(i)[1]);
//
//		}

		// if (vp.blobs != null) {
		// for (int i = 0; i < vp.blobs.size(); i++) {
		// for (int k = 0; k < vp.blobs.get(i).length; k++) {
		// System.out.print(vp.blobs.get(i)[k] + " ");
		// }
		// System.out.println("");
		// }
		// } else
		// System.out.println("There are no blobs.");

		// ------------------PERIODIC---------------------------------

		// while(true){
		//
		// }

	}

}
