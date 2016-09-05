package se.karolinska.corticostriatal.calciumImaging;

import ij.process.ShortProcessor;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.micromanager.utils.ReportingUtils;

/**
 *  Imaging Processor.
 *
 *  This class handles the processing involved in Calcium Imaging. It calculates
 *  mean values, deviations, stores intermediate results, etc. Ideally, this is
 *  called after an image acquisition sequence so that the acquisition does not
 *  suffer from delays.
 *
 *  @author Matthijs
 */
public class Processor {

    private Sequence                    parent;
    private LinkedList<TreeMap>         images      = new LinkedList<>();
    private LinkedList<CalciumImage>    difference  = new LinkedList<>();
    private LinkedList<double[][]>      cumDiff     = new LinkedList<>();
    private int                         width, height;
    private static ShortProcessor[]     diffImage; 


    public Processor (Sequence parent) {
        this.parent = parent;
    }


    /**
     *  Retrieve the stored images in this processor.
     */
    public LinkedList<TreeMap> getImages () {
        return images;
    }
    

    /**
     *  Retrieve the continuous difference images.
     */
    public LinkedList<CalciumImage> getDifferences () {
        return difference;
    }


    /**
     *  Show the difference images.
     */
    public void showDifferences () {
        parent.postLabel.setImages(difference);
    }
    public void showPost () {
        LinkedList<CalciumImage> post = ((LinkedList<CalciumImage>) images.getLast().get("post"));
        parent.postLabel.setImages(post);
    }


    /**
     *  Add a sequence of images to the images list. This separates those images
     *  by pre- or post-spike timing; the pre-spike images can then be used to
     *  generate a baseline, while the post-spike images can be used to
     *  calculate the difference from that baseline.
     * 
     *  To conserve memory, this will first remove the previous set of acquired
     *  raw images, unless the user explicitly opts to retain them.
     */
    public void addSequence () {
        if (!CalciumImaging.dialog.storeAllRawCheckbox.isSelected())
            removeLast();
        TreeMap<String,LinkedList> sequence = new TreeMap<>();
        sequence.put("pre",  parent.preLabel.getImages());
        sequence.put("post", parent.postLabel.getImages());
        loadMean(sequence);
        removeMean(sequence);
        images.add(sequence);
        updateContinuousDifference();
    }

    
    /**
     *  Add a difference image to the list of difference images, at the specified index.
     *  
     *  @param difference   ShortProcessor representation of a difference image.
     *  @param i            Index at which this image should be inserted.
     */
    public synchronized static void addDifferenceImage (ShortProcessor difference, int i) {
        diffImage[i] = difference;
    }
    

    /**
     *  Update the continuous difference. This is a frame-by-frame summed
     *  difference between the mean image and the latest capture. For a single
     *  sequence, this is identical to the regular diff, but for sequential
     *  sequences, the continuous difference shows smaller but persistent
     *  deviations.
     */
    private void updateContinuousDifference () {
        ShortProcessor current;
        double[][] values;

        if (!CalciumImaging.dialog.showDifferenceRadio.isSelected() &&
            !CalciumImaging.dialog.showBothRadio.isSelected())
            return;

        parent.postLabel.clearImages();
        if (cumDiff.isEmpty())
            initializeCumulativeDifferences(width, height);

        // For every frame:
        for (int i = 0; i < ((LinkedList) images.getLast().get("difference")).size(); i++) {
            values  = cumDiff.get(i);
            current = (ShortProcessor) ((LinkedList) images.getLast().get("difference")).get(i);

            // For every pixel:
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++)
                    values[x][y] += (current.get(x, y));

            minimize(values);
            cumDiff.set(i, values);
            CalciumImage diffImage = getDifference(values, width, height);
            diffImage.tags = ((LinkedList<CalciumImage>) images.getLast().get("post")).get(i).tags;
            parent.progress.setValue(26 + i);
            difference.add(diffImage);
            parent.postLabel.addImage(diffImage);
        }
    }


    /**
     *  Initialise the cumulative differences list, ie., the list of short
     *  arrays representing the differences found so far. This makes sure that
     *  each short is initialised.
     *
     *  @param width
     *  @param height
     */
    private void initializeCumulativeDifferences (int width, int height) {
        for (int i = 0; i <= ((LinkedList) images.getLast().get("difference")).size(); i++) {
            double[][] values = new double[width][height];
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++)
                    values[x][y] = 0;
            cumDiff.add(values);
        }
    }


    /**
     *  Produce a difference image from the pixel values, and the size of the
     *  original image.
     */
    private CalciumImage getDifference (double[][] values, int width, int height) {
        ShortProcessor diff  = new ShortProcessor(width, height);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                diff.putPixel(x, y, (short) ((values[x][y])));

        return CalciumImage.fromImage(diff.getBufferedImage());
    }


    /**
     *  Remove the previous recorded set of pre- and post-spike images, if these
     *  are no longer required.
     */
     private void removeLast () {
        if (images.size() < 2)
            return;

        int lastIndex = images.size() - 2;
        ((LinkedList) images.get(lastIndex).get("pre")).clear();
        ((LinkedList) images.get(lastIndex).get("post")).clear();
        ((LinkedList) images.get(lastIndex).get("difference")).clear();
        images.remove(lastIndex);
        difference.clear();
    }


    /**
     *  Calculate the mean value of the pre-spike image, for a single sequence.
     *  
     *  @param sequence
     */
    private void loadMean (TreeMap sequence) {
        LinkedList<CalciumImage> pre = (LinkedList) sequence.get("pre");

        height                  = pre.get(0).getHeight(null);
        width                   = pre.get(0).getWidth(null);
        double[][] values       = new double[width][height];
        double size             = pre.size();
        ShortProcessor mean     = new ShortProcessor(width, height);
        ShortProcessor curr;

        for (int i = 0; i < pre.size(); i++) {
            curr    = new ShortProcessor(pre.get(i));
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++)
                    values[x][y]  += curr.get(x, y);
        }
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                mean.set(x, y, ((int) (values[x][y] / size)));

        sequence.put("mean", mean);
    }


    /**
     *  Subtract the mean image from the post-spike images in this sequence.
     * 
     *  Use a ExecutorService to process multiple images simultaneously.
     */
    private void removeMean (TreeMap sequence) {
        ShortProcessor mean                 = (ShortProcessor) sequence.get("mean");
        LinkedList<CalciumImage> post       = (LinkedList<CalciumImage>) sequence.get("post");
        diffImage                           = new ShortProcessor[post.size()];
        ExecutorService executor            = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1); //creating a pool of all available CPUs
        ShortProcessor processor;

        for (int i = 0; i < post.size(); i++) {
            processor           = new ShortProcessor(CalciumImage.fromImage(post.get(i)));
            Runnable worker     = new ProcessorWorker(processor, mean, i);
            executor.execute(worker);
        }
        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
            parent.progress.setValue(25);
            sequence.put("difference", new LinkedList(Arrays.asList(diffImage)));
        } catch (Exception e) {
            ReportingUtils.logError(e, "Images could not be analyzed in a timely fashion.");
        }
    }


    /**
     *  Minimize a short-array representation of a cumulative-difference image.
     */
    private void minimize (double[][] image) {
        double total = 0.0;

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                total += image[x][y];
        total /= (width * height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((image[x][y] - total) < 0)
                    image[x][y] = 0;
                else if (image[x][y] - total > Integer.MAX_VALUE)
                    image[x][y] = Integer.MAX_VALUE;
                else
                    image[x][y] -= total;
            }
        }
    }
}