package communications;

public class Test_Main {

	public static void main(String[] args) {
		RaspberryPi rPi = new RaspberryPi("localhost");
		VisionProcessor vp = new VisionProcessor(rPi.getSendPort(), rPi.getReceivePort());
		rPi.requestNewThread();
		
		rPi.updatePorts();
		
		VisionProcessor vp1 = new VisionProcessor(rPi.getSendPort(), rPi.getReceivePort());
		rPi.requestNewThread();
		
	}

}
