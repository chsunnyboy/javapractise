package com.bjrltcp.beans;

import com.bjrltcp.tools.TcpTools;

public class ControlResp extends BaseResp{
    private Integer codeNum;

    public ControlResp(){
        super();
    }

    public ControlResp(byte[] respBytes){
        super(respBytes);
        byte[] codeNumByte = new byte[8];
        System.arraycopy(respBytes, 40, codeNumByte, 0, 8);
        codeNum = TcpTools.byteArrayToInt(codeNumByte);
    }

    public Integer getCodeNum() {
        return codeNum;
    }

    public void setCodeNum(Integer codeNum) {
        this.codeNum = codeNum;
    }
    public String toString() {
        return "HeartBeatResp{" +
                "funcCode=" + super.getFuncCode() +
                ", respCode=" + super.getRespCode() +
                ", clientCode='" + super.getClientCode() + '\'' +
                ", codeNum=" + codeNum +
                "}";
    }
}
