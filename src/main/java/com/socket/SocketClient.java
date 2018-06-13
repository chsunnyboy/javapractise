package com.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {

	public static void main(String[] args) {
		Socket socket;
		try {
			socket = new Socket("127.0.0.1",8888);
			OutputStream os=socket.getOutputStream();
			PrintWriter print=new PrintWriter(os);
			print.write("Hello server");
			print.close();
			
//			InputStream is=socket.getInputStream();
//			InputStreamReader isr=new InputStreamReader(is);
//			System.out.println(isr.read());
			
			
			
//			socket = new Socket("127.0.0.1",7777);
//			DataInputStream datais=new DataInputStream(socket.getInputStream());
//			DataOutputStream dataos=new DataOutputStream(socket.getOutputStream());
//			dataos.writeUTF("runtime");
//			String str=datais.readUTF();
//			System.out.println(str);
//			dataos.close();
//			datais.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
