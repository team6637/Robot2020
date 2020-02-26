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
import frc.robot.util.Gains;
import frc.robot.util.PID;

public class TurnToAngleCommand extends CommandBase {
  private final Gains turnGains;
  private final PID pid;
  double turnPower, error, m_targetAngle, currentAngle;
  private final DriveSubsystem m_drive;
  private final double tolerance = 0.5;

  /**
   * Creates a new TurnToAngleCommand.
   */
  public TurnToAngleCommand(DriveSubsystem drive, double targetAngle) {
    m_drive = drive;
    m_targetAngle = Math.IEEEremainder(targetAngle, 360);
    SmartDashboard.putNumber("tta target", targetAngle);

    addRequirements(drive);
    turnGains = new Gains(.055, 0, 0, false, "tta turn gains");
    pid = new PID(turnGains, targetAngle);

  }

  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    m_drive.resetAngle();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turnGains.kPUpdated();
    turnGains.kIUpdated();
    turnGains.kDUpdated();

    currentAngle = m_drive.getAngle();
    error = m_targetAngle - currentAngle;
    SmartDashboard.putNumber("tta error", error);  

    turnPower = pid.getCorrection(currentAngle);
    
    if(turnPower > 0.5)
    turnPower = 0.5;

    if(turnPower < -0.5) 
    turnPower = -0.5;

    SmartDashboard.putNumber("tta turn power", turnPower);  

    m_drive.autonDrive(0, -turnPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(error) < tolerance)
      return true;
    
    return false;
  }
}
