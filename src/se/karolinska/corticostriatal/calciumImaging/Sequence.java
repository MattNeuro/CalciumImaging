package se.karolinska.corticostriatal.calciumImaging;

import javax.swing.JProgressBar;
import org.micromanager.utils.ReportingUtils;

/**
 *  Calcium Imaging Sequence
 *
 *  This handles the time-dependent aspects of acquiring a calcium imaging
 *  sequence. When ran, it starts taking snapshots (keeping track of the time),
 *  send a pulse mid-way, then continue to take snapshots.
 *
 *  @author Matthijs
 */
public class Sequence extends Thread {

    private static int      maxProgress = 41;
    
    private int             sequenceCount;
    private int             interval    = 5000;     // Default interval between sequences
    private long            pulseTime;
    private long            startTime;

    public  Processor       processor;
    public  ImageLabel      preLabel, postLabel;
    public  boolean         shouldStop  = true;
    public  JProgressBar    progress;
    
    private Pulse           pulse;


    /**
     *  Run an image acquisition sequence. We keep doing this as long as the
     *  shouldStop variable is false; when we want to stop, the sequence is
     *  completed before exiting this loop. Thus, setting shouldStop to false
     *  may not immediately stop this sequence.
     */
    @Override
    public void run() {
        try {
            initialize();
            do {
                prepareSequence();
                runSequence();
                waitForInterval();
            }  while (!shouldStop);
        } catch (Exception e) {
            ReportingUtils.logError(e);
        }
        close();
    }


    /**
     *  Send a TTL pulse over LPT1.
     */
    public void sendPulse () throws Exception {
        Thread thread   = new Thread(pulse);
        pulseTime       = System.currentTimeMillis();
        thread.start();
    }
    
    
    /**
     *  Wait for the user-specified interval between sequences.
     * 
     *  Update the progress bar while we are waiting to indicate the time that
     *  is passing.
     */
    private void waitForInterval () throws Exception {
        long start      = System.currentTimeMillis();
        long end        = interval - (start - startTime);
        long step       = end / ((long) maxProgress);

        long now;
        long target;
        
        if (start < 0 || step < 0)
            return;

        for (int i = 0; i < maxProgress; i++) {
            now         = System.currentTimeMillis() - start;
            target      = (i + 1) * step;
            if ((target - now) > 0)
                Thread.sleep(target - now);
            progress.setValue(maxProgress - i);
        }
    }


    /**
     *  Prepare the sequence. This runs any pre-run checks, as well as
     *  initializes necessary parameters and suggest garbage collection.
     */
    private void prepareSequence () {
        int shutterChannel      = Integer.parseInt(CalciumImaging.dialog.LPTChannelInputShutter.getText());
        preLabel.clearImages();
        postLabel.clearImages();
        checkSequenceCount();
        pulse.openShutter(shutterChannel);
        startTime               = System.currentTimeMillis();        
    }


    /**
     *  Check the number of repetitions in the sequence. Upon the last iteration,
     *  stop running the sequence. At each step, if applicable, update the stop
     *  button text to reflect the current sequence number.
     */
    private void checkSequenceCount () {
        int sequenceLimit = 0;
        try {
            sequenceLimit = Integer.parseInt(CalciumImaging.dialog.repetitionsInput.getText());
        } catch (Exception e) {
            return;
        }
        if (++sequenceCount == sequenceLimit)
            shouldStop = true;

        if (CalciumImaging.dialog.startButton.isEnabled())
            CalciumImaging.dialog.startButton.setText("Stop acquisition (" + sequenceCount
                                            + " / " + sequenceLimit + ")");
    }


    /**
     *  Run a single Image Acquisition sequence.
     *
     *  This takes snapshots prior to and after sending a pulse to IGOR, so we
     *  have a baseline with which we can compare the image intensity.
     */
    private void runSequence () throws Exception {       
        acquireSequence();

        grabImages(8,   preLabel);
        grabImages(16,  postLabel);

        pulse.closeShutter();
        
        setPulseTime();
        processor.addSequence();
    }


    /**
     *  Update the stored images with the pulse time, so the relative time to
     *  the pulse can be calculated.
     */
    private void setPulseTime () {
        for (CalciumImage image : preLabel.getImages())
            image.setPulseTime(pulseTime);
        for (CalciumImage image : postLabel.getImages())
            image.setPulseTime(pulseTime);
        preLabel.updateIcon(true);
        postLabel.updateIcon(true);
    }


    /**
     *  Snap a number of images from the image acquisition analyzer and show
     *  the image on our canvas.
     *
     *  @param count        The number of images to retrieve from the analyzer.
     *  @param target       What GUI element to display these images on.
     */
    private void grabImages (int count, ImageLabel target) throws Exception {
        ImageAnalyser analyzer = ImageAnalyser.getInstance();
        CalciumImage image;

        for (int i = 0; i < count; i++) {
            image      = analyzer.pollNextCalciumImage();
            showImage(image, target);
        }
    }


    /**
     *  Acquire an image sequence. This is done by signaling the
     *  image analyzer to start recording. After it is done recording,
     *  we can then retrieve the images from the analyzer.
     */
    synchronized private void acquireSequence () throws Exception {
        ImageAnalyser analyzer = ImageAnalyser.getInstance();
        analyzer.startSequence(this);
        this.wait();
    }


    /**
     *  Initialize the image acquisition sequence. This clears the previously
     *  recorded images, disables the "start" button (so we do not run multiple
     *  sequences in parallel) and sets references to the image labels.
     */
    private void initialize () {
        ReportingUtils.logMessage("Starting image sequence.");
        int channel     = Integer.parseInt(CalciumImaging.dialog.LPTChannelInput.getText());
        pulse           = new PulseArduino(channel);
        sequenceCount   = 0;
        processor       = new Processor(this);

        interval        = Integer.parseInt(CalciumImaging.dialog.intervalInput.getText());
        CalciumImaging.dialog.showDifferenceRadio.setSelected(true);
        
        if (CalciumImaging.dialog.continuousCheckbox.isSelected()) {
            CalciumImaging.dialog.startButton.setText("Stop acquisition");
            CalciumImaging.dialog.startButton.setEnabled(true);
            shouldStop = false;
        } else {
            CalciumImaging.dialog.startButton.setEnabled(false);
        }
        progress    = CalciumImaging.dialog.progressBar;
        preLabel    = (ImageLabel) CalciumImaging.dialog.preSpikeLabel;
        postLabel   = (ImageLabel) CalciumImaging.dialog.postSpikeLabel;
        preLabel.clearImages();
        postLabel.clearImages();
        postLabel.setRows(2);
        progress.setMaximum(maxProgress);
        progress.setValue(0);
    }


    /**
     *  Perform cleanup; reset the graphical user interface elements that may
     *  have been changed during this run.
     */
    private void close () {
        CalciumImaging.dialog.startButton.setEnabled(true);
        CalciumImaging.dialog.startButton.setText("Start acquisition");
        progress.setValue(0);

        if (CalciumImaging.dialog.showBothRadio.isSelected())
            new Overlay();
    }


    /**
     *  Show a TaggedImage in the preview panes.
     */
    private void showImage (CalciumImage image, ImageLabel target) {
        try {
            CalciumImaging.dialog.addImage(target, image);
        } catch (Exception e) {
            ReportingUtils.logMessage("Could not show snapped image:\r\n" + e);
            ReportingUtils.logError(e);
        }
    }
}
