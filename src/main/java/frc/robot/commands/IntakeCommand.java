/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShelbowSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class IntakeCommand extends CommandBase {
  /**
   * Creates a new IntakeCommand.
   */

  IntakeSubsystem m_intake;
  IndexerSubsystem m_indexer;
  ShelbowSubsystem m_shelbow;
  ShooterSubsystem m_shooter;

  int counter = 0;

  public IntakeCommand(IntakeSubsystem intake, IndexerSubsystem indexer, ShelbowSubsystem shelbow, ShooterSubsystem shooter) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake, indexer, shelbow, shooter);
    m_intake = intake;
    m_indexer = indexer;
    m_shelbow = shelbow;
    m_shooter = shooter;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {      
    
    m_shooter.rollBackward();
    m_shelbow.goToUpPosition();
    m_intake.acquire();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // if ball is in the intake
    if(m_indexer.getBallSensor()) {
      m_indexer.forward();
      m_shooter.rollBackward();

      // reset counter
      counter = 0;

    // if no ball
    } else {
      
      // for the first x counts, run the ball in
      if(counter < 5) {
        m_indexer.forward();
        m_shooter.rollBackward();
      } else {
        m_indexer.stop();
        m_shooter.stop();
      }
    
      // increment counter 
      counter++;
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shooter.stop();
    m_intake.stop();
    m_indexer.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
