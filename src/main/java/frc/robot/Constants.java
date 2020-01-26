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
        public static final double pulsePerRevolution = 360;
    }


    /**
     * 
     * Intake Constants
     * 
     */
    public static final class IntakeConstants {

        // PWM
        public static final int motorPort = 7;

        public static final double speed = 0.4;
    }

    
    /**
     * 
     * Shelbow Constants
     * 
     */
    public static final class ShelbowConstants {

        // CAN
        public static final int masterID = 8;
        public static final int slaveID = 9;

        public static final double speed = 0.5;
    }


    /**
     * 
     * Indexer Constants
     * 
     */
    public static final class IndexerConstants {

        // PWM
        public static final int topMotorPort = 5;
        public static final int bottomMotorPort = 6;

        // TODO: we should never run motors with this low of speed
        // please make sure fab team adds some gear reduction to indexer so we can bring this value up!
        public static final double speed = 0.2;
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

        // TODO: switch wires on shooter motors and take away the negative here. 
        // we need these motors driving forward. Which means the talons have to be green while shooting
        // (ask mr j to help)
        public static final double speed = -0.8;
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

        public static final double speed = 0.6;
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
        public static final int encoderCPR = 1024;
        public final static double wheelDiameter = 2.0;
        public final static double targetSpins = 3.5;
        public final static double spinCPCounts = ((100 /(wheelDiameter * Math.PI)) * targetSpins) * encoderCPR;

    }
}
