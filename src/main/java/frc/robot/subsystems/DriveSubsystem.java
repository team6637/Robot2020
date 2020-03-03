package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU.CalibrationMode;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {

  private final WPI_TalonSRX leftMaster = new WPI_TalonSRX(DriveConstants.leftMasterID);
  private final WPI_TalonSRX leftSlave = new WPI_TalonSRX(DriveConstants.leftSlaveID);
  private final WPI_TalonSRX rightMaster = new WPI_TalonSRX(DriveConstants.rightMasterID);
  private final WPI_TalonSRX rightSlave = new WPI_TalonSRX(DriveConstants.rightSlaveID);

  private final DifferentialDrive drive = new DifferentialDrive(leftMaster, rightMaster);

  //private final DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(28));
  //private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(kinematics, );

  private final Encoder leftEncoder = new Encoder(DriveConstants.leftEncoderPortA, DriveConstants.leftEncoderPortB, false, Encoder.EncodingType.k4X);
  
  private final Encoder rightEncoder = new Encoder(DriveConstants.rightEncoderPortA, DriveConstants.rightEncoderPortB, true, Encoder.EncodingType.k4X);

  private final PigeonIMU gyro;
  private final boolean m_tunable;

  public DriveSubsystem(boolean tunable) {
    m_tunable = tunable;

    leftMaster.setNeutralMode(NeutralMode.Coast);
    leftSlave.setNeutralMode(NeutralMode.Coast);

    leftMaster.setInverted(false);
    leftSlave.setInverted(false);
    rightMaster.setInverted(true);
    rightSlave.setInverted(true);

    rightMaster.setNeutralMode(NeutralMode.Coast);
    rightSlave.setNeutralMode(NeutralMode.Coast);

    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster);

    drive.setRightSideInverted(false);

    leftEncoder.setReverseDirection(true);
    rightEncoder.setReverseDirection(false);

    leftEncoder.setDistancePerPulse(Math.PI * DriveConstants.wheelDiameter / DriveConstants.pulsePerRevolution);

    rightEncoder.setDistancePerPulse(Math.PI * DriveConstants.wheelDiameter / DriveConstants.pulsePerRevolution);

    gyro = new PigeonIMU(rightSlave);
    //calibrateGyro();
  }

  //public Rotation2d getHeading() {
   // return Rotation2d.fromDegrees(gyro.getAngle());
 // }

  public void arcadeDrive(double move, double turn){
    turn = turn * 0.7;
    drive.arcadeDrive(move, turn, true);
  }

  public void autonDrive(double move, double turn){
    drive.arcadeDrive(move, turn);
  }

  public void stop() {
    drive.arcadeDrive(0, 0);    
  }


  public double getLeftDistance(){
    return leftEncoder.getDistance();
  }

  public double getLeftRate(){
    return leftEncoder.getRate();	
  }    

  public double getRightDistance(){
    return rightEncoder.getDistance();
  }

  public double getRightRate(){
    return rightEncoder.getRate();
  }

  public double getAverageDistance(){
    return (getLeftDistance() + getRightDistance()) / 2;
  }

  public void resetEncoders(){
    leftEncoder.reset();
    rightEncoder.reset();
  }
  
  public double getAngle() {
    double [] ypr = new double[3];
    gyro.getYawPitchRoll(ypr);
    return ypr[0];
  }

  public void resetAngle() {
    gyro.setYaw(0);
    gyro.setFusedHeading(0);
  }

  public void calibrateGyro(){
    gyro.enterCalibrationMode(CalibrationMode.Temperature);
  }

  //Returns the heading of the robot in degrees from 180 to 180
  public double getHeading() {
    return Math.IEEEremainder(getAngle(), 360) * (DriveConstants.gyroReversed ? -1.0 : 1.0);
  }

  @Override
  public void periodic() {
    if(m_tunable) {
      SmartDashboard.putNumber("gyro angle", getAngle());
      SmartDashboard.putNumber("gyro heading", getHeading());

      SmartDashboard.putNumber("left encoder", getLeftDistance());
      SmartDashboard.putNumber("right encoder", getRightDistance());      
    }
  }
}
