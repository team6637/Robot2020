/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IndexerConstants;

public class IndexerSubsystem extends SubsystemBase {

  PWMSparkMax topMotor = new PWMSparkMax(IndexerConstants.topMotorPort);
  PWMSparkMax bottomMotor = new PWMSparkMax(IndexerConstants.bottomMotorPort);
 
  public IndexerSubsystem() {

    // TODO: add NeutralMode Coast here. (see drivetrain)
    // add setInverted(false) here, see shooter

  }
  
  public void forward() {
    topMotor.set(IndexerConstants.speed);
    bottomMotor.set(IndexerConstants.speed);
  }

  // TODO: this shouldn't be negative
  // you'll have to change this to positive speed and switch the wires on the motor
  public void backward() {
    topMotor.set(-IndexerConstants.speed);
    bottomMotor.set(-IndexerConstants.speed);
  }

  public void stop() {
    topMotor.set(0);
    bottomMotor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
