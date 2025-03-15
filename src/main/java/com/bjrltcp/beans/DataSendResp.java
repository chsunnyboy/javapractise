package com.bjrltcp.beans;

public class DataSendResp extends BaseResp{
    public DataSendResp(){
        super();
    }
    public DataSendResp(byte[] respBytes){
        super(respBytes);
    }

    @Override
    public String toString() {
        return "DataSendResp{" +
                "funcCode=" + super.getFuncCode() +
                ", respCode=" + super.getRespCode() +
                ", clientCode='" + super.getClientCode() + '\'' +
                "}";
    }
}
