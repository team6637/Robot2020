/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import frc.robot.Constants.ShooterConstants;

/**
 * Conversions
 */
public class Conversions {

    public double angleToDistance(double angle) {

        double distance, h1, h2, angleAdjusted;
        double targetHeight = ShooterConstants.targetHeight;
        double angleHeightMultiplier = ShooterConstants.angleHeightMultiplier;

        // change in Angle / change in height of camera
        // use the range of 30 - 41 degrees to find the height as the angle changes
        h1 = 30 - ((41 - angle) * angleHeightMultiplier);
        h2 = targetHeight - h1;
        angleAdjusted = angle - 11.0;
        distance = h2 / Math.tan(Math.toRadians(angleAdjusted));
        return distance;
    }


    // If you have two linear ranges, this will return a value from one of the ranges if you have a value from the other range

    // Example, if your bot needs to shoot a target RPM at a certain distance
    // and you know the distance the bot currently at
    // feed this the known RPM range, as value 1 range
    // feed this the known distance range, as value 2 range
    // feed this the current distance as currentValue2
    // returns target RPM

    public double getRangedValue1FromValue2(double rangeValue1Lowest, double rangeValue1Highest, double rangeValue2Lowest, double rangeValue2Highest, double currentValue2) {
        // Example: RPM range
        double rangeValue1Change = rangeValue1Highest - rangeValue1Lowest;

        // Example: Distance range
        double rangeValue2Change = rangeValue2Highest - rangeValue2Lowest;

        // Example: get multiplier for RPM range / Distance range
        double multiplier = rangeValue1Change / rangeValue2Change;

        // Example: gets target RPM from change in distance with multiplier plus lowest
        double targetValue1 = multiplier * (currentValue2 - rangeValue2Lowest) + rangeValue1Lowest;

        // Example: returns target RPM
        return targetValue1;
    }

}
