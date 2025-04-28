package com.dtl645.dlt64507.message;

import com.dtl645.base.BaseMsg;
import com.dtl645.dlt64507.controllCode.ControllCode07;
import com.dtl645.requests.MsgRequest;
import com.dtl645.requests.MsgResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadDataMsg07 extends BaseMsg {
    private static final Logger logger = LoggerFactory.getLogger(ReadDataMsg07.class);
    Map<String,byte[]> byteMsgs = new HashMap<String,byte[]>();
    Map<String,Integer> flagFormats=new HashMap<>();
    MsgRequest msgRequest;
    public void setMsgRequest(MsgRequest msgRequest) {
        this.msgRequest = msgRequest;
    }
    public void addDataFlags(String address , String reqFlagName, Integer flagFormat) {
        byte[] bytes = this.buildDlt645Request(address,reqFlagName);
        byteMsgs.put(address+":"+reqFlagName,bytes);
        flagFormats.put(address+":"+reqFlagName,flagFormat);
    }
    public void clearDataFlags() {
        byteMsgs.clear();
        flagFormats.clear();
    }
    public Map<String, byte[]> getByteMsgs() {
        return byteMsgs;
    }
    public void setByteMsgs(Map<String, byte[]> byteMsgs) {
        this.byteMsgs = byteMsgs;
    }
    @Override
    public byte[] buildDlt645Request(String address,String reqDataFlags) {
        byte[] wake = new byte[]{ (byte)0xFE , (byte)0xFE , (byte)0xFE , (byte)0xFE };
        byte[] headMsg = super.getHeadMsg(address);
        byte[] dataFlags = this.getFlagByteValue07(reqDataFlags);
        byte[] mergedArray  =  new byte[headMsg.length + 1 + 1 + dataFlags.length];

        System.arraycopy(headMsg, 0, mergedArray, 0, headMsg.length);
        mergedArray[headMsg.length] = ControllCode07.READ_DATA.controllCode();
        mergedArray[headMsg.length+1] = 0x04;
        System.arraycopy(dataFlags, 0, mergedArray, headMsg.length + 1 + 1, dataFlags.length);
        byte cs = this.calculateCs(mergedArray);
        byte[] result =new byte[20];
        System.arraycopy(wake,0,result,0,4);
        System.arraycopy(mergedArray,0,result,4,mergedArray.length);
        result[wake.length + mergedArray.length] = cs;
        result[wake.length + mergedArray.length + 1] = 0x16;
        return result;
    }

    public byte[] buildDlt645Request_hx(String address,String reqDataFlags,Integer seq){
        byte[] wake = new byte[]{ (byte)0xFE , (byte)0xFE , (byte)0xFE , (byte)0xFE };
        byte[] headMsg = super.getHeadMsg(address);
        byte[] dataFlags = this.getFlagByteValue07(reqDataFlags);
        byte[] mergedArray  =  new byte[headMsg.length + 1 + 1 + dataFlags.length+1];

        System.arraycopy(headMsg, 0, mergedArray, 0, headMsg.length);
        mergedArray[headMsg.length] = ControllCode07.READ_DATA_SUBDATA.controllCode();
        mergedArray[headMsg.length+1] = 0x05;
        System.arraycopy(dataFlags, 0, mergedArray, headMsg.length + 1 + 1, dataFlags.length);
        mergedArray[headMsg.length + 1 + 1 + dataFlags.length]= seq.byteValue();
        byte cs = this.calculateCs(mergedArray);
        byte[] result =new byte[21];
        System.arraycopy(wake,0,result,0,4);
        System.arraycopy(mergedArray,0,result,4,mergedArray.length);
        result[wake.length + mergedArray.length] = cs;
        result[wake.length + mergedArray.length + 1] = 0x16;
        return result;
    }
    @Override
    public Map<String,MsgResponse<String>> parseDlt645Response(Map<String, byte[]> response) {
        Map<String,MsgResponse<String>> result = new HashMap<>();
        Set<String> keyset = response.keySet();
        for(String key : keyset){
            String address = key.split(":")[0];
            String flagCode = key.split(":")[1];
            MsgResponse<String> resp=new MsgResponse<>();
            byte[] bytes = response.get(key);//单次读取数据响应结果
            if(bytes!=null && bytes.length>0){
                try {
                    int byteLength = bytes.length;
                    //查找第一个68出现的下角标
                    int index = findIndex(bytes, (byte)0x68);
                    Integer sum=0;
                    for(int i = index; i < byteLength-2; i++){
                        sum+=bytes[i];
                    }
                    if(bytes[byteLength-1] != (byte)0x16){
                        String message="结束符不正确："+super.bytesToHex(bytes[byteLength-1]);
                        logger.error(message);
                        resp.setSuccess(false);
                        resp.setMessage(message);
                    }
                    if(sum.byteValue()!=bytes[byteLength-2]){
                        String message="验证码校验错误，计算验证码：" + super.bytesToHex(sum.byteValue()) + "，数据中验证码："+ super.bytesToHex(bytes[byteLength-2]);
                        logger.info(message);
                        resp.setSuccess(false);
                        resp.setMessage(message);
                    }
                    byte dataLength = bytes[index+9];
                    byte controllCode = bytes[index+8];
                    byte[] dataFlags = this.getFlagByteValue07(flagCode);
                    if(controllCode == ControllCode07.READ_DATA_RESP_NORMAL_NOEXISTSUBDATA.controllCode()){
                        //正常响应无后续数据
                        byte[] data = new byte[dataLength-4];
                        //检查返回数据的数据标识是否和发送的一致
                        for(int i=0;i<dataFlags.length;i++){
                            if(dataFlags[i] != bytes[index+9+1+i]){
                                byte[] _dataFlags = new byte[4];
                                System.arraycopy(response, index+10, _dataFlags, 0, 4);
                                String message = "请求数据标识和响应数据标识不一致，请求数据标识："+ bytesToHex(dataFlags,4)+"，响应数据标识："+bytesToHex(_dataFlags,4);
                                logger.info(message);
                                resp.setSuccess(false);
                                resp.setMessage(message);
                            }
                        }
                        //如果数据域的长度为4说明没有数据
                        if(dataLength == 4){
                            resp.setSuccess(false);
                            resp.setMessage("无请求数据");
                        }else{
                            //获取数据
                            System.arraycopy(bytes, index+14, data, 0,dataLength-4);
                            resp = RespDataAnalyse.parseData(data, this.flagFormats.get(key));
                        }
                    }else if(controllCode == ControllCode07.READ_DATA_RESP_NORMAL_EXISTSUBDATA.controllCode()){
                        //正常响应有后续数据
                        if(dataLength == 4){
                            resp.setSuccess(false);
                            resp.setMessage("无请求数据");
                        }else{
                            int seq=0;
                            Boolean hasNext=true;
                            List<byte[]> list = new ArrayList<byte[]>();
                            list.add(Arrays.copyOfRange(bytes,index+14,bytes.length-2));
                            while(hasNext){
                                byte[] bytes_hx = buildDlt645Request_hx(address,flagCode, ++seq);
                                Map<String, byte[]> map = new HashMap<>();
                                map.put(key,bytes_hx);
                                Map<String, byte[]> bytes_hx_map = this.msgRequest.request(map);
                                byte[] bytes_hx_resp = bytes_hx_map.get(key);
                                int index2 = findIndex(bytes_hx_resp, (byte) 0x68);
                                byte controllCode2 = bytes_hx_resp[index2+8];
                                if(controllCode2 == ControllCode07.READ_DATA_SUBDATA_NORMAL_NOEXISTSUBDATA.controllCode()){
                                    hasNext=false;
                                    list.add(Arrays.copyOfRange(bytes_hx_resp,index2+14,bytes_hx.length-2));
                                }else if(controllCode2 == ControllCode07.READ_DATA_SUBDATA_ERR.controllCode()){
                                    hasNext=false;
                                }else if(controllCode2 == ControllCode07.READ_DATA_SUBDATA_NORMAL_EXISTSUBDATA.controllCode()){
                                    list.add(Arrays.copyOfRange(bytes_hx_resp,index2+14,bytes_hx.length-2));
                                }
                            }
                            if(list.size()>0){
                                AtomicInteger size = new AtomicInteger();
                                list.forEach(v-> size.addAndGet(v.length));
                                byte[] bytes_all=new byte[size.get()];
                                for(int i=0,j=0;i<list.size();i++){
                                    System.arraycopy(list.get(i),0,bytes_all,j,list.get(i).length);
                                    j+=list.get(i).length;
                                }
                                resp = RespDataAnalyse.parseData(bytes_all, this.flagFormats.get(key));
                            }
                        }
                    }else if(controllCode == ControllCode07.READ_DATA_RESP_ERR.controllCode()){
                        //异常响应
                        resp = RespDataAnalyse.parseErrData(bytes, this.flagFormats.get(key));
                    }
                } catch (Exception e) {
                    logger.info("数据处理异常：数据标识{},异常信息{}",flagCode,getExceptionMsg(e));
                    resp.setSuccess(false);
                    resp.setMessage("数据处理异常:"+getExceptionMsg(e));
                }
            }else{
                resp.setSuccess(false);
                resp.setMessage("请求数据失败");
            }
            result.put(key,resp);
        }
        return result;
    }
    public String getExceptionMsg(Exception e){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
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
