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

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SpinnerConstants;

public class SpinnerSubsystem extends SubsystemBase {

  public WPI_TalonSRX spinMotor = new WPI_TalonSRX(SpinnerConstants.motorID);

  DoubleSolenoid solenoid = new DoubleSolenoid(2, 3);
 
  public SpinnerSubsystem() {
    spinMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);	
    spinMotor.setSensorPhase(false);
    spinMotor.setInverted(false);
    spinMotor.setNeutralMode(NeutralMode.Coast);
  }

  public void lower() {
    solenoid.set(DoubleSolenoid.Value.kReverse);
  }

  public void raise() {
    solenoid.set(DoubleSolenoid.Value.kForward);
  }

  public void spin() {
    spinMotor.set(SpinnerConstants.speed);
  }

  public void stop() {
    spinMotor.set(0);
  }

  public int getPosition() {
    return spinMotor.getSelectedSensorPosition();
  }

  public void resetPosition() {
    spinMotor.setSelectedSensorPosition(2000, 0, 10);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
