package com.dtl645.requests;

import com.dtl645.base.BaseMsg;
import com.dtl645.base.SerialParameters;
import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgRequest {
    private static final Logger logger = LoggerFactory.getLogger(MsgRequest.class);
    public Map<String,MsgResponse> request(List<BaseMsg> readDataMsgs, SerialParameters parameters){
        Map<String,MsgResponse> result = new HashMap<String,MsgResponse>();
        SerialPort comPort = SerialPort.getCommPort(parameters.getPortName()); // 替换为你的串口名称
        comPort.setBaudRate(parameters.getBaudRate()); // 设置波特率
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10, 1); // 设置超时
        comPort.openPort(); // 打开串口
        comPort.setNumStopBits(parameters.getStopbits());
        comPort.setNumDataBits(parameters.getDatabits());
        comPort.setParity(parameters.getParity());
        //comPort.setFlowControl(parameters.getFlowControlIn()|parameters.getFlowControlOut());

        for(BaseMsg readDataMsg : readDataMsgs){
            byte[] bytes = readDataMsg.buildDlt645Request();
            logger.info("发送报文{}", bytesToHex(bytes,bytes.length));
            comPort.writeBytes(bytes, bytes.length);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.info("读取数据休眠异常{}",e.getMessage());
            }
            // 接收数据
            byte[] readBuffer = new byte[1000]; // 根据需要调整缓冲区大小
            int numRead = comPort.readBytes(readBuffer, readBuffer.length);
            logger.info("收到报文{}", bytesToHex(readBuffer,numRead));
            MsgResponse msgResponse = readDataMsg.parseDlt645Response(readBuffer, numRead);
            result.put("READ_DATA",msgResponse);
        }
        comPort.closePort(); // 关闭串口
        return result;
    }

    public String bytesToHex(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString();
    }
}
