/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class DriveConstants {
        public static final int leftMasterID = 1;
        public static final int leftSlaveID = 2;
        public static final int rightMasterID = 3;
        public static final int rightSlaveID = 4;

        public static final double wheelDiameter = 0.2032;
        public static final double pulsePerRevolution = 360;

        public static final int leftEncoderPortA = 0;
        public static final int leftEncoderPortB = 1;
        public static final int rightEncoderPortA = 2;
        public static final int rightEncoderPortB = 3;
            
    }

    public static final class SpinnerConstants {
        public static final int motorID = 5;
        public static final double speed = 0.8;
    }

    public static final class ShooterConstants {
        public static final int topMotorID = 6;
        public static final int bottomMotorID = 7;
        public static final double speed = 0.8;
    }

    public static final class IntakeConstants {
        public static final int motorPort = 0;
        public static final double speed = 0.6;
    }

    public static final class WinchConstants {
        public static final int motorPort = 1;
        public static final double speed = 0.6;
    }

    public static final class ExtenderConstants {
        public static final int motorPort = 2;
        public static final double speed = 0.7;
    }

    public static final class IndexerConstants {
        public static final int topMotorPort = 3;
        public static final int bottomMotorPort = 4;
        public static final double speed = 0.4;
    }

    public static final class ShelbowConstants {
        public static final int masterID = 8;
        public static final int slaveID = 9;
        public static final double speed = 0.5;
    }
}