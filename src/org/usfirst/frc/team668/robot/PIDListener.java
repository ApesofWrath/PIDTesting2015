package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 *Listener for any PID code that will return a score out of 100% based 
 *on encoder readouts for PID error and motor current draw graph.
 */
public class PIDListener {
	
	private static double closenessPercentage; // PID error percentage of final score
	private static double toscillationPercentage; // time & oscillation percentage of final score
	private static double currentPercentage; // current percentage of final score
	private static double finalScore; // final PID rating out of 100
	
	private static Timer timer; // This times for both toscillation and current (calculus/integral unit stuff)
	private static double currentTime; // The current timer time since the last reset
	private static double lastOscillation = 0, secondToLastOscillation = 0;	// It will allow us to find when we have three consecutive readings
			
	static double toscillation = -1.0, current = -1.0, closeness = -1.0; // This will default to error
			
	public static boolean check() {
		if (Robot.time.get() >= PIDMap.MAXIMUM_TIME) {
			stop("Time");
			Robot.disable();
			return true;
		}
		if (Robot.hammerEncoder.get() >= PIDMap.MAXIMUM_PID_ERROR) {
			stop("Encoder");
			Robot.disable();
			return true;
		}
		if (Robot.pdp.getCurrent(PIDMap.PDP_OUTPUT_PORT) >= PIDMap.MAXIMUM_INIT_CURRENT) {
			stop("Current Init");
			Robot.disable();
			return true;
		}
		if (Robot.pdp.getCurrent(PIDMap.PDP_OUTPUT_PORT) >= PIDMap.MAXIMUM_SUSTAIN_CURRENT && Robot.time
				.get() >= PIDMap.CURRENT_CHECK_TIME) {
			stop("Current Continuous");
			Robot.disable();
			return true;
		}
		return false;
	}
	
	public static void stop(String error) {
		Robot.hammerTalon.set(0.0);
		System.out.println("Error: " + error);
		Robot.disable();
	}
	
	public static void startTime() {
		Robot.time.reset();
		Robot.time.start();
	}
	
	public static void listen() {
		if (!check()) {
			timer.stop();
			currentTime = timer.get();
			timer.reset();
			current += Robot.pdp.getCurrent(PIDMap.PDP_OUTPUT_PORT) * currentTime;
			toscillation += Robot.hammerEncoder.get() * currentTime;
			timer.start();
			if (closeness != 0 && Robot.hammerEncoder.get() != 0) {
				if (Robot.hammerEncoder.get() > lastOscillation + PIDMap.PID_ERROR_THRESHOLD && Robot.hammerEncoder
						.get() < lastOscillation - PIDMap.PID_ERROR_THRESHOLD) {
					if (Robot.hammerEncoder.get() > secondToLastOscillation + PIDMap.PID_ERROR_THRESHOLD && Robot.hammerEncoder
							.get() < secondToLastOscillation - PIDMap.PID_ERROR_THRESHOLD) {
						closeness = (Robot.hammerEncoder.get() + lastOscillation + secondToLastOscillation) / PIDMap.AMOUNT_OF_READINGS;
						lastOscillation = 0;
						secondToLastOscillation = 0;
					}
					else {
						secondToLastOscillation = 0;
					}
				}
				else {
					secondToLastOscillation = 0;
				}
				lastOscillation = Robot.hammerEncoder.get();
			}
		}
		else {
			Robot.disable(); // Locks the robot
		}
	}
	
	public static double score(double toscillation, double current, double closeness) {
		// toscillation percentage calculation
		if (toscillation > 0.0) {
			toscillationPercentage = 100.0 - (100.0 * (toscillation / PIDMap.TOSCILLATION_CONSTANT));
		}
		
		// current percentage calculation
		if (current > 0.0) {
			currentPercentage = 100.0 - (100.0 * (current / PIDMap.CURRENT_CONSTANT));
		}
		
		// closeness percentage calculation
		closeness = Math.abs(closeness);
		if (closeness <= 10.0) { // maximum PID error is 10.000
			closenessPercentage = 100.0 - (10.0 * closeness); // converts PID error to a 0-100 percentage
		}
		
		finalScore = (closenessPercentage + toscillationPercentage + currentPercentage) / 3;
		SmartDashboard.putNumber("Score: ", finalScore);
		return finalScore;
	}
}
