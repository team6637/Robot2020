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
    // TODO : change neutral mode to coast instead of brake. This way the shooter doesn't brake when not in use, we want it to keep spinning (coasting)
    topMotor.setNeutralMode(NeutralMode.Brake);

    // TODO: we will be using an integrated encoder on each motor, so please setup one for the bottom motor
    // also setup other settings like you did for the top (change neutral mode to coast)
  }

  public void spin(double speed) {
    topMotor.set(ControlMode.PercentOutput, speed);
    bottomMotor.set(ControlMode.PercentOutput, speed);
  }

  public void stop() {
    topMotor.set(0);
    bottomMotor.set(0);
  }


  // TODO: set up encoder methods to get and reset the current encoder position
  // look on line 130 here for the getPosition and resetPosition methods for both motors: https://github.com/team6637/Robot2019/blob/master/src/main/java/frc/robot/subsystems/Wrist.java
  // also setup a getVelocity method for both methods which we will use in our velocity PID control


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
