package com.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.net.ServerSocketFactory;

public class SocketTest {

	public static void main(String[] args) {
		Thread serverThread = new Thread(new Runnable() {
		      public void run() {
		        try {
		          BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(3);
		          ThreadFactory threadFactory = Executors.defaultThreadFactory();
		          RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler()
		          {
		            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		              System.out.println("{} is rejected"+r);
		            }
		          };
		          ThreadPoolExecutor threadpool = new ThreadPoolExecutor(1, 3, 20L, TimeUnit.SECONDS, workQueue, threadFactory, rejectedExecutionHandler);
		          
		          ServerSocket server = ServerSocketFactory.getDefault().createServerSocket(8888);
		          System.out.println("SocketServer startup at port:{}"+Integer.valueOf(8888));
		          try {
		        	  
		            for (;;) {
		              Socket socket = server.accept();
		              System.out.println("socket server is already connected."+socket);
		              
		              threadpool.execute(new RuntimeProcessor(socket));
		            }
		            
		          } catch (Throwable t) { System.out.println("error"+ t);
		          }
		        }
		        catch (Throwable t) {
		        	System.out.println("runtime server startup error"+ t);
		        }
		      }
		    }, "RuntimeServer");
		    //serverThread.setDaemon(true);
		    serverThread.setPriority(1);
		    serverThread.start();
		    System.out.println(new Date());
	}

}
final class RuntimeProcessor implements Runnable
{
	Socket socket;

	public RuntimeProcessor(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			InputStream in = socket.getInputStream();
			InputStreamReader isr=new InputStreamReader(in);
			BufferedReader br=new BufferedReader(isr);
			
			OutputStream out =socket.getOutputStream();
			OutputStreamWriter oswr=new OutputStreamWriter(out);
			String str=null;
			while((str=br.readLine())!=null){
				System.out.println("客户端发送了："+str);
				oswr.write(str);
			}
			
//			in.close();
//			isr.close();
//			br.close();
		} catch (Throwable t) {
			System.out.println("error"+ t);
			try {
				if ((socket != null) && (!socket.isClosed())) {
					socket.close();
				}
			} catch (IOException e) {
				System.out.println("error"+ e);
			}
		}
	}
}