package com.bjrltcp.beans;

import com.bjrltcp.tools.TcpTools;

public class BaseResp {
    private Integer funcCode;
    private Integer respCode;
    private String clientCode;

    public BaseResp(){

    }

    public BaseResp(byte[] respBytes){
        //解析功能码
        byte[] funcCodeByte = new byte[4];
        System.arraycopy(respBytes, 0, funcCodeByte, 0, 4);
        this.funcCode = TcpTools.byteArrayToInt(funcCodeByte);

        //解析应答码
        byte[] respCodeByte = new byte[4];
        System.arraycopy(respBytes, 4, respCodeByte, 0, 4);
        this.respCode = TcpTools.byteArrayToInt(respCodeByte);

        //解析客户端码
        byte[] clientCodeByte = new byte[16];//32
        System.arraycopy(respBytes, 8, clientCodeByte, 0, 16);
        clientCode = new String(clientCodeByte);

    }
    public Integer getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(Integer funcCode) {
        this.funcCode = funcCode;
    }

    public Integer getRespCode() {
        return respCode;
    }

    public void setRespCode(Integer respCode) {
        this.respCode = respCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "funcCode=" + funcCode +
                ", respCode=" + respCode +
                ", clientCode='" + clientCode + '\'' +
                '}';
    }
}
