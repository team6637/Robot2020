/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IndexerConstants;

public class IndexerSubsystem extends SubsystemBase {

  PWMSparkMax topMotor = new PWMSparkMax(IndexerConstants.topMotorPort);
  PWMSparkMax bottomMotor = new PWMSparkMax(IndexerConstants.bottomMotorPort);

  DigitalInput ballSensor;
 
  public IndexerSubsystem() {
    topMotor.setInverted(true);
    bottomMotor.setInverted(true);

    ballSensor = new DigitalInput(1);

  }
  
  public void forward() {
    topMotor.set(IndexerConstants.speed);
    bottomMotor.set(IndexerConstants.speed);
  }

  public boolean getBallSensor() {
    return ballSensor.get();
  }

  public void forwardWithSensor() {
    if(ballSensor.get()) {
      forward();
    } else {
      stop();
    }
  }

  public void backward() {
    topMotor.set(-IndexerConstants.speed);
    bottomMotor.set(-IndexerConstants.speed);
  }

  public void stop() {
    topMotor.set(0.0);
    bottomMotor.set(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    SmartDashboard.putBoolean("ball sensor", ballSensor.get());

  }
}
