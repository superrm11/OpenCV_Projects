package cameraSettings;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class ChangeCameraSettings
{

	private static ViewWindow frame = null;

	private static boolean endProgram = false;

	public static void main(String[] args)
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoCapture cap = new VideoCapture();
		cap.open(0);
		cap.set(Highgui.CV_CAP_PROP_SETTINGS, 1);

		frame = new ViewWindow();
		frame.setVisible(true);
		Mat m = new Mat();
		Mat newMat = new Mat();
		while (true)
		{
			cap.read(m);
			if (m != null)
			{
				Imgproc.resize(m, newMat, new Size(640, 480));
				frame.refresh(newMat);
			}
			if (frame.isClosing() == true)
			{
				// cap.release();
				break;
			}
		}

	}
}
