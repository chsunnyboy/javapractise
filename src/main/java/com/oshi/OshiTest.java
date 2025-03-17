package com.oshi;

import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.util.List;
public class OshiTest {
    public static void main(String[] args) {
        getSystemInfo();
    }
    public static void getSystemInfo() {
        // 创建SystemInfo对象
        SystemInfo si = new SystemInfo();

        // 获取硬件抽象层
        HardwareAbstractionLayer hal = si.getHardware();

        //获取操作系统信息
        OperatingSystem os = si.getOperatingSystem();

        // 输出系统信息
        printSystemInfo(os);

        // 输出CPU信息
        printCpuInfo(hal.getProcessor());

        // 输出内存信息
        printMemoryInfo(hal.getMemory());

        // 输出磁盘信息
        printDiskInfo(os.getFileSystem().getFileStores());

        // 输出传感器信息
        printSensorsInfo(hal.getSensors());

        //查看网络接口流量
        printNetWorkFlows(hal.getNetworkIFs());
    }

    private static void printSystemInfo(OperatingSystem os) {
        System.out.println("操作系统: " + os);
    }

    private static void printCpuInfo(CentralProcessor processor) {
        //System.out.println("CPU型号: " + processor);
        System.out.println("CPU使用率: " + processor.getSystemCpuLoad(1000) * 100f + "%");
    }

    private static void printMemoryInfo(GlobalMemory memory) {
        System.out.println("总内存: " + FormatUtil.formatBytes(memory.getTotal()));
        System.out.println("可用内存: " + FormatUtil.formatBytes(memory.getAvailable()));
        System.out.println("内存使用率: " + ((memory.getTotal() - memory.getAvailable()) / (double) memory.getTotal()) * 100f + "%");
    }

    private static void printDiskInfo(List<OSFileStore> fsArray) {
        for (OSFileStore fs : fsArray) {
            long used = fs.getTotalSpace() - fs.getUsableSpace();
            System.out.println("磁盘: " + fs.getMount());
            System.out.println("总大小: " + FormatUtil.formatBytes(fs.getTotalSpace()));
            System.out.println("已用大小: " + FormatUtil.formatBytes(used));
            System.out.println("使用率: " + (used / (double) fs.getTotalSpace()) * 100f + "%");
        }
    }

    private static void printSensorsInfo(Sensors sensors) {
        System.out.println("CPU温度: " + sensors.getCpuTemperature() + "°C");
        System.out.println("风扇速度: " + sensors.getFanSpeeds());
    }
    private static void printNetWorkFlows(List<NetworkIF> nifs){
        for(NetworkIF networkIF:nifs){
            System.out.println(String.format("Interface:%s,Bytes Received:%s,Bytes Sent:%s",networkIF.getName(),networkIF.getBytesRecv(),networkIF.getBytesSent()));
            System.out.println("*********************");
        }
    }

}
