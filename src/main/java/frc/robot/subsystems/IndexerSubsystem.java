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

  private final PWMSparkMax topMotor = new PWMSparkMax(IndexerConstants.topMotorPort);
  private final PWMSparkMax bottomMotor = new PWMSparkMax(IndexerConstants.bottomMotorPort);

  private final DigitalInput ballSensor;
  private final DigitalInput ballSensorTop;

  private int ballsShot = 0;
  private boolean ballTopIsSensed = false;

  private final boolean m_tunable;
 
  public IndexerSubsystem(boolean tunable) {
    m_tunable = tunable;

    topMotor.setInverted(true);
    bottomMotor.setInverted(true);

    ballSensor = new DigitalInput(1);
    ballSensorTop = new DigitalInput(2);
  }
  
  public void forward() {
    topMotor.set(IndexerConstants.speed);
    bottomMotor.set(IndexerConstants.speed);
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
  
  public void backwardSlow() {
    topMotor.set(-IndexerConstants.speedSlow);
    bottomMotor.set(-IndexerConstants.speedSlow);
  }
  public void stop() {
    topMotor.set(0.0);
    bottomMotor.set(0.0);
  }

  public boolean getBallSensor() {
    return ballSensor.get() ? false : true;
  }

  public boolean getBallSensorTop() {
    return ballSensorTop.get() ? false : true;
  }

  public void resetBallsShot() {
    ballsShot = 0;
  }

  @Override
  public void periodic() {

    // check if sensor sees ball
    if(getBallSensor()) {
      if(!ballTopIsSensed) {
        ballTopIsSensed = true;
        ballsShot++;
      }
    } else {
      ballTopIsSensed = false;
    }

    if(m_tunable) {
      SmartDashboard.putBoolean("ball sensor bottom", getBallSensor());
      SmartDashboard.putBoolean("ball sensor top", getBallSensorTop());
      SmartDashboard.putNumber("balls shot", ballsShot);
    }
  }

  
}
