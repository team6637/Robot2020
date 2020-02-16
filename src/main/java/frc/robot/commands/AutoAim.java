/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.ShelbowSubsystem;
import frc.robot.util.Gains;
import frc.robot.util.PID;

public class AutoAim extends CommandBase {
  /**
   * Creates a new TurnToAnglePID.
   */
  double kP = 0.13;
  double turnPower, error;
  private final DriveSubsystem m_drive;
  private final LimelightSubsystem m_limelight;
  private final ShelbowSubsystem m_shelbow;
  boolean targetWasSeen;
  Gains gains;
  PID pid;
  double yOffset = 4;

  public AutoAim(LimelightSubsystem limelight, DriveSubsystem drive, ShelbowSubsystem shelbow) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive, limelight, shelbow);
    m_limelight = limelight;
    m_shelbow = shelbow;
    m_drive = drive;

    gains = new Gains(0.085, 0.003, 0.5, true, "turn gains");
    pid = new PID(gains, 0.0);
    SmartDashboard.putNumber("shelbow y offset", yOffset);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drive.resetAngle();
    m_limelight.setupAutoAim();
    targetWasSeen = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    gains.kPUpdated();
    gains.kIUpdated();
    gains.kDUpdated();

    if(m_limelight.isTarget()) {      
      error = -m_limelight.getTx();

      //turnPower = gains.getKP() * error;
      pid.setTarget(error);
      turnPower = pid.getCorrection(0);

      if(turnPower > 0.5) {
        turnPower = 0.5;
      }

      if(turnPower < -0.5) {
        turnPower = -0.5;
      }

      // if(error > 0.75 && turnPower > 0.0 && turnPower < 0.3) {
      //   turnPower = 0.3;
      // }

      // if(error < -0.75 && turnPower < 0.0 && turnPower > -0.3) {
      //   turnPower = -0.4;
      // }

      m_drive.autonDrive(0, -turnPower);
      yOffset = SmartDashboard.getNumber("shelbow y offset", yOffset);
      m_shelbow.setPositionFromDegrees(m_limelight.getTy() + yOffset);

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
