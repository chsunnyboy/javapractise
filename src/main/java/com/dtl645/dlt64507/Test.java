package com.dtl645.dlt64507;

import com.dtl645.base.SerialParameters;
import com.dtl645.dlt64507.message.ReadDataMsg07;
import com.dtl645.requests.MsgRequest;
import com.dtl645.requests.MsgResponse;
import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) throws Exception {

        SerialParameters params = new SerialParameters("COM3",9600,SerialPort.FLOW_CONTROL_DISABLED,SerialPort.FLOW_CONTROL_DISABLED,8,1,SerialPort.EVEN_PARITY);
        String id="041600000741";
//        String id="000001703515";

        MsgRequest msgRequest = new MsgRequest(params);
        ReadDataMsg07 readMsg = new ReadDataMsg07();
        readMsg.setMsgRequest(msgRequest);
        readMsg.addDataFlags(id,"03010000",7);

        /*InputStream resourceAsStream = Test.class.getResourceAsStream("/dataflags/dataflags.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        Stream<String> lines = bufferedReader.lines();
        AtomicInteger times= new AtomicInteger();*/

        msgRequest.openPort();
        int i=0;
        while(!msgRequest.isOpen() && i++ < 5){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            msgRequest.openPort();
        }


        /*lines.forEach(v->{
            readMsg.clearDataFlags();
            if(!v.startsWith("#") && !"".equals(v)){
                readMsg.addDataFlags(id,v.split(",")[0],Integer.valueOf(v.split(",")[1]));
                Map<String, byte[]> response = msgRequest.request(readMsg.getByteMsgs());
                Map<String, MsgResponse<String>> stringMsgResponseMap = readMsg.parseDlt645Response(response);

                for(Map.Entry<String, MsgResponse<String>> entry : stringMsgResponseMap.entrySet()){
                    logger.info(entry.getKey()+"：");
                    if(entry.getValue().isSuccess()){
                        logger.info(entry.getValue().getData());
                    }else{
                        logger.info(entry.getValue().getMessage());
                    }
                }
                times.getAndIncrement();
                logger.info("读取数据标识对应的数据次数**************************************"+times.get());
            }
        });
        msgRequest.closePort();*/

        Map<String, byte[]> response = msgRequest.request(readMsg.getByteMsgs());
        Map<String, MsgResponse<String>> stringMsgResponseMap = readMsg.parseDlt645Response(response);

        for(Map.Entry<String, MsgResponse<String>> entry : stringMsgResponseMap.entrySet()){
            logger.info(entry.getKey()+"：");
            if(entry.getValue().isSuccess()){
                logger.info(entry.getValue().getData());
            }else{
                logger.info(entry.getValue().getMessage());
            }
        }
    }

    private static String bytesToHex(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString();
    }
}
