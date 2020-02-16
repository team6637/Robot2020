/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public final class Constants {

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
        public static final int leftEncoderPortA = 9;
        public static final int leftEncoderPortB = 8;
        public static final int rightEncoderPortA = 7;
        public static final int rightEncoderPortB = 6;

        public static final double wheelDiameter = 0.2032;
        public static final int pulsePerRevolution = 360;

        public static final boolean gyroReversed = false;

        public static final double turnToleranceDeg = 5.0;
        public static final double turnRateToleranceDegPerS = 10.0;
    }


    /**
     * 
     * Intake Constants
     * 
     */
    public static final class IntakeConstants {

        // PWM
        public static final int motorPort = 7;

        public static final double speed = 0.65;
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

        public static final int kTimeoutMs = 30;

        public static final double yRangeBottom = 0;
        public static final double yRangeTop = 9;
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

        public static final double speed = 0.5;
        public static final double backSpeed = -0.4;
        public static final double unitsPerRotation = 4096.0;
        public static final double tolerance = 200.0;
        public static final int kPIDLoopIdx = 0;
        public static final int kTimeoutMs = 30;
        public static final double targetHeight = 99.0;

        // this was change in angle / change in height of cam from ground
        public static final double angleHeightMultiplier = 0.294;

        public static final double closestRangeInches = 120.0;
        public static final double farthestRangeInches = 300.0;

        public static final double closestRangeTopRPM = 1000;
        public static final double farthestRangeTopRPM = 2000;
        
        public static final double closestRangeBottomRPM = 2900;
        public static final double farthestRangeBottomRPM = 4400;
    }


    /**
     * 
     * Extender Constants
     * 
     */   
    public static final class ExtenderConstants {

        // PWM
        public static final int motorPort = 8;

        public static final double speed = 0.7;
    }


    /**
     * 
     * Winch Constants
     * 
     */
    public static final class WinchConstants {

        // PWM
        public static final int motorPort = 9;

        public static final double speed = -0.9;
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

        //mathing
        public static final int unitsPerRotation = 4096;
        public final static double wheelDiameter = 2.0;
        public final static double targetSpins = 3.5;
        public final static double targetUnitsForTargetSpins = ((100 /(wheelDiameter * Math.PI)) * targetSpins) * unitsPerRotation;

    }
}
