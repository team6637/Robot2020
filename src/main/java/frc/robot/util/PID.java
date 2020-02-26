package frc.robot.util;

public class PID {

	Gains gains;
	
	double target = 0;
	double lastError;
	double totalError;
	
	public PID(Gains gains, double target) {
		this.gains = gains;
		lastError = 0;
		totalError = 0;
		
		setTarget(target);
	}
	
	public void setTarget(double target) {
		this.target = target;
	}
	public double getCorrection(double current){
		double error = target - current;
		totalError = totalError + error;
		double changeInError = error - lastError;
		lastError = error;
		
		double correction = gains.getKP() * error + gains.getKI() * totalError + gains.getKD() * changeInError;
    	if (correction > 1) correction = 1;
    	if (correction < -1) correction = -1;
		    	
    	return correction;
	}
}