/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ShelbowConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.ShelbowSubsystem;
import frc.robot.util.Conversions;
import frc.robot.util.Gains;
import frc.robot.util.PID;

public class AutoAim extends CommandBase {

  private final DriveSubsystem m_drive;
  private final LimelightSubsystem m_limelight;
  private final ShelbowSubsystem m_shelbow;
  
  Gains turnGains;
  PID pid;
  Conversions conversions;
  double turnPower, error, distance, yOffset;

  public AutoAim(LimelightSubsystem limelight, DriveSubsystem drive, ShelbowSubsystem shelbow) {
    addRequirements(drive, limelight, shelbow);
    m_limelight = limelight;
    m_shelbow = shelbow;
    m_drive = drive;

    //turnGains = new Gains(DriveConstants.turnKp, DriveConstants.turnKi, DriveConstants.turnKd, true, "turn gains");
    turnGains = new Gains(DriveConstants.turnKp, DriveConstants.turnKi, DriveConstants.turnKd, true, "aa turn gains");
    pid = new PID(turnGains, 0);
    conversions = new Conversions();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drive.resetAngle();
    m_limelight.setupAutoAim();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turnGains.kPUpdated();
    turnGains.kIUpdated();
    turnGains.kDUpdated();

    if(m_limelight.isTarget()) {
      
      // ADJUST DRIVETRAIN
      error = -m_limelight.getTx();

      pid.setTarget(error);
      turnPower = pid.getCorrection(0);

      if(turnPower > 0.5)
        turnPower = 0.5;

      if(turnPower < -0.5) 
        turnPower = -0.5;

      m_drive.autonDrive(0, -turnPower);

      // ADJUST SHELBOW
      distance = conversions.angleToDistance(m_shelbow.getAngleWithoutYOffset());

      SmartDashboard.putNumber("distance from target", distance);

      // calculate yOffset from current distance
      yOffset = conversions.getRangedValue1FromValue2(ShelbowConstants.yRangeBottom, ShelbowConstants.yRangeTop, ShooterConstants.closestRangeInches, ShooterConstants.farthestRangeInches, distance);

      if(yOffset > Constants.ShelbowConstants.yRangeMax) 
        yOffset = Constants.ShelbowConstants.yRangeMax;
        
      if(yOffset < 0) yOffset = 0;

      // disable y offset calculations
      //yOffset = 0;

      // keep track of yOffset in shelbow so we can get angle without it
      m_shelbow.setYOffset(yOffset);

      m_shelbow.setPositionFromDegreeOffset(m_limelight.getTy() + yOffset);

    } else {
      m_drive.stop();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_limelight.setupDriveMode();
    m_drive.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
