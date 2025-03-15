package com.bjrltcp.tools;

import com.bjrltcp.beans.*;
import com.bjrltcp.beans.wrap.ExecuteResultData;

import javax.naming.ldap.Control;

public class TcpResp {



    public static byte[] processServerMsg11(byte[] bytes) throws Exception {
        //解析功能码
        byte[] funcCodeByte = new byte[4];
        System.arraycopy(bytes, 0, funcCodeByte, 0, 4);
        int funcCode = TcpTools.byteArrayToInt(funcCodeByte);

        if(funcCode==TcpFuncCode.RESP_FUNCCODE_LOGIN){
            LoginResp loginResp = new LoginResp(bytes);
            System.out.println(loginResp);
            return null;
        }else if(funcCode==TcpFuncCode.RESP_FUNCCODE_TIMING){
            DataSendResp dataSendResp = new DataSendResp(bytes);
            System.out.println("数据定时上传"+dataSendResp);
            return null;
        }else if(funcCode==TcpFuncCode.RESP_FUNCCODE_CHANGE){
            DataSendResp dataSendResp = new DataSendResp(bytes);
            System.out.println("突变上报响应报文"+dataSendResp);
            return null;
        }else if(funcCode==TcpFuncCode.RESP_FUNCCODE_HEARTBEAT){
            HeartBeatResp heartBeatResp = new HeartBeatResp(bytes);
            System.out.println("发送心跳响应报文"+heartBeatResp);
            return null;
        }else if(funcCode==TcpFuncCode.CONTROL_FUNCCODE){
            TcpControlMessage tcpControlMessage = new TcpControlMessage(bytes);
            System.out.println("指令下发报文"+tcpControlMessage);
            ExecuteResultData executeResultData = new ExecuteResultData(tcpControlMessage.getCodeNum());
            return executeResultData.getDataByte();

        }else if(funcCode==TcpFuncCode.CONTROL_FUNCCODE_RESP){
            ControlResp controlResp = new ControlResp(bytes);
            System.out.println("指令下发执行结果回馈响应报文"+controlResp);
            return null;
        }
        return null;
    }

    public static void processServerMsg(byte[] bytes){
        try {
            byte[] funcCodeByte = new byte[4];
            System.arraycopy(bytes, 0, funcCodeByte, 0, 4);
            int funcCode = TcpTools.byteArrayToInt(funcCodeByte);
            if(funcCode==TcpFuncCode.RESP_FUNCCODE_LOGIN){
                if(bytes.length>40){
                    byte[] leftbyte = new byte[bytes.length-40];
                    byte[] loginbyte = new byte[40];
                    System.arraycopy(bytes, 0, loginbyte, 0, 40);
                    System.arraycopy(bytes, 40, leftbyte, 0, bytes.length-40);

                    LoginResp loginResp = new LoginResp(loginbyte);
                    System.out.println("登录Tcp服务端响应信息:"+loginResp);
                    processServerMsg(leftbyte);
                }else{
                    LoginResp loginResp = new LoginResp(bytes);
                }
            }else if(funcCode==TcpFuncCode.RESP_FUNCCODE_TIMING){
                if(bytes.length>40){
                    byte[] leftbyte = new byte[bytes.length-40];
                    byte[] datasendbyte = new byte[40];
                    System.arraycopy(bytes, 0, datasendbyte, 0, 40);
                    System.arraycopy(bytes, 40, leftbyte, 0, bytes.length-40);

                    DataSendResp dataSendResp = new DataSendResp(datasendbyte);
                    System.out.println("数据定时上传服务端响应信息:{}"+dataSendResp);
                    processServerMsg(leftbyte);
                }else{
                    DataSendResp dataSendResp = new DataSendResp(bytes);
                    System.out.println("数据定时上传服务端响应信息:{}"+dataSendResp);
                }
            }else if(funcCode==TcpFuncCode.RESP_FUNCCODE_CHANGE){
                if(bytes.length>40){
                    byte[] leftbyte = new byte[bytes.length-40];
                    byte[] changesendbyte = new byte[40];
                    System.arraycopy(bytes, 0, changesendbyte, 0, 40);
                    System.arraycopy(bytes, 40, leftbyte, 0, bytes.length-40);

                    DataSendResp dataSendResp = new DataSendResp(changesendbyte);
                    System.out.println("数据定时上传服务端响应信息:{}"+dataSendResp);
                    processServerMsg(leftbyte);
                }else{
                    DataSendResp dataSendResp = new DataSendResp(bytes);
                    System.out.println("数据定时上传服务端响应信息:{}"+dataSendResp);
                }
            }else if(funcCode==TcpFuncCode.RESP_FUNCCODE_HEARTBEAT){
                if(bytes.length>72){
                    byte[] leftbyte = new byte[bytes.length-72];
                    byte[] heartBeatbyte = new byte[72];
                    System.arraycopy(bytes, 0, heartBeatbyte, 0, 72);
                    System.arraycopy(bytes, 72, leftbyte, 0, bytes.length-72);

                    HeartBeatResp heartBeatResp = new HeartBeatResp(heartBeatbyte);
                    System.out.println("发送心跳服务端响应信息:{}"+heartBeatResp);
                    processServerMsg(leftbyte);
                }else{
                    HeartBeatResp heartBeatResp = new HeartBeatResp(bytes);
                    System.out.println("发送心跳服务端响应信息:{}"+heartBeatResp);
                }
            }else if(funcCode==TcpFuncCode.CONTROL_FUNCCODE){
                if(bytes.length>57){
                    byte[] leftbyte = new byte[bytes.length-57];
                    byte[] controlbyte = new byte[57];
                    System.arraycopy(bytes, 0, controlbyte, 0, 57);
                    System.arraycopy(bytes, 57, leftbyte, 0, bytes.length-57);

                    TcpControlMessage tcpControlMessage = new TcpControlMessage(controlbyte);
                    System.out.println("指令下发报文{}"+tcpControlMessage);
                    dealControllMessage(tcpControlMessage);
                    processServerMsg(leftbyte);
                }else{
                    TcpControlMessage tcpControlMessage = new TcpControlMessage(bytes);
                    System.out.println("指令下发报文{}"+tcpControlMessage);
                    dealControllMessage(tcpControlMessage);
                }
            }else if(funcCode==TcpFuncCode.CONTROL_FUNCCODE_RESP){
                if(bytes.length>48){
                    byte[] leftbyte = new byte[bytes.length-48];
                    byte[] controlRespbyte = new byte[48];
                    System.arraycopy(bytes, 0, controlRespbyte, 0, 48);
                    System.arraycopy(bytes, 48, leftbyte, 0, bytes.length-48);

                    ControlResp controlResp = new ControlResp(controlRespbyte);
                    System.out.println("发送指令执行结果回馈服务端响应信息{}"+controlResp);
                    processServerMsg(leftbyte);
                }else{
                    ControlResp controlResp = new ControlResp(bytes);
                    System.out.println("发送指令执行结果回馈服务端响应信息{}"+controlResp);
                }
            }
        } catch (Exception e) {
            System.out.println("响应数据解析异常{}"+e.getMessage());
        }
    }

    public static void dealControllMessage(TcpControlMessage controllMessage){

    }
}
