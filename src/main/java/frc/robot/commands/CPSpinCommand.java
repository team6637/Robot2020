package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.SpinnerConstants;
import frc.robot.subsystems.SpinnerSubsystem;

public class CPSpinCommand extends CommandBase {
  
  SpinnerSubsystem m_subsystem;
  
  /**
   * Creates a new ControlPanelSpinCommand.
   */
  public CPSpinCommand(SpinnerSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //spin motor
    m_subsystem.spin();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(m_subsystem.getPosition() > SpinnerConstants.targetUnitsForTargetSpins){
      return true;
    }
    return false;
  }
}
