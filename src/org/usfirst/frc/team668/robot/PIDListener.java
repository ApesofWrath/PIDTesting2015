package org.usfirst.frc.team668.robot;

public class PIDListener {
	
	private static double closenessPercentage;
	
	public static double score(double toscillation, double current, double closeness) {
		if (toscillation < 0.0) {
			return -1.0;
		}
		if (closeness <= 10.0) {
			closenessPercentage = 100.0 - (10.0 * closeness);
		}
		else {
			closenessPercentage = 0.0;
		}
		return 0.0; //temporary value for compiler
	}
}
