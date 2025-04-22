package com.dtl645.base;

import com.dtl645.requests.MsgResponse;
import java.util.Map;

public abstract class BaseMsg {
    private final byte start = 0x68;
    private final byte end = 0x68;
    private String address;
    public BaseMsg(String address) {
        this.address = address;
    }
    protected byte[] getFlagByteValue07(String flagName){
        byte[] flagByteValue = new byte[4];
        flagByteValue[0]=(byte)(Integer.valueOf(flagName.substring(0,2),16).byteValue()+(byte)0x33);
        flagByteValue[1]=(byte)(Integer.valueOf(flagName.substring(2,4),16).byteValue()+(byte)0x33);
        flagByteValue[2]=(byte)(Integer.valueOf(flagName.substring(4,6),16).byteValue()+(byte)0x33);
        flagByteValue[3]=(byte)(Integer.valueOf(flagName.substring(6,8),16).byteValue()+(byte)0x33);
        // 手动交换首尾元素
        for (int i = 0; i < flagByteValue.length / 2; i++) {
            int temp = flagByteValue[i];
            flagByteValue[i] = flagByteValue[flagByteValue.length - 1 - i];
            flagByteValue[flagByteValue.length - 1 - i] = (byte)temp;
        }
        return flagByteValue;
    }
    public byte[] getHeadMsg() {
        if(address.length()<12){
            address = String.format("%12s", address).replace(' ', '0');
        }
        byte[] head = new byte[8];
        head[0] = start;
        head[7] = end;
        for(int i = 0; i < address.length(); i+=2) {
            head[i/2+1] = Byte.valueOf(address.substring(address.length()-i-2, address.length()-i),16).byteValue();
        }
        return head;
    }

    public byte calculateCs(byte[] data) {
        Integer sum = 0;
        for(int i=0 ; i<data.length; i++){
            sum+=data[i];
        }
        return sum.byteValue();
    }
    public String bytesToHex(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString();
    }

    public String bytesToHex(byte value) {
        return String.format("%02X", value);
    }
    public abstract byte[] buildDlt645Request(String reqFlagName);
    public abstract Map<String,MsgResponse<String>> parseDlt645Response(Map<String, byte[]> response);
}
