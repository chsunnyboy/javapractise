package com.bjrltcp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) throws Exception {
        // 创建一个DatagramSocket实例
        DatagramSocket clientSocket = new DatagramSocket();
        clientSocket.setSoTimeout(5000);
        InetAddress IPAddress = InetAddress.getByName("localhost");

            byte[] sendData = "测试UDP连接等待问题".getBytes();

            // 创建发送的DatagramPacket实例
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            // 发送数据报文
            clientSocket.send(sendPacket);

            byte[] receiveData = new byte[1024];
            // 创建接收的DatagramPacket实例
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            // 接收服务端的响应
            clientSocket.receive(receivePacket);
            // 获取并打印响应数据
            String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("从服务器接收到: " + modifiedSentence);

    }
}
