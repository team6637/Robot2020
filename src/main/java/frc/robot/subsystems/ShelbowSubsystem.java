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
import frc.robot.Constants;
import frc.robot.Constants.ShelbowConstants;
import frc.robot.util.Gains;

public class ShelbowSubsystem extends SubsystemBase {
  
  private WPI_TalonSRX motorMaster = new WPI_TalonSRX(ShelbowConstants.masterID);
  private WPI_TalonSRX motorSlave = new WPI_TalonSRX(ShelbowConstants.slaveID);

  private final boolean m_tunable;

  private double yOffset = 0;
  private Gains gains;
  private boolean m_motionMagicIsRunning = false;
  private int targetPosition;
  private int lastExecutedPosition = 0;
  private int maxVelocity = 30;
  private int maxAcceleration = 50;

  private boolean newMotionMagic = false;
  private int motionMagicCounter = 0;

  public ShelbowSubsystem(boolean tunable) {

    m_tunable = tunable;

    // kP, kI, kD, kF
    gains = new Gains(3, 0.0006, 3.0, 0, true, "shelbow gains");

    motorMaster.configFactoryDefault();
    motorSlave.configFactoryDefault();

    motorMaster.setNeutralMode(NeutralMode.Brake);
    motorSlave.setNeutralMode(NeutralMode.Brake);

    motorMaster.setInverted(false);
    motorSlave.setInverted(true); 

    motorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    motorMaster.setSensorPhase(false);
    initQuadrature();

    motorMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 10, ShelbowConstants.timeoutMs);
    motorMaster.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, 10, ShelbowConstants.timeoutMs);

    // peak output
		motorMaster.configPeakOutputForward(+1.0, ShelbowConstants.timeoutMs);
    motorMaster.configPeakOutputReverse(-1.0, ShelbowConstants.timeoutMs);
    motorSlave.configPeakOutputForward(+1.0, ShelbowConstants.timeoutMs);
    motorSlave.configPeakOutputReverse(-1.0, ShelbowConstants.timeoutMs);

    // nominal output - minimum output needed to move the system
    motorMaster.configNominalOutputForward(0.06, ShelbowConstants.timeoutMs);
    motorMaster.configNominalOutputReverse(0.0, ShelbowConstants.timeoutMs);
    motorSlave.configNominalOutputForward(0.06, ShelbowConstants.timeoutMs);
    motorSlave.configNominalOutputReverse(0.0, ShelbowConstants.timeoutMs);
    		
    // config pidf
    motorMaster.selectProfileSlot(0, 0);
		motorMaster.config_kP(0, gains.getKP(), ShelbowConstants.timeoutMs);
		motorMaster.config_kI(0, gains.getKI(), ShelbowConstants.timeoutMs);
		motorMaster.config_kD(0, gains.getKD(), ShelbowConstants.timeoutMs);
		motorMaster.config_kF(0, gains.getKF(), ShelbowConstants.timeoutMs);

		// set speeds
		motorMaster.configMotionAcceleration(maxAcceleration, ShelbowConstants.timeoutMs);
		motorMaster.configMotionCruiseVelocity(maxVelocity, ShelbowConstants.timeoutMs);

    // Current Limiting
    motorMaster.configPeakCurrentLimit(Constants.current40AmpPeakCurrentLimit, ShelbowConstants.timeoutMs);
		motorMaster.configPeakCurrentDuration(Constants.current40AmpPeakCurrentDuration, ShelbowConstants.timeoutMs);
		motorMaster.configContinuousCurrentLimit(Constants.current40AmpContinuousCurrentLimit, ShelbowConstants.timeoutMs);
    motorMaster.enableCurrentLimit(true);

    // setup and run Motion Magic
    setTargetPosition(ShelbowConstants.upPosition);
    startMotionMagic();
  }



  /* 
   * MANUAL DRIVE
  */

  public void shelbowFlex(double move){  
    motorMaster.set(ControlMode.PercentOutput, move);
    motorSlave.follow(motorMaster);
    setYOffset(0);
  }

  public void stop() {
    motorMaster.set(ControlMode.PercentOutput, 0);
    motorSlave.follow(motorMaster);
  }



  /* 
   * POSITION
  */
  public int getPosition() {
    int pos = motorMaster.getSensorCollection().getQuadraturePosition();
    return pos;    
  }

  public void setTargetPosition(int target) {
    targetPosition = target;
    if (m_tunable) SmartDashboard.putNumber("shelbow target", targetPosition);
  }

  public void setPositionFromDegreeOffset(double degrees){
    int ticksToMove = getTicksFromDegrees(degrees);
    setTargetPosition(ticksToMove + getPosition());
  }

  public int getTicksFromDegrees(double degrees){
    return (int) (degrees * ShelbowConstants.ticksPerDegrees);
  }
  
  public void goToDownPosition() {
    setYOffset(0);
    setTargetPosition(ShelbowConstants.downPosition);
  }
  
  public void goToCenterPosition() {
    setYOffset(0);
    setTargetPosition(ShelbowConstants.centerPosition);
  }

  public void goToUpPosition() {
    setYOffset(0);
    setTargetPosition(ShelbowConstants.upPosition);
  }

  // used during winch action
  public void goToNearlyUpPosition() {
    setYOffset(0);
    setTargetPosition(ShelbowConstants.upPosition - 10);
  }


  /* 
   * ANGLE
  */
  public double getAngle() {
    return getPosition() / ShelbowConstants.ticksPerDegrees - ShelbowConstants.absoluteEncoderAngleOffset;
  }

  public double getAngleWithoutYOffset() {
    return getAngle() - yOffset;
  }

  public void setYOffset(double val) {
    yOffset = val;
  }

  public double getYOffset() {
    return yOffset;
  }



