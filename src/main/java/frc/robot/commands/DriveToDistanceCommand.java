/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.util.Gains;
import frc.robot.util.PID;

public class DriveToDistanceCommand extends CommandBase {

  private final Gains driveGains;
  private final PID pid;
  double drivePower, error, m_targetDistance, currentDistance;
  private final DriveSubsystem m_drive;
  private final double tolerance = Units.inchesToMeters(3);
  private int onTargetCounter = 0;
  /**
   * Creates a new DriveToDistanceCommand.
   */
  public DriveToDistanceCommand(DriveSubsystem drive, double targetDistance) {
    m_drive = drive;
    m_targetDistance = targetDistance;
    SmartDashboard.putNumber("dtd target", targetDistance);

    addRequirements(drive);
    driveGains = new Gains(12, 0, 50, true, "dtd drive gains"); 
    pid = new PID(driveGains, targetDistance);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    m_drive.resetEncoders();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // delete me
    //m_targetDistance = SmartDashboard.getNumber("dtd target", m_targetDistance);
    //pid.setTarget(m_targetDistance);

    driveGains.kPUpdated();
    driveGains.kIUpdated();
    driveGains.kDUpdated();

    currentDistance = m_drive.getRightDistance();
    SmartDashboard.putNumber("dtd current distance", currentDistance);
    error = m_targetDistance - currentDistance;
    SmartDashboard.putNumber("dtd error", error);

    drivePower = pid.getCorrection(currentDistance);

    if(drivePower > 0.5)
      drivePower = 0.5;

    if(drivePower < -0.5)
      drivePower = -0.5;

    SmartDashboard.putNumber("dtd drive power", drivePower);

    m_drive.autonDrive(drivePower, 0);

    if (Math.abs(error) < tolerance) {
      onTargetCounter++;
    } else {
      onTargetCounter = 0;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(onTargetCounter >= 50)
      return true;
    
    return false;
  }
}
