package se.karolinska.corticostriatal.calciumImaging;

import javax.swing.JLabel;
import javax.swing.WindowConstants;
import org.jfree.chart.ChartPanel;


/**
 *  GU Interface for the Calcium Imaging plug-in
 *
 *  @author Matthijs
 */
public class ImagingDialog extends javax.swing.JFrame {

    public CalciumImaging parent;


    /** Creates new form ImagingDialog */
    public ImagingDialog (CalciumImaging parent) {
        initComponents();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.parent = parent;
        this.setTitle("Calcium Imaging");
    }


    public void setPlugin (CalciumImaging newParent) {
        this.parent = newParent;
    }


    public void addImage (JLabel target, CalciumImage image) {
        ((ImageLabel) target).addImage(image);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectDisplayMode = new javax.swing.ButtonGroup();
        startButton = new javax.swing.JButton();
        snapshotSplitPane = new javax.swing.JSplitPane();
        preSpikeSnapshotPanel = new javax.swing.JPanel();
        preSpikeLabel = new ImageLabel();
        postSpikeSnapshotPanel = new javax.swing.JPanel();
        postSpikeLabel = new ImageLabel();
        histogramPanel = //new ChartPanel(null, true);
        new ChartPanel(null, 100, 150, 100, 150, 10000, 150, true, true, false, false, false, true);
        controlContainer = new javax.swing.JPanel();
        continuousCheckbox = new javax.swing.JCheckBox();
        storeAllRawCheckbox = new javax.swing.JCheckBox();
        repetitionsLabel = new javax.swing.JLabel();
        repetitionsInput = new javax.swing.JTextField();
        intervalInput = new javax.swing.JTextField();
        intervalLabel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        LPTChannelLabelPulse = new javax.swing.JLabel();
        LPTChannelInput = new javax.swing.JTextField();
        LPTChannelInputShutter = new javax.swing.JTextField();
        LPTChannelLabelShutter = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        roiHistogramButton = new javax.swing.JButton();
        menu = new javax.swing.JMenuBar();
        File = new javax.swing.JMenu();
        saveRawButton = new javax.swing.JMenuItem();
        saveDiffButton = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        exitButton = new javax.swing.JMenuItem();
        menuShow = new javax.swing.JMenu();
        showRawRadio = new javax.swing.JRadioButtonMenuItem();
        showDifferenceRadio = new javax.swing.JRadioButtonMenuItem();
        showBothRadio = new javax.swing.JRadioButtonMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        startButton.setBackground(new java.awt.Color(-1,true));
        startButton.setFont(startButton.getFont().deriveFont(startButton.getFont().getSize()+1f));
        startButton.setText("Start Acquisition");
        startButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        snapshotSplitPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        snapshotSplitPane.setDividerLocation(80);
        snapshotSplitPane.setDividerSize(1);
        snapshotSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        snapshotSplitPane.setResizeWeight(0.333);
        snapshotSplitPane.setContinuousLayout(true);
        snapshotSplitPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                snapshotSplitPaneComponentResized(evt);
            }
        });

        preSpikeSnapshotPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                preSpikeSnapshotPanelMouseClicked(evt);
            }
        });

        preSpikeLabel.setBackground(new java.awt.Color(-1,true));
        preSpikeLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        preSpikeLabel.setDoubleBuffered(true);
        preSpikeLabel.setOpaque(true);
        preSpikeLabel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                preSpikeLabelComponentResized(evt);
            }
        });

        javax.swing.GroupLayout preSpikeSnapshotPanelLayout = new javax.swing.GroupLayout(preSpikeSnapshotPanel);
        preSpikeSnapshotPanel.setLayout(preSpikeSnapshotPanelLayout);
        preSpikeSnapshotPanelLayout.setHorizontalGroup(
            preSpikeSnapshotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 433, Short.MAX_VALUE)
            .addGroup(preSpikeSnapshotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(preSpikeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))
        );
        preSpikeSnapshotPanelLayout.setVerticalGroup(
            preSpikeSnapshotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
            .addGroup(preSpikeSnapshotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(preSpikeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE))
        );

        snapshotSplitPane.setTopComponent(preSpikeSnapshotPanel);

        postSpikeSnapshotPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                postSpikeSnapshotPanelMouseClicked(evt);
            }
        });

        postSpikeLabel.setBackground(new java.awt.Color(-1,true));
        postSpikeLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        postSpikeLabel.setOpaque(true);
        postSpikeLabel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                postSpikeLabelComponentResized(evt);
            }
        });

        javax.swing.GroupLayout postSpikeSnapshotPanelLayout = new javax.swing.GroupLayout(postSpikeSnapshotPanel);
        postSpikeSnapshotPanel.setLayout(postSpikeSnapshotPanelLayout);
        postSpikeSnapshotPanelLayout.setHorizontalGroup(
            postSpikeSnapshotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 433, Short.MAX_VALUE)
            .addGroup(postSpikeSnapshotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(postSpikeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))
        );
        postSpikeSnapshotPanelLayout.setVerticalGroup(
            postSpikeSnapshotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 179, Short.MAX_VALUE)
            .addGroup(postSpikeSnapshotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(postSpikeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
        );

        snapshotSplitPane.setRightComponent(postSpikeSnapshotPanel);
        postSpikeSnapshotPanel.getAccessibleContext().setAccessibleDescription("");

        histogramPanel.setBackground(new java.awt.Color(-1,true));
        histogramPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        histogramPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                histogramPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout histogramPanelLayout = new javax.swing.GroupLayout(histogramPanel);
        histogramPanel.setLayout(histogramPanelLayout);
        histogramPanelLayout.setHorizontalGroup(
            histogramPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        histogramPanelLayout.setVerticalGroup(
            histogramPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 179, Short.MAX_VALUE)
        );

        controlContainer.setBackground(new java.awt.Color(-1,true));
        controlContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        continuousCheckbox.setBackground(new java.awt.Color(-1,true));
        continuousCheckbox.setText("Continuous acquisition");
        continuousCheckbox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                continuousCheckboxStateChanged(evt);
            }
        });

        storeAllRawCheckbox.setBackground(new java.awt.Color(-1,true));
        storeAllRawCheckbox.setText("Store all raw images");
        storeAllRawCheckbox.setEnabled(false);
        storeAllRawCheckbox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                storeAllRawCheckboxStateChanged(evt);
            }
        });
        storeAllRawCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeAllRawCheckboxActionPerformed(evt);
            }
        });

        repetitionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        repetitionsLabel.setText("Number of repetitions");
        repetitionsLabel.setEnabled(false);

        repetitionsInput.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        repetitionsInput.setToolTipText("Repetitions");
        repetitionsInput.setEnabled(false);

        intervalInput.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        intervalInput.setText("5000");
        intervalInput.setToolTipText("Repetitions");
        intervalInput.setEnabled(false);

        intervalLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        intervalLabel.setText("Sequence interval (ms)");
        intervalLabel.setEnabled(false);

        LPTChannelLabelPulse.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LPTChannelLabelPulse.setText("Channel Pulse");

        LPTChannelInput.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        LPTChannelInput.setText("6");
        LPTChannelInput.setToolTipText("Repetitions");
        LPTChannelInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LPTChannelInputActionPerformed(evt);
            }
        });

        LPTChannelInputShutter.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        LPTChannelInputShutter.setText("3");
        LPTChannelInputShutter.setToolTipText("Repetitions");
        LPTChannelInputShutter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LPTChannelInputShutterActionPerformed(evt);
            }
        });

        LPTChannelLabelShutter.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LPTChannelLabelShutter.setText("Channel Shutter");

        javax.swing.GroupLayout controlContainerLayout = new javax.swing.GroupLayout(controlContainer);
        controlContainer.setLayout(controlContainerLayout);
        controlContainerLayout.setHorizontalGroup(
            controlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(controlContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlContainerLayout.createSequentialGroup()
                        .addComponent(repetitionsInput, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(repetitionsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlContainerLayout.createSequentialGroup()
                        .addComponent(intervalInput, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(intervalLabel))
                    .addComponent(continuousCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(storeAllRawCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(controlContainerLayout.createSequentialGroup()
                        .addComponent(LPTChannelInput, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LPTChannelLabelPulse, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlContainerLayout.createSequentialGroup()
                        .addComponent(LPTChannelInputShutter, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LPTChannelLabelShutter, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        controlContainerLayout.setVerticalGroup(
            controlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(continuousCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(storeAllRawCheckbox)
                .addGap(15, 15, 15)
                .addGroup(controlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(repetitionsLabel)
                    .addComponent(repetitionsInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(intervalInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(intervalLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LPTChannelLabelPulse)
                    .addComponent(LPTChannelInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(controlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LPTChannelLabelShutter)
                    .addComponent(LPTChannelInputShutter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        roiHistogramButton.setText("Plot ROI Histogram");
        roiHistogramButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roiHistogramButtonActionPerformed(evt);
            }
        });

        menu.setBackground(new java.awt.Color(-4144960,true));

        File.setBackground(new java.awt.Color(-4144960,true));
        File.setText("File");

        saveRawButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        saveRawButton.setDoubleBuffered(true);
        saveRawButton.setLabel("Save Raw Images");
        saveRawButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveRawButtonActionPerformed(evt);
            }
        });
        File.add(saveRawButton);
        saveRawButton.getAccessibleContext().setAccessibleDescription("Save all the raw images in a single TIFF file");

        saveDiffButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        saveDiffButton.setActionCommand("Save Differences");
        saveDiffButton.setDoubleBuffered(true);
        saveDiffButton.setLabel("Save Differences");
        saveDiffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDiffButtonActionPerformed(evt);
            }
        });
        File.add(saveDiffButton);
        saveDiffButton.getAccessibleContext().setAccessibleDescription("");

        File.add(jSeparator2);

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        File.add(exitButton);

        menu.add(File);

        menuShow.setBackground(new java.awt.Color(-4144960,true));
        menuShow.setText("Show");

        selectDisplayMode.add(showRawRadio);
        showRawRadio.setText("Show Raw images");
        showRawRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDisplayType(evt);
            }
        });
        menuShow.add(showRawRadio);

        selectDisplayMode.add(showDifferenceRadio);
        showDifferenceRadio.setSelected(true);
        showDifferenceRadio.setText("Show Difference");
        showDifferenceRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDisplayType(evt);
            }
        });
        menuShow.add(showDifferenceRadio);

        selectDisplayMode.add(showBothRadio);
        showBothRadio.setText("Show both");
        showBothRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDisplayType(evt);
            }
        });
        menuShow.add(showBothRadio);

        menu.add(menuShow);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(controlContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(roiHistogramButton)
                            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(histogramPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(snapshotSplitPane))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(controlContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(snapshotSplitPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(histogramPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(roiHistogramButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        parent.toggleSequence();
    }//GEN-LAST:event_startButtonActionPerformed

    private void snapshotSplitPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_snapshotSplitPaneComponentResized
        snapshotSplitPane.setDividerLocation(0.333);
    }//GEN-LAST:event_snapshotSplitPaneComponentResized

    private void postSpikeLabelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_postSpikeLabelComponentResized
        ((ImageLabel) postSpikeLabel).updateIcon(true);
    }//GEN-LAST:event_postSpikeLabelComponentResized

    private void preSpikeLabelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_preSpikeLabelComponentResized
        ((ImageLabel) preSpikeLabel).updateIcon(true);
    }//GEN-LAST:event_preSpikeLabelComponentResized

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        parent.dialog = null;
    }//GEN-LAST:event_formWindowClosed

    private void saveRawButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveRawButtonActionPerformed
        new FileSaver(this, FileSaver.SAVE_RAW);
}//GEN-LAST:event_saveRawButtonActionPerformed

    private void saveDiffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDiffButtonActionPerformed
        new FileSaver(this, FileSaver.SAVE_DIFFERENCE);
    }//GEN-LAST:event_saveDiffButtonActionPerformed

    private void continuousCheckboxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_continuousCheckboxStateChanged
        if (continuousCheckbox.isSelected()) {
            repetitionsInput.setEnabled(true);
            repetitionsLabel.setEnabled(true);
            intervalInput.setEnabled(true);
            intervalLabel.setEnabled(true);
            storeAllRawCheckbox.setEnabled(true);
        } else {
            repetitionsInput.setEnabled(false);
            repetitionsLabel.setEnabled(false);
            intervalInput.setEnabled(false);
            intervalLabel.setEnabled(false);
            storeAllRawCheckbox.setEnabled(false);
        }
    }//GEN-LAST:event_continuousCheckboxStateChanged

    private void postSpikeSnapshotPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_postSpikeSnapshotPanelMouseClicked
        ((ImageLabel) postSpikeLabel).showImageAt(evt.getX(), evt.getY());
    }//GEN-LAST:event_postSpikeSnapshotPanelMouseClicked

    private void preSpikeSnapshotPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_preSpikeSnapshotPanelMouseClicked
        ((ImageLabel) preSpikeLabel).showImageAt(evt.getX(), evt.getY());
    }//GEN-LAST:event_preSpikeSnapshotPanelMouseClicked

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void roiHistogramButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roiHistogramButtonActionPerformed
        Markings.plotRoiHistogram();
    }//GEN-LAST:event_roiHistogramButtonActionPerformed

    private void histogramPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_histogramPanelMouseClicked
        Markings.getInstance().showGraph();
    }//GEN-LAST:event_histogramPanelMouseClicked

    private void selectDisplayType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDisplayType
        selectDisplayType();
    }//GEN-LAST:event_selectDisplayType

    private void LPTChannelInputShutterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LPTChannelInputShutterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LPTChannelInputShutterActionPerformed

    private void storeAllRawCheckboxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_storeAllRawCheckboxStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_storeAllRawCheckboxStateChanged

    private void storeAllRawCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeAllRawCheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_storeAllRawCheckboxActionPerformed

    private void LPTChannelInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LPTChannelInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LPTChannelInputActionPerformed

    private void selectDisplayType () {
        if (showRawRadio.isSelected())
            CalciumImaging.getProcessor().showPost();
        if (showDifferenceRadio.isSelected())
            CalciumImaging.getProcessor().showDifferences();
        if (showBothRadio.isSelected())
            new Overlay();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu File;
    public javax.swing.JTextField LPTChannelInput;
    public javax.swing.JTextField LPTChannelInputShutter;
    private javax.swing.JLabel LPTChannelLabelPulse;
    private javax.swing.JLabel LPTChannelLabelShutter;
    public javax.swing.JCheckBox continuousCheckbox;
    private javax.swing.JPanel controlContainer;
    private javax.swing.JMenuItem exitButton;
    public javax.swing.JPanel histogramPanel;
    public javax.swing.JTextField intervalInput;
    private javax.swing.JLabel intervalLabel;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenu menuShow;
    public javax.swing.JLabel postSpikeLabel;
    private javax.swing.JPanel postSpikeSnapshotPanel;
    public javax.swing.JLabel preSpikeLabel;
    private javax.swing.JPanel preSpikeSnapshotPanel;
    public javax.swing.JProgressBar progressBar;
    public javax.swing.JTextField repetitionsInput;
    private javax.swing.JLabel repetitionsLabel;
    private javax.swing.JButton roiHistogramButton;
    private javax.swing.JMenuItem saveDiffButton;
    private javax.swing.JMenuItem saveRawButton;
    private javax.swing.ButtonGroup selectDisplayMode;
    public javax.swing.JRadioButtonMenuItem showBothRadio;
    public javax.swing.JRadioButtonMenuItem showDifferenceRadio;
    public javax.swing.JRadioButtonMenuItem showRawRadio;
    private javax.swing.JSplitPane snapshotSplitPane;
    public javax.swing.JButton startButton;
    public javax.swing.JCheckBox storeAllRawCheckbox;
    // End of variables declaration//GEN-END:variables

}
