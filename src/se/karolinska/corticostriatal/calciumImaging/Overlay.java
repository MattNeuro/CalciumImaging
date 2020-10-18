package se.karolinska.corticostriatal.calciumImaging;

import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 *  Generate and display "Overlay" images, that show both the raw background
 *  image, as well as the distinct difference relative to the mean, superimposed
 *  on that raw background in a different color.
 *
 *  @author Matthijs
 */
public class Overlay {

    private     TreeMap         diffImages;


    public Overlay () {
        initialize();
        generateImages();
    }
    


    private void generateImages () {
        LinkedList<CalciumImage> differences;
        LinkedList<CalciumImage> post;
        CalciumImage result, image, base;
        differences             = CalciumImaging.getProcessor().getDifferences();
        post                    = (LinkedList<CalciumImage>) diffImages.get("post");
        CalciumImaging.getSequence().postLabel.clearImages();

        for (int i = 0; i < differences.size(); i++) {
            base    = post.get(i);
            image   = differences.get(i);
            result  = generateImage(base, image);
            showImage(result);
        }
    }


    /**
     *  Generate an overlay image, based on a mean "base" image and an image
     *  listing the deviations from that base.
     *
     *  @param base             The ground, mean image.
     *  @param differenceImage  Differences to that mean image.
     *  @return                 A combined image with the difference in the red channel.
     */
    private CalciumImage generateImage (CalciumImage base, CalciumImage differenceImage) {
        ColorProcessor overlay      = new ColorProcessor(base);
        ImageProcessor difference   = new ShortProcessor(differenceImage);
        int     width               = overlay.getWidth();
        int     height              = overlay.getHeight();
        int[]   color               = {0, 0, 0};

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                color       = overlay.getPixel(x, y, color);
                color[1]   += difference.get(x, y);
                overlay.putPixel(x, y, color);
            }
        }
        CalciumImage result  = new CalciumImage(width, height,
                                                BufferedImage.TYPE_INT_RGB);
        result.tags = base.tags;
        result.getGraphics().drawImage(overlay.createImage(), 0, 0, null);
        return result;
    }
    

    /**
     *  Show an image in the post-spike window.
     */
    private void showImage (CalciumImage result) {
        CalciumImaging.getSequence().postLabel.addImage(result);
    }


    /**
     *  Load references to the images in the last sequence.
     */
    private void initialize () {
        diffImages  =  CalciumImaging.getProcessor().getImages().getLast();
    }
}