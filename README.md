# PIDTesting2015 <br />
<h2 align='center'>PIDTesting 2015 - Impulse</h2>
This project's only use is to test PID.<br />
Currently, we are working on 2014 Impulse's Hammer.<br />
Here are our current values:<br />
P -0.018<br />
I -0.001<br />
D -0.030<br />
These values should also be hard coded into the program as defaults.<br />
<br />
<h2 align='center'>---DOCUMENTATION---</h2>
We have found that having a low P value is beneficial. We started by keeping the I and D values at 0 and trying to find a value for P that worked by itself. For us, the P value -0.018 was self-sufficient and could stabilize itself, albeit at a lower location than the desired setpoint. Also, we have noticed that we want to keep I at an extremely low value and that higher values of D cause really bad oscillation at higher setpoints.<br />
Here is also a list of some mistakes we made for future reference: <br />
<ui>
  <li>Guessing the value of P randomly</li>
  <li>Starting P/I/D starting too high (You want to start at 0.0 and go up gradually)</li>
  <li>Having P, I and D values / setpoint hard coded into the program</li>
  <li>Using the throttle to control P/I/D/setpoint</li>
  <li>Having buttons change the values 0.001/1 at a time (Have a variable/button combo that changes how much to change the values each time)</li>
  <li>Having to disable the program/robot every time the robot failed</li>
  <li>Not having a automatic encoder shut-off when the hammer went over 150.0 (almost vertical)</li>
</ui>
<h3 align='center'>IT MUST BE NOTED THAT AN EXPERIMENT OF THIS NATURE <b>REQUIRES</b> THE ROBOT TO BE ON BLOCKS AND AT A SAFE LOCATION. THE ROBOT CAN EASILY BECOME A KILLING MACHINE IF YOU ARE NOT CAREFUL. IF YOU TOUCH THE ROBOT AT ANY TIME, THE WIRES CONNECTED TO THE MOTORS MUST BE UNPLUGGED AND/OR THE ROBOT MUST BE OFF. <b>IT IS NOT SUFFICIENT TO DISABLE THE ROBOT ON THE DRIVER STATION. WE HAVE HAD TIMES WHERE THE COMPUTER HAS LOST CONTROL OF THE ROBOT (OTHER REASONS)</b>. JUST MAKE SURE THAT YOU ARE REALLY SAFE AND DON'T ACCIDENTALLY KILL/HURT ANYONE WHEN EXPERIMENTING WITH PID.</h3>
<h4>NOTE: A value of -0.1 would be "higher" than -0.05 in this example.</h4>
-Andy<br />
2015<br />
