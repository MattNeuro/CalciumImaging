package se.karolinska.corticostriatal.calciumImaging;

import java.awt.Toolkit;
import org.micromanager.utils.ReportingUtils;

/**
 *  Send a TTL pulse to mark when excitation should occur.
 *
 *  For alternative methods to send a TTL pulse, modify this class with the
 *  appropriate device / state change.
 *
 *  @author Matthijs
 */
public abstract class Pulse implements Runnable {


    protected byte        portMask = 0x0000001;
    protected static byte portState;


    public      abstract void openShutter  (int port);
    public      abstract void closeShutter ()           throws Exception;
    protected   abstract void sendPulse    ()           throws Exception;
    
    
    /**
     *  Create a new Pulse object on the specified port. When this thread is
     *  run, it will send a pulse on that port.
     *
     *  Use a bit-shift to convert the integer port number into a bit-mask.
     *
     *  @param port The port to send the pulse on.
     */
     public Pulse (int port) {
        if (port > 0)
            portMask = (byte) (portMask << (port - 1));
    }


    /**
     *  Run the Pulse thread.
     *
     *  This should be called from a Thread context; in other words, start
     *  with:
     *
     *      Thread thread = new Thread(new Pulse());
     *      thread.start();
     *
     *  Once called, this will run the pulse code in a separate thread so as to
     *  not interfere with normal application performance. 
     */
    @Override
    public void run () {
        try {
            Toolkit.getDefaultToolkit().beep();            
            sendPulse();
        } catch (Exception e) {
            ReportingUtils.logError(e);
        }
    }

}
