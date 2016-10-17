package communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VisionProcessor {
	/**
	 * Starts the server wait for the vision processing client to connect.
	 * @author Ryan McGee
	 * 
	 * @param recievePort The port that will recieve data from the client.
	 * @param sendPort	The port that will send data to the client.
	 */
	SearchForClient a;
	public void startServer(int recievePort, int sendPort){
		a = new SearchForClient(recievePort, sendPort);
		a.start();
	}
	
	public void requestProcessedImage(){
		a.requestProcessedImage = true;
	}
	
	
	
	static class SearchForClient extends Thread{
		int recievePort;
		int sendPort;
		
		boolean requestProcessedImage = false;
	
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
				
				while(true){
					if(requestProcessedImage = true){
						
						requestProcessedImage = false;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
