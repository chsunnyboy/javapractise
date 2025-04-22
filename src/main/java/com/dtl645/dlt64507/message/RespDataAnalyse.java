package com.dtl645.dlt64507.message;

import com.dtl645.requests.MsgResponse;
import com.eclipsesource.json.JsonObject;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class RespDataAnalyse {
    public static final String KEY_DATA="data";
    public static final String KEY_FLAG="flag";
    public static final String KEY_JSON="json";
    public static final BigDecimal scale1=new BigDecimal(0.1);
    public static final BigDecimal scale2=new BigDecimal(0.01);
    public static final BigDecimal scale3=new BigDecimal(0.001);
    public static final BigDecimal scale4=new BigDecimal(0.0001);

    public static MsgResponse<String> parseData(byte[] data,Integer flagFormat){
        MsgResponse<String> response = new MsgResponse<String>();

        switch(flagFormat){
            case 1: response=parseData1(data, flagFormat);break;
            case 2: response=parseData2(data, flagFormat); break;
            case 3: response=parseData3(data, flagFormat); break;
            case 4: response=parseData4(data, flagFormat); break;
            case 5: response=parseData5(data, flagFormat); break;
            case 6: response=parseData6(data, flagFormat); break;
            case 7: response=parseData7(data, flagFormat); break;
            case 8: response=parseData8(data, flagFormat); break;
            case 9: response=parseData9(data, flagFormat); break;
            case 10: response=parseData10(data, flagFormat); break;
            case 11: response=parseData11(data, flagFormat); break;
            case 12: response=parseData12(data, flagFormat); break;
            case 13: response=parseData13(data, flagFormat); break;
            case 14: response=parseData14(data, flagFormat); break;
            case 15: response=parseData15(data, flagFormat); break;
            case 16: response=parseData16(data, flagFormat); break;
            case 17: response=parseData17(data, flagFormat); break;
            case 18: response=parseData18(data, flagFormat); break;
            case 19: response=parseData19(data, flagFormat); break;
            case 20: response=parseData20(data, flagFormat); break;
            case 21: response=parseData21(data, flagFormat); break;
            case 22: response=parseData22(data, flagFormat); break;
            case 23: response=parseData23(data, flagFormat); break;
            case 24: response=parseData24(data, flagFormat); break;
            case 25: response=parseData25(data, flagFormat); break;
            case 26: response=parseData26(data, flagFormat); break;
            case 27: response=parseData27(data, flagFormat); break;
            case 28: response=parseData28(data, flagFormat); break;
            case 29: response=parseData29(data, flagFormat); break;
        }
        return response;
    }

    /**
     * 电能量数据标识，XXXXXX.XX 4字节
     * @param data
     * @param flagFormat=1
     * @return
     */

    public static MsgResponse<String> parseData1(byte[] data, Integer flagFormat){
        StringBuilder sb = new StringBuilder();
        for(int i=data.length-1 ; i>=0; i--){
            sb.append(String.format("%02X", (byte)(data[i]-0x33)));
        }
        JsonObject json = new JsonObject();
        json.add(KEY_DATA, scale2(sb.toString()));
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,false);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }
    /**
     * 最大需量及发生时间，XX.XXXX YYMMDDhhmm 8字节
     * @param bytes
     * @param flagFormat=2
     * @return
     */
    public static MsgResponse<String> parseData2(byte[] bytes, Integer flagFormat){
        StringBuilder sb = new StringBuilder();
        byte[] demand = new byte[3];
        byte[] time = new byte[5];
        System.arraycopy(bytes,0,demand,0,3);
        System.arraycopy(bytes,3,time,0,5);
        for(int i=demand.length-1 ; i>=0; i--){
            sb.append(String.format("%02X", (byte)(demand[i]-0x33)));
        }
        String demand_value=scale4(sb.toString());
        sb=new StringBuilder();
        for(int i=time.length-1 ; i>=0; i--){
            sb.append(String.format("%02X", time[i]-0x33));
        }

        JsonObject data = new JsonObject();
        data.add("max_demand",demand_value);
        data.add("time",sb.toString());
        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * 变量数据标识编码表
     * 数据格式 XXX.X
     * @param bytes
     * @param flagFormat=3
     * @return
     */
    public static MsgResponse<String> parseData3(byte[] bytes, Integer flagFormat){
        StringBuilder sb = new StringBuilder();
        for(int i=bytes.length-1 ; i>=0; i--){
            sb.append(String.format("%02X", (byte)(bytes[i]-0x33)));
        }
        JsonObject json = new JsonObject();
        json.add(KEY_DATA, scale1(sb.toString()));
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,false);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * 数据格式 XXX.XXX
     * @param bytes
     * @param flagFormat=4
     * @return
     */
    public static MsgResponse<String> parseData4(byte[] bytes, Integer flagFormat){
        StringBuilder sb = new StringBuilder();
        for(int i=bytes.length-1 ; i>=0; i--){
            sb.append(String.format("%02X", (byte)(bytes[i]-0x33)));
        }
        JsonObject json = new JsonObject();
        json.add(KEY_DATA, scale3(sb.toString()));
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,false);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * 数据格式 XX.XXXX
     * @param bytes
     * @param flagFormat=5
     * @return
     */
    public static MsgResponse<String> parseData5(byte[] bytes, Integer flagFormat){
        StringBuilder sb = new StringBuilder();
        for(int i=bytes.length-1 ; i>=0; i--){
            sb.append(String.format("%02X", (byte)(bytes[i]-0x33)));
        }
        JsonObject json = new JsonObject();
        json.add(KEY_DATA, scale4(sb.toString()));
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,false);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * 数据为整数 XXXXXXXX
     * @param bytes
     * @param flagFormat=6
     * @return
     */
    public static MsgResponse<String> parseData6(byte[] bytes, Integer flagFormat){
        StringBuilder sb = new StringBuilder();
        for(int i=bytes.length-1 ; i>=0; i--){
            sb.append(String.format("%02X", (byte)(bytes[i]-0x33)));
        }
        JsonObject json = new JsonObject();
        json.add(KEY_DATA, scale0(sb.toString()));
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,false);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * A相、B相、C相（失压总次数，总累计时间（分））
     * 数据格式18字节
     * XXXXXX，XXXXXX
     * XXXXXX，XXXXXX
     * XXXXXX，XXXXXX
     * @param bytes
     * @param flagFormat=7
     * @return
     * {
     *   "data": {
     *     "a_total_number": "",
     *     "a_total_time": "",
     *     "b_total_number": "",
     *     "b_total_time": "",
     *     "c_total_number": "",
     *     "c_total_time": ""
     *   },
     *   "flag": 7,
     *   "json": true
     * }
     */
    public static MsgResponse<String> parseData7(byte[] bytes, Integer flagFormat){

        String[] array = new String[6];
        for(int i=0;i<bytes.length;i+=3){
            StringBuilder sb = new StringBuilder();
            for(int j=2;j>=0;j--){
                sb.append(String.format("%02X", (byte)(bytes[i+j]-0x33)));
            }
            array[i/3]=sb.toString();
        }
        JsonObject data = new JsonObject();
        data.add("a_total_number",scale0(array[0]));
        data.add("a_total_time",scale0(array[1]));
        data.add("b_total_number",scale0(array[2]));
        data.add("b_total_time",scale0(array[3]));
        data.add("c_total_number",scale0(array[4]));
        data.add("c_total_time",scale0(array[5]));

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * A相、B相、C相（失压、欠压、过压）记录
     * 数据格式
     *  YYMMDDhhmmss YYMMDDhhmmss XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX
     *  XXXXXX.XX  XXX.X  XXX.XXX XX.XXXX  XX.XXXX  X.XXX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXX.X
     *  XXX.XXX  XX.XXXX  XX.XXXX  X.XXX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXX.X  XXX.XXX  XX.XXXX
     *  XX.XXXX  X.XXX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX
     * @param bytes
     * @param flagFormat=8
     * @return
     *
     */
    public static MsgResponse<String> parseData8(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,6,4,4,4,4,4,4,4,4,2,3,3,3,2,4,4,4,4,2,3,3,3,2,4,4,4,4,2,3,3,3,2,4,4,4,4};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("start_time",array[0]);
        data.add("end_time",array[1]);
        data.add("zx_yg_zdn_inc",scale2(array[2]));
        data.add("fx_yg_zdn_inc",scale2(array[3]));
        data.add("zh_wg1_zdn_inc",scale2(array[4]));
        data.add("zh_wg2_zdn_inc",scale2(array[5]));
        data.add("a_zx_yg_dn_inc",scale2(array[6]));
        data.add("a_fx_yg_dn_inc",scale2(array[7]));
        data.add("a_zh_wg1_dn_inc",scale2(array[8]));
        data.add("a_zh_wg2_dn_inc",scale2(array[9]));
        data.add("a_dy",scale1(array[10]));
        data.add("a_dl",scale3(array[11]));
        data.add("a_yggl",scale4(array[12]));
        data.add("a_wggl",scale4(array[13]));
        data.add("a_glys",scale3(array[14]));
        data.add("b_zx_yg_dn_inc",scale2(array[15]));
        data.add("b_fx_yg_dn_inc",scale2(array[16]));
        data.add("b_zh_wg1_dn_inc",scale2(array[17]));
        data.add("b_zh_wg2_dn_inc",scale2(array[18]));
        data.add("b_dy",scale1(array[19]));
        data.add("b_dl",scale3(array[20]));
        data.add("b_yggl",scale4(array[21]));
        data.add("b_wggl",scale4(array[22]));
        data.add("b_glys",scale3(array[23]));
        data.add("c_zx_yg_dn_inc",scale2(array[24]));
        data.add("c_fx_yg_dn_inc",scale2(array[25]));
        data.add("c_zh_wg1_dn_inc",scale2(array[26]));
        data.add("c_zh_wg2_dn_inc",scale2(array[27]));
        data.add("c_dy",scale1(array[28]));
        data.add("c_dl",scale3(array[29]));
        data.add("c_yggl",scale4(array[30]));
        data.add("c_wggl",scale4(array[31]));
        data.add("c_glys",scale3(array[32]));
        data.add("z_ass",scale2(array[33]));
        data.add("a_ass",scale2(array[34]));
        data.add("b_ass",scale2(array[35]));
        data.add("c_ass",scale2(array[36]));

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * 全失压总次数，总累计时间
     * 数据格式 XXXXXX，XXXXXX 次,分
     * @param bytes
     * @param flagFormat=9
     * @return
     */
    public static MsgResponse<String> parseData9(byte[] bytes, Integer flagFormat){

        String[] array = new String[2];
        for(int i=0;i<bytes.length;i+=3){
            StringBuilder sb = new StringBuilder();
            for(int j=2;j>=0;j--){
                sb.append(String.format("%02X", (byte)(bytes[i+j]-0x33)));
            }
            array[i/3]=sb.toString();
        }
        JsonObject data = new JsonObject();
        data.add("total_number",scale0(array[0]));
        data.add("total_time",scale0(array[1]));

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     *  YYMMDDhhmmss XXX.XXX YYMMDDhhmmss
     * @param bytes
     * @param flagFormat==10
     * @return
     */
    public static MsgResponse<String> parseData10(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,3,6};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("start_time",array[0]);
        data.add("flow_value",scale3(array[1]));
        data.add("end_time",array[2]);

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     *  YYMMDDhhmmss YYMMDDhhmmss
     * @param bytes
     * @param flagFormat=11
     * @return
     */
    public static MsgResponse<String> parseData11(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,6};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("start_time",array[0]);
        data.add("end_time",array[1]);

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     *  数据格式 YYMMDDhhmmss YYMMDDhhmmss XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX
     *          XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX
     * @param bytes
     * @param flagFormat=12
     * @return
     */
    public static MsgResponse<String> parseData12(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,6,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("start_time",array[0]);
        data.add("end_time",array[1]);
        data.add("zx_yg_zdn_inc",scale2(array[2]));
        data.add("fx_yg_zdn_inc",scale2(array[3]));
        data.add("zh_wg1_zdn_inc",scale2(array[4]));
        data.add("zh_wg2_zdn_inc",scale2(array[5]));
        data.add("a_zx_yg_dn_inc",scale2(array[6]));
        data.add("a_fx_yg_dn_inc",scale2(array[7]));
        data.add("a_zh_wg1_dn_inc",scale2(array[8]));
        data.add("a_zh_wg2_dn_inc",scale2(array[9]));
        data.add("b_zx_yg_dn_inc",scale2(array[10]));
        data.add("b_fx_yg_dn_inc",scale2(array[11]));
        data.add("b_zh_wg1_dn_inc",scale2(array[12]));
        data.add("b_zh_wg2_dn_inc",scale2(array[13]));
        data.add("c_zx_yg_dn_inc",scale2(array[14]));
        data.add("c_fx_yg_dn_inc",scale2(array[15]));
        data.add("c_zh_wg1_dn_inc",scale2(array[16]));
        data.add("c_zh_wg2_dn_inc",scale2(array[17]));

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     *  数据格式 YYMMDDhhmmss YYMMDDhhmmss XX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX
     *          XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX
     * @param bytes
     * @param flagFormat=13
     * @return
     */
    public static MsgResponse<String> parseData13(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,6,2,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("start_time",array[0]);
        data.add("end_time",array[1]);
        data.add("max_unbalance_rate",scale2(array[2]));
        data.add("zx_yg_zdn_inc",scale2(array[3]));
        data.add("fx_yg_zdn_inc",scale2(array[4]));
        data.add("zh_wg1_zdn_inc",scale2(array[5]));
        data.add("zh_wg2_zdn_inc",scale2(array[6]));
        data.add("a_zx_yg_dn_inc",scale2(array[7]));
        data.add("a_fx_yg_dn_inc",scale2(array[8]));
        data.add("a_zh_wg1_dn_inc",scale2(array[9]));
        data.add("a_zh_wg2_dn_inc",scale2(array[10]));
        data.add("b_zx_yg_dn_inc",scale2(array[11]));
        data.add("b_fx_yg_dn_inc",scale2(array[12]));
        data.add("b_zh_wg1_dn_inc",scale2(array[13]));
        data.add("b_zh_wg2_dn_inc",scale2(array[14]));
        data.add("c_zx_yg_dn_inc",scale2(array[15]));
        data.add("c_fx_yg_dn_inc",scale2(array[16]));
        data.add("c_zh_wg1_dn_inc",scale2(array[17]));
        data.add("c_zh_wg2_dn_inc",scale2(array[18]));

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * 数据格式
     *  YYMMDDhhmmss YYMMDDhhmmss XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX
     *  XXXXXX.XX  XXX.X  XXX.XXX XX.XXXX  XX.XXXX  X.XXX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXX.X
     *  XXX.XXX  XX.XXXX  XX.XXXX  X.XXX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXX.X  XXX.XXX  XX.XXXX
     *  XX.XXXX  X.XXX
     * @param bytes
     * @param flagFormat=14
     * @return
     *
     */
    public static MsgResponse<String> parseData14(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,6,4,4,4,4,4,4,4,4,2,3,3,3,2,4,4,4,4,2,3,3,3,2,4,4,4,4,2,3,3,3,2,4,4,4,4};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("start_time",array[0]);
        data.add("end_time",array[1]);
        data.add("zx_yg_zdn_inc",scale2(array[2]));
        data.add("fx_yg_zdn_inc",scale2(array[3]));
        data.add("zh_wg1_zdn_inc",scale2(array[4]));
        data.add("zh_wg2_zdn_inc",scale2(array[5]));
        data.add("a_zx_yg_dn_inc",scale2(array[6]));
        data.add("a_fx_yg_dn_inc",scale2(array[7]));
        data.add("a_zh_wg1_dn_inc",scale2(array[8]));
        data.add("a_zh_wg2_dn_inc",scale2(array[9]));
        data.add("a_dy",scale1(array[10]));
        data.add("a_dl",scale3(array[11]));
        data.add("a_yggl",scale4(array[12]));
        data.add("a_wggl",scale4(array[13]));
        data.add("a_glys",scale3(array[14]));
        data.add("b_zx_yg_dn_inc",scale2(array[15]));
        data.add("b_fx_yg_dn_inc",scale2(array[16]));
        data.add("b_zh_wg1_dn_inc",scale2(array[17]));
        data.add("b_zh_wg2_dn_inc",scale2(array[18]));
        data.add("b_dy",scale1(array[19]));
        data.add("b_dl",scale3(array[20]));
        data.add("b_yggl",scale4(array[21]));
        data.add("b_wggl",scale4(array[22]));
        data.add("b_glys",scale3(array[23]));
        data.add("c_zx_yg_dn_inc",scale2(array[24]));
        data.add("c_fx_yg_dn_inc",scale2(array[25]));
        data.add("c_zh_wg1_dn_inc",scale2(array[26]));
        data.add("c_zh_wg2_dn_inc",scale2(array[27]));
        data.add("c_dy",scale1(array[28]));
        data.add("c_dl",scale3(array[29]));
        data.add("c_yggl",scale4(array[30]));
        data.add("c_wggl",scale4(array[31]));
        data.add("c_glys",scale3(array[32]));
        data.add("z_ass",scale2(array[33]));
        data.add("a_ass",scale2(array[34]));
        data.add("b_ass",scale2(array[35]));
        data.add("c_ass",scale2(array[36]));

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * 数据格式：XXXXXX XXXX.XX XXXX.XX XXXXXX XXXXXX XXX.X MMDDhhmm XXX.X MMDDhhmm
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData15(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{3,3,3,3,3,2,4,2,4};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("dy_jc_time",scale0(array[0]));
        data.add("dy_hgl",scale2(array[1]));
        data.add("dy_cxl",scale2(array[2]));
        data.add("dy_csx_time",scale0(array[3]));
        data.add("dy_cxx_time",scale0(array[4]));
        data.add("dy_max",scale1(array[5]));
        data.add("dy_max_time",array[6]);
        data.add("dy_min",scale1(array[7]));
        data.add("dy_min_time",array[8]);

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * 数据格式 XXXXXX  XXXXXX  XXXXXX  XXXXXX  XXXXXX  XXXXXX
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData16(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{3,3,3,3,3,3};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("zx_ygxl_cxzcs",scale0(array[0]));
        data.add("fx_wgxl_cxzcs",scale0(array[1]));
        data.add("1_wgxl_cxzcs",scale0(array[2]));
        data.add("2_wgxl_cxzcs",scale0(array[3]));
        data.add("3_wgxl_cxzcs",scale0(array[4]));
        data.add("4_wgxl_cxzcs",scale0(array[5]));

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * 数据格式 YYMMDDhhmmss YYMMDDhhmmss XX.XXXX YYMMDDhhmm
     * @param bytes
     * @param flagFormat=16
     * @return
     */
    public static MsgResponse<String> parseData17(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,6,3,5};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("start_time",array[0]);
        data.add("end_time",array[1]);
        data.add("max_demand",scale4(array[2]));
        data.add("max_demand_time",array[3]);

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * YYMMDDhhmmss C0C1C2C3 XXXXXXXX（10个标识符）
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData18(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,4,4,4,4,4,4,4,4,4,4,4};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("happen_time",array[0]);
        data.add("operator_code",array[1]);
        data.add("data_flag1",array[2]);
        data.add("data_flag2",array[3]);
        data.add("data_flag3",array[4]);
        data.add("data_flag4",array[5]);
        data.add("data_flag5",array[6]);
        data.add("data_flag6",array[7]);
        data.add("data_flag7",array[8]);
        data.add("data_flag8",array[9]);
        data.add("data_flag9",array[10]);
        data.add("data_flag10",array[11]);

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     *  数据格式
     *  YYMMDDhhmmss C0C1C2C3   XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX
     *  XXXXXX.XX    XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX
     *  XXXXXX.XX    XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX
     *  XXXXXX.XX    XXXXXX.XX  XXXXXX.XX  XXXXXX.XX  XXXXXX.XX
     * @param bytes
     * @param flagFormat=19
     * @return
     */
    public static MsgResponse<String> parseData19(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("happen_time",array[0]);
        data.add("operator_code",array[1]);
        data.add("zx_yg_zdn",scale2(array[2]));
        data.add("fx_yg_zdn",scale2(array[3]));
        data.add("1_wg_zdn",scale2(array[4]));
        data.add("2_wg_zdn",scale2(array[5]));
        data.add("3_wg_zdn",scale2(array[6]));
        data.add("4_wg_zdn",scale2(array[7]));
        data.add("a_zx_yg_dn",scale2(array[8]));
        data.add("a_fx_yg_dn",scale2(array[9]));
        data.add("a_1_wg_zdn",scale2(array[10]));
        data.add("a_2_wg_zdn",scale2(array[11]));
        data.add("a_3_wg_zdn",scale2(array[12]));
        data.add("a_4_wg_zdn",scale2(array[13]));
        data.add("b_zx_yg_dn",scale2(array[14]));
        data.add("b_fx_yg_dn",scale2(array[15]));
        data.add("b_1_wg_zdn",scale2(array[16]));
        data.add("b_2_wg_zdn",scale2(array[17]));
        data.add("b_3_wg_zdn",scale2(array[18]));
        data.add("b_4_wg_zdn",scale2(array[19]));
        data.add("c_zx_yg_dn",scale2(array[20]));
        data.add("c_fx_yg_dn",scale2(array[21]));
        data.add("c_1_wg_zdn",scale2(array[22]));
        data.add("c_2_wg_zdn",scale2(array[23]));
        data.add("c_3_wg_zdn",scale2(array[24]));
        data.add("c_4_wg_zdn",scale2(array[25]));

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * 数据格式 YYMMDDhhmmss C0C1C2C3
     * XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm
     * XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm
     * XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm
     * XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm
     * XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm
     * XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm
     * XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm
     * XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm XX.XXXX YYMMDDhhmm
     * @param bytes
     * @param flagFormat=20
     * @return
     */
    public static MsgResponse<String> parseData20(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,4,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5,3,5};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("happen_time",array[0]);
        data.add("operator_code",array[1]);
        data.add("zx_yg_zd_xl",scale4(array[2]));
        data.add("zx_yg_zd_xl_time",array[3]);
        data.add("fx_yg_zd_xl",scale4(array[4]));
        data.add("fx_yg_zd_xl_time",array[5]);
        data.add("1_wg_zd_xl",scale4(array[6]));
        data.add("1_wg_zd_xl_time",array[7]);
        data.add("2_wg_zd_xl",scale4(array[8]));
        data.add("2_wg_zd_xl_time",array[9]);
        data.add("3_wg_zd_xl",scale4(array[10]));
        data.add("3_wg_zd_xl_time",array[11]);
        data.add("4_wg_zd_xl",scale4(array[12]));
        data.add("4_wg_zd_xl_time",array[13]);
        data.add("a_zx_yg_zd_xl",scale4(array[14]));
        data.add("a_zx_yg_zd_xl_time",array[15]);
        data.add("a_fx_yg_zd_xl",scale4(array[16]));
        data.add("a_fx_yg_zd_xl_time",array[17]);
        data.add("a_1_wg_zd_xl",scale4(array[18]));
        data.add("a_1_wg_zd_xl_time",array[19]);
        data.add("a_2_wg_zd_xl",scale4(array[20]));
        data.add("a_2_wg_zd_xl_time",array[21]);
        data.add("a_3_wg_zd_xl",scale4(array[22]));
        data.add("a_3_wg_zd_xl_time",array[23]);
        data.add("a_4_wg_zd_xl",scale4(array[24]));
        data.add("a_4_wg_zd_xl_time",array[25]);
        data.add("b_zx_yg_zd_xl",scale4(array[26]));
        data.add("b_zx_yg_zd_xl_time",array[27]);
        data.add("b_fx_yg_zd_xl",scale4(array[28]));
        data.add("b_fx_yg_zd_xl_time",array[29]);
        data.add("b_1_wg_zd_xl",scale4(array[30]));
        data.add("b_1_wg_zd_xl_time",array[31]);
        data.add("b_2_wg_zd_xl",scale4(array[32]));
        data.add("b_2_wg_zd_xl_time",array[33]);
        data.add("b_3_wg_zd_xl",scale4(array[34]));
        data.add("b_3_wg_zd_xl_time",array[35]);
        data.add("b_4_wg_zd_xl",scale4(array[36]));
        data.add("b_4_wg_zd_xl_time",array[37]);
        data.add("c_zx_yg_zd_xl",scale4(array[38]));
        data.add("c_zx_yg_zd_xl_time",array[39]);
        data.add("c_fx_yg_zd_xl",scale4(array[40]));
        data.add("c_fx_yg_zd_xl_time",array[41]);
        data.add("c_1_wg_zd_xl",scale4(array[42]));
        data.add("c_1_wg_zd_xl_time",array[43]);
        data.add("c_2_wg_zd_xl",scale4(array[44]));
        data.add("c_2_wg_zd_xl_time",array[45]);
        data.add("c_3_wg_zd_xl",scale4(array[46]));
        data.add("c_3_wg_zd_xl_time",array[47]);
        data.add("c_4_wg_zd_xl",scale4(array[48]));
        data.add("c_4_wg_zd_xl_time",array[49]);

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * YYMMDDhhmmss C0C1C2C3 XXXXXXXX
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData21(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,4,4};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("happen_time",array[0]);
        data.add("operator_code",array[1]);
        data.add("data_flag",array[2]);

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * C0C1C2C3 YYMMDDhhmmss YYMMDDhhmmss
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData22(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{4,6,6};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("operator_code",array[0]);
        data.add("timing_before_time",array[1]);
        data.add("timing_after_time",array[2]);

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * YYMMDDhhmmss C0C1C2C3 hhmmNN(3x14x16)
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData23(byte[] bytes, Integer flagFormat){
        JsonObject data = new JsonObject();
        StringBuilder sb = new StringBuilder();
        for(int j=1 ; j<=6; j++){
            sb.append(String.format("%02X", (byte)(bytes[6-j]-0x33)));
        }
        data.add("happen_time",sb.toString());
        sb = new StringBuilder();
        for(int j=1 ; j<=4; j++){
            sb.append(String.format("%02X", (byte)(bytes[10-j]-0x33)));
        }
        data.add("operator_code",sb.toString());
        for(int i=10 ; i<bytes.length; i+=3){
            int a = 1;
            if(i-10>3*14*8){
                a=2;
            }
            int b=0;
            int c=0;
            if(i>=10+3*14*8){
                b=(i-10-3*14*8)/42+1;
                c=(i-10-3*14*8-42*(b-1))/3+1;
            }else{
                b = (i-10+1)/42+1;
                c=(i-10-42*(b-1))/3+1;
            }
            JsonObject _data = new JsonObject();
            if(i >= bytes.length){
                break;
            }
            if(i+1>=bytes.length){
                _data.add("NN",String.format("%02X", (byte)(bytes[i]-0x33)));
                data.add(a+"-"+b+"-"+c,_data);
                break;
            }
            if(i+2>=bytes.length){
                _data.add("NN",String.format("%02X", (byte)(bytes[i]-0x33)));
                _data.add("mm",String.format("%02X", (byte)(bytes[i+1]-0x33)));
                data.add(a+"-"+b+"-"+c,_data);
                break;
            }
            _data.add("NN",String.format("%02X", (byte)(bytes[i]-0x33)));
            _data.add("mm",String.format("%02X", (byte)(bytes[i+1]-0x33)));
            _data.add("hh",String.format("%02X", (byte)(bytes[i+2]-0x33)));
            data.add(a+"-"+b+"-"+c,_data);
        }

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * YYMMDDhhmmss C0C1C2C3 MMDDNN（14*2）
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData24(byte[] bytes, Integer flagFormat){
        JsonObject data = new JsonObject();
        StringBuilder sb = new StringBuilder();
        for(int j=1 ; j<=6; j++){
            sb.append(String.format("%02X", (byte)(bytes[6-j]-0x33)));
        }
        data.add("happen_time",sb.toString());
        sb = new StringBuilder();
        for(int j=1 ; j<=4; j++){
            sb.append(String.format("%02X", (byte)(bytes[10-j]-0x33)));
        }
        data.add("operator_code",sb.toString());
        for(int i=10 ; i<bytes.length; i+=3){
            int a = 1;
            if(i-10>3*14){
                a=2;
            }
            int b=0;
            if(i>=10+3*14){
                b=(i-10-3*14)/3+1;
            }else{
                b = (i-10)/3+1;
            }
            JsonObject _data = new JsonObject();
            if(i >= bytes.length){
                break;
            }
            if(i+1>=bytes.length){
                _data.add("NN",String.format("%02X", (byte)(bytes[i]-0x33)));
                data.add(a+"-"+b,_data);
                break;
            }
            if(i+2>=bytes.length){
                _data.add("NN",String.format("%02X", (byte)(bytes[i]-0x33)));
                _data.add("DD",String.format("%02X", (byte)(bytes[i+1]-0x33)));
                data.add(a+"-"+b,_data);
                break;
            }
            _data.add("NN",String.format("%02X", (byte)(bytes[i]-0x33)));
            _data.add("DD",String.format("%02X", (byte)(bytes[i+1]-0x33)));
            _data.add("MM",String.format("%02X", (byte)(bytes[i+2]-0x33)));
            data.add(a+"-"+b,_data);
        }

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * YYMMDDhhmmss C0C1C2C3 XX
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData25(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,4,1};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("happen_time",array[0]);
        data.add("operator_code",array[1]);
        data.add("time_number",array[2]);

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * YYMMDDhhmmss C0C1C2C3 YYMMDDNN（254）
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData26(byte[] bytes, Integer flagFormat){
        JsonObject data = new JsonObject();
        StringBuilder sb = new StringBuilder();
        for(int j=1 ; j<=6; j++){
            sb.append(String.format("%02X", (byte)(bytes[6-j]-0x33)));
        }
        data.add("happen_time",sb.toString());
        sb = new StringBuilder();
        for(int j=1 ; j<=4; j++){
            sb.append(String.format("%02X", (byte)(bytes[10-j]-0x33)));
        }
        data.add("operator_code",sb.toString());
        for(int i=10 ; i<bytes.length; i+=4){
            int a = (i-10)/4+1;
            JsonObject _data = new JsonObject();
            if(i >= bytes.length){
                break;
            }
            if(i+1>=bytes.length){
                _data.add("NN",String.format("%02X", (byte)(bytes[i]-0x33)));
                data.add("data"+a,_data);
                break;
            }
            if(i+2>=bytes.length){
                _data.add("NN",String.format("%02X", (byte)(bytes[i]-0x33)));
                _data.add("DD",String.format("%02X", (byte)(bytes[i+1]-0x33)));
                data.add("data"+a,_data);
                break;
            }
            if(i+3>=bytes.length){
                _data.add("NN",String.format("%02X", (byte)(bytes[i]-0x33)));
                _data.add("DD",String.format("%02X", (byte)(bytes[i+1]-0x33)));
                _data.add("MM",String.format("%02X", (byte)(bytes[i+2]-0x33)));
                data.add("data"+a,_data);
                break;
            }
            _data.add("NN",String.format("%02X", (byte)(bytes[i]-0x33)));
            _data.add("DD",String.format("%02X", (byte)(bytes[i+1]-0x33)));
            _data.add("MM",String.format("%02X", (byte)(bytes[i+2]-0x33)));
            _data.add("YY",String.format("%02X", (byte)(bytes[i+3]-0x33)));
            data.add("data"+a,_data);
        }

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * YYMMDDhhmmss C0C1C2C3 DDhh DDhh DDhh
     * @param bytes
     * @param flagFormat=27
     * @return
     */
    public static MsgResponse<String> parseData27(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,4,2,2,2};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("happen_time",array[0]);
        data.add("operator_code",array[1]);
        data.add("settle_data1",array[2]);
        data.add("settle_data2",array[3]);
        data.add("settle_data3",array[4]);

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * YYMMDDhhmmss YYMMDDhhmmss XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX
     * XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX XXXXXX.XX
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData28(byte[] bytes, Integer flagFormat){
        Integer[] datatypes = new Integer[]{6,6,4,4,4,4,4,4,4,4,4,4,4,4};
        String[] array = new String[datatypes.length];
        int index=0;
        for(int i=0 ; i<datatypes.length; i++){
            index += datatypes[i];
            StringBuilder sb = new StringBuilder();
            for(int j=1 ; j<=datatypes[i]; j++){
                sb.append(String.format("%02X", (byte)(bytes[index-j]-0x33)));
            }
            array[i] = sb.toString();
        }

        JsonObject data = new JsonObject();
        data.add("start_time",array[0]);
        data.add("end_time",array[1]);
        data.add("before_zx_yg_zdn",scale2(array[2]));
        data.add("before_fx_yg_zdn",scale2(array[3]));
        data.add("before_1_wg_zdn",scale2(array[4]));
        data.add("before_2_wg_zdn",scale2(array[5]));
        data.add("before_3_wg_zdn",scale2(array[6]));
        data.add("before_4_wg_zdn",scale2(array[7]));
        data.add("after_zx_yg_zdn",scale2(array[8]));
        data.add("after_fx_yg_zdn",scale2(array[9]));
        data.add("after_1_wg_zdn",scale2(array[10]));
        data.add("after_2_wg_zdn",scale2(array[11]));
        data.add("after_3_wg_zdn",scale2(array[12]));
        data.add("after_4_wg_zdn",scale2(array[13]));

        JsonObject json = new JsonObject();
        json.add(KEY_DATA, data);
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,true);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * YYMMDDhhmm
     * @param data
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseData29(byte[] data, Integer flagFormat){
        StringBuilder sb = new StringBuilder();
        for(int i=data.length-1 ; i>=0; i--){
            sb.append(String.format("%02X", (byte)(data[i]-0x33)));
        }
        JsonObject json = new JsonObject();
        json.add(KEY_DATA, scale2(sb.toString()));
        json.add(KEY_FLAG, flagFormat);
        json.add(KEY_JSON,false);
        MsgResponse<String> resp = new MsgResponse<>();
        resp.setData(json.toString());
        resp.setSuccess(true);
        return resp;
    }

    /**
     * D1、D2、D4、D6、D7、D8、D9、DA、DB、DC、DD
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseErrData(byte[] bytes, Integer flagFormat){
        MsgResponse<String> resp = new MsgResponse<>();
        String[] errInfo = new String[]{"其他错误","费率数超","日时段数超","年时区数超","通信速率不能更改","密码错/未授权","无请求数据","其他错误"};
        int index = findIndex(bytes, (byte) 0x68);
        byte err_num = (byte)(bytes[index+10]-0x33);
        String err_num_str = String.format("%8s", Integer.toBinaryString(err_num & 0xFF)).replace(' ', '0');
        String errMessage="";
        for(int i=0; i<8; i++){
            if(err_num_str.charAt(i) == '1'){
                errMessage+=errInfo[i]+";";
            }
        }
        resp.setSuccess(false);
        resp.setMessage(errMessage);
        return resp;
    }

    /**
     * C3
     * @param bytes
     * @param flagFormat
     * @return
     */
    public static MsgResponse<String> parseSerrData(byte[] bytes, Integer flagFormat){

        return null;
    }

    public static int findIndex(byte[] array,byte value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i; //找到值为value的字节，返回下标
            }
        }
        return -1; // 未找到，返回-1
    }

    /**
     * 0位小数数据
     * @param data
     * @return
     */
    public static String scale0(String data){
        return new BigDecimal(data).setScale(0, RoundingMode.HALF_UP).toString();
    }
    /**
     * 1位小数数据
     * @param data
     * @return
     */
    public static String scale1(String data){
        return new BigDecimal(data).multiply(scale1).setScale(1, RoundingMode.HALF_UP).toString();
    }
    /**
     * 2位小数数据
     * @param data
     * @return
     */
    public static String scale2(String data){
        return new BigDecimal(data).multiply(scale2).setScale(2, RoundingMode.HALF_UP).toString();
    }
    /**
     * 3位小数数据
     * @param data
     * @return
     */
    public static String scale3(String data){
        return new BigDecimal(data).multiply(scale3).setScale(3, RoundingMode.HALF_UP).toString();
    }
    /**
     * 4位小数数据
     * @param data
     * @return
     */
    public static String scale4(String data){
        return new BigDecimal(data).multiply(scale4).setScale(4, RoundingMode.HALF_UP).toString();
    }
}
