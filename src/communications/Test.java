package communications;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class Test
{

	public static void main(String[] args)
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat m = Highgui.imread("http://10.3.39.11/jpg/image.jpg");
		Highgui.imwrite("/home/pi/image.jpg", m);
		
	}

}
