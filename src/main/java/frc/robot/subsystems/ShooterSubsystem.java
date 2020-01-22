/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
//top/bottom
  WPI_TalonSRX topMotor = new WPI_TalonSRX(ShooterConstants.topMotorID);
  WPI_TalonSRX bottomMotor = new WPI_TalonSRX(ShooterConstants.bottomMotorID);

 
  public ShooterSubsystem() {
    topMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);	
    topMotor.setSensorPhase(false);
    topMotor.setInverted(false);
    topMotor.setNeutralMode(NeutralMode.Brake);
  }

  public void spin(double speed) {
    topMotor.set(ControlMode.PercentOutput, speed);
    bottomMotor.set(ControlMode.PercentOutput, speed);
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
