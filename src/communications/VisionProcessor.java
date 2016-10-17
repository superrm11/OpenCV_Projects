package communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VisionProcessor {
	
	
	static class SearchForClient extends Thread{
		int recievePort;
		int sendPort;
		public SearchForClient(int recievePort, int sendPort){
			this.recievePort = recievePort;
			this.sendPort = sendPort;
		}
		public void run(){
			try {
				ServerSocket recieveListener = new ServerSocket(recievePort);
				ServerSocket sendListener = new ServerSocket(sendPort);
				
				Socket recieveSocket = recieveListener.accept();
				Socket sendSocket = sendListener.accept();
				
				OutputStream os = sendSocket.getOutputStream();
				InputStream is = recieveSocket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
