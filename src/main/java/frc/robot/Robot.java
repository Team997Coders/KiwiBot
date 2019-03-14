/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.KiwiDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private AutoMode autoSelected;
  private final SendableChooser<AutoMode> m_chooser = new SendableChooser<>();

  // Last loggeed timestamp in milliseconds
  private double lastTime = 0;

  // Delta time in updates in seconds
  public static double deltaTime = 0;

  public static KiwiDrive kiwiDrive;
  public static DriveMix driveMix;

  public static Joystick gamepad1, gamepad2;

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Do Nothing", AutoMode.Nothing);
    m_chooser.addOption("Driver Control", AutoMode.Teleop);
    SmartDashboard.putData("SiCkO mOdE", m_chooser);

    driveMix = DriveMix.FieldRelativeHolonomic;

    kiwiDrive = new KiwiDrive();
  }
  
  @Override
  public void robotPeriodic() {
    deltaTime = (System.currentTimeMillis() - lastTime) * 1000;
    lastTime = System.currentTimeMillis();
  }
  
  @Override
  public void autonomousInit() {
    autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    //System.out.println("Auto selected: " + m_autoSelected);
  }
  
  @Override
  public void autonomousPeriodic() {
    switch (autoSelected) {
      case Nothing:
        // Put custom auto code here
        break;
      case Teleop:
      default:
        teleopPeriodic();
        break;
    }
  }

  @Override
  public void teleopPeriodic() {

    // Use the selected drive mixer.

    switch (driveMix) {
      case Arcade:
        kiwiDrive.holonomicMix(-gamepad1.getRawAxis(RobotMap.Gamepads.leftY), 0,
          gamepad1.getRawAxis(RobotMap.Gamepads.rightX));
        break;
      case FieldRelativeHolonomic:
        kiwiDrive.fieldRelativeHolonomicMix(-gamepad1.getRawAxis(RobotMap.Gamepads.leftY),
          gamepad1.getRawAxis(RobotMap.Gamepads.leftX),
          gamepad1.getRawAxis(RobotMap.Gamepads.rightX));
        break;
      case Holonomic:
        kiwiDrive.holonomicMix(-gamepad1.getRawAxis(RobotMap.Gamepads.leftY),
          gamepad1.getRawAxis(RobotMap.Gamepads.leftX),
          gamepad1.getRawAxis(RobotMap.Gamepads.rightX));
        break;
      case Tank:
        kiwiDrive.tankMix(-gamepad1.getRawAxis(RobotMap.Gamepads.leftY),
          -gamepad1.getRawAxis(RobotMap.Gamepads.rightY));
        break;
    }
  }
  
  @Override
  public void testPeriodic() {
  }

  public enum DriveMix {
    Holonomic, FieldRelativeHolonomic,
    Arcade, Tank
  }

  public enum AutoMode {
    Teleop, Nothing
  }
}
