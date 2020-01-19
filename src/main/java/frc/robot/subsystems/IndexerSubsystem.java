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

  PWMSparkMax topIndexerMotor = new PWMSparkMax(IndexerConstants.topMotorPort);
  PWMSparkMax bottomIndexerMotor = new PWMSparkMax(IndexerConstants.bottomMotorPort);
 
  public IndexerSubsystem() {

  }

  public void forward() {
    topIndexerMotor.set(IndexerConstants.speed);
    bottomIndexerMotor.set(IndexerConstants.speed);
  }

  public void backward() {
    topIndexerMotor.set(-IndexerConstants.speed);
    bottomIndexerMotor.set(-IndexerConstants.speed);

  }

  public void stop() {
    topIndexerMotor.set(0);
    bottomIndexerMotor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
