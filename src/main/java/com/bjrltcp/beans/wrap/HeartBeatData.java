package com.bjrltcp.beans.wrap;

import com.bjrltcp.tools.DESTools;
import com.bjrltcp.tools.TcpFuncCode;
import com.bjrltcp.tools.TcpTools;

public class HeartBeatData {

    private String clientCode;

    public HeartBeatData(){
        super();
    }
    public HeartBeatData(String clientCode){
        this.clientCode=clientCode;
    }
    public byte[] getDataByte() throws Exception {
        byte[] bytes = clientCode.getBytes();
        byte[] clientCodeByte=new byte[32];
        for(int i=0;i<bytes.length;i++){
            clientCodeByte[i] = bytes[i];
        }

        byte[] length_before = TcpTools.reverseBytes(TcpTools.intToByte(32));
        byte[] encodeBytes = DESTools.desEncodeData(clientCodeByte);
        byte[] length_after = TcpTools.reverseBytes(TcpTools.intToByte(encodeBytes.length));

        byte[] result = TcpTools.mergeByteArrays(
                TcpTools.getPackageHead(),
                TcpTools.getFuncCode(TcpFuncCode.RESP_FUNCCODE_HEARTBEAT),
                length_before,
                length_after,
                encodeBytes,
                TcpTools.getPackageEnd());
        return result;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }
}
