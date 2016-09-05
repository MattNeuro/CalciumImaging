package se.karolinska.corticostriatal.calciumImaging;

import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import java.util.concurrent.ConcurrentLinkedQueue;
import mmcorej.TaggedImage;
import org.micromanager.api.TaggedImageAnalyzer;
import org.micromanager.utils.ReportingUtils;

/**
 *  An ImageAnalyser, which is hooked into the incoming image acquisition 
 *  pipeline.
 * 
 *  @author Matthijs
 */
public class ImageAnalyser extends TaggedImageAnalyzer {


    private static  ImageAnalyser   instance;
    private         boolean         record      = false;
    private         int             index;
    private         Sequence        sequence;
    private final ConcurrentLinkedQueue<CalciumImage> images;


    /**
     *  Retrieve a reference to an ImageAnalyser instance. There should
     *  be just one of these, so we only instantiate a new ImageAnalyser when
     *  no instance is available.
     */
    public static ImageAnalyser getInstance () {
        if (instance == null)
            instance  = new ImageAnalyser();
        return ImageAnalyser.instance;
    }


    /**
     *  Start the acquisition sequence: this tells the instance to start
     *  recording, resetting any previously stored images and setting the
     *  acquisition index to zero.
     *
     *  @param sequence The calling parent sequence.
     */
    public void startSequence (Sequence sequence) {
        images.clear();
        index           = 0;
        this.sequence   = sequence;
        record          = true;
    }


    /**
     *  Retrieve the next calcium image from the LinkedList. This removes the
     *  LAST image from the list, which was the FIRST to be added to it.
     *
     *  @return A CalciumImage from the recorded set.
     */
    public CalciumImage pollNextCalciumImage () {
        synchronized(images) {
            return images.poll();
        }
    }


    /**
     *  Create a new ImageAnalyser object. Store a reference to this object
     *  in a private static field, so we can retrieve it from another class.
     */
    private ImageAnalyser () {
        images = new ConcurrentLinkedQueue<CalciumImage>();
    }
    

    /**
     *  Record an image. This stores the provided TaggedImage as a new
     *  CalciumImage in a list. When enough images have been stored, we
     *  stop recording. After the 8th image, send a TTL pulse.
     */
    private void recordImage (TaggedImage ti) throws Exception {
        if (index == 8)
            sequence.sendPulse();

        ImageProcessor  source;
        CalciumImage    image;

        long time       = System.currentTimeMillis() - ((long) CalciumImaging.core.getExposure());
        int  width      = (int) CalciumImaging.core.getImageWidth();
        int  height     = (int) CalciumImaging.core.getImageHeight();

        if (CalciumImaging.core.getBytesPerPixel() == 1)
            source      = new ByteProcessor(width, height);
        else 
            source      = new ShortProcessor(width, height);

        source.setPixels(ti.pix);
        image           = CalciumImage.fromImage(source.getBufferedImage());
        image.tags      = ti.tags;
        image.setTime(time);
        images.add(image);
        CalciumImaging.dialog.progressBar.setValue(index);

        // Stop recording. Wake up our parent thread.
        if (index == 24) {
            record = false;
            synchronized(sequence) {
                sequence.notify();
            }
        }
    }



    /**
     *  Analyze an incoming image that's still in the pipeline.
     *
     *  If we are recording (record flag set to true), the image is passed
     *  on to the recordImage method which will duplicate it (create a deep
     *  copy) and log time of recording.
     *
     *  @param ti   The incoming TaggedImage.
     */
    @Override
    protected void analyze (TaggedImage ti) {
        if (!record)
            return;

        try {
            index++;
            synchronized(ti) {
                recordImage(ti);
            }
        } catch (Exception e) {
            ReportingUtils.logError(e);
        }
    }
}