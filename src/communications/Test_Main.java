package communications;

public class Test_Main {

	public static void main(String[] args) {
		RaspberryPi rPi = new RaspberryPi();
		VisionProcessor vp = new VisionProcessor(rPi.getSendPort(), rPi.getReceivePort());
		rPi.requestNewThread();
		
		vp.setThresholdValues(100, 200, 0, 150, 250, 50, -50);
		
	}

}
