package com.bjrltcp;

import com.bjrltcp.beans.wrap.DeviceData;
import com.bjrltcp.beans.wrap.HeartBeatData;
import com.bjrltcp.beans.wrap.LoginData;
import com.bjrltcp.tools.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TestTcpData {
    public static void main(String[] args) throws Exception {
        //testTimingSendData();
        testheartbeatSendData();
    }

    /**
     * 定时上报和突变上报
     * @throws Exception
     */
    public static void testTimingSendData() throws Exception {
        //登录
        // 构造TCP报文数据，包括指定的包头和包尾
        String desCode="01234567";
        String clientCode="bjrl202303990002";
        LoginData loginData = new LoginData(desCode,clientCode);

        Socket socket = new Socket("8.140.29.244", 3333);
        // 与服务器建立输入输出流
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        // 向服务器发送数据
        outputStream.write(loginData.getDataByte());
        outputStream.flush();
        System.out.println(socket.isConnected()?"socket 已经建立连接******************":"socket 没有建立连接******************");

        if(socket.isConnected()){
            //Thread.sleep(10000);
            Map<String,Object> yxDataMap = new HashMap<String,Object>();
            yxDataMap.put("1011", Byte.valueOf((byte)1));
            yxDataMap.put("1012", Byte.valueOf((byte)0));
            yxDataMap.put("1013", Byte.valueOf((byte)1));
            yxDataMap.put("1014", Byte.valueOf((byte)0));

            Map<String,Object> ycDataMap = new HashMap<String,Object>();
            ycDataMap.put("1015", Float.valueOf(13.12f));
            ycDataMap.put("1016", Float.valueOf(13.13f));
            ycDataMap.put("1017", Float.valueOf(13.14f));
            ycDataMap.put("1018", Float.valueOf(13.15f));

            DeviceData deviceData = new DeviceData(2,yxDataMap,ycDataMap);
            System.out.println("Generated Packet: " + TcpTools.bytesToHex(deviceData.getDataByte()));
            // 向服务器发送数据
            outputStream.write(deviceData.getDataByte());
            outputStream.flush();

            HeartBeatData heartBeatData = new HeartBeatData("bjrl202303990002");
            // 向服务器发送数据
            outputStream.write(heartBeatData.getDataByte());
            outputStream.flush();

            Thread.sleep(10000);
            //接受服务器数据
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                //byteArrayOutputStream.write(buffer, 0, bytesRead);
                TcpResp.processServerMsg(buffer);
                buffer = new byte[1024];
            }

            //关闭socket
            socket.close();
        }
    }

    public static void testheartbeatSendData() throws Exception{
        //登录
        // 构造TCP报文数据，包括指定的包头和包尾
        String desCode="01234567";
        String clientCode="bjrl202303990002";
        LoginData loginData = new LoginData(desCode, clientCode);

        Socket socket = new Socket("8.140.29.244", 3333);

        // 与服务器建立输入输出流
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        System.out.println("Generated Packet: " + TcpTools.bytesToHex(loginData.getDataByte()));
        // 向服务器发送数据
        outputStream.write(loginData.getDataByte());
        outputStream.flush();

        System.out.println(socket.isConnected()?"socket 已经建立连接******************":"socket 没有建立连接******************");

        if(socket.isConnected()){

            HeartBeatData heartBeatData = new HeartBeatData("bjrl202303990002");
            // 向服务器发送数据
            System.out.println("发送心跳数据******************");
            outputStream.write(heartBeatData.getDataByte());
            outputStream.flush();

            //接受服务器数据
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                TcpResp.processServerMsg(buffer);
                buffer = new byte[1024];
            }

            //关闭socket
            //socket.close();
        }
    }
}
