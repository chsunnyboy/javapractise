package com.bjrltcp.beans.wrap;

import com.bjrltcp.tools.RSATools;
import com.bjrltcp.tools.TcpFuncCode;
import com.bjrltcp.tools.TcpTools;

public class LoginData {

    private String desCode;
    private String clientCode;

    public LoginData(){
        super();
    }
    public LoginData(String desCode,String clientCode){
        this.desCode=desCode;
        this.clientCode=clientCode;
    }
    public byte[] getDataByte() throws Exception {
        byte[] length_before=TcpTools.reverseBytes(TcpTools.intToByte(40));
        String desContent = desCode+clientCode;
        byte[] data = RSATools.encryptRSA(desContent, RSATools.getPublicKey());
        byte[] length_after=TcpTools.reverseBytes(TcpTools.intToByte(data.length));
        byte[] result = TcpTools.mergeByteArrays(
                TcpTools.getPackageHead(),
                TcpTools.getFuncCode(TcpFuncCode.RESP_FUNCCODE_LOGIN),
                length_before,
                length_after,
                data,
                TcpTools.getPackageEnd());
        return result;
    }

    public String getDesCode() {
        return desCode;
    }

    public void setDesCode(String desCode) {
        this.desCode = desCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }
}
