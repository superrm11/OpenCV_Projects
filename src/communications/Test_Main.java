package communications;

public class Test_Main {

	public static void main(String[] args) throws InterruptedException {
		// -------------------INIT------------------------------------
		RaspberryPi rPi = new RaspberryPi();
		VisionProcessor vp = new VisionProcessor(rPi.port);
		rPi.requestNewThread();

		vp.threshold(0, 171, 0, 0, 214, 79, -102);
		// vp.dilate(11, 1);
		vp.sendOperations();
		vp.saveRawImage("C:/Users/Ryan McGee/Documents");
		vp.saveProcessedImage("C:/Users/Ryan McGee/Documents");
		vp.requestSingleProcessedImage();
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
