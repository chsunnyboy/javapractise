package com.bjrltcp;

public class PIDController {


    private double kp; // Proportional gain
    private double ki; // Integral gain
    private double kd; // Derivative gain
    private double setPoint; // Desired pressure
    private double integral = 0;
    private double prevError = 0;
    public PIDController(double kp, double ki, double kd, double setPoint) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.setPoint = setPoint;
    }
    public double calculateOutput(double currentPressure) {
        double error = setPoint - currentPressure;
        integral += error;
        double derivative = error - prevError;
        double output = kp * error + ki * integral + kd * derivative;
        prevError = error;
        return output;
    }
    public static void main(String[] args) {
        double currentPressure = 24; // Current pressure in the heat exchange station
        double desiredPressure = 31; // Desired pressure
        PIDController controller = new PIDController(0.7, 0.05, 0.05, desiredPressure);
        // Simulate the control loop
        for (int i = 0; i < 100; i++) {    //停止条件需要研究，类似死区不调整
            double output = controller.calculateOutput(currentPressure);
            System.out.println("Current output: " + output);
            currentPressure += output; // Adjust the valve based on the output
            System.out.println("Current Pressure: " + currentPressure);
        }
    }

}
