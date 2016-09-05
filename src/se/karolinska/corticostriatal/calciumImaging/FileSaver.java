package se.karolinska.corticostriatal.calciumImaging;

import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import org.json.JSONObject;
import org.micromanager.acquisition.TaggedImageStorageMultipageTiff;
import org.micromanager.utils.ReportingUtils;

/**
 *  Saving to File
 *  <br><br>
 *  This handles saving the acquired images to file. All images can be saved to
 *  a single TIFF image file, so this opens a storage solution for such a file 
 *  and proceeds to add each image in the acquired sequence to it.
 *  <br>
 *  @author Matthijs
 */
public class FileSaver {

    private         JFileChooser                chooser;
    private         File                        file;
    private static  File                        path;
    private TaggedImageStorageMultipageTiff     storage;
    private LinkedList<TreeMap>                 images;
    private LinkedList<CalciumImage>            differences;
    JSONObject                                  summary;

    public final static int SAVE_RAW            = 0;
    public final static int SAVE_DIFFERENCE     = 1;


    /**
     *  Create a new File Saver.
     *
     *  @param parent   Reference to the GUI element that spawned the save dialogue.
     *  @param  mode    Toggle between saving raw images (mode = 0) or saving the f/Df images (mode = 1).
     */
    public FileSaver (ImagingDialog parent, int mode) {
        chooser     = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (path != null)
            chooser.setCurrentDirectory(path);

        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal != JFileChooser.APPROVE_OPTION)
            return;

        path        = chooser.getSelectedFile();
        images      = CalciumImaging.getProcessor().getImages();
        differences = CalciumImaging.getProcessor().getDifferences();

        try {
            if (mode == SAVE_RAW)
                saveRawImages();
            if (mode == SAVE_DIFFERENCE)
                saveDifferenceImages();
        } catch (Exception e) {
            ReportingUtils.logError(e);
        }
        closeFileStorage();
    }


    /**
     *  Save the raw images we collected.
     */
    private void saveRawImages () throws Exception {
        openFileStorage("raw", 24, images.size());
        for (TreeMap sequence : images) {
            LinkedList<CalciumImage> preImages = (LinkedList<CalciumImage>) sequence.get("pre");
            for (CalciumImage image : preImages)
                storage.putImage(image.asTaggedImage());
            LinkedList<CalciumImage> postImages = (LinkedList<CalciumImage>) sequence.get("post");
            for (CalciumImage image : postImages)
                storage.putImage(image.asTaggedImage());
        }
    }


    /**
     *  Save the difference images, as well as the mean image.
     *
     *  Difference images by themselves contain no tags, so we copy the tags
     *  from the corresponding (original) post-spike image.
     */
    private void saveDifferenceImages () throws Exception {
        openFileStorage("difference", differences.size() + 1, 1);
        storeMean(images.getLast());
        for (int i = 0; i < differences.size(); i++)
            storage.putImage(differences.get(i).asTaggedImage());
    }


    /**
     *  Store the mean image for this sequence. Since mean images are computed
     *  from pre-images, they do not by themselves contain image tags, which are
     *  however required for the correct functioning of the TIFF store. Thus, we
     *  copy the tags from the first pre-sequence image.
     */
    private void storeMean (TreeMap sequence) throws Exception {
        ShortProcessor processor = (ShortProcessor) sequence.get("mean");
        CalciumImage mean = CalciumImage.fromImage(processor.getBufferedImage());
        mean.tags  = ((LinkedList<CalciumImage>) sequence.get("pre")).getFirst().tags;
        storage.putImage(mean.asTaggedImage());
    }


    /**
     *  Open a file storage object for writing. Use the specified type as
     *  indicator of whether this is a raw sequence or a difference sequence.
     */
    private void openFileStorage (String type, int frames, int slices) throws Exception {
        loadSummary(frames, slices);
        file = new File(path.getAbsolutePath() + "/" + getDateTime() + "_" + type);
        file.mkdir();
        storage = new TaggedImageStorageMultipageTiff(file.getAbsolutePath(), true, summary, true, false, true);
    }


    /**
     *  Close the file storage. This prevents it from being written to, and
     *  enables other applications to open the storage file.
     */
    private void closeFileStorage () {
        System.out.println("Closing file storage.");
        storage.writeDisplaySettings();
        storage.finished();
        storage.close();
    }


    /**
     *  Load the JSON summary data for this sequence.
     *
     *  @param frames   The number of frames in this TIFF image per sequence.
     *  @throws Exception
     */
    private void loadSummary (int frames, int slices) throws Exception {
        summary = new JSONObject();
        summary.put("Prefix",       "CI_raw_");
        summary.put("TimeFirst",    true);
        summary.put("SlicesFirst",  false);
        summary.put("Slices",       slices);
        summary.put("Channels",     1);
        summary.put("Frames",       frames);
        summary.put("PixelType",    "GRAY16");
        summary.put("Width",        ((ImageProcessor) images.getLast().get("mean")).getWidth());
        summary.put("Height",       ((ImageProcessor) images.getLast().get("mean")).getHeight());
        summary.put("Positions",    1);
        summary.put("ChColors",     new org.json.JSONArray("[1]"));
        summary.put("ChNames",      new org.json.JSONArray("[\"CA\"]"));
        summary.put("ChMins",       new org.json.JSONArray("[0]"));
        summary.put("ChMaxes",      new org.json.JSONArray("[65535]"));
    }


    /**
     *  Return a date and time string that can be used in a file-name.
     */
    private String getDateTime () {
        CalciumImage first      = ((LinkedList<CalciumImage>) images.getLast().get("pre")).get(0);
        DateFormat dateFormat   = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss");
        Date date               = first.getStartDate();
        return dateFormat.format(date);
    }
}