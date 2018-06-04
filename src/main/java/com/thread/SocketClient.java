package com.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class SocketClient {

	public static void main(String[] args) {
		Socket socket;
		try {
			socket = new Socket("127.0.0.1",7777);
			System.out.println(socket.getLocalPort());
			OutputStream os=socket.getOutputStream();
			PrintWriter print=new PrintWriter(os);
			print.write("Hello server");
			print.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
