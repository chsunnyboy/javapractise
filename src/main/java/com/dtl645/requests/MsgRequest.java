package com.dtl645.requests;

import com.dtl645.base.SerialParameters;
import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MsgRequest {
    private static final Logger logger = LoggerFactory.getLogger(MsgRequest.class);

    private SerialPort comPort;
    private SerialParameters parameters;
    public MsgRequest(SerialParameters parameters) {
        this.parameters = parameters;
    }

    public boolean openPort(){
        comPort = SerialPort.getCommPort(parameters.getPortName()); // 替换为你的串口名称
        comPort.setBaudRate(parameters.getBaudRate()); // 设置波特率
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING , 1000, 1); // 设置超时
        comPort.setNumStopBits(parameters.getStopbits());
        comPort.setNumDataBits(parameters.getDatabits());
        comPort.setParity(parameters.getParity());
        //comPort.setFlowControl(parameters.getFlowControlIn()|parameters.getFlowControlOut());
        if(!comPort.isOpen()){
            return comPort.openPort(); // 打开串口
        }
        return true;
    }
    private boolean isOpen() {
        return comPort.isOpen();
    }
    private boolean closePort() {
        if (comPort != null && comPort.isOpen()) {
            return comPort.closePort();
        }
        return true;
    }

    public Map<String,byte[]> request(Map<String, byte[]> msgs){
        Map<String,byte[]> result = new HashMap<>();
        openPort();
        int i=0;
        while(!isOpen() && i++ < 5){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            openPort();
        }
        for(String flagName : msgs.keySet()){
            byte[] bytes = msgs.get(flagName);
            logger.info("发送报文：{}，数据标识：{}", bytesToHex(bytes,bytes.length),flagName);
            try {
                byte[] resBytes= writeAndRead(bytes);
                result.put(flagName,resBytes);
            } catch (Exception e) {
                logger.error("获取数据异常：{}，数据标识：{}",getExceptionMsg(e),flagName);
                result.put(flagName,null);
            }
        }
        closePort(); // 关闭串口
        return result;
    }
    public String getExceptionMsg(Exception e){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
    public byte[] writeAndRead(byte[] msg) throws Exception {
        int writeBytes = comPort.writeBytes(msg, msg.length);
        if(writeBytes > 0){
            int times=0;
            while (comPort.bytesAvailable() <= 0 && times++ < 5) {
                Thread.sleep(50);
            }
            // 接收数据
            byte[] readBuffer = new byte[1000]; // 根据需要调整缓冲区大小
            int numRead = comPort.readBytes(readBuffer, readBuffer.length);
            logger.info("收到报文{}", bytesToHex(readBuffer,numRead));
            List<byte[]> bytesList = new ArrayList<>();
            bytesList.add(Arrays.copyOf(readBuffer,numRead));
            //假定还有数据没有读取完
            int inc=1;
            while(inc<10){
                if(numRead>0 && readBuffer[numRead-1] != 0x16){
                    logger.info("缓冲区中数据不完整，继续读取");
                    Thread.sleep(100);
                    if(comPort.bytesAvailable()>0){
                        readBuffer = new byte[1000];
                        numRead = comPort.readBytes(readBuffer, readBuffer.length);
                        bytesList.add(Arrays.copyOf(readBuffer,numRead));
                        logger.info("收到报文{}", bytesToHex(readBuffer,numRead));
                    }
                }else{
                    break;
                }
                inc++;
            }
            if(bytesList.size()>0){
                AtomicInteger size = new AtomicInteger();
                bytesList.forEach(v-> size.addAndGet(v.length));
                byte[] bytes_all=new byte[size.get()];
                for(int i=0,j=0;i<bytesList.size();i++){
                    System.arraycopy(bytesList.get(i),0,bytes_all,j,bytesList.get(i).length);
                    j+=bytesList.get(i).length;
                }
                return bytes_all;
            }
        }
        return null;
    }

    public String bytesToHex(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString();
    }
}
