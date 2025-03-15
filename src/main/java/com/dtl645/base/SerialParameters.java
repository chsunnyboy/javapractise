package com.dtl645.base;

import com.fazecast.jSerialComm.SerialPort;
public class SerialParameters {
    private String portName;
    private int baudRate;
    private int flowControlIn;
    private int flowControlOut;
    private int databits;
    private int stopbits;
    private int parity;
    public SerialParameters() {
        portName = "";
        baudRate = 9600;
        flowControlIn = SerialPort.FLOW_CONTROL_DISABLED;
        flowControlOut = SerialPort.FLOW_CONTROL_DISABLED;
        databits = 8;
        stopbits = SerialPort.ONE_STOP_BIT;
        parity = SerialPort.NO_PARITY;
    }
    public SerialParameters(String portName, int baudRate,int flowControlIn,int flowControlOut,int databits,int stopbits,int parity) {
        this.portName = portName;
        this.baudRate = baudRate;
        this.flowControlIn = flowControlIn;
        this.flowControlOut = flowControlOut;
        this.databits = databits;
        this.stopbits = stopbits;
        this.parity = parity;
    }
    public String getPortName() {
        return portName;
    }
    public void setPortName(String name) {
        portName = name;
    }
    public void setBaudRate(int rate) {
        baudRate = rate;
    }
    public int getBaudRate() {
        return baudRate;
    }
    public void setFlowControlIn(int flowcontrol) {
        flowControlIn = flowcontrol;
    }
    public int getFlowControlIn() {
        return flowControlIn;
    }
    public void setFlowControlOut(int flowControlOut) {
        this.flowControlOut = flowControlOut;
    }
    public int getFlowControlOut() {
        return flowControlOut;
    }
    public void setDatabits(int databits) {
        this.databits = databits;
    }
    public int getDatabits() {
        return databits;
    }
    public void setStopbits(int stopbits) {
        this.stopbits = stopbits;
    }
    public int getStopbits() {
        return stopbits;
    }
    public void setParity(int parity) {
        this.parity = parity;
    }
    public int getParity() {
        return parity;
    }
    @Override
    public String toString() {
        return "SerialParameters{" +
                "portName='" + portName + '\'' +
                ", baudRate=" + baudRate +
                ", flowControlIn=" + flowControlIn +
                ", flowControlOut=" + flowControlOut +
                ", databits=" + databits +
                ", stopbits=" + stopbits +
                ", parity=" + parity +
                '}';
    }
}
