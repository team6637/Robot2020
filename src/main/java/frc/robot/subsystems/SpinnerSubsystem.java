/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SpinnerConstants;

public class SpinnerSubsystem extends SubsystemBase {

  WPI_TalonSRX spinMotor = new WPI_TalonSRX(SpinnerConstants.motorID);
 
  public SpinnerSubsystem() {

  }

  public void spin() {
    spinMotor.set(SpinnerConstants.speed);
  }

  public void stop() {
    spinMotor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
