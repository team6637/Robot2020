/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {

  private final PWMSparkMax intakeMotor = new PWMSparkMax(IntakeConstants.motorPort);
 
  public IntakeSubsystem() {
    intakeMotor.setInverted(false);
  }

  public void acquire() {
    intakeMotor.set(IntakeConstants.speed);
  }

  public void backward() {
    intakeMotor.set(-IntakeConstants.speed);
  }

  public void stop() {
    intakeMotor.set(0.0);
  }

  @Override
  public void periodic() {
  }
}
