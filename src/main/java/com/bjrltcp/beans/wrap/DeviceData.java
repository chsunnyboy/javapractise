package com.bjrltcp.beans.wrap;

import com.bjrltcp.tools.DESTools;
import com.bjrltcp.tools.TcpTools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class DeviceData {
    private Integer funcCode;

    private Map<String,Object> yxDataMap;

    private Map<String,Object> ycDataMap;

    public DeviceData() {
    }

    public DeviceData(Integer funcCode,Map<String,Object> yxDataMap,Map<String,Object> ycDataMap) {
        this.funcCode=funcCode;
        this.yxDataMap=yxDataMap;
        this.ycDataMap=ycDataMap;
    }
    public byte[] getDataByte() throws Exception {

        byte[] deviceId1 = TcpTools.getDeviceId();

        Short yxNum=Short.valueOf(yxDataMap.keySet().size()+"");
        byte[] yxNumBytes = TcpTools.reverseBytes(TcpTools.shortToByte(yxNum));
        byte[] yxDataBytes = mapToByteArray(yxDataMap);

        Short ycNum=Short.valueOf(ycDataMap.keySet().size()+"");
        byte[] ycNumBytes = TcpTools.reverseBytes(TcpTools.shortToByte(ycNum));
        byte[] ycDataBytes = mapToByteArray(ycDataMap);

        byte[] oriDataBytes = TcpTools.mergeByteArrays(deviceId1, yxNumBytes, yxDataBytes, ycNumBytes, ycDataBytes);

        byte[] length_before = TcpTools.reverseBytes(TcpTools.intToByte(oriDataBytes.length));
        byte[] encodeBytes = DESTools.desEncodeData(oriDataBytes);
        byte[] length_after = TcpTools.reverseBytes(TcpTools.intToByte(encodeBytes.length));

        byte[] result = TcpTools.mergeByteArrays(
                TcpTools.getPackageHead(),
                TcpTools.getFuncCode(this.funcCode),
                length_before,
                length_after,
                encodeBytes,
                TcpTools.getPackageEnd());
        return result;
    }

    public byte[] mapToByteArray(Map<String,Object> data){
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();){
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                byte[] pointKey = TcpTools.shortToByte(Short.valueOf(key));
                outputStream.write(TcpTools.reverseBytes(pointKey));

                Object object = data.get(key);
                byte[] pointVal = null;
                if(object instanceof Float){
                    pointVal = TcpTools.floatToByte((Float) object);
                }else if(object instanceof Byte){
                    pointVal = new byte[1];
                    pointVal[0] = (byte) object;
                }
                outputStream.write(TcpTools.reverseBytes(pointVal));
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(Integer funcCode) {
        this.funcCode = funcCode;
    }

    public Map<String, Object> getYxDataMap() {
        return yxDataMap;
    }

    public void setYxDataMap(Map<String, Object> yxDataMap) {
        this.yxDataMap = yxDataMap;
    }

    public Map<String, Object> getYcDataMap() {
        return ycDataMap;
    }

    public void setYcDataMap(Map<String, Object> ycDataMap) {
        this.ycDataMap = ycDataMap;
    }
}
