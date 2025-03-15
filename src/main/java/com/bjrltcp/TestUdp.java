package com.bjrltcp;

import com.bjrltcp.tools.DESTools;
import com.bjrltcp.tools.TcpTools;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUdp {
    public static void main(String[] args) throws Exception {
        String serialNumber = "";
        String mct = "4G"; //移动通信技术
        String isp = "CU"; //互联网服务提供商
        String imei = "864568060613107"; //国际移动设备身份码
        String iccid = "89860621120000163143"; //集成电路卡识别码
        String ver_sf = "安全工业智能网关系统"; //软件版本
        String ver_hw = "KBOX-R41";//硬件版本
        String csq = "23"; //信号质量
        String err_l = ""; //故障类型编码
        int ts_rst = 0; //复位次数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date()); //设备的当前时间

        JsonObject reg = new JsonObject();
        reg.add("DID","bjrl202403990121");
        reg.add("IP","8.140.29.244");
        reg.add("SN",serialNumber);
        reg.add("MCT",mct);
        reg.add("ISP",isp);
        reg.add("IMEI",imei);
        reg.add("ICCID",iccid);
        reg.add("VER_SF",ver_sf);
        reg.add("VER_HW",ver_hw);
        reg.add("CSQ",csq);
        reg.add("ERR_L",err_l);
        reg.add("TS_RST",ts_rst);
        reg.add("TIME",time);
        JsonObject deviceInfo = new JsonObject();
        deviceInfo.add("REG",reg);
        byte[] encryptData = DESTools.desEncodeData(deviceInfo.toString());
        JsonObject data = new JsonObject();
        data.add("k","bjrl202403990121");
        String hexData = TcpTools.bytesToHex(encryptData);
        data.add("d",hexData);
        Boolean sendUdpData = sendUdpData(data);
    }
    public static Boolean sendUdpData(JsonObject data) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("8.140.29.244");
        int port = 3334;
        byte[] bytes = data.toString().getBytes();

        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        socket.send(packet);

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        socket.close();
        JsonObject object = Json.parse(receivedMessage).asObject();
        String err = object.getString("err","1");
        String msg = object.getString("msg","OK");
        if(!"0".equals(err)){
            System.out.println("发送udp数据失败，失败原因："+msg);
            return false;
        }
        return true;
    }
}
