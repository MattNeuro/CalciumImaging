package se.karolinska.corticostriatal.calciumImaging;

import org.micromanager.utils.ReportingUtils;

/**
 *  Send a TTL pulse through an Arduino
 * 
 *  This extends the basic Pulse class with an implementation designed
 *  for the Arduino. It supports the Pulse methods to open and close the 
 *  shutter (set state on the specified port before and after taking images)
 *  as well as sending the TTL pulse itself.
 * 
 *  @author Matthijs
 */
public class PulseArduino extends Pulse {

    
    /**
     *  Create a new Pulse-over-LPT object.
     * 
     *  @param port     Port to use for the pulse. This is not 
     *                  the same as the port used for opening and closing
     *                  any optional shutters.
     */
    public PulseArduino (int port) {
        super(port);
        ReportingUtils.logMessage("Sending pulse to arduino on port " + port);
    }
    
    
    /**
     *  Update the state of the shutter port to instruct the shutter driver
     *  to open.
     * 
     *  @param port     The port to use for opening the shutter. Port 1 here 
     *                  corresponds to digital pin 8 of the Arduino, and on.
     *                  
     */
    @Override
    public void openShutter(int port) {
        try {
            CalciumImaging.core.setShutterOpen(true);
            ReportingUtils.logMessage("Opening shutter.");
        } catch (Exception e) {
            ReportingUtils.logError(e, "Could not open shutter.");
        }        
    }


    /**
     *  Close the shutter through the configured shutter device.
     * 
     *  @throws Exception 
     */
    @Override
    public void closeShutter() {
        try {
            CalciumImaging.core.setShutterOpen(false);
        } catch (Exception e) {
            ReportingUtils.logError(e, "Could not open shutter.");
        }
    }

    
    /**
     *  Switch the state on the port specified in this class' constructor to 
     *  UP, wait two milliseconds, then switch it back DOWN. This is the equivalent
     *  of a TTL pulse, which can be picked up by e.g. Igor.
     * 
     *  @throws Exception 
     */
    @Override
    protected void sendPulse() throws Exception {
        byte oldState;
        oldState = (byte) Integer.parseInt(CalciumImaging.core.getProperty("Arduino-Switch", "State"));
        ReportingUtils.logMessage("Sending pulse. Current Arduino state: " + oldState);
        CalciumImaging.core.setProperty("Arduino-Switch", "State", portMask | oldState );
        Thread.sleep(2);
        CalciumImaging.core.setProperty("Arduino-Switch", "State", oldState);
    }  
}