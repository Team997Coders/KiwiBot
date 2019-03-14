package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Spark;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class KiwiDrive {

  private boolean isFieldOrientated = true;

  private double initNav = 0;
  private double modifier = 0.5;
  private double ramp = 1.0;

  private double prevForward = 0.0;
  private double prevStrafe = 0.0;

  private Spark motorA, motorB, motorC;

  private AHRS navx;

  public KiwiDrive() {
    motorA = new Spark(RobotMap.Ports.motorA);
    motorB = new Spark(RobotMap.Ports.motorB);
    motorC = new Spark(RobotMap.Ports.motorC);

    motorA.setInverted(true);
    motorB.setInverted(true);
    motorC.setInverted(true);

    try {
      navx = new AHRS(RobotMap.Ports.AHRS);
      navx.reset();
      navx.zeroYaw();
      initNav = navx.getAngle();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void fieldRelativeHolonomicMix(double forward, double strafe, double spin, boolean useRamp) {
    forward = Math.cos(getNav()) * forward;
    strafe = -Math.sin(getNav()) * strafe;

    holonomicMix(forward, strafe, spin, useRamp);
  }

  public void holonomicMix(double forward, double strafe, double spin, boolean useRamp) {

    if (useRamp) {
      double maxIncrement = Robot.deltaTime * ramp;

      if (Math.abs(forward - prevForward) > maxIncrement) {
        double sign = (forward - prevForward) / Math.abs(forward - prevForward);
        forward = (maxIncrement * sign) + prevForward;
      }

      if (Math.abs(strafe - prevStrafe) > maxIncrement) {
        double sign = (strafe - prevStrafe) / Math.abs(strafe - prevStrafe);
        strafe = (maxIncrement * sign) + prevStrafe;
      }
    }

    prevForward = forward;
    prevStrafe = strafe;

    double angle = Math.atan2(strafe, forward);
    double magnitude = Math.sqrt((forward * forward) + (strafe * strafe));

    double a = ((Math.sin(angle) * magnitude) + spin) * modifier;
    double b = ((Math.sin(angle + 120) * magnitude) + spin) * modifier;
    double c = ((Math.sin(angle - 120) * magnitude) + spin) * modifier;

    motorA.set(a);
    motorB.set(b);
    motorC.set(c);
  }

  public void tankMix(double left, double right) {
    motorA.set((left - right) / 2);
    motorB.set(left);
    motorC.set(-right);
  }

  public double getNavRad() {
    return (navx.getAngle() - initNav) * 2 * Math.PI;
  }

  public double getNav() {
    return navx.getAngle() - initNav;
  }

}