package com.bjrltcp;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        // 创建一个DatagramSocket实例，并指定监听的端口
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];

        while (true) {
            // 创建一个DatagramPacket实例，用于接收数据
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            // 接收数据报文
            serverSocket.receive(receivePacket);
            // 获取数据
            String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("接收到: " + sentence);

            // 获取客户端的地址和端口号
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            // 回复客户端
            String capitalizedSentence = sentence.toUpperCase();
            byte[] sendData = capitalizedSentence.getBytes();
            // 创建发送的DatagramPacket实例
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            // 发送数据报文
            serverSocket.send(sendPacket);
        }
    }
}
