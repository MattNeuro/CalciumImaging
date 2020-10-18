package se.karolinska.corticostriatal.calciumImaging;

import ij.process.ShortProcessor;


/**
 *  Perform image operations in parallel. 
 * 
 *  @author Matthijs
 */
public class ProcessorWorker implements Runnable {

    
    private ShortProcessor image, mean, difference;
    private int index;
    
    
    /**
     *  Instantiate a new ProcessorWorker. This will process the image based on 
     *  a general "average" image for the whole recorded series.
     *  <br><br>
     *  In short, it removes the "mean" values for each pixel from this particular image,
     *  and subsequently stretches the image values over the entire possible pixel range,
     *  normalising it.
     * 
     *  @param processor
     *  @param mean
     *  @param index 
     */
    public ProcessorWorker (ShortProcessor image, ShortProcessor mean, int index) {
        this.image      = image;
        this.mean       = mean;
        this.index      = index;
    }
    
    
    @Override
    public void run() {
        this.difference = new ShortProcessor(image.getWidth(), image.getHeight());
        normalize();
        removeImageMean();        
        Processor.addDifferenceImage(difference, index);
    }
    

    /**
     *  Remove the mean value from a single image.
     *
     *  @param processor    Image processor to remove mean from.
     *  @param mean         Mean values.
     */
    private void removeImageMean () {
        double     value = 0.0, grey = 0.0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                value = image.get(x, y);
                grey  = mean.get(x, y);
                difference.putPixel(x, y, (short) ((value - grey)));
            }
        }
    }


    /**
     *  Ensure the global light intensity in the given image matches the overall
     *  light intensity in the mean image. Do this by amplifying the image
     *  intensity such that the total pixel values match the total value of the
     *  mean image.
     *  <br><br>
     *  If the mean image is brighter on average, the conversion factor will be
     *  greater than one, and the source image will be made more bright. Likewise,
     *  if it is less bright than the new image, the conversion will be less than
     *  one and each pixel will decrease in luminosity.
     *
     *  @param image    The image to normalise
     *  @param mean     The mean image to base the normalisation on.
     */
    private void normalize () {
       double totalImage = 0.0, totalMean = 0.0;
       for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                totalImage += image.get(x, y);
                totalMean  += mean.get(x, y);
            }
       }
       double conversion = totalMean / totalImage;
       image.multiply(conversion);
    }   
}