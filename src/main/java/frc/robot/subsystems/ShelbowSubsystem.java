/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShelbowConstants;

public class ShelbowSubsystem extends SubsystemBase {

  WPI_TalonSRX masterMotor = new WPI_TalonSRX(ShelbowConstants.masterID);
  WPI_TalonSRX slaveMotor = new WPI_TalonSRX(ShelbowConstants.slaveID);

  public ShelbowSubsystem() {
    masterMotor.setNeutralMode(NeutralMode.Coast);
    slaveMotor.setNeutralMode(NeutralMode.Coast);

    // TODO: add setInverted() to both motors. I think one will be true and the other false. see how we had this setup with the lift last year
    // if you are confused, please ask on this one.

    masterMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    masterMotor.setSensorPhase(false);
  }
  
  public void shelbowFlex(double move){    
    masterMotor.set(move);
  }

  public double getPosition() {
    return masterMotor.getSelectedSensorPosition();
  }
  public void resetPosition() {
    masterMotor.setSelectedSensorPosition(2000, 0, 10);
  }

  // made a public getter so we can pass this object outside of this subsystem
  // since the pigeon gyro is plugged into this, we have to pass this object into the gyro, which lives in the drive subsystem
  public WPI_TalonSRX getMasterReference() {
    return masterMotor;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
