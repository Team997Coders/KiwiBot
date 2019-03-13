package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

public class RobotMap {

  public static class Ports {

    public static final SerialPort.Port AHRS = Port.kUSB;

    public static final int
      motorA = 0, // Front motor
      motorB = 1, // Left motor
      motorC = 2; // Right motor

  }

  public static class Gamepads {

    public static final int
      gamepad1 = 0,
      gamepad2 = 1,

      buttonA = 1,
      buttonB = 2,
      buttonX = 3,
      buttonY = 4,

      leftX = 0,
      leftY = 1,
      rightX = 4,
      rightY = 5;

  }

}