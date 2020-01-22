/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShelbowConstants;

public class ShelbowSubsystem extends SubsystemBase {

  WPI_TalonSRX masterMotor = new WPI_TalonSRX(ShelbowConstants.masterID);
  WPI_TalonSRX slaveMotor = new WPI_TalonSRX(ShelbowConstants.slaveID);

  DifferentialDrive drive = new DifferentialDrive(masterMotor, slaveMotor);
  
  public void shelbowFlex(double move){
  masterMotor.set(move);
  }
  
  public ShelbowSubsystem() {
    masterMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    masterMotor.setSensorPhase(false);
  }

   public double getPosition() {
    return masterMotor.getSelectedSensorPosition();
  }
  public void resetPosition() {
    masterMotor.setSelectedSensorPosition(2000, 0, 10);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
