/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorSensorV3.RawColor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SpinnerConstants;


public class SpinnerSubsystem extends SubsystemBase {

  public WPI_TalonSRX spinMotor = new WPI_TalonSRX(SpinnerConstants.motorID);

  private final ColorSensorV3 colorSensor = new ColorSensorV3(SpinnerConstants.i2cPort);

  private ColorMatch colorMatcher = new ColorMatch();
  private Color colorUpdated;

  public SpinnerSubsystem() {
    spinMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
    spinMotor.setSensorPhase(false);
    spinMotor.setInverted(false);
    spinMotor.setNeutralMode(NeutralMode.Coast);

    colorMatcher.addColorMatch(SpinnerConstants.blueTarget);
    colorMatcher.addColorMatch(SpinnerConstants.greenTarget);
    colorMatcher.addColorMatch(SpinnerConstants.redTarget);
    colorMatcher.addColorMatch(SpinnerConstants.yellowTarget);
  }

  public void spin() {
    spinMotor.set(SpinnerConstants.speed);
  }

  public void stop() {
    spinMotor.set(0);
  }

  public int getPosition() {
    return spinMotor.getSelectedSensorPosition();
  }

  public void resetPosition() {
    spinMotor.setSelectedSensorPosition(2000, 0, 10);
  }

  public void configureColorSensor() {
    //colorSensor.configureColorSensor(res, rate, gain);
  }

  public RawColor getColor() {
    return null;
    //return colorSensor.getRawColor();
  }

  public int getRed() {
    return colorSensor.getRed();
  }

  public int getGreen() {
    return colorSensor.getGreen();
  }

  public int getBlue() {
    return colorSensor.getBlue();
  }

  //where is yellow??

  public int getIR() {
    return colorSensor.getIR();
  }

  public boolean hasReset() {
    return colorSensor.hasReset();
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    Color detectedColor = colorSensor.getColor();

    String colorString;
    ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);

    if (match.color == SpinnerConstants.blueTarget) {
      colorString = "Blue";
    } else if (match.color == SpinnerConstants.redTarget) {
      colorString = "Red";
    } else if (match.color == SpinnerConstants.greenTarget) {
      colorString = "Green";
    } else if (match.color == SpinnerConstants.yellowTarget) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }

    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("Confidence", match.confidence);
    SmartDashboard.putString("Detected Color", colorString);
  }  
}