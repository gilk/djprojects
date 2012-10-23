/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.editor;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.filechooser.FileFilter;
import javolution.util.FastMap;
import org.dj.editor.api.EditorController;
import org.dj.editor.api.EditorService;
import org.dj.executor.api.Executor;
import org.dj.explorer.api.ExplorerComponent;
import org.dj.explorer.api.ExplorerController;
import org.dj.explorer.api.View;
import org.dj.parallel.api.GCompletedTask;
import org.dj.plot.api.PlotController;
import org.dj.plot.api.PlotService;
import org.dj.service.api.Service;
import org.lblrtm.host.api.LBLRTMHostInfo;
import org.lblrtm.lblrtmdata.api.AtmosphericModel;
import org.lblrtm.lblrtmdata.api.Observer;
import org.lblrtm.lblrtmfilewriter.api.*;
import org.lblrtm.profile.api.DJProfile;
import org.lblrtm.profile.api.ProfileEditor;
import org.lblrtm.task.api.*;
import org.netbeans.api.options.OptionsDisplayer;
import org.openide.explorer.view.OutlineView;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class TaskEditorPanel extends javax.swing.JPanel {

    /**
     * Creates new form TaskEditor
     */
    
    private final LBLRTMParallelTask task;
    private DJRangeSet rS;
    private DoubleListManager customLevels;
    private static Map<String,Component> componentMap;
    private static final EditorService eS = Lookup.getDefault().lookup(EditorService.class);
    private static final PlotService pS = Lookup.getDefault().lookup(PlotService.class);
    private static final Executor ex = Lookup.getDefault().lookup(Executor.class);
    private static final CalculationTemplateService cTS = Lookup.getDefault().lookup(CalculationTemplateService.class);
    private static final ScannerTemplateService sTS = Lookup.getDefault().lookup(ScannerTemplateService.class);

    
    public TaskEditorPanel(LBLRTMParallelTask t){
        
        this.task=t;
       
        
        Observer observer = task.getTask().getTAPE5().getObserver();
        observer.getProperty(Observer.PROP_ALTITUDE);
        
        initComponents();
        
        this.calcTemplateCombo.setSelectedItem(task.getTask().getTAPE5().getCalculationTemplate());

        this.profilePanel.add(new ProfileEditor((DJProfile)t.getTask().getTAPE5().getProfile()));
        updateProfileEnabled();
       
        ((LBLRTMParallelTaskAbstr)task).pullRanges();
        this.rS = new DJRangeSet(task.getWavelengthRanges());

        this.customLevels=new DoubleListManager(task.getTask().getTAPE5().getCalculationTemplate().getCustomLayerBoundaries());
        ExplorerController cSEC = (ExplorerController)eS.createObject(customLevels);
        cSEC.setView(View.OUTLINE_VIEW);
        OutlineView oV = (OutlineView)cSEC.getComponent(ExplorerComponent.EXPLORER_WINDOW);
        oV.setPropertyColumns(DoubleEntry.PROP_DOUBLE_VALUE, "Boundary");
        oV.getOutline().setRootVisible(false);
        oV.getOutline().removeColumn(oV.getOutline().getColumnModel().getColumn(0));
        cSEC.setComponentVisible(ExplorerComponent.PROPERTIES_WINDOW, false);
        cSEC.setComponentVisible(ExplorerComponent.VIEW_PICKER, false);
        cSEC.setComponentVisible(ExplorerComponent.PROPERTIES_TOGGLE, false);
        
        
        customLevels.pullValuesFromList();
        
        this.layerBoundries.add(cSEC.getComponent());
        
        ExplorerController eC = (ExplorerController)eS.createObject(rS);
        
        
        this.rangesPanel.add(eC.getComponent());
        this.boundryPanel.add(new SurfaceEditorPanel(task.getTask().getTAPE5().getSurface()));
        
        eC.setView(View.OUTLINE_VIEW);
        OutlineView v = (OutlineView)eC.getComponent(ExplorerComponent.EXPLORER_WINDOW);

        v.setPropertyColumns(DJRangeObject.PROP_START, "<html>v1 (cm<sup>-1</sup>)</html>",DJRangeObject.PROP_END, "<html>v2 (cm<sup>-1</sup>)</html>");
        v.getOutline().setRootVisible(false);
        v.getOutline().removeColumn(v.getOutline().getColumnModel().getColumn(0));
        
        eC.setComponentVisible(ExplorerComponent.PROPERTIES_WINDOW, false);
        eC.setComponentVisible(ExplorerComponent.VIEW_PICKER, false);
        eC.setComponentVisible(ExplorerComponent.PROPERTIES_TOGGLE, false);
        
        this.dVLabel.setText("<html>Dv (cm<sup>-1</sup>)</html>");
        
        pullRunTemplateVals();
        this.hostSelectorHolder.add(new HostSelector(this.task));
        

        
    }
    
    private void pullRunTemplateVals(){
        
        DJTAPE5 t5  = this.task.getTask().getTAPE5();
        CalculationTemplate cT=  t5.getCalculationTemplate();
        ScannerTemplate sT = t5.getScannerTemplate();
        
        this.calculationTemplateEditor.removeAll();
        this.scanTemplate.removeAll();
        
        EditorController cTC = eS.createObject(cT);
        EditorController sTC = eS.createObject(sT);
        
        this.calculationTemplateEditor.add(cTC.getComponent());
        this.scanTemplate.add(sTC.getComponent());
        
        this.calcTemplateCombo.setSelectedItem(cTS.getServiceForName(t5, cT.getName()));
        this.scannerCombo.setSelectedItem(sTS.getServiceForName(t5, sT.getName()));

        
    }
    
    public Map<String,Component> getComponentMap(){
        
        if(componentMap==null){
            
            componentMap = new FastMap<String,Component>();
            
        }
        
        return componentMap;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        tabPane = new javax.swing.JTabbedPane();
        parametersPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        dVLabel = new javax.swing.JLabel("<html>Dv (cm<sup>-1</sup></html>");
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        calcTemplateCombo = new javax.swing.JComboBox();
        atmTypeCombo = new AtmosphereTypeCombo(this.task.getTask().getTAPE5());
        hostSelectorHolder = new javax.swing.JPanel();
        obsHeight = new DoubleEditorField(
            task.getTask().getTAPE5().
            getObserver().getProperty(
                Observer.PROP_ALTITUDE));
        endHeight = new DoubleEditorField(
            task.getTask().getTAPE5().
            getProperty(DJTAPE5.PROP_END_HEIGHT));
        angle = new DoubleEditorField(
            task.getTask().getTAPE5().
            getProperty(DJTAPE5.PROP_ANGLE));
        jTextField1 = new DoubleEditorField(this.task.getTask().getTAPE5().getProperty(DJTAPE5.PROP_DV));
        localDir = new javax.swing.JButton();
        calculationTemplateEditor = new javax.swing.JPanel();
        rangesPanel = new javax.swing.JPanel();
        boundryPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        scannerCombo = new javax.swing.JComboBox();
        scanTemplate = new javax.swing.JPanel();
        resultsPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        profilePanel = new javax.swing.JPanel();
        layerBoundries = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        runLBLRTM = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();

        jFormattedTextField1.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jFormattedTextField1.text")); // NOI18N

        jPanel1.setLayout(new java.awt.GridLayout(0, 1, 4, 4));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jLabel1.text")); // NOI18N
        jPanel1.add(jLabel1);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jLabel3.text")); // NOI18N
        jPanel1.add(jLabel3);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jLabel2.text")); // NOI18N
        jPanel1.add(jLabel2);

        jLabel6.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jLabel6.text")); // NOI18N
        jPanel1.add(jLabel6);

        jLabel7.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jLabel7.text")); // NOI18N
        jPanel1.add(jLabel7);

        jLabel8.setText("<html>Angle (<sup>o</sup>)</html>");
        jPanel1.add(jLabel8);

        dVLabel.setText(dVLabel.getText());
        jPanel1.add(dVLabel);

        jLabel5.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jLabel5.text")); // NOI18N
        jPanel1.add(jLabel5);

        jPanel2.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        calcTemplateCombo.setModel(new DefaultComboBoxModel(cTS.getAllServices(task.getTask().getTAPE5()).toArray()));
        calcTemplateCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcTemplateComboActionPerformed(evt);
            }
        });
        jPanel2.add(calcTemplateCombo);

        atmTypeCombo.setModel(atmTypeCombo.getModel());
        atmTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atmTypeComboActionPerformed(evt);
            }
        });
        jPanel2.add(atmTypeCombo);

        hostSelectorHolder.setLayout(new java.awt.GridLayout(1, 1));
        jPanel2.add(hostSelectorHolder);

        obsHeight.setText(obsHeight.getText());
        jPanel2.add(obsHeight);

        endHeight.setText(endHeight.getText());
        endHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endHeightActionPerformed(evt);
            }
        });
        jPanel2.add(endHeight);

        angle.setText(""+task.getTask().getTAPE5().getAngle());
        angle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                angleActionPerformed(evt);
            }
        });
        jPanel2.add(angle);

        jTextField1.setText(""+this.task.getTask().getTAPE5().getDV());
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel2.add(jTextField1);

        localDir.setText(this.task.getTask().getLocalDir().getPath());
        localDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                localDirActionPerformed(evt);
            }
        });
        jPanel2.add(localDir);

        calculationTemplateEditor.setLayout(new java.awt.GridLayout(1, 1));

        javax.swing.GroupLayout parametersPanelLayout = new javax.swing.GroupLayout(parametersPanel);
        parametersPanel.setLayout(parametersPanelLayout);
        parametersPanelLayout.setHorizontalGroup(
            parametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(parametersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calculationTemplateEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                .addContainerGap())
        );
        parametersPanelLayout.setVerticalGroup(
            parametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(parametersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(parametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(parametersPanelLayout.createSequentialGroup()
                        .addGroup(parametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 21, Short.MAX_VALUE))
                    .addComponent(calculationTemplateEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabPane.addTab(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.parametersPanel.TabConstraints.tabTitle"), parametersPanel); // NOI18N

        rangesPanel.setLayout(new java.awt.GridLayout(1, 0));
        tabPane.addTab(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.rangesPanel.TabConstraints.tabTitle"), rangesPanel); // NOI18N

        boundryPanel.setLayout(new java.awt.GridLayout(1, 0));
        tabPane.addTab(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.boundryPanel.TabConstraints.tabTitle"), boundryPanel); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jLabel4.text")); // NOI18N

        scannerCombo.setModel(new DefaultComboBoxModel(sTS.getAllServices(task.getTask().getTAPE5()).toArray()));
        scannerCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scannerComboActionPerformed(evt);
            }
        });

        scanTemplate.setLayout(new java.awt.GridLayout(1, 1));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scannerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 372, Short.MAX_VALUE))
                    .addComponent(scanTemplate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scannerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scanTemplate, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        resultsPanel.setLayout(new java.awt.GridLayout(1, 1));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );

        resultsPanel.add(jPanel4);

        tabPane.addTab(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.resultsPanel.TabConstraints.tabTitle"), resultsPanel); // NOI18N

        profilePanel.setLayout(new java.awt.GridLayout(1, 0));

        layerBoundries.setLayout(new java.awt.GridLayout());

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(layerBoundries, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(profilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(layerBoundries, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                    .addComponent(profilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabPane.addTab(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        jToolBar1.setRollover(true);

        runLBLRTM.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.runLBLRTM.text")); // NOI18N
        runLBLRTM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runLBLRTMActionPerformed(evt);
            }
        });
        jToolBar1.add(runLBLRTM);
        jToolBar1.add(jSeparator1);

        jButton1.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabPane)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPane))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void calcTemplateComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcTemplateComboActionPerformed
        // TODO add your handling code here:
     this.calculationTemplateEditor.removeAll();
     Service<CalculationTemplate,DJTAPE5> s = (Service<CalculationTemplate,DJTAPE5>)this.calcTemplateCombo.getSelectedItem();
     this.calculationTemplateEditor.add(eS.createObject(s.createObject(task.getTask().getTAPE5())).getComponent());
    
    }//GEN-LAST:event_calcTemplateComboActionPerformed

    private void atmTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atmTypeComboActionPerformed
        // TODO add your handling code here:
        updateProfileEnabled();
        
    }//GEN-LAST:event_atmTypeComboActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        File f = new FileChooserBuilder("testDir").showSaveDialog();

        if (f != null) {
            try {
                //TAPE5 t5 = this.createPerturbedTAPE5(0.5);
                DJTAPE5 t = (DJTAPE5)task.getTask().getTAPE5().duplicate();
                t.setRunTemplate(t.getCalculationTemplate());
                t.writeToFile(f);
                
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void localDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_localDirActionPerformed
        // TODO add your handling code here:
        
                File f = new FileChooserBuilder("testDir").showOpenDialog();

        if (f != null && f.isDirectory()) {

           this.task.getTask().setLocalDir(f);
           this.localDir.setText(f.getPath());
           

        }

    }//GEN-LAST:event_localDirActionPerformed

    private void endHeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endHeightActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endHeightActionPerformed

    private void runLBLRTMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runLBLRTMActionPerformed
        // TODO add your handling code here:
        Runnable r = new Runnable(){

            @Override
            public void run() {
                
                LBLRTMParallelTask t = task.duplicate();
                
                try{
                    
                    t.run();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                finally{
                    GCompletedTask<LBLRTMTask, LBLRTMHostInfo, TaskResult> cT = t.getCompletedTask();
                    TaskResult result = cT.getResult();
                    PlotController pC = pS.createObject(result);
                    JComponent plot = pC.getPlot();
                    resultsPanel.removeAll();
                    resultsPanel.add(plot);
                    tabPane.setSelectedComponent(resultsPanel);
                    
                }
                
            }
            
            
        };
        
        ex.execute(r);
        
        
    }//GEN-LAST:event_runLBLRTMActionPerformed

    private void angleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_angleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_angleActionPerformed

    private void scannerComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scannerComboActionPerformed
        // TODO add your handling code here:
        this.scanTemplate.removeAll();
        Service<ScannerTemplate,DJTAPE5> s = (Service<ScannerTemplate,DJTAPE5>)this.scannerCombo.getSelectedItem();
        this.scanTemplate.add(eS.createObject(s.createObject(task.getTask().getTAPE5())).getComponent());
    }//GEN-LAST:event_scannerComboActionPerformed

    private void updateProfileEnabled(){
        
        this.profilePanel.setEnabled(this.task.getTask().getTAPE5().getAtmosphericModel().equals(AtmosphericModel.USER_DEFINED));
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField angle;
    private javax.swing.JComboBox atmTypeCombo;
    private javax.swing.JPanel boundryPanel;
    private javax.swing.JComboBox calcTemplateCombo;
    private javax.swing.JPanel calculationTemplateEditor;
    private javax.swing.JLabel dVLabel;
    private javax.swing.JTextField endHeight;
    private javax.swing.JPanel hostSelectorHolder;
    private javax.swing.JButton jButton1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel layerBoundries;
    private javax.swing.JButton localDir;
    private javax.swing.JTextField obsHeight;
    private javax.swing.JPanel parametersPanel;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JPanel rangesPanel;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JButton runLBLRTM;
    private javax.swing.JPanel scanTemplate;
    private javax.swing.JComboBox scannerCombo;
    private javax.swing.JTabbedPane tabPane;
    // End of variables declaration//GEN-END:variables
}
