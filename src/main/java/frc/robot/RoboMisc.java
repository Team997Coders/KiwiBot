package frc.robot;

public class RoboMisc {

  public static double VectorToYaw(double x, double y) {

    double offset = 0;
    double dir = 1;

    if (isPos(y) && isPos(x)) { // Quadrant 1
      offset = 0;
      dir = 1;
    } else if (isPos(y) && !isPos(x)) { // Quadrant 2
      offset = 0;
      dir = -1;
    } else if (!isPos(y) && !isPos(x)) { // Quadrant 3
      offset = 90;
      dir = -1;
    } else if (!isPos(y) && isPos(x)) { // Quadrant 4
      offset = 90;
      dir = 1;
    }

    double newAngle = (dir * offset) + (dir * Math.atan2(Math.abs(y), Math.abs(x)));

    return newAngle;
  }

  public static double getMag(double x, double y) {
    return Math.sqrt((x * x) + (y * y));
  }

  public static boolean isPos(double a) {
    return a >= 0;
  }

  public static double rampValue(double val, double prev, double ramp, double clamp) {

    double newVal = val;

    double maxIncrement = Robot.deltaTime * ramp;

    if (Math.abs(val - prev) > maxIncrement) {
      double sign = (val - prev) / Math.abs(val - prev);
      newVal = (maxIncrement * sign) + prev;
    }

    if (Math.abs(newVal) > clamp) {
      newVal = (Math.abs(newVal) / newVal) * clamp;
    }

    return newVal;
  }

}