package org.usfirst.frc.team668.robot;																																					

import edu.wpi.first.wpilibj.Timer;

/*Listener for any PID code that will return a score out of 100% based 
 *on encoder readouts for PID error and motor current draw graph.
 * 
 * 
 */
public class PIDListener {
	
	private static double closenessPercentage; //PID error percentage of final score
	private static double toscillationPercentage; //time & oscillation percentage of final score
	private static double currentPercentage; //current percentage of final score
	
	private static Timer timer; // This times for both toscillation and current (calculus/integral unit stuff)
	private static double currentTime; // The current timer time since the last reset
	private static double last = 0, lastLast = 0;	// Last is the last hammer encoder reading, last last is the last to that last
													// It will allow us to find when we have three consecutive readings 
	
	static double toscillation = -1.0, current = -1.0, closeness = -1.0; // This will default to error
	
	public static boolean check() {
		if (Robot.time.get() >= 10) {
			stop("Time");
			return true;
		}
		if (Robot.hammerEncoder.get() >= 100) {
			stop("Encoder");
			return true;
		}
		if (Robot.pdp.getCurrent(14) >= 80) {
			stop("Current Init");
			return true;
		}
		if (Robot.pdp.getCurrent(14) >= 20 && Robot.time.get() >= 5) {
			stop("Current Continuous");
			return true;
		}
		return false;
	}
	
	public static void stop(String petyr) {
		Robot.hammerTalon.set(0.0);
		System.out.println("Error: " + petyr);
	}
	
	public static void startTime() {
		Robot.time.reset();
		Robot.time.start();
	}
	
	public static void listen() {
		timer.stop();
		currentTime = timer.get();
		timer.reset();
		current += Robot.pdp.getCurrent(14) * currentTime;
		toscillation += Robot.hammerEncoder.get() * currentTime;
		timer.start();
		if(closeness != 0) {
			if(Robot.hammerEncoder.get() > last + 0.5 && Robot.hammerEncoder.get() < last - 0.5) {
				if(Robot.hammerEncoder.get() > lastLast + 0.5 && Robot.hammerEncoder.get() < lastLast - 0.5) {
					closeness = (Robot.hammerEncoder.get() + last + lastLast) / 3.0;
					last = 0;
					lastLast = 0;
				}
				else {
					lastLast = last;
					last = Robot.hammerEncoder.get();
				}
			}
			last = Robot.hammerEncoder.get();
		}
	}
	
	public static double score(double toscillation, double current, double closeness) { 
		//toscillation percentage calculation
		if (toscillation > 0.0) {
			toscillationPercentage = 100.0 - (100.0 * (toscillation / 1.0)); // 1.0 is a placeholder
		}
		else {
			return -1.0;
		}
		//current percentage calculation
		if (current > 0.0) {
			currentPercentage = 100.0 - (100.0 * (current / 1.0)); //1.0 is a placeholder
		}
		else {
			return -1.0;
		}
		//closeness percentage calculation
		closeness = Math.abs(closeness);
		if (closeness <= 10.0) { //maximum PID error is 10.000
			closenessPercentage = 100.0 - (10.0 * closeness); //converts PID error to a 0-100 percentage
		}
		else {
			closenessPercentage = 0.0; //gives closeness a 0 if it exceeds maximum
		}
		
		return closenessPercentage + toscillationPercentage + currentPercentage;
	}
}
