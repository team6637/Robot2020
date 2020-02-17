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
import frc.robot.util.Gains;
import frc.robot.util.Conversions;

public class ShooterSubsystem extends SubsystemBase {

  
  private final WPI_TalonSRX topMotor = new WPI_TalonSRX(ShooterConstants.topMotorID);
  private final WPI_TalonSRX bottomMotor = new WPI_TalonSRX(ShooterConstants.bottomMotorID);
  
  private final boolean m_tunable;
 
  private final Gains topGains;
  private final Gains bottomGains;
  private final Conversions conversions = new Conversions();

  double topTargetRPM = ShooterConstants.closestRangeTopRPM;
  double bottomTargetRPM = ShooterConstants.closestRangeBottomRPM;
  double distance;
  
  public ShooterSubsystem(boolean tunable) {
    m_tunable = tunable;

    // kP, kI, kD, kF
    topGains = new Gains(1.65, 0.0, 40.0, 0.6, m_tunable, "shooter top");
    bottomGains = new Gains(1.65, 0.0, 40.0, 0.6, m_tunable, "shooter bottom");

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
		topMotor.config_kP(ShooterConstants.kPIDLoopIdx, topGains.getKP(), ShooterConstants.kTimeoutMs);
		topMotor.config_kI(ShooterConstants.kPIDLoopIdx, topGains.getKI(), ShooterConstants.kTimeoutMs);
		topMotor.config_kD(ShooterConstants.kPIDLoopIdx, topGains.getKD(), ShooterConstants.kTimeoutMs);
    topMotor.config_kF(ShooterConstants.kPIDLoopIdx, topGains.getKF(), ShooterConstants.kTimeoutMs);

		bottomMotor.config_kP(ShooterConstants.kPIDLoopIdx, bottomGains.getKP(), ShooterConstants.kTimeoutMs);
		bottomMotor.config_kI(ShooterConstants.kPIDLoopIdx, bottomGains.getKI(), ShooterConstants.kTimeoutMs);
    bottomMotor.config_kD(ShooterConstants.kPIDLoopIdx, bottomGains.getKD(), ShooterConstants.kTimeoutMs);
    bottomMotor.config_kF(ShooterConstants.kPIDLoopIdx, bottomGains.getKF(), ShooterConstants.kTimeoutMs);
    
    if(m_tunable) {
      SmartDashboard.putNumber("shooter top target rpm", topTargetRPM);
      SmartDashboard.putNumber("shooter bottom target rpm", bottomTargetRPM);
    }
  }

  // manual shoot
  public void shoot() {
    topMotor.set(ControlMode.PercentOutput, ShooterConstants.speed);
    bottomMotor.set(ControlMode.PercentOutput, ShooterConstants.speed);
  }

  public void backward(){
    topMotor.set(ControlMode.PercentOutput, ShooterConstants.backSpeed);
    bottomMotor.set(ControlMode.PercentOutput, ShooterConstants.backSpeed);
  }

  // velocity control
  public void setSweetSpotVelocity() {

    // update gains while tuning
    if(m_tunable)
      reconfigureLocalVariables();

    topMotor.set(ControlMode.Velocity, rpmToUnitsPer100ms(ShooterConstants.closestRangeTopRPM));
    bottomMotor.set(ControlMode.Velocity, rpmToUnitsPer100ms(ShooterConstants.closestRangeBottomRPM));
  }  

  // velocity control
  public void setVelocityFromAngle(double angle) {

    // update gains while tuning
    if(m_tunable)
      reconfigureLocalVariables();

    distance = conversions.angleToDistance(angle);
    
    // set top RPM
    topTargetRPM = conversions.getRangedValue1FromValue2(ShooterConstants.closestRangeTopRPM, ShooterConstants.farthestRangeTopRPM, ShooterConstants.closestRangeInches, ShooterConstants.farthestRangeInches, distance);

    // constrain top RPM to within limit range
    if(topTargetRPM < ShooterConstants.RPMLowLimit)
      topTargetRPM = ShooterConstants.RPMLowLimit;

    if(topTargetRPM > ShooterConstants.RPMHighLimit)
      topTargetRPM = ShooterConstants.RPMHighLimit;

    // set bottom RPM
    bottomTargetRPM = conversions.getRangedValue1FromValue2(ShooterConstants.closestRangeBottomRPM, ShooterConstants.farthestRangeBottomRPM, ShooterConstants.closestRangeInches, ShooterConstants.farthestRangeInches, distance);

    // constrain bottom RPM to within limit range
    if(bottomTargetRPM < ShooterConstants.RPMLowLimit)
      bottomTargetRPM = ShooterConstants.RPMLowLimit;

    if(bottomTargetRPM > ShooterConstants.RPMHighLimit)
      bottomTargetRPM = ShooterConstants.RPMHighLimit;

    if(m_tunable) {
      SmartDashboard.putNumber("shooter top target rpm", topTargetRPM);
      SmartDashboard.putNumber("shooter bottom target rpm", bottomTargetRPM);
    }

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
    if(topGains.kPUpdated())
      topMotor.config_kP(ShooterConstants.kPIDLoopIdx, topGains.getKP(), ShooterConstants.kTimeoutMs);

    if(topGains.kIUpdated())
      topMotor.config_kI(ShooterConstants.kPIDLoopIdx, topGains.getKI(), ShooterConstants.kTimeoutMs);

    if(topGains.kDUpdated())
      topMotor.config_kD(ShooterConstants.kPIDLoopIdx, topGains.getKD(), ShooterConstants.kTimeoutMs);

    if(topGains.kFUpdated())
      topMotor.config_kF(ShooterConstants.kPIDLoopIdx, topGains.getKF(), ShooterConstants.kTimeoutMs);
      
    if(bottomGains.kPUpdated())
      bottomMotor.config_kP(ShooterConstants.kPIDLoopIdx, bottomGains.getKP(), ShooterConstants.kTimeoutMs);

    if(bottomGains.kIUpdated())
      bottomMotor.config_kI(ShooterConstants.kPIDLoopIdx, bottomGains.getKI(), ShooterConstants.kTimeoutMs);

    if(bottomGains.kDUpdated())
      bottomMotor.config_kD(ShooterConstants.kPIDLoopIdx, bottomGains.getKD(), ShooterConstants.kTimeoutMs);

    if(bottomGains.kFUpdated())
      bottomMotor.config_kF(ShooterConstants.kPIDLoopIdx, bottomGains.getKF(), ShooterConstants.kTimeoutMs);
  }
 
  @Override
  public void periodic() {

    if(m_tunable) {
      // get motor velocity. if higher than max, set max to this new high
      double topVelocity = unitsPer100msToRPM(getTopVelocity());
      double bottomVelocity = unitsPer100msToRPM(getBottomVelocity());

      // put values into SmartDashboard
      SmartDashboard.putNumber("shooter top speed", topVelocity);
      SmartDashboard.putNumber("shooter bottom speed", bottomVelocity);
    } 
  }
}