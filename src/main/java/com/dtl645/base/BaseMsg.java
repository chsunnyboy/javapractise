package com.dtl645.base;

import com.dtl645.requests.MsgResponse;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public abstract class BaseMsg {
    private final byte start = 0x68;
    private final byte end = 0x68;
    private String address;
    public BaseMsg(String address) {
        this.address = address;
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
    public String parseData(byte[] data,int scale){
        StringBuilder sb = new StringBuilder();
        for(int i=data.length-1 ; i>=0; i--){
            sb.append(String.format("%02X", (byte)(data[i]-0x33)));
        }
        BigDecimal num = new BigDecimal(Long.parseLong(sb.toString(),10));
        return num.divide(new BigDecimal(Math.pow(10,scale)), scale, BigDecimal.ROUND_HALF_UP).toString();
    }
    public String bytesToHex(byte value) {
        return String.format("%02X", value);
    }
    public abstract byte[] buildDlt645Request();
    public abstract MsgResponse parseDlt645Response(byte[] response, int byteLength);
}
