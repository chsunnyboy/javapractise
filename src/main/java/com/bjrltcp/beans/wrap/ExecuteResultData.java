package com.bjrltcp.beans.wrap;

import com.bjrltcp.tools.TcpFuncCode;
import com.bjrltcp.tools.TcpTools;

public class ExecuteResultData {
    private Long codeNum;

    public ExecuteResultData() {
        super();
    }

    public ExecuteResultData(Long codeNum) {
        this.codeNum=codeNum;
    }
    public byte[] getDataByte() throws Exception {
        //回应下发的指令
        byte[] length = TcpTools.reverseBytes(TcpTools.intToByte(12)); //有效数据前总长度,有效数据后总长度需要高低互换
        byte[] respCodeNum = TcpTools.longToByte(this.codeNum);
        byte[] respResult = TcpTools.intToByte(0);

        return TcpTools.mergeByteArrays(
                TcpTools.getPackageHead(),
                TcpTools.getFuncCode(TcpFuncCode.CONTROL_FUNCCODE_RESP),
                length,
                length,
                respCodeNum,
                respResult,
                TcpTools.getPackageEnd());
    }

    public Long getCodeNum() {
        return codeNum;
    }

    public void setCodeNum(Long codeNum) {
        this.codeNum = codeNum;
    }
}
