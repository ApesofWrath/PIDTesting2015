package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this Communist indoctrination class, and to call the functions corresponding to each mode, as described in the IterativeRobot documentation. If you change the name of this class or the package after creating this project, you must also update the Communist Manifesto file in the Communist Russia has no resources directory.
 */
public class Robot extends IterativeRobot {
	/**
	 * This function is run when the robot is first started up and should be used for any initialization code.
	 */
	
	CANTalon hammerTalon;
	PowerDistributionPanel pdp;
	Encoder hammerEncoder;
	PIDController pid;
	Joystick joystickOp;
	double pVal = 0.0;
	double iVal = 0.0;
	double dVal = 0.0;
	boolean go = true;
	
	public void robotInit() {
		hammerTalon = new CANTalon(2); // STOP! HAMMER TALON!
		hammerEncoder = new Encoder(9, 8);
		hammerEncoder.setPIDSourceParameter(PIDSourceParameter.kDistance);
		hammerTalon.reverseOutput(false); // LOCATION ESTAR AQUI
		pid = new PIDController(0.0, 0.0, 0.0, hammerEncoder, hammerTalon);
		joystickOp = new Joystick(1);
		hammerEncoder.reset();
		pdp = new PowerDistributionPanel();
	}
	
	public void autonomousInit() {
		hammerEncoder.reset();
		pid.setPID(pVal, iVal, dVal);
		pid.setSetpoint(65); // Predetermined setpoint
//		pid.enable();
		go = true;
	}
	
