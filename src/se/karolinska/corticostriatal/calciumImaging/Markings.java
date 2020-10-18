package se.karolinska.corticostriatal.calciumImaging;

import ij.IJ;
import ij.gui.Roi;
import ij.process.ColorProcessor;
import ij.process.ShortProcessor;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.LinkedList;
import mmcorej.TaggedImage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.micromanager.utils.ReportingUtils;

/**
 *  Manage marked locations in the images. For specified marked locations, or
 *  the entire image, this can display the mean intensity values over time.
 *
 *  For this to work, multiple images in a sequence must have been acquired
 *  first.
 *
 *  @author Matthijs
 */
public class Markings {

    private static Markings         instance;
    private XYSeriesCollection      dataset;
    private ChartPanel              panel;
    private JFreeChart              chart;


    private Markings () {
        dataset = new XYSeriesCollection();
        panel   = (ChartPanel) CalciumImaging.dialog.histogramPanel;
    }


    /**
     *  Singleton producer. Generate a new Markings instance if required,
     *  or if one already exists, return it.
     */
    public static Markings getInstance () {
        if (instance == null)
            instance = new Markings();
        return instance;
    }


    /**
     *  Plot an ROI histogram. This assumes we have taken a series of images
     *  already, and optionally selected points or regions of interest in those
     *  images for which we want to know the intensity changes over time.
     */
    public static void plotRoiHistogram () {
        Markings markings = getInstance();
        markings.dataset.removeAllSeries();
        
        try {
            Roi roi         = IJ.getImage().getRoi();
            if (roi == null)
                markings.plotRoiTotal();
            else if(roi.getType() == Roi.POINT)
                markings.plotRoiPoints(roi);
            else if (roi.getType() == Roi.RECTANGLE)
                markings.plotRoiRectangle(roi);
            else
                markings.plotRoiTotal();

            markings.plotHistogram();
        } catch (Exception e) { ReportingUtils.logError(e); }
    }


    /**
     *  Show the intensity graph in the live preview window.
     */
    public void showGraph () {
        int width                   = (int) CalciumImaging.core.getImageWidth();
        int height                  = (int) CalciumImaging.core.getImageHeight();

        BufferedImage image         = chart.createBufferedImage(width, height);
        ColorProcessor processor    = new ColorProcessor(image);
        ShortProcessor shortP       = (ShortProcessor) processor.convertToShort(true);
        CalciumImaging.application.enableLiveMode(false);
        CalciumImaging.application.displayImage(new TaggedImage(shortP.getPixels(), null));
    }


    /**
     *  Create a chart object and add it to the JPanel designated for histogram
     *  plotting. All data currently available in this.dataset will be displayed
     *  as a XYLineChart.
     *
     *  @throws Exception   In case no dataset is generated, this will throw an
     *                      exception.
     */
    private void plotHistogram () throws Exception {
        if (dataset.getSeriesCount() == 0)
            throw new Exception("No dataset available to plot.");

        chart = ChartFactory.createXYLineChart("",                          // Title
                                               "",                          // x-axis Label
                                               "",                          // y-axis Label
                                               dataset,                     // Dataset
                                               PlotOrientation.VERTICAL,    // Orientation
                                               false,                       // Show Legend
                                               true,                        // Use tooltips
                                               false                        // Configure chart to generate URLs?
                                               );
        XYPlot plot     = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.lightGray);

        LegendTitle lt  = new LegendTitle(plot);
        lt.setPosition(RectangleEdge.TOP);
        lt.setBackgroundPaint(Color.WHITE);
        lt.setFrame(new BlockBorder(Color.black));

        XYTitleAnnotation ta = new XYTitleAnnotation(0.98, 0.98, lt, RectangleAnchor.TOP_RIGHT);
        ta.setMaxWidth(0.48);
        plot.addAnnotation(ta);
        panel.setChart(chart);
    }


    /**
     *  Add a region of interest to the list of areas for which we need to
     *  plot a histogram; that is, gather histogram data and add it to the
     *  graph builder.
     *
     *  @param roi  The rectangular bounds of the region of interest.
     */
    private void addHistogram (Rectangle roi, String name) {
        LinkedList<CalciumImage> pre    = CalciumImaging.getSequence().preLabel.getImages();
        LinkedList<CalciumImage> post   = CalciumImaging.getSequence().postLabel.getImages();
        XYSeries data = new XYSeries(name);

        for (CalciumImage image : pre) {
            Raster area = image.getData(roi);
            data.add(image.getRelativePulseTime(), getMean(area));
        }

        for (CalciumImage image : post) {
            Raster area = image.getData(roi);
            data.add(image.getRelativePulseTime(), getMean(area));
            //data.addValue((Number) getMean(area), name, image.getRelativePulseTime());
        }
        dataset.addSeries(data);
    }


    /**
     *  Obtain the mean intensity value of the specified image.
     */
    private double getMean (Raster area) {
        int width           = area.getWidth();
        int height          = area.getHeight();
        double[] pixels     = new double[width * height];
        pixels              = area.getPixels(area.getMinX(), area.getMinY(),
                                             width, height, pixels);
        double total        = 0.0;
        for (double pixel : pixels)
            total += pixel;
        return (total / (width * height));
    }


    /**
     *  Plot the histogram for a total region-of-interest, in other words, the
     *  entire image is our region of interest. For this, we need to figure out
     *  how large the images are.
     */
    private void plotRoiTotal () {
        CalciumImage first     = CalciumImaging.getSequence().preLabel.getImage(0);
        Rectangle    rectangle = new Rectangle();
        rectangle.setRect(0, 0, first.getWidth(), first.getHeight());
        addHistogram(rectangle, "Complete image");
    }


    /**
     *  Plot the Region-of-Interest histogram for a rectangular region.
     */
    private void plotRoiRectangle (Roi roi) {
        Rectangle rectangle = roi.getPolygon().getBounds();
        addHistogram(rectangle, "Area of interest");
    }


    /**
     *  Plot the histograms for a Region Of Interest consisting of a number
     *  of independent points. This differs from the regional ROI's, in that
     *  each point is plotted as a separate histogram.
     */
    private void plotRoiPoints (Roi roi) {
        Polygon pg = roi.getPolygon();
        for (int i = 0; i < pg.npoints; i++) {
            Rectangle rectangle = getRectangle(pg.xpoints[i], pg.ypoints[i]);
            addHistogram(rectangle, "Point " + (i + 1));
        }
    }


    /**
     *  Get the  four corners of a rectangle surrounding a central points.
     */
    private Rectangle getRectangle (int x, int y) {
        Rectangle result = new Rectangle();
        result.setRect(x - 10, y - 10, 20, 20);
        return result;
    }
}
