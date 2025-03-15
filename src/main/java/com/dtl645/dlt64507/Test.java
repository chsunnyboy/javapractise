package com.dtl645.dlt64507;

import com.dtl645.base.BaseMsg;
import com.dtl645.base.SerialParameters;
import com.dtl645.dlt64507.message.ReadDataMsg07;
import com.dtl645.requests.MsgRequest;
import com.dtl645.requests.MsgResponse;
import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
        /*SerialPort comPort = SerialPort.getCommPort("COM1"); // 替换为你的串口名称
        comPort.setBaudRate(9600); // 设置波特率
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10, 1); // 设置超时
        comPort.openPort(); // 打开串口
        comPort.setNumStopBits(1);
        comPort.setNumDataBits(8);
        comPort.setParity(SerialPort.EVEN_PARITY);
        // 电压数据块
        //byte[] dataToSend = {0x68, 0x41, 0x07, 0x00, 0x00, 0x16, 0x04, 0x68, 0x11, 0x04, 0x33, 0x32, 0x34, 0x35, (byte)0x215, 0x16};
        //A相电压
        //byte[] dataToSend = {0x68, 0x41, 0x07, 0x00, 0x00, 0x16, 0x04, 0x68, 0x11, 0x04, 0x33, 0x34, 0x34, 0x35, (byte)0x217, 0x16};
        //读取电能量 1997
        //byte[] dataToSend = {0x68, 0x41, 0x07, 0x00, 0x00, 0x16, 0x04, 0x68, 0x01, 0x02, 0x43, (byte)0xC3, (byte)0x23B, 0x16};
        //读取电能量 2007
        byte[] dataToSend = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,0x68, 0x41, 0x07, 0x00, 0x00, 0x16, 0x04, 0x68, 0x11, 0x04, 0x33, 0x33 , 0x34 , 0x33, (byte)0x214, 0x16};

        comPort.writeBytes(dataToSend, dataToSend.length); // 发送数据

        Thread.sleep(1000);
        // 接收数据
        byte[] readBuffer = new byte[1000]; // 根据需要调整缓冲区大小
        int numRead = comPort.readBytes(readBuffer, readBuffer.length);

        String hex = bytesToHex(readBuffer, numRead);
        if (numRead > 0) {
            System.out.println("Received data: " + hex);
        }
        //AgreementDemo.analysis(hex);
        comPort.closePort(); // 关闭串口*/


        ReadDataMsg07 req = new ReadDataMsg07("41600000741",new byte[]{0x33,0x33,0x34,0x33});
        /*byte[] bytes = req.buildDlt645Request();
        comPort.writeBytes(bytes, bytes.length);
        Thread.sleep(1000);
        // 接收数据
        byte[] readBuffer = new byte[1000]; // 根据需要调整缓冲区大小
        int numRead = comPort.readBytes(readBuffer, readBuffer.length);

        String hex = bytesToHex(readBuffer, numRead);
        if (numRead > 0) {
            System.out.println("Received data: " + hex);
        }
        //AgreementDemo.analysis(hex);
        comPort.closePort(); // 关闭串口
        */


        SerialParameters params = new SerialParameters("COM1",9600,SerialPort.FLOW_CONTROL_RTS_ENABLED,SerialPort.FLOW_CONTROL_CTS_ENABLED,8,1,SerialPort.EVEN_PARITY);
        List<BaseMsg> readDataMsgs = new ArrayList<>();
        readDataMsgs.add(req);
        MsgRequest msgRequest = new MsgRequest();
        Map<String, MsgResponse> request = msgRequest.request(readDataMsgs, params);
        System.out.println(request.get("READ_DATA").getData());
    }

    private static String bytesToHex(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString();
    }
}
