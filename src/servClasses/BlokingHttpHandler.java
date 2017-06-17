package servClasses;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BlokingHttpHandler {
	public static void main(String[] args) throws IOException  {
		
		ServerSocket ss = new ServerSocket(8085);
		while(true){
			Socket s = ss.accept();
			System.out.println("Client accept");
			new Thread (new SocketProcessor(s)).start();
		}
	}
}

