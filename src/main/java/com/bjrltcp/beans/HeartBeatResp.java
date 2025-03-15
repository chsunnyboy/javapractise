package com.bjrltcp.beans;

import com.bjrltcp.tools.TcpTools;

public class HeartBeatResp extends BaseResp{
    private String respTime;

    public HeartBeatResp(){
        super();
    }
     public HeartBeatResp(byte[] respBytes){
        super(respBytes);
        byte[] respTimeByte = new byte[19];
        System.arraycopy(respBytes, 40, respTimeByte, 0, 19);
        respTime = new String(respTimeByte);
     }
    public String getRespTime() {
        return respTime;
    }

    public void setRespTime(String respTime) {
        this.respTime = respTime;
    }

    @Override
    public String toString() {
        return "HeartBeatResp{" +
                "funcCode=" + super.getFuncCode() +
                ", respCode=" + super.getRespCode() +
                ", clientCode='" + super.getClientCode() + '\'' +
                ", respTime='" + respTime + '\'' +
                "}";
    }
}