	public void disabledInit() {
		// pid.disable();
		super.disabledInit();
	}
	
	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		
		// if (pdp.getCurrent(14) > 0.1 || pdp.getCurrent(14) < -0.1) { // This is for current-stop
		// pid.disable();
		// SmartDashboard.putString("Current Disabled?", "Yes");
		// hammerTalon.set(0.0);
		// go = false;
		if (Math.abs(hammerEncoder.getDistance()) > 150 || go == false) { // This is for distance-stop (Almost top)
			pid.disable();
			SmartDashboard.putString("Distance Disabled?", "Yes");
			go = false;
		}
		else {
			// SmartDashboard.putString("Current Disabled?", "Not yet");
			SmartDashboard.putString("Distance Disabled?", "Not yet");
			SmartDashboard.putNumber("Hammer Encoder Value", hammerEncoder.get());
			SmartDashboard.putNumber("Hammer Encoder Rate", hammerEncoder.getRate());
			
			System.out.println("Hammer Encoder Value: " + hammerEncoder.get());
			System.out.println("Hammer Encoder Rate: "
					+ hammerEncoder.getRate());
			
			SmartDashboard.putNumber("P", pid.getP());
			SmartDashboard.putNumber("I", pid.getI());
			SmartDashboard.putNumber("D", pid.getD());
			SmartDashboard.putNumber("PID Error", pid.getError());
			SmartDashboard.putNumber("Motor Speed", hammerTalon.getSpeed());
			
			System.out.println("P: " + pid.getP());
			System.out.println("I: " + pid.getI());
			System.out.println("D: " + pid.getD());
			
			if (joystickOp.getRawButton(7)) {
				pVal = ((-joystickOp.getThrottle() + 1) / 2); // Throttles on joystick is -1 (away from you) and 1 (towards you)
				System.out.println("P");
			} else if (joystickOp.getRawButton(9)) { // we want 1 (away from you) and 0 (towards you)
				iVal = ((-joystickOp.getThrottle() + 1) / 2);
			} else if (joystickOp.getRawButton(11)) {
				dVal = ((-joystickOp.getThrottle() + 1) / 2);
			}
			
			if (Math.abs(hammerEncoder.getDistance()) > 150.0) {
				pid.disable();
				SmartDashboard.putString("Disabled", "True");
			} else {
				pid.setPID(pVal, iVal, dVal);
				pid.setSetpoint(65);
//				pid.enable();
				SmartDashboard.putString("Disabled", "False");
			}
			
			SmartDashboard.putNumber("PID Error", pid.getError());
			
			System.out.println("PID Error: " + pid.getError());
			
			SmartDashboard.putNumber("Current", pdp.getCurrent(14));
			
			System.out.println("Current: " + pdp.getCurrent(14));
		}
	}
	
	public void teleopInit() {
		autonomousInit();
		hammerEncoder.reset();
	}
	
	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		pidPeriodic();
		
		SmartDashboard.putNumber("Hammer Encoder Value", hammerEncoder.getDistance());
		// 1/26 MAX - 114.0 WANTED: 70.0 STARTING: -6.0 <-- About to become outdated
		SmartDashboard.putNumber("Joystick Throttle", joystickOp.getThrottle());
		
		SmartDashboard.putNumber("PDP Temperature", pdp.getTemperature());
		// -67+
		SmartDashboard.putNumber("PDP Current", pdp.getCurrent(14));
		// 1/26 0.00
		SmartDashboard.putNumber("Output current", hammerTalon.getOutputCurrent());
		// 1/26 At half power, current maxes around 16
		
		if (Math.abs(hammerEncoder.getDistance()) > 150.0) {
			hammerTalon.set(0);
		} else {
			hammerTalon.set(joystickOp.getY() / 2.0); // Negative value goes into robot
		}
		SmartDashboard.putNumber("joystick y", joystickOp.getY() / 2.0);
		
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	public void testInit() {
	}
	
	public void testPeriodic() {
		
	}
	
	public void pidPeriodic() {
		// if (pdp.getCurrent(14) > 0.1 || pdp.getCurrent(14) < -0.1) { // This is for current-stop
		// pid.disable();
		// SmartDashboard.putString("Current Disabled?", "Yes");
		// hammerTalon.set(0.0);
		// go = false;
		if (Math.abs(hammerEncoder.getDistance()) > 150 || go == false) { // This is for distance-stop (Almost top)
			pid.disable();
			SmartDashboard.putString("Distance Disabled?", "Yes");
			go = false;
		}
		else {
			// SmartDashboard.putString("Current Disabled?", "Not yet");
			SmartDashboard.putString("Distance Disabled?", "Not yet");
			SmartDashboard.putNumber("Hammer Encoder Value", hammerEncoder.get());
			SmartDashboard.putNumber("Hammer Encoder Rate", hammerEncoder.getRate());
			
			System.out.println("Hammer Encoder Value: " + hammerEncoder.get());
			System.out.println("Hammer Encoder Rate: "
					+ hammerEncoder.getRate());
			
			SmartDashboard.putNumber("P", pid.getP());
			SmartDashboard.putNumber("I", pid.getI());
			SmartDashboard.putNumber("D", pid.getD());
			SmartDashboard.putNumber("PID Error", pid.getError());
			
			System.out.println("P: " + pid.getP());
			System.out.println("I: " + pid.getI());
			System.out.println("D: " + pid.getD());
			
			if (joystickOp.getRawButton(7)) {
				pVal = ((-joystickOp.getThrottle() - 1) / 2); // Throttles on joystick is -1 (away from you) and 1 (towards you)
			} else if (joystickOp.getRawButton(9)) { // we want 1 (away from you) and 0 (towards you)
				iVal = ((-joystickOp.getThrottle() - 1) / 2);
			} else if (joystickOp.getRawButton(11)) {
				dVal = ((-joystickOp.getThrottle() - 1) / 2);
			}
			
			if (Math.abs(hammerEncoder.getDistance()) > 150.0) {
				pid.disable();
				go = false;
				SmartDashboard.putString("Disabled", "True");
			} else {
				pid.setPID(pVal, iVal, dVal);
				if (joystickOp.getRawButton(2)) {
					pid.disable();
					SmartDashboard.putString("Disabled", "True");
				}
				else if (joystickOp.getRawButton(1)) { // We want disable to be priority over enable and only have one work
					pid.enable();
					SmartDashboard.putString("Disabled", "False");
				}
//				pid.enable();
			}
			
			SmartDashboard.putNumber("PID Error", pid.getError());
			
			System.out.println("PID Error: " + pid.getError());
			
			SmartDashboard.putNumber("Current", pdp.getCurrent(14));
			
			System.out.println("Current: " + pdp.getCurrent(14));
		}
	}
	
}