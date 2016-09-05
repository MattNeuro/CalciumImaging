package se.karolinska.corticostriatal.calciumImaging;

import mmcorej.CMMCore;
import org.micromanager.api.ScriptInterface;
import org.micromanager.utils.ReportingUtils;

/**
 *  Calcium CalciumImaging plug-in for MicroManager.
 *
 *  @author Matthijs
 */
public class CalciumImaging implements org.micromanager.api.MMPlugin {

    public static String        menuName           = "Calcium Imaging";
    public static String        tooltipDescription = "Start a calcium imaging sequence.";

    public static CMMCore           core;
    public static ScriptInterface   application;
    public static ImagingDialog     dialog;

    private static Sequence         sequence;


    /**
     *  Show the calcium imaging dialogue interface. If it was already opened
     *  earlier, this will move it to the front so it becomes visible again.
     */
    public void show () {
        ReportingUtils.logMessage("Showing the Calcium Imaging dialog, and properties.");
        if (dialog == null) {
            dialog = new ImagingDialog(this);
            dialog.setVisible(true);
        } else {
            dialog.setPlugin(this);
            dialog.toFront();
        }
    }


    /**
     *  If a sequence is running, send it the stop signal. If no sequence is
     *  running, start the sequence.
     */
    public void toggleSequence () {
        if (sequence == null || !sequence.isAlive())
            startSequence();
        else {
            dialog.startButton.setEnabled(false);
            sequence.shouldStop = true;
        }
    }


    /**
     *  Start a new sequence. This assumes that there are no currently running
     *  sequences.
     */
    public void startSequence () {
        application.enableLiveMode(true);
        sequence = new Sequence();
        sequence.start();
    }


    /**
     *  Retrieve reference to the image processor, which stores the actual raw
     *  and processed images.
     */
    public static Processor getProcessor () {
        return CalciumImaging.sequence.processor;
    }
    public static Sequence getSequence () {
        return CalciumImaging.sequence;
    }


    /**
     *  Set references to the application and MicroManager core.
     *
     *  Also add the image analyzer processor, so we have a hook into when new
     *  images are retrieved in the live window.
     * 
     *  @param si   ScriptInterface to the application.
     */
    public void setApp (ScriptInterface si) {
        application     = si;
        core            = si.getMMCore();
        application.addImageProcessor(ImageAnalyser.getInstance());

        if (core.getCameraDevice().equalsIgnoreCase("DCam")) {
            try {
                application.enableLiveMode(false);
                core.setProperty("DCam", "PixelType", "16bit");
            } catch (Exception e) {
                ReportingUtils.logError(e);
            }
        }
    }


    /**
     *  Plug-in information, description, copyright, etc.
     */
    public String getDescription()  { return "Calcium Imaging timing plugin.";  }
    public String getInfo()         { return "Calcium Imaging timing plugin.";  }
    public String getVersion()      { return "0.7-14062016";                    }
    public String getCopyright()    { return "Karolinska Institutet, Stockholm, 2016. Author: Matthijs Dorst"; }
    public void dispose ()              {}    
    public void configurationChanged () {}
}