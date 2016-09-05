/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.karolinska.corticostriatal.calciumImaging;

import org.micromanager.utils.ReportingUtils;

/**
 *
 * @author Matthijs
 */
public class PulseLPT extends Pulse {


    /**
     *  Create a new Pulse-over-LPT object.
     * 
     *  @param port     Port to use for the pulse. This is not the same as the
     *                  port used for opening and closing any optional shutters.
     */
    public PulseLPT (int port) {
        super(port);
    }
    
    
    /**
     *  Changing state does not take long enough to warrant running in a new
     *  thread; use static open and close calls for this.
     */
    public void openShutter (int port) {
        byte portMask = 0x0000001;
        
        if (port > 0)
            portMask = (byte) (portMask << (port - 1));
        
        try {
            portState = (byte) CalciumImaging.core.getState("LPT1");
            ReportingUtils.logMessage("Current LPT1 state: " + portState);
            CalciumImaging.core.setState("LPT1", portMask | portState );
        } catch (Exception e) {
            ReportingUtils.logError(e, "Could not open shutter.");
        }
    }
    
    public void closeShutter () throws Exception {
        CalciumImaging.core.setState("LPT1", portState );
    }    
        
    
    /**
     *  Send a TTL pulse over the parallel port.
     */
    protected void sendPulse () throws Exception {
        byte oldState;
        oldState = (byte) CalciumImaging.core.getState("LPT1");
        ReportingUtils.logMessage("Current LPT1 state: " + oldState);
        CalciumImaging.core.setState("LPT1", portMask | oldState );
        Thread.sleep(2);
        CalciumImaging.core.setState("LPT1", oldState);
    }    
}
