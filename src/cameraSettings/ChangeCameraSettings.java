package cameraSettings;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.opencv.core.Core;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

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

		Resolution windowRes;

		switch ((int) Math.round(screenRes.getWidth()))
		{
		case 2560:
			windowRes = Resolution.k1600x1200;
			break;
		case 1920:
			windowRes = Resolution.k960x720;
			break;
		case 1280:
			windowRes = Resolution.k800x600;
			break;
		case 1024:
			windowRes = Resolution.k800x600;
			break;
		case 900:
			windowRes = Resolution.k640x480;
			break;
		default:
			windowRes = Resolution.k640x480;
		}

		frame = new ViewWindow(windowRes);
		frame.setVisible(true);

		while (true)
		{
			if (frame.isClosing() == true)
			{
				System.exit(0);
			}
		}

	}

	private static Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();

	public enum Resolution
	{
		k320x240, k480x320, k640x480, k800x600, k960x720, k1600x1200
	}

}
