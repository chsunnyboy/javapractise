package com.bjrltcp.tools;

public class TcpFuncCode {

    //登录功能码
    public static final Integer RESP_FUNCCODE_LOGIN = 1;

    //定时上报功能码
    public static final Integer RESP_FUNCCODE_TIMING = 2;

    //突变上报功能码
    public static final Integer RESP_FUNCCODE_CHANGE = 3;

    //发送心跳功能码
    public static final Integer RESP_FUNCCODE_HEARTBEAT = 9;

    //服务端指令下发功能码
    public static final Integer CONTROL_FUNCCODE = 32;

    //回复下发指令执行结果功能码、服务端响应执行结果功能码
    public static final Integer CONTROL_FUNCCODE_RESP = 33;
}
