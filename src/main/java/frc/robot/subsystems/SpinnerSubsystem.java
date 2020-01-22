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

  // TODO: add a Pneumatic solenoid to lower/raise the spinner
 
  public SpinnerSubsystem() {

    // TODO: setup the integrated Encoder like you did in the Shooter. 
    // neutral mode should be brake since we want the spinner to brake to a halt when the button isn't pressed

  }

  public void spin() {
    spinMotor.set(SpinnerConstants.speed);
  }

  // TODO: add the methods for the pneumatic controlled lower/raise of the spinner

  public void stop() {
    spinMotor.set(0);
  }

  // TODO: set up encoder methods to get and reset the current encoder position
  // look on line 130 here for the getPosition and resetPosition methods: https://github.com/team6637/Robot2019/blob/master/src/main/java/frc/robot/subsystems/Wrist.java

  // TODO: we know the CPR of this encoder is 1024. That's how many counts this encoder reads in 1 rotation.
  // we also know the wheel diameter of the spinner and the control panel. 
  // we need to use this data to come up with the correct number of counts needed to spin the control panel 3><5 rotations. 
  // pew pew (how do we handle this? a button that fires a command that ......... ) 

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
