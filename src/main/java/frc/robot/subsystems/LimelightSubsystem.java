
package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimelightSubsystem extends SubsystemBase {
	/**
	 * Creates a new LimelightSubsystem.
	 */

	private final NetworkTable table;
	private final boolean m_tunable;

	public LimelightSubsystem(boolean tunable) {
		m_tunable = tunable;

		table = NetworkTableInstance.getDefault().getTable("limelight");
		setPipeline(0);
		setupDriveMode();
	}

	public boolean isTarget() {
		return getValue("tv").getDouble(0.0) == 1;
	}

	/**
	 * Horizontal offset from crosshair to target (-27 degrees to 27 degrees).
	 * 
	 * @return tx as reported by the Limelight.
	 */
	public double getTx() {
		return getValue("tx").getDouble(0.0);
	}

	/**
	 * Vertical offset from crosshair to target (-20.5 degrees to 20.5 degrees).
	 * 
	 * @return ty as reported by the Limelight.
	 */
	public double getTy() {
		return getValue("ty").getDouble(0.0);
	}

	/**
	 * Area that the detected target takes up in total camera FOV (0% to 100%).
	 * 
	 * @return Area of target.
	 */
	public double getTa() {
		return getValue("ta").getDouble(0.0);
	}

	/**
	 * Gets target skew or rotation (-90 degrees to 0 degrees).
	 * 
	 * @return Target skew.
	 */
	public double getTs() {
		return getValue("ts").getDouble(0.0);
	}

	/**
	 * Sets LED mode of Limelight.
	 * 
	 * @param mode
	 * Light mode for Limelight.
	 */
	public void setLedMode(int ledMode) {
		getValue("ledMode").setNumber(ledMode);
  }
  
	/**
	 * Sets camera mode for Limelight.
	 * 
	 * @param mode
	 *            Camera mode for Limelight.
	 */
	public void setCameraMode(int cameraMode) {
		getValue("camMode").setNumber(cameraMode);
  }
  
  public void setCameraMode0() {
    setCameraMode(0);
  }

  public void setCameraMode1() {
    setCameraMode(1);
	}
	
	public void setupAutoAim() {
		setLedMode(3);
		setCameraMode(0);
	  }
	  
	public void setupDriveMode() {
		setLedMode(1);
		setCameraMode(1);
	}

	/**
	 * Sets pipeline number (0-9 value).
	 * 
	 * @param number
	 *            Pipeline number (0-9).
	 */
	public void setPipeline(int number) {
		getValue("pipeline").setNumber(number);
	}

  private NetworkTableEntry getValue(String key) {
		return table.getEntry(key);
	}

  @Override
  public void periodic() {
		if(m_tunable) {
			SmartDashboard.putNumber("limelight y", getTy());
			SmartDashboard.putNumber("limelight x", getTx());
		}
  }
}
