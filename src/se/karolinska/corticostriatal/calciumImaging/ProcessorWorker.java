package se.karolinska.corticostriatal.calciumImaging;

import ij.process.ShortProcessor;


/**
 *  Perform image operations in parallel. 
 * 
 *  @author Matthijs
 */
public class ProcessorWorker implements Runnable {

    
    private ShortProcessor processor, mean, difference;
    private int index;
    
    
    /**
     *  Instantiate a new processorworker. This will process the image based on 
     *  a general "average" image for the whole recorded series.
     * 
     *  @param processor
     *  @param mean
     *  @param index 
     */
    public ProcessorWorker (ShortProcessor processor, ShortProcessor mean, int index) {
        this.processor  = processor;
        this.mean       = mean;
        this.index      = index;
        this.difference = new ShortProcessor(processor.getWidth(), processor.getHeight());
    }
    
    
    
    @Override
    public void run() {
        normalize(processor, mean);
        removeImageMean(processor, mean);        
        Processor.addDifferenceImage(difference, index);
    }
    

    /**
     *  Remove the mean value from a single image.
     *
     *  @param processor    Image processor to remove mean from.
     *  @param mean         Mean values.
     */
    private void removeImageMean (ShortProcessor processor, ShortProcessor mean) {
        double     value = 0.0, grey = 0.0;

        for (int x = 0; x < mean.getWidth(); x++) {
            for (int y = 0; y < mean.getHeight(); y++) {
                value = processor.get(x, y);
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
     *
     *  @param image    The image to normalize
     *  @param mean     The mean image to base the normalization on.
     */
    private void normalize (ShortProcessor image, ShortProcessor mean) {
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