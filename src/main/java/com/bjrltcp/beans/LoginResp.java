package com.bjrltcp.beans;

public class LoginResp extends BaseResp{

    public LoginResp(){
        super();
    }
    public LoginResp(byte[] respBytes){
        super(respBytes);
    }

    @Override
    public String toString() {
        return "LoginResp{" +
                "funcCode=" + super.getFuncCode() +
                ", respCode=" + super.getRespCode() +
                ", clientCode='" + super.getClientCode() + '\'' +
                "}";
    }
}
