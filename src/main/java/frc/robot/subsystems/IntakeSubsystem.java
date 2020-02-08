/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {

  PWMSparkMax intakeMotor = new PWMSparkMax(IntakeConstants.motorPort);

  DoubleSolenoid solenoid = new DoubleSolenoid(1, 0);

  DigitalInput ballSensor;
 
  public IntakeSubsystem() {
    intakeMotor.setInverted(false);
    raise();

    ballSensor = new DigitalInput(1);
  }

  public void lower() {
    solenoid.set(DoubleSolenoid.Value.kReverse);
  }

  public void raise() {
    solenoid.set(DoubleSolenoid.Value.kForward);
  }


  public void acquire() {
    intakeMotor.set(IntakeConstants.speed);
  }

  public void stop() {
    intakeMotor.set(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    SmartDashboard.putBoolean("ball sensor", ballSensor.get());
  }
}
