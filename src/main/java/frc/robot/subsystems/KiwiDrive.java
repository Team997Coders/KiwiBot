package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Spark;
import frc.robot.RoboMisc;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class KiwiDrive {

  //private boolean isFieldOrientated = true;

  private double initNav = 0;
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
    forward = (Math.cos(getNav()) * forward) + (Math.sin(getNav()) * strafe);
    strafe = (Math.sin(getNav()) * -forward) + (Math.cos(getNav()) * strafe);

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

    double a = (Math.sin(angle) * magnitude) + spin;
    double b = (Math.sin(angle + 120) * magnitude) + spin;
    double c = (Math.sin(angle - 120) * magnitude) + spin;

    double h = a;

    if (b > h) {
      h = b;
    }
    if (c > h) {
      h = c;
    }

    double factor = 1 / h;

    motorA.set(a * factor);
    motorB.set(b * factor);
    motorC.set(c * factor);
  }

  public void holonomicMix2(double forw, double str, double rot) {
    double[] angles = new double[] { 0, -120, 120 };
    double yaw = RoboMisc.VectorToYaw(str, forw);

    for (int i = 0; i < angles.length; i++) {
      angles[i] -= yaw;
    }

    double small = 0;

    for (int i = 0; i < angles.length; i++) {
      double h = (angles[i] % 90) * (Math.abs(angles[i]) / angles[i]);
      if (h > small) {
        small = h;
      }
    }

    small = (small / 180) * (Math.PI);

    double mag = Math.sin(small) * RoboMisc.getMag(str, forw);

    double[] speeds = new double[] {
      (mag / Math.sin((angles[0] / 180) * Math.PI)) + rot,
      (mag / Math.sin((angles[1] / 180) * Math.PI)) + rot,
      (mag / Math.sin((angles[2] / 180) * Math.PI)) + rot
    };

    double larg = -1;

    for (int i = 0; i < speeds.length; i++) {
      if (speeds[i] > larg) {
        larg = speeds[i];
      }
    }

    double normalizeVal = 1 / larg;

    for (int i = 0; i < speeds.length; i++) {
      speeds[i] *= normalizeVal;
    }

    motorA.set(speeds[0]);
    motorB.set(speeds[1]);
    motorC.set(speeds[2]);
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