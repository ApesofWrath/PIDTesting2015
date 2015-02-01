package org.usfirst.frc.team668.robot;

public class PIDMap {
	
	public static final double TOSCILLATION_CONSTANT = 1.0; //1.0 is placeholder 
	public static final double CURRENT_CONSTANT = 1.0; //1.0 is placeholder
	
	public static final double MAXIMUM_TIME = 10;
	
	public static final double MAXIMUM_PID_ERROR = 100;
	
	public static final double MAXIMUM_INIT_CURRENT = 80;
	public static final double MAXIMUM_SUSTAIN_CURRENT = 20;
	public static final double CURRENT_CHECK_TIME = 5;
	
	public static final int PDP_OUTPUT_PORT = 14;
		
	public static final double PID_ERROR_THRESHOLD = 0.5;
	
	public static final int AMOUNT_OF_READINGS = 3; // More sophisticated adjustments are necessary
	
	
	
}
