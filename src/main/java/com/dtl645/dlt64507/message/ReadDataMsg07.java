package com.dtl645.dlt64507.message;

import com.dtl645.base.BaseMsg;
import com.dtl645.dlt64507.controllCode.ControllCode07;
import com.dtl645.requests.MsgResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ReadDataMsg07 extends BaseMsg {
    private static final Logger logger = LoggerFactory.getLogger(ReadDataMsg07.class);
    byte dataLength = 0x04;
    byte controllCode = ControllCode07.READ_DATA.code();
    byte[] dataFlags;

    public ReadDataMsg07(String address, byte[] dataFlags) {
        super(address);
        this.dataFlags = dataFlags;
    }
    @Override
    public byte[] buildDlt645Request() {
        byte[] wake = new byte[]{ (byte)0xFE , (byte)0xFE , (byte)0xFE , (byte)0xFE };
        byte[] headMsg = super.getHeadMsg();
        byte[] mergedArray  =  new byte[headMsg.length + 1 + 1 + dataFlags.length];

        System.arraycopy(headMsg, 0, mergedArray, 0, headMsg.length);
        mergedArray[headMsg.length] = controllCode;
        mergedArray[headMsg.length+1] = dataLength;
        System.arraycopy(dataFlags, 0, mergedArray, headMsg.length + 1 + 1, dataFlags.length);
        byte cs = this.calculateCs(mergedArray);
        byte[] result =new byte[20];
        result[0] = (byte)0xFE;
        result[1] = (byte)0xFE;
        result[2] = (byte)0xFE;
        result[3] = (byte)0xFE;
        for(int i = 0; i < headMsg.length; i++){
            result[i+4] = headMsg[i];
        }
        result[4+headMsg.length] = controllCode;
        result[4+headMsg.length+1] = dataLength;
        for(int i = 0; i < dataFlags.length; i++){
            result[4+headMsg.length+1+1+i] = dataFlags[i];
        }
        result[4 + headMsg.length + 1 + 1 + dataFlags.length] = cs;
        result[4 + headMsg.length + 1 + 1 + dataFlags.length + 1] = 0x16;

        return result;
    }

    @Override
    public MsgResponse<Float> parseDlt645Response(byte[] response,int byteLength) {
        MsgResponse<Float> resp = new MsgResponse<>();
        //查找第一个68出现的下角标
        int index = findIndex(response, (byte)0x68);
        Integer sum=0;
        for(int i = index; i < byteLength-2; i++){
            sum+=response[i];
        }

        if(response[byteLength-1] != (byte)0x16){
            String message="结束符不正确："+super.bytesToHex(response[byteLength-1]);
            logger.error(message);
            resp.setSuccess(false);
            resp.setMessage(message);
            return resp;
        }
        if(sum.byteValue()!=response[byteLength-2]){
            String message="验证码校验错误，计算验证码：" + super.bytesToHex(sum.byteValue()) + "，数据中验证码："+ super.bytesToHex(response[byteLength-2]);
            logger.info(message);
            resp.setSuccess(false);
            resp.setMessage(message);
            return resp;
        }
        byte controllCode = response[index+8];
        byte dataLength = response[index+9];
        byte[] data = new byte[dataLength-4];
        if(controllCode == ControllCode07.READ_DATA_RESP_NORMAL_NOEXISTSUBDATA.code()){
            //正常响应无后续数据
            //检查返回数据的数据表示是否和发送的一致
            for(int i=0;i<dataFlags.length;i++){
                if(dataFlags[i] != response[index+9+1+i]){
                    byte[] _dataFlags = new byte[4];
                    System.arraycopy(response, index+10, _dataFlags, 0, 4);
                    String message = "请求数据标识和响应数据标识不一致，请求数据标识："+ bytesToHex(dataFlags,4)+"，响应数据标识："+bytesToHex(_dataFlags,4);
                    logger.info(message);
                    resp.setSuccess(false);
                    resp.setMessage(message);
                }
            }
            //获取数据
            System.arraycopy(response, index+14, data, 0,dataLength-4);
            String s = parseData(data, 2);
            resp.setData(Float.parseFloat(s));
            resp.setSuccess(true);
        }else if(controllCode == ControllCode07.READ_DATA_RESP_NORMAL_EXISTSUBDATA.code()){
            //正常响应有后续数据
        }else if(controllCode == ControllCode07.READ_DATA_RESP_NORMAL_EXISTSUBDATA.code()){
            //异常响应
        }
        return resp;
    }
    public static int findIndex(byte[] array,byte value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i; //找到值为value的字节，返回下标
            }
        }
        return -1; // 未找到，返回-1
    }
}
