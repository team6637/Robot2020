package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {

  private final WPI_TalonSRX leftMaster = new WPI_TalonSRX(DriveConstants.leftMasterID);
  private final WPI_TalonSRX leftSlave = new WPI_TalonSRX(DriveConstants.leftSlaveID);
  private final WPI_TalonSRX rightMaster = new WPI_TalonSRX(DriveConstants.rightMasterID);
  private final WPI_TalonSRX rightSlave = new WPI_TalonSRX(DriveConstants.rightSlaveID);

  private final DifferentialDrive drive = new DifferentialDrive(leftMaster, rightMaster);

    // initiate encoders
		Encoder leftEncoder = new Encoder(DriveConstants.leftEncoderPortA, DriveConstants.leftEncoderPortB, false, Encoder.EncodingType.k4X);
    Encoder rightEncoder = new Encoder(DriveConstants.rightEncoderPortA, DriveConstants.leftEncoderPortB, true, Encoder.EncodingType.k4X);
    
    // TODO: setup Pigeon Gyro
    // documentation starting page 30: https://www.ctr-electronics.com/downloads/pdf/Pigeon%20IMU%20User's%20Guide.pdf
    	

  public DriveSubsystem() {

    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster);

    //DIO ports
    leftEncoder.setDistancePerPulse(Math.PI * DriveConstants.wheelDiameter / DriveConstants.pulsePerRevolution); 
    rightEncoder.setDistancePerPulse(Math.PI * DriveConstants.wheelDiameter / DriveConstants.pulsePerRevolution); 

  }

  public void arcadeDrive(double move, double turn){
    drive.arcadeDrive(move, turn);
  }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
   
    public double getLeftDistance(){
		return -leftEncoder.getDistance();
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

    // TODO: setup getAngle and resetGyro methods for gyro
    // documentation starting page 30: https://www.ctr-electronics.com/downloads/pdf/Pigeon%20IMU%20User's%20Guide.pdf

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
