package com.dtl645.dlt64507.controllCode;

public enum ControllCode07 {
    //读取数据请求控制码
    READ_DATA((byte)0x11),
    //读取数据从站正常应答码（无后续数据帧）
    READ_DATA_RESP_NORMAL_NOEXISTSUBDATA((byte)0x91),
    //读取数据从站正常应答码（有后续数据帧）
    READ_DATA_RESP_NORMAL_EXISTSUBDATA((byte)0xB1),
    //读取数据从站异常应答
    READ_DATA_RESP_ERR((byte)0xD1);

    private byte controllCode;
    private ControllCode07(byte controllCode){
        this.controllCode = controllCode;
    }
    public byte code() {
        return controllCode;
    }
}