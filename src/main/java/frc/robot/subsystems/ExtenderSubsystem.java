/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ExtenderConstants;

public class ExtenderSubsystem extends SubsystemBase {
  
  //Spark extenderMotor = new Spark(ExtenderConstants.motorPort);
  Spark extenderMotor = new Spark(ExtenderConstants.motorPort);

  public ExtenderSubsystem() {

  }

  public void extend() {
    extenderMotor.set(ExtenderConstants.speed);
  }

  public void stop() {
    extenderMotor.set(0);
  }

  public void retract(){
    extenderMotor.set(-ExtenderConstants.speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
