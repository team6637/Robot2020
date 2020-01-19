/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShelbowConstants;

public class ShelbowSubsystem extends SubsystemBase {

  WPI_TalonSRX masterMotor = new WPI_TalonSRX(ShelbowConstants.masterID);
  WPI_TalonSRX slaveMotor = new WPI_TalonSRX(ShelbowConstants.slaveID);

  public ShelbowSubsystem() {

  }
  public void 

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
