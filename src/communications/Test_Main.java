package communications;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class Test_Main
{

	public static void main(String[] args) throws InterruptedException
	{
		
		// -------------------INIT------------------------------------
		RaspberryPi rPi = new RaspberryPi();
		VisionProcessor vp = new VisionProcessor(rPi.port);
		rPi.requestNewThread();

		vp.threshold(0, 77, 0, 87, 255, 94, 0, VisionProcessor.BGR);
		vp.erode(3, 1);
		vp.dilate(3, 2);
		vp.sendOperations();

		vp.saveRawImage("/home/pi/pics");
		vp.saveProcessedImage("/home/pi/pics");

		// -------------------ENDINIT-----------------------------------
		long startTime = System.currentTimeMillis();
		long timerStartTime = System.currentTimeMillis();
		long newTime = 0;
		long endTime;
		vp.requestSingleProcessedImage();

		// ------------------PERIODIC------------------------

		int average = 0;
		int count = 0;
		while (count < 100)
		{
			if (vp.blobsAreNew)
			{
				endTime = System.currentTimeMillis();
				 System.out.println("Time it took: " + (endTime - startTime) +
				 " millis");
				 int[] blob = vp.getWidestBlob(vp.blobs);
//				 System.out.println("X: " + (blob[0] + (blob[2] / 2)) + "\nY: " + (blob[1] + (blob[3] / 2)));
				count++;
				startTime = System.currentTimeMillis();
				vp.requestSingleProcessedImage();
				vp.blobsAreNew = false;

			}

		}
		rPi.stopAllThreads();
		vp.stopThread();
		
		

		// Thread.sleep(1000);
		//
		// vp = new VisionProcessor(rPi.port);
		// rPi.requestNewThread();
		//
		// vp.threshold(0, 77, 0, 87, 255, 94, 0);
		// vp.erode(3, 1);
		// vp.dilate(3, 2);
		// vp.sendOperations();
		//
		// vp.saveRawImage("/home/pi/pics");
		// vp.saveProcessedImage("/home/pi/pics");
		//
		// startTime = System.currentTimeMillis();
		// timerStartTime = System.currentTimeMillis();
		// newTime = 0;
		// vp.requestSingleProcessedImage();
		// while (newTime < 10 * 1000)
		// {
		// if (vp.blobsAreNew)
		// {
		// endTime = System.currentTimeMillis();
		// System.out.println("Time it took: " + (endTime - startTime) + "
		// millis");
		// startTime = System.currentTimeMillis();
		// vp.requestSingleProcessedImage();
		// vp.blobsAreNew = false;
		// }
		// newTime = System.currentTimeMillis() - timerStartTime;
		// Thread.sleep(1);
		//
		// }
		// rPi.stopAllThreads();
		// vp.stopThread();

	}

}
