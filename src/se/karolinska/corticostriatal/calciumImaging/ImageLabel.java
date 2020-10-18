package se.karolinska.corticostriatal.calciumImaging;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.micromanager.utils.ReportingUtils;

/**
 *  The ImageLabels are JLabels used to display images. They can store a number
 *  of CalciumImage objects and display all of them in a single icon.
 *
 * @author Matthijs
 */
public class ImageLabel extends JLabel {

    private static final int            ROW_LENGTH  = 8;
    private int                         rows        = 1;
    private LinkedList<CalciumImage>    images      = new LinkedList<>();
    private BufferedImage               buffer;
    private Graphics2D                  graphics;


    /**
     *  Add a new image to the images list.
     */
    public void addImage (CalciumImage image) {
        images.add(image);
        updateIcon(false);
    }

    public void setImage (int index, CalciumImage image) {
        images.set(index, image);
        updateIconStartStop(index, index + 1);
    }

    public void setImages (LinkedList<CalciumImage> imageList) {
        images = imageList;
        updateIcon(true);
    }


    /**
     *  Retrieve the images stored in this label as a LinkedList
     */
    public LinkedList<CalciumImage> getImages () {
        return images;
    }

    public CalciumImage getImage (int index) {
        return images.get(index);
    }

    
    /**
     *  Show the image at the designated position in the big snapshot panel.
     *
     *  For this, we first need to determine which of the images stored is
     *  located at that position.
     */
    public void showImageAt (int x, int y) {
        int height          = getThisHeight();
        int width           = getThisWidth();
        int horizontal      = (x * ROW_LENGTH)  / width;
        int vertical        = (y * rows)        / height;
        int index           = (vertical * ROW_LENGTH) + horizontal;

        ReportingUtils.logMessage("Showing image " + index + ".");
        CalciumImaging.application.enableLiveMode(false);
        CalciumImaging.application.displayImage(images.get(index).asTaggedImage());
    }


    /**
     *  Clear the images list. This also clears the graphics buffer and creates
     *  a new buffer, based on the current window size.
     */
    public void clearImages () {
        ReportingUtils.logMessage("Clearing images.");
        //images.clear();
        images      = new LinkedList<>();
        clearGraphics();
    }


    /**
     *  Set the number of rows over which our images are spread.
     *
     *  @param rows
     */
    public void setRows (int rows) {
        this.rows = rows;
    }


    /**
     *  Display all images in this list in the JLabel as icon. We resize these
     *  images first to fit in the available space.
     */
    public void updateIcon (boolean drawAll) {
        if (drawAll)
            clearGraphics();

        int start   = drawAll ? 0 : images.size() - 1;
        updateIconStartStop(start, images.size());
    }


    /**
     *  Clear the graphics component. This throws away the old graphics object
     *  responsible for drawing the image on, and creates a new one.
     */
     private void clearGraphics () {
        int height  = getThisHeight();
        int width   = getThisWidth();
        if (graphics != null) {
            graphics.dispose();
            buffer.flush();
        }
        buffer      = new BufferedImage(width, height - 2, BufferedImage.TYPE_INT_RGB);
        graphics    = buffer.createGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, width, height);
    }


    /**
     *  Display the images from start to stop in the JLabel as icon. This means
     *  any existing images not overwritten will remain on display, thus we can
     *  selectively update parts of the image.
     *
     *  @param start    Images from start are replaced.
     *  @param stop     Images up to stop are replaced.
     */
    private void updateIconStartStop (int start, int stop) {
        int height      = getThisHeight();
        int width       = getThisWidth();
        int newWidth    = (width / ROW_LENGTH) - 2;
        int newHeight   = (height / rows) - 2;

        int top, left;
        long pulseTime;
        Image target;

        if (graphics == null)
            return;

        height = height / rows;

        for (int i = start; i < stop; i++) {
            pulseTime   = images.get(i).getRelativePulseTime();
            target      = images.get(i);
            left        = ((width / ROW_LENGTH) * (i % ROW_LENGTH)) + 2;
            top         = (i >= ROW_LENGTH) ? height : 0;
            graphics.drawImage(target, left, top, newWidth, newHeight, null);
            drawPulseTime(pulseTime, left, top);
        }
        ImageIcon thumbnail = new ImageIcon(buffer);
        this.setIcon(thumbnail);
    }


    /**
     *  When the time relative to a pulse is known, we draw this time on top of
     *  the image.
     *
     *  @param pulseTime    Time in ms relative to a pulse.
     *  @param left         Absolute distance to the left of the graphics.
     *  @param top          Absolute distance to the top of the graphics.
     */
    private void drawPulseTime (long pulseTime, int left, int top) {
        if (pulseTime == 0)
            return;
        String  text            = pulseTime + " ms";
        Font    font            = new Font("Georgia", Font.PLAIN , 12);
        Graphics2D graphics2D   = (Graphics2D) graphics;
        TextLayout textLayout   = new TextLayout(text, font, graphics2D.getFontRenderContext());
        graphics2D.setColor(Color.BLACK);
        textLayout.draw(graphics2D, left + 5, top + 15);
        textLayout.draw(graphics2D, left + 3, top + 15);
        textLayout.draw(graphics2D, left + 5, top + 13);
        textLayout.draw(graphics2D, left + 3, top + 13);
        graphics2D.setColor(Color.WHITE);
        textLayout.draw(graphics2D, left + 4, top + 14);
    }


    /**
     *  Return the available width of this component, which is not the same as the
     *  width currently allocated to it.
     */
    private int getThisWidth () {
        return CalciumImaging.dialog.getWidth() - 219;
    }
    private int getThisHeight () {
        return (rows * (CalciumImaging.dialog.getHeight() - 242)) / 3;
    }
}