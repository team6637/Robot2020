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
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShelbowConstants;
import frc.robot.util.Gains;

public class ShelbowSubsystem extends SubsystemBase {

  private int angleOffset = 62;
  private int ticksPerDegree = 11;

  private final boolean m_tunable;
  
  WPI_TalonSRX motorMaster = new WPI_TalonSRX(ShelbowConstants.masterID);
  WPI_TalonSRX motorSlave = new WPI_TalonSRX(ShelbowConstants.slaveID);

  Gains gains;

  // setup predefined setpoints
  private final int downPosition = 852;
  public int centerPosition = 1056;
  public int upPosition = 1235;

  private boolean m_motionMagicIsRunning = false;

  // starting position
  private int targetPosition = upPosition;
  private int lastExecutedPosition;

  private final int onTargetThreshold = 50;
  private int maxVelocity = 30;
  private int maxAcceleration = 50;

  public void goToUpPosition() {
    setTargetPosition(upPosition);
  }

  public void goToDownPosition() {
    setTargetPosition(downPosition);
  }

  public void goToCenterPosition() {
    setTargetPosition(centerPosition);
  }

  public ShelbowSubsystem(final boolean tunable) {

    m_tunable = tunable;

    // kP, kI, kD, kF
    gains = new Gains(2.3, 0.002, 0, 0, m_tunable, "shelbow gains");

    motorMaster.configFactoryDefault();
    motorSlave.configFactoryDefault();

    motorMaster.setNeutralMode(NeutralMode.Brake);
    motorSlave.setNeutralMode(NeutralMode.Brake);

    motorMaster.setInverted(false);
    motorSlave.setInverted(true); 

    motorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    motorMaster.setSensorPhase(false);
    initQuadrature();

    motorSlave.follow(motorMaster);

    motorMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 10, ShelbowConstants.kTimeoutMs);
    motorMaster.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, 10, ShelbowConstants.kTimeoutMs);

    // peak output
		motorMaster.configPeakOutputForward(+1.0, ShelbowConstants.kTimeoutMs);
    motorMaster.configPeakOutputReverse(-1.0, ShelbowConstants.kTimeoutMs);
    motorSlave.configPeakOutputForward(+1.0, ShelbowConstants.kTimeoutMs);
    motorSlave.configPeakOutputReverse(-1.0, ShelbowConstants.kTimeoutMs);

    // nominal output - minimum output needed to move the system
    motorMaster.configNominalOutputForward(0.06, ShelbowConstants.kTimeoutMs);
    motorMaster.configNominalOutputReverse(0.0, ShelbowConstants.kTimeoutMs);
    motorSlave.configNominalOutputForward(0.06, ShelbowConstants.kTimeoutMs);
    motorSlave.configNominalOutputReverse(0.0, ShelbowConstants.kTimeoutMs);
    		
    // config pidf
    motorMaster.selectProfileSlot(0, 0);
		motorMaster.config_kP(0, gains.getKP(), ShelbowConstants.kTimeoutMs);
		motorMaster.config_kI(0, gains.getKI(), ShelbowConstants.kTimeoutMs);
		motorMaster.config_kD(0, gains.getKD(), ShelbowConstants.kTimeoutMs);
		motorMaster.config_kF(0, gains.getKF(), ShelbowConstants.kTimeoutMs);

		// set speeds
		motorMaster.configMotionAcceleration(maxAcceleration, ShelbowConstants.kTimeoutMs);
		motorMaster.configMotionCruiseVelocity(maxVelocity, ShelbowConstants.kTimeoutMs);

    // Current Limiting
    // motorMaster.configPeakCurrentLimit(ShelbowConstants.current30AmpPeakCurrentLimit, ShelbowConstants.kTimeoutMs);
		// motorMaster.configPeakCurrentDuration(ShelbowConstants.current30AmpPeakCurrentDuration, ShelbowConstants.kTimeoutMs);
		// motorMaster.configContinuousCurrentLimit(ShelbowConstants.current30AmpContinuousCurrentLimit, ShelbowConstants.kTimeoutMs);
		// motorMaster.enableCurrentLimit(true);
    
    // set target to starting position
    setTargetPosition(upPosition);

    if(m_tunable) {
      SmartDashboard.putNumber("shelbow target", targetPosition);
      SmartDashboard.putNumber("shelbow closed loop error", this.motorMaster.getClosedLoopError());
      SmartDashboard.putNumber("shelbow velocity", maxVelocity);
      SmartDashboard.putNumber("shelbow acceleration", maxAcceleration);
    }

    // run Motion Magic for the first time
    startMotionMagic();
  }

  // MANUAL DRIVE
  public void shelbowFlex(final double move){  
    motorMaster.set(ControlMode.PercentOutput, move);
    motorSlave.follow(motorMaster);
    setTargetPosition(getPosition());
  }

  public void stop() {
    motorMaster.set(ControlMode.PercentOutput, 0);
    motorSlave.follow(motorMaster);
  }  

   // ENCODER
  public int getPosition() {
    final int pos = motorMaster.getSensorCollection().getQuadraturePosition();
    return pos;    
  }

  // SET TARGET POSITION
  public void setTargetPosition(int target) {
    if(target > upPosition) {
      target = upPosition;
    } else if(target < downPosition) {
      target = downPosition;
    }
    targetPosition = target;
    if (m_tunable) SmartDashboard.putNumber("shelbow target", targetPosition);
  }

  // MOTION MAGIC IS CLOSE ENOUGH
  public boolean atSetpoint() {
    final int currentPosition = getPosition();
    final int positionError = Math.abs(targetPosition - currentPosition);
    return positionError < onTargetThreshold;
  }
  
  // GET ANGLE
  public double getAngle() {
    final double angle = getPosition() / ticksPerDegree - angleOffset;
    //final double angle = 0.0;
    return angle;
  }

  public void setPositionFromDegrees(double degrees){
    int ticksToMove = getTicksFromDegrees(degrees);
    setTargetPosition(ticksToMove + getPosition());
  }

  public int getTicksFromDegrees(double degrees){
    return (int) (degrees * ticksPerDegree);
  }

  public void startMotionMagic() {
    m_motionMagicIsRunning = true;
  }

  public void stopMotionMagic() {
    m_motionMagicIsRunning = false;
  }

  public void setPositionFromAngleOffset() {
    getAngle();
  }
  
  // SET MOTION MAGIC
  public void setMotionMagic() {
    
    // print a random number to visually show when MM restarts
    if(m_tunable)
      SmartDashboard.putNumber("Motion Control Starting", Math.random());

    // Do It!!!
		motorMaster.set(ControlMode.MotionMagic, targetPosition);
    motorSlave.follow(motorMaster);

    // keep track so we know when targetPosition has changed (in periodic method)
    lastExecutedPosition = targetPosition;

    
  }
  
  @Override
  public void periodic() {
    boolean changed = false;

    // handle the pulse width sensor
    initQuadrature();

    // if targetPosition has changed since MM was last called, call MM again
    if(lastExecutedPosition != targetPosition)
      changed = true;

    // tune from Smart Dashboard
    if(m_tunable) {
      SmartDashboard.putNumber("shelbow position", getPosition());
      SmartDashboard.putNumber("shelbow angle", getAngle());

      // if the following values change in Smart Dashboard, update them locally
      // motion magic will then use the values the next time targetPosition changes
      final int sdVel = (int) SmartDashboard.getNumber("shelbow velocity", maxVelocity);
      if(sdVel != maxVelocity) {
        maxVelocity = sdVel;
        motorMaster.configMotionCruiseVelocity(maxVelocity, ShelbowConstants.kTimeoutMs);
      }

      final int sdAccel = (int) SmartDashboard.getNumber("shelbow acceleration", maxAcceleration);
      if(sdAccel != maxAcceleration) {
        this.maxAcceleration = sdAccel;
        motorMaster.configMotionAcceleration(maxAcceleration, ShelbowConstants.kTimeoutMs);
      }

      if(gains.kPUpdated())
        motorMaster.config_kP(0, gains.getKP(), ShelbowConstants.kTimeoutMs);

      if(gains.kIUpdated())
        motorMaster.config_kI(0, gains.getKI(), ShelbowConstants.kTimeoutMs);

      if(gains.kDUpdated())
        motorMaster.config_kD(0, gains.getKD(), ShelbowConstants.kTimeoutMs);

      if(gains.kFUpdated())
        motorMaster.config_kF(0, gains.getKF(), ShelbowConstants.kTimeoutMs);
      
      final int sdTargetPosition = (int) SmartDashboard.getNumber("shelbow target", targetPosition);
      if(sdTargetPosition != targetPosition)
        setTargetPosition(sdTargetPosition);

    }
    
    // set motion magic
    if(changed && m_motionMagicIsRunning)
      setMotionMagic();

  }

  
  
  
  
  

  final boolean kDiscontinuityPresent = false;
  public void initQuadrature() {
		// get the absolute pulse width position
    int pulseWidth = motorMaster.getSensorCollection().getPulseWidthPosition();
    
		// If there is a discontinuity in our measured range, subtract one half rotation to remove it
		if (kDiscontinuityPresent) {

		// Calculate the center
			//int newCenter;
			//newCenter = (upPosition + downPosition) / 2;
			//newCenter &= 0xFFF;

		// Apply the offset so the discontinuity is in the unused portion of the sensor
			//pulseWidth -= newCenter;
		}

		// Mask out the bottom 12 bits to normalize to [0,4095], or in other words, to stay within [0,360) degrees 
		pulseWidth = pulseWidth & 0xFFF;

		// Update Quadrature position
		motorMaster.getSensorCollection().setQuadraturePosition(pulseWidth, ShelbowConstants.kTimeoutMs);
	}
}