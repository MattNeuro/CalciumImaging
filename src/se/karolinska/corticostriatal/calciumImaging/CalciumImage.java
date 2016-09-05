package se.karolinska.corticostriatal.calciumImaging;

import ij.process.ColorProcessor;
import ij.process.ShortProcessor;
import java.awt.image.BufferedImage;
import java.util.Date;
import mmcorej.TaggedImage;
import org.json.JSONObject;
import org.micromanager.utils.ReportingUtils;

/**
 *  Annotated image used in calcium imaging.
 *
 *  @author Matthijs
 */
public class CalciumImage extends BufferedImage {
    
    private long         time;
    public  JSONObject   tags;


    /**
     *  CalciumImage constructor. Mimics the BufferedImage constructor, and
     *  exists solely for that purpose.
     *
     *  @param width        The width of this image in pixels.
     *  @param height       The height of this image in pixels.
     *  @param imageType    The color-type of this image (default: TYPE_USHORT_GRAY).
     */
    public CalciumImage (int width, int height, int imageType) {
        super(width, height, imageType);
    }


    /**
     *  Retrieve the pixels in this Image as an Object. Use an ImageProcessor
     *  to achieve this.
     */
    public Object getPixels () {
        ShortProcessor processor;

        // Not a ushort gray type image. Assume this is a color image and recast:
        if (super.getType() != TYPE_USHORT_GRAY) {
            ColorProcessor colorP   = new ColorProcessor(this);
            processor               = (ShortProcessor) colorP.convertToShort(true);
        } else {
            processor               = new ShortProcessor(this);
        }
        return processor.getPixels();
    }


    /**
     *  Store the time of when this image was taken. This
     *  provides us with the relative time
     *  before or after the pulse.
     *
     *  @param time    Time in milliseconds when this image was acquired.
     */
    public void setTime (long time) {
        this.time = time;
        if (tags == null)
            return;
        try {
            tags.put("time", time);
        } catch (Exception e) {
            ReportingUtils.logError(e);
        }
    }


    /**
     *  Set the time at which the pulse occurred. This allows us to calculate
     *  the time of this image relative to the pulse.
     *
     *  @param pulse    The time (in milliseconds) when the pulse was send to
     *                  the slice.
     */
    public void setPulseTime (long pulse) {
        try {
            tags.put("pulseTime", time - pulse);
        } catch (Exception e) {
            ReportingUtils.logError(e);
        }
    }


    /**
     *  Retrieve the time (in milliseconds) between the start of this image and
     *  the pulse. This can be both negative (for images acquired prior to a
     *  pulse) as well as positive (for images taken after a pulse), and it may
     *  also be zero if the pulse-time is not yet recorded.
     *
     *  @return Time between taking this image and the pulse.
     */
    public long getRelativePulseTime () {
        try {
            return tags.getLong("pulseTime");
        } catch (Exception e) {
            return ((long) 0);
        }
    }


    /**
     *  Convert this CalciumImage to a TaggedImage and return it.
     */
    public TaggedImage asTaggedImage () {
        return new TaggedImage(getPixels(), tags);
    }


    /**
     *  Return the date-time when this image was first acquired as a JAVA date
     *  object.
     */
    public Date getStartDate () {
        return new Date(time);
    }


    /**
     *  Create a deep copy.
     */
    @Override
    public CalciumImage clone () {
        CalciumImage clone  = CalciumImage.fromImage(this);
        clone.time          = this.time;
        clone.tags          = this.tags;
        return clone;
    }


    /**
     *  Create a new CalciumImage from a JAVA AWT Image source. This creates a
     *  deep copy.
     */
    public static CalciumImage fromImage (BufferedImage source) {
        CalciumImage target = new CalciumImage(source.getWidth(),
                                               source.getHeight(),
                                               BufferedImage.TYPE_USHORT_GRAY);
        target.getGraphics().drawImage(source, 0, 0, null);
        return target;
    }
}