package visionUtility;

import java.awt.TextArea;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class DisplayWindow
{
	DisplayWindow()
	{
		startWindow();
	}

	private JFrame frame;
	private TextArea textArea;

	private void startWindow()
	{
		frame = new JFrame("Output");
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(0, 0, 400, 300);
		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(0, 0, 382, 253);
		frame.getContentPane().add(textArea);
	}

	public void displayWindow()
	{
		if (VisionUtility.arrayOfPoints == null)
			return;
		frame.setVisible(true);
		
		this.setBlobInfo(VisionUtility.sortRects(VisionUtility.toRects(VisionUtility.arrayOfPoints)));
	}

	public boolean isVisible()
	{
		return frame.isVisible();
	}

	public void setBlobInfo(ArrayList<int[]> blobs)
	{
		textArea.setText("");
		for (int i = 0; i < blobs.size(); i++)
		{
			textArea.append("Blob " + i + ":\n\t" + "X Center: " + blobs.get(i)[0] + "\n\t" + "Y Center: "
					+ blobs.get(i)[1] + "\n\t" + "Width: " + blobs.get(i)[2] + "\n\t" + "Height: " + blobs.get(i)[3]
					+ "\n\t" + "Aspect Ratio: " + (blobs.get(i)[2] / (double) blobs.get(i)[3]) + "\n\t" + "Area: "
					+ (blobs.get(i)[2] * blobs.get(i)[3]) + "\n\n");
		}
	}

	public static int numOfBlobs = 0;
}
