package com.bjrltcp.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;

public class TcpTools {
    public static byte[] getPackageHead(){
        short data = 0x55aa;
        byte[] bytes = shortToByte(data);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(bytes);
            outputStream.write(bytes);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getPackageEnd(){
        short data1 = 0x6868;
        short data2 = 0x1616;
        byte[] bytes1 = shortToByte(data1);
        byte[] bytes2 = shortToByte(data2);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(bytes1);
            outputStream.write(bytes2);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] mergeByteArrays(byte[]... arrays) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            for (byte[] array : arrays) {
                outputStream.write(array);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getFuncCode(Integer funcCode){
        byte[] bytes = reverseBytes(intToByte(funcCode));
        return bytes;
    }

    /**
     * Object实际类型为Float或者Byte
     * @param data
     * @return
     */
    public static byte[] getDataBytes(Map<String,Object> data){
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();){
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                byte[] pointKey = shortToByte(Short.valueOf(key));
                outputStream.write(reverseBytes(pointKey));

                Object object = data.get(key);
                byte[] pointVal = null;
                if(object instanceof Float){
                    pointVal = floatToByte((Float) object);
                }else if(object instanceof Byte){
                    pointVal = new byte[1];
                    pointVal[0] = (byte) object;
                }
                outputStream.write(reverseBytes(pointVal));
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getDeviceId(){
        Short deviceId=1;
        byte[] bytes = shortToByte(deviceId);
        return reverseBytes(bytes);
    }

    /**
     * 字节顺序倒排
     * @param bytes
     * @return
     */
    public static byte[] reverseBytes(byte[] bytes){
        int length = bytes.length;
        byte[] result=new byte[length];
        for(int i=0;i<length;i++){
            result[i] = bytes[length-i-1];
        }
        return result;
    }

    public static byte[] intToByte(Integer data){
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(data);
        return buffer.array();
    }
    public static byte[] longToByte(Long data){
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(data);
        return buffer.array();
    }
    public static byte[] floatToByte(Float data){
        ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES);
        buffer.putFloat(data);
        return buffer.array();
    }
    public static byte[] shortToByte(Short data){
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.putShort(data);
        return buffer.array();
    }

    /**
     * 小端序
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes){
        int intValue = (bytes[3] << 24) | ((bytes[2] & 0xFF) << 16) | ((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF);
        return intValue;
    }

    /**
     * 小端序
     * @param bytes
     * @return
     */
    public static short byteArrayToShort(byte[] bytes){
        short shortValue = (short) ((bytes[1] << 8) | (bytes[0] & 0xFF));
        return shortValue;
    }

    /**
     * 小端序
     * @param bytes
     * @return
     */
    public static long byteArrayToLong(byte[] bytes){
        long longValue = ((long) (bytes[7] & 0xFF) << 56) |
                ((long) (bytes[6] & 0xFF) << 48) |
                ((long) (bytes[5] & 0xFF) << 40) |
                ((long) (bytes[4] & 0xFF) << 32) |
                ((long) (bytes[3] & 0xFF) << 24) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[1] & 0xFF) << 8) |
                (bytes[0] & 0xFF);
        return longValue;
    }

    /**
     * 小端序
     * @param bytes
     * @return
     */
    public static float byteArrayToFloat(byte[] bytes){
        int intValue = ((bytes[3] & 0xFF) << 24) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[1] & 0xFF) << 8) |
                (bytes[0] & 0xFF);

        float floatValue = Float.intBitsToFloat(intValue);
        return floatValue;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02X", b));
        }
        return hexStringBuilder.toString();
    }
}
