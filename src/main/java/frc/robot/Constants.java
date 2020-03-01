/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Units;


public final class Constants {

    // 30AMP CURRENT LIMITS
    public static final int current30AmpPeakCurrentLimit = 25;
    public static final int current30AmpPeakCurrentDuration = 200;
    public static final int current30AmpContinuousCurrentLimit = 25;

    // 40AMP CURRENT LIMITS
    public static final int current40AmpPeakCurrentLimit = 35;
    public static final int current40AmpPeakCurrentDuration = 200;
    public static final int current40AmpContinuousCurrentLimit = 35;

    public static Color[] WHEEL_COLORS = new Color[4]; // Yellow, Red, Green, Blue
    public static String[][] WHEEL_POSITIONS = {
        {"c", "r", "r", "l"}, // Yellow; Order of strings goes to Target: Y, R, G, B
        {"l", "c", "r", "r"}, // Red
        {"r", "l", "c", "r"}, // Green
        {"r", "r", "l", "c"}}; // Blue

    /**
     * 
     * Drive Constants
     * 
     */
    public static final class DriveConstants {

        // CAN
        public static final int leftMasterID = 1;
        public static final int leftSlaveID = 2;
        public static final int rightMasterID = 3;
        public static final int rightSlaveID = 4;

        // DIO
        public static final int leftEncoderPortA = 5;
        public static final int leftEncoderPortB = 4;
        public static final int rightEncoderPortA = 7;
        public static final int rightEncoderPortB = 6;

        public static final double wheelDiameter = Units.inchesToMeters(8);
        public static final int pulsePerRevolution = 360;

        public static final boolean gyroReversed = false;

        public static final double turnToleranceDeg = 5.0;
        public static final double turnRateToleranceDegPerS = 10.0;

        public static final double moveKp = 0.0;
        public static final double moveKi = 0.0;
        public static final double moveKd = 0.0;

        public static final double turnKp = 0.085;
        public static final double turnKi = 0.003;
        public static final double turnKd = 0.5;
    }


    /**
     * 
     * Intake Constants
     * 
     */
    public static final class IntakeConstants {

        // PWM
        public static final int motorPort = 7;

        public static final double speed = 0.8;
    }

    
    /**
     * 
     * Shelbow Constants
     * 
     */
    public static final class ShelbowConstants {

        // CAN
        public static final int masterID = 9;
        public static final int slaveID = 8;

        public static final int ticksPerDegrees = 11;
        public static final int timeoutMs = 30;

        public static final double yRangeBottom = 0;
        public static final double yRangeTop = 7;
        public static final double yRangeMax = 9;

        public static final int onTargetThreshold = 50;




        // actual angle when arm is down: 15.65 degrees
        // actual angle when arm is up: 47.75 degrees
        public static final double absoluteEncoderAngleOffset = 56;  

        public static final int downPosition = 796;
        public static final int centerPosition = 971;
        public static final int upPosition = 1145;

        public static final int autonPositionFrontCenter = 1080;
        public static final int autonPositionTrenchClose = 1050;
        public static final int autonPositionTrenchFar = 1065;
        
    }


    /**
     * 
     * Indexer Constants
     * 
     */
    public static final class IndexerConstants {

        // PWM
        public static final int topMotorPort = 6;
        public static final int bottomMotorPort = 5;

        public static final double speed = 1.0;
        public static final double speedSlow = 0.4;
    }
    
    
    /**
     * 
     * Shooter Constants
     * 
     */
    public static final class ShooterConstants {

        // CAN
        public static final int topMotorID = 6;
        public static final int bottomMotorID = 7;

        public static final int autonIntakeShootTopRPM = 1800;
        public static final int autonIntakeShootBottomRPM = 3900;

        public static final double speed = 0.5;
        public static final double backSpeed = -0.55;
        public static final double unitsPerRotation = 4096.0;
        public static final double tolerance = 75.0;
        public static final int kPIDLoopIdx = 0;
        public static final int kTimeoutMs = 30;
        public static final double targetHeight = 99.0;

        // this was change in angle / change in height of cam from ground
        public static final double angleHeightMultiplier = 0.294;

        public static final double closestRangeInches = 120.0;
        public static final double farthestRangeInches = 240.0;

        public static final double closestRangeTopRPM = 1800;
        public static final double closestRangeBottomRPM = 3600;

        // 20 foot stats
        public static final double farthestRangeTopRPM = 3200;
        public static final double farthestRangeBottomRPM = 4800;

        // 25 foot stats with y offset 8
        public static final double farthestRangeTopRPM25 = 3000;
        public static final double farthestRangeBottomRPM25 = 4800;

        public static final double RPMLowLimit = 3500;
        public static final double RPMHighLimit = 4900;
    }


    /**
     * 
     * Extender Constants
     * 
     */   
    public static final class ExtenderConstants {

        // PWM
        public static final int motorPort = 8;

        public static final double speed = 0.65;
        public static final double downSpeed = -0.4;
    }


    /**
     * 
     * Winch Constants
     * 
     */
    public static final class WinchConstants {

        // PWM
        public static final int motorPort = 9;

        public static final double speed = 1.0;
    }


    /**
     * 
     * Spinner Constants
     * 
     */
    public static final class SpinnerConstants {

        // CAN
        public static final int motorID = 5;

        public static final double speed = 0.6;

        //I2C
        public final static I2C.Port i2cPort = I2C.Port.kOnboard;

        public final static Color blueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
        public final static Color greenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
        public final static Color redTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
        public final static Color yellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
        

        //mathing
        public static final int unitsPerRotation = 4096;
        public final static double wheelDiameter = 2.0;
        public final static double targetSpins = 3.5;
        public final static double targetUnitsForTargetSpins = ((100 /(wheelDiameter * Math.PI)) * targetSpins) * unitsPerRotation;

    }
}
