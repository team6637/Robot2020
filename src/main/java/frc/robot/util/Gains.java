/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class Gains {
    
    public double m_kP, m_kI, m_kD, m_kF;
	public int m_kIZone;
	public double m_kPeakOutput;
	public String m_name;
	
	public Gains(double kP, double kI, double kD, boolean tunable, String name){
		m_kP = kP;
		m_kI = kI;
		m_kD = kD;
		m_name = name;

		if(tunable) {
	        SmartDashboard.putNumber(m_name + " kp", m_kP);
    	    SmartDashboard.putNumber(m_name + " ki", m_kI);
        	SmartDashboard.putNumber(m_name + " kd", m_kD);
		}
	}

	public Gains(double kP, double kI, double kD, double kF, boolean tunable, String name){
		m_kP = kP;
		m_kI = kI;
		m_kD = kD;
		m_kF = kF;
		m_name = name;

		if(tunable) {
	        SmartDashboard.putNumber(m_name + " kp", m_kP);
    	    SmartDashboard.putNumber(m_name + " ki", m_kI);
        	SmartDashboard.putNumber(m_name + " kd", m_kD);
			SmartDashboard.putNumber(m_name + " kf", m_kF);
		}
	}
	
	public Gains(double kP, double kI, double kD, double kF, int kIZone, double kPeakOutput){
		m_kP = kP;
		m_kI = kI;
		m_kD = kD;
		m_kF = kF;
		m_kIZone = kIZone;
		m_kPeakOutput = kPeakOutput;
	}

	public double getKP() {
		return m_kP;
	}

	public double getKI() {
		return m_kI;
	}

	public double getKD() {
		return m_kD;
	}

	public double getKF() {
		return m_kF;
	}
	
	public boolean kPUpdated() {
		double sd_gain = SmartDashboard.getNumber(m_name + " kp", m_kP);
		if(m_kP != sd_gain) {
			m_kP = sd_gain;
			return true;
		}
		return false;
	}

	public boolean kIUpdated() {
		double sd_gain = SmartDashboard.getNumber(m_name + " ki", m_kI);
		if(m_kI != sd_gain) {
			m_kI = sd_gain;
			return true;
		}
		return false;
	}

	public boolean kDUpdated() {
		double sd_gain = SmartDashboard.getNumber(m_name + " kd", m_kD);
		if(m_kD != sd_gain) {
			m_kD = sd_gain;
			return true;
		}
		return false;
	}

	public boolean kFUpdated() {
		double sd_gain = SmartDashboard.getNumber(m_name + " kF", m_kF);
		if(m_kF != sd_gain) {
			m_kF = sd_gain;
			return true;
		}
		return false;
	}

}