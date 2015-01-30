# PIDTesting2015 <br />
<h2>PIDTesting 2015 - Impulse</h2>
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
<h2 format='bold'>NOTE: A value of -0.1 would be "higher" than -0.05 in this example.</h2>
-Andy<br />
2015<br />