/*   
 * MOTION MAGIC
 */  

  // keep track of MM with these
  // in the periodic loop, it uses this to refresh MM if values have changed
  public void startMotionMagic() {
    m_motionMagicIsRunning = true;
  }

  public void stopMotionMagic() {
    m_motionMagicIsRunning = false;
  }
  
  // set MM based on targetPosition
  public void setMotionMagic() {
    // print a random number to visually show when MM restarts
    if(m_tunable)
      SmartDashboard.putNumber("Motion Control Starting", Math.random());

     if(!newMotionMagic) {
       newMotionMagic = true;
       motionMagicCounter = 0;
     }

    // check if targetPosition is in range
    if(targetPosition > ShelbowConstants.upPosition) {
      setTargetPosition(ShelbowConstants.upPosition);
    } else if(targetPosition < ShelbowConstants.downPosition) {
      setTargetPosition(ShelbowConstants.downPosition);
    }

    // Do It!!!
		motorMaster.set(ControlMode.MotionMagic, targetPosition);
    motorSlave.follow(motorMaster);

    // keep track so we know when targetPosition has changed (in periodic method)
    lastExecutedPosition = targetPosition;    
  }

  public boolean atSetpoint() {
    int currentPosition = getPosition();
    int positionError = Math.abs(targetPosition - currentPosition);
    return positionError < ShelbowConstants.onTargetThreshold;
  }


    
  /* 
  * Loop
  */
  @Override
  public void periodic() {

    boolean changed = false;

    // handle the pulse width sensor
    initQuadrature();

    // SAFETY - set target to current position if enough time passes
    // if motion magic just started, increment the counter
    // if(newMotionMagic) {
    //   motionMagicCounter++;
    // }

    // after the motion magic has run long enough, make target = current position
    // if(motionMagicCounter > 200 && newMotionMagic) {
    //    newMotionMagic = false;
       
    //    if(targetPosition != getPosition())
    //      setTargetPosition(getPosition());
    // }



    // if targetPosition has changed since MM was last called, call MM again
    if(lastExecutedPosition != targetPosition)
      changed = true;

    // tune from Smart Dashboard
    if(m_tunable) {
      SmartDashboard.putNumber("shelbow position", getPosition());
      SmartDashboard.putNumber("shelbow angle", getAngle());
      SmartDashboard.putNumber("shelbow yOffset", yOffset);

      SmartDashboard.putBoolean("new motion magic", newMotionMagic);
      SmartDashboard.putNumber("motion magic counter", motionMagicCounter);

      // if the following values change in Smart Dashboard, update them locally
      // if(gains.kPUpdated())
      //   motorMaster.config_kP(0, gains.getKP(), ShelbowConstants.timeoutMs);

      // if(gains.kIUpdated())
      //   motorMaster.config_kI(0, gains.getKI(), ShelbowConstants.timeoutMs);

      // if(gains.kDUpdated())
      //   motorMaster.config_kD(0, gains.getKD(), ShelbowConstants.timeoutMs);

      // if(gains.kFUpdated())
      //   motorMaster.config_kF(0, gains.getKF(), ShelbowConstants.timeoutMs);
      
      // int sdTargetPosition = (int) SmartDashboard.getNumber("shelbow target", targetPosition);
      // if(sdTargetPosition != targetPosition)
      //   setTargetPosition(sdTargetPosition);

    }
    
    // set motion magic
    if(changed && m_motionMagicIsRunning)
      setMotionMagic();
  }

  
  
  
  /* 
  * Set Quad encoder to the Absolute Pulse Width value 
  */
  final boolean kDiscontinuityPresent = false;
  public void initQuadrature() {
		// get the absolute pulse width position
    int pulseWidth = motorMaster.getSensorCollection().getPulseWidthPosition();
    
		// If there is a discontinuity in our measured range, subtract one half rotation to remove it
		if (kDiscontinuityPresent) {
      int newCenter;
			newCenter = (ShelbowConstants.upPosition + ShelbowConstants.downPosition) / 2;
			newCenter &= 0xFFF;
			pulseWidth -= newCenter;
		}

		// Mask out the bottom 12 bits to normalize to [0,4095] to stay within [0,360) degrees 
		pulseWidth = pulseWidth & 0xFFF;

		// Update Quadrature position
		motorMaster.getSensorCollection().setQuadraturePosition(pulseWidth, ShelbowConstants.timeoutMs);
	}
}