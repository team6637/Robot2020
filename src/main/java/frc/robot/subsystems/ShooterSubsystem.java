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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {

  boolean m_tunable = false;
  
  WPI_TalonSRX topMotor = new WPI_TalonSRX(ShooterConstants.topMotorID);
  WPI_TalonSRX bottomMotor = new WPI_TalonSRX(ShooterConstants.bottomMotorID);

  // keep track of the fastest speed each motor hits
  double topMaxVelocity = 0;  
  double bottomMaxVelocity = 0;

  // set constants to local variables so we can tune with SmartDashboard
  double topTargetRPM = ShooterConstants.topTargetRPM;
  double bottomTargetRPM = ShooterConstants.bottomTargetRPM;

  public ShooterSubsystem(boolean tunable) {
    m_tunable = tunable;

    // config factory defaults for top and bottom
    topMotor.configFactoryDefault();
    bottomMotor.configFactoryDefault();

    // configure sensor, direction of motors/sensors, neutral mode
    topMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);	
    topMotor.setSensorPhase(true);
    topMotor.setInverted(false);
    topMotor.setNeutralMode(NeutralMode.Coast);

    bottomMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);	
    bottomMotor.setSensorPhase(true);
    bottomMotor.setInverted(false);
    bottomMotor.setNeutralMode(NeutralMode.Coast);

    // config nominal and peak outputs for top and bottom
    topMotor.configNominalOutputForward(0, ShooterConstants.kTimeoutMs);
		topMotor.configNominalOutputReverse(0, ShooterConstants.kTimeoutMs);
		topMotor.configPeakOutputForward(1, ShooterConstants.kTimeoutMs);
    topMotor.configPeakOutputReverse(-1, ShooterConstants.kTimeoutMs);
    
    bottomMotor.configNominalOutputForward(0, ShooterConstants.kTimeoutMs);
		bottomMotor.configNominalOutputReverse(0, ShooterConstants.kTimeoutMs);
		bottomMotor.configPeakOutputForward(1, ShooterConstants.kTimeoutMs);
		bottomMotor.configPeakOutputReverse(-1, ShooterConstants.kTimeoutMs);

    // config fpid for top and bottom
		topMotor.config_kP(ShooterConstants.kPIDLoopIdx, ShooterConstants.topGains.getKP(), ShooterConstants.kTimeoutMs);
		topMotor.config_kI(ShooterConstants.kPIDLoopIdx, ShooterConstants.topGains.getKI(), ShooterConstants.kTimeoutMs);
		topMotor.config_kD(ShooterConstants.kPIDLoopIdx, ShooterConstants.topGains.getKD(), ShooterConstants.kTimeoutMs);
    topMotor.config_kF(ShooterConstants.kPIDLoopIdx, ShooterConstants.topGains.getKF(), ShooterConstants.kTimeoutMs);

		bottomMotor.config_kP(ShooterConstants.kPIDLoopIdx, ShooterConstants.bottomGains.getKP(), ShooterConstants.kTimeoutMs);
		bottomMotor.config_kI(ShooterConstants.kPIDLoopIdx, ShooterConstants.bottomGains.getKI(), ShooterConstants.kTimeoutMs);
    bottomMotor.config_kD(ShooterConstants.kPIDLoopIdx, ShooterConstants.bottomGains.getKD(), ShooterConstants.kTimeoutMs);
    bottomMotor.config_kF(ShooterConstants.kPIDLoopIdx, ShooterConstants.bottomGains.getKF(), ShooterConstants.kTimeoutMs);
    
    if(tunable) {
      SmartDashboard.putNumber("shooter top target rpm", topTargetRPM);
      SmartDashboard.putNumber("shooter bottom target rpm", bottomTargetRPM);
    }
  }

  // manual shoot
  public void shoot() {
    topMotor.set(ControlMode.PercentOutput, ShooterConstants.speed);
    bottomMotor.set(ControlMode.PercentOutput, ShooterConstants.speed);
  }

  // velocity control
  public void setVelocity() {

    // update gains while tuning
    if(m_tunable)
      reconfigureLocalVariables();

    topMotor.set(ControlMode.Velocity, rpmToUnitsPer100ms(topTargetRPM));
    bottomMotor.set(ControlMode.Velocity, rpmToUnitsPer100ms(bottomTargetRPM));
  }

  // stop the motors
  public void stop() {
    topMotor.set(ControlMode.PercentOutput, 0.0);
    bottomMotor.set(ControlMode.PercentOutput, 0.0);
  }

  // get velocity
  public double getTopVelocity() {
    return topMotor.getSelectedSensorVelocity();
  }
  public double getBottomVelocity() {
    return bottomMotor.getSelectedSensorVelocity();
  }
  
  // give RPM, get u/100ms
  public double rpmToUnitsPer100ms(double rpm) {
    return rpm * ShooterConstants.unitsPerRotation / 600;
  }

  // give u/100ms, get RPM
  public double unitsPer100msToRPM(double unitsPer100ms) {
    return unitsPer100ms / ShooterConstants.unitsPerRotation * 600;
  }

  // check if motors are near setpoint
  public boolean atSetpoint() {
    if( getTopVelocity() >= rpmToUnitsPer100ms(topTargetRPM - ShooterConstants.tolerance) && getBottomVelocity() >= rpmToUnitsPer100ms(bottomTargetRPM - ShooterConstants.tolerance)){
      return true;
    }
    return false;
  }

  // reconfigure variables based on Smart Dashboard manipulation
  public void reconfigureLocalVariables() {

    // upate targetRPM variable from SmartDashboard
    topTargetRPM = SmartDashboard.getNumber("shooter top target rpm", topTargetRPM);
    bottomTargetRPM = SmartDashboard.getNumber("shooter bottom target rpm", bottomTargetRPM);

    // if changed in SmartDashboard, configure the motor controllers
    if(ShooterConstants.topGains.kPUpdated())
      topMotor.config_kP(ShooterConstants.kPIDLoopIdx, ShooterConstants.topGains.getKP(), ShooterConstants.kTimeoutMs);

    if(ShooterConstants.topGains.kIUpdated())
      topMotor.config_kI(ShooterConstants.kPIDLoopIdx, ShooterConstants.topGains.getKI(), ShooterConstants.kTimeoutMs);

    if(ShooterConstants.topGains.kDUpdated())
      topMotor.config_kD(ShooterConstants.kPIDLoopIdx, ShooterConstants.topGains.getKD(), ShooterConstants.kTimeoutMs);

    if(ShooterConstants.topGains.kFUpdated())
      topMotor.config_kF(ShooterConstants.kPIDLoopIdx, ShooterConstants.topGains.getKF(), ShooterConstants.kTimeoutMs);
      
    if(ShooterConstants.bottomGains.kPUpdated())
      bottomMotor.config_kP(ShooterConstants.kPIDLoopIdx, ShooterConstants.bottomGains.getKP(), ShooterConstants.kTimeoutMs);

    if(ShooterConstants.bottomGains.kIUpdated())
      bottomMotor.config_kI(ShooterConstants.kPIDLoopIdx, ShooterConstants.bottomGains.getKI(), ShooterConstants.kTimeoutMs);

    if(ShooterConstants.bottomGains.kDUpdated())
      bottomMotor.config_kD(ShooterConstants.kPIDLoopIdx, ShooterConstants.bottomGains.getKD(), ShooterConstants.kTimeoutMs);

    if(ShooterConstants.bottomGains.kFUpdated())
      bottomMotor.config_kF(ShooterConstants.kPIDLoopIdx, ShooterConstants.bottomGains.getKF(), ShooterConstants.kTimeoutMs);
  }
 
  @Override
  public void periodic() {

    if(m_tunable) {
      // get motor velocity. if higher than max, set max to this new high
      double topVelocity = unitsPer100msToRPM(getTopVelocity());
      if(topVelocity > topMaxVelocity) topMaxVelocity = topVelocity;

      double bottomVelocity = unitsPer100msToRPM(getBottomVelocity());
      if(bottomVelocity > bottomMaxVelocity) bottomMaxVelocity = bottomVelocity;

      // put values into SmartDashboard
      SmartDashboard.putNumber("shooter top speed", topVelocity);
      SmartDashboard.putNumber("shooter top max", topMaxVelocity);
      SmartDashboard.putNumber("shooter bottom speed", bottomVelocity);
      SmartDashboard.putNumber("shooter bottom max", bottomMaxVelocity); 
    } 
  }
}