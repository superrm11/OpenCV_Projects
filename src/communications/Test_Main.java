package communications;

public class Test_Main {

	public static void main(String[] args) throws InterruptedException {
		// -------------------INIT------------------------------------
		RaspberryPi rPi = new RaspberryPi();
		VisionProcessor vp = new VisionProcessor(rPi.port);
		rPi.requestNewThread();
		
		vp.setThresholdValues(0, 22, 0, 31, 107, 4, -33);
		vp.threshold();
//		vp.dilate(11, 2);
		vp.sendOperations();
//		Thread.sleep(5000);
//		vp.requestSingleProcessedImage();
//		if (vp.blobs != null) {
//			for (int i = 0; i < vp.blobs.size(); i++) {
//				for (int k = 0; k < vp.blobs.get(i).length; k++) {
//					System.out.print(vp.blobs.get(i)[k] + "  ");
//				}
//				System.out.println("");
//			}
//		} else
//			System.out.println("There are no blobs.");

		// ------------------PERIODIC---------------------------------

		// while(true){
		//
		// }

	}

}
