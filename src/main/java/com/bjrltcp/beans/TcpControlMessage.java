package com.bjrltcp.beans;

import com.bjrltcp.tools.TcpTools;

public class TcpControlMessage {
    private Integer funcCode;
    private Integer reserved;//保留字
    private String clientCode;
    private Short deviceId;
    private Short pointKey;
    private Object pointValue; //Integer、Float
    private Long codeNum;  //指令编号
    private Byte writeType;  //1浮点数；2整数

    public TcpControlMessage(){
        super();
    }

    public TcpControlMessage(byte[] respBytes){
        //解析功能码
        byte[] funcCodeByte = new byte[4];
        System.arraycopy(respBytes, 0, funcCodeByte, 0, 4);
        this.funcCode = TcpTools.byteArrayToInt(funcCodeByte);

        //保留字
        byte[] reservedByte = new byte[4];
        System.arraycopy(respBytes, 4, reservedByte, 0, 4);
        this.reserved = TcpTools.byteArrayToInt(reservedByte);

        //解析客户端码
        byte[] clientCodeByte = new byte[16];//32
        System.arraycopy(respBytes, 8, clientCodeByte, 0, 16);
        this.clientCode = new String(clientCodeByte);

        byte[] deviceIdByte = new byte[2];
        System.arraycopy(respBytes, 40, deviceIdByte, 0, 2);
        this.deviceId = TcpTools.byteArrayToShort(deviceIdByte);


        byte[] pointKeyByte = new byte[2];
        System.arraycopy(respBytes, 42, pointKeyByte, 0, 2);
        this.pointKey = TcpTools.byteArrayToShort(pointKeyByte);

        byte[] codeNumByte = new byte[8];
        System.arraycopy(respBytes, 44, codeNumByte, 0, 8);
        this.codeNum = TcpTools.byteArrayToLong(codeNumByte);

        this.writeType = respBytes[52];

        byte[] dataWriteValue = new byte[8];
        System.arraycopy(respBytes, 53, dataWriteValue, 0, 4);
        if(this.writeType==1){
            this.pointValue=TcpTools.byteArrayToFloat(dataWriteValue);
        }else if(this.writeType==2){
            this.pointValue = TcpTools.byteArrayToInt(dataWriteValue);
        }
    }

    public Integer getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(Integer funcCode) {
        this.funcCode = funcCode;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public Short getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Short deviceId) {
        this.deviceId = deviceId;
    }

    public Short getPointKey() {
        return pointKey;
    }

    public void setPointKey(Short pointKey) {
        this.pointKey = pointKey;
    }

    public Object getPointValue() {
        return pointValue;
    }

    public void setPointValue(Object pointValue) {
        this.pointValue = pointValue;
    }

    public Long getCodeNum() {
        return codeNum;
    }

    public void setCodeNum(Long codeNum) {
        this.codeNum = codeNum;
    }

    public Byte getWriteType() {
        return writeType;
    }

    public void setWriteType(Byte writeType) {
        this.writeType = writeType;
    }

    @Override
    public String toString() {
        return "TcpControlMessage{" +
                "funcCode=" + funcCode +
                ", reserved=" + reserved +
                ", clientCode='" + clientCode + '\'' +
                ", deviceId=" + deviceId +
                ", pointKey=" + pointKey +
                ", pointValue=" + pointValue +
                ", codeNum=" + codeNum +
                ", writeType=" + writeType +
                '}';
    }
}
