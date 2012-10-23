/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.explorer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import org.dj.explorer.api.ExplorerComponent;
import org.dj.explorer.api.ExplorerController;
import org.dj.explorer.api.View;
import org.dj.node.api.DJNode;
import org.openide.explorer.ExplorerManager;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class ExplorerPanel extends javax.swing.JPanel implements ExplorerManager.Provider, Lookup.Provider {

    /**
     * Creates new form ExplorerPanel
     */
    //private ExplorerControllerListener l;
    
    private double splitPaneRatio = 0.3;
    private Map<ExplorerComponent, JComponent[]> componentMap;

    public ExplorerPanel(ExplorerController eC) {

        initComponents();

        this.eC = eC;
        

        initialiseComponents();
        updateRoot();
        
        load();
        updateView();
        
        this.eC.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                
                load();
                
                if(pce.getPropertyName().equals(ExplorerController.PROP_VIEW)){
                    
                    updateView();
                }else if(pce.getPropertyName().equals(ExplorerController.PROP_ROOT)){
                    
                    updateRoot();
                }else if(pce.getPropertyName().equals(ExplorerController.PROP_EXPLORER_COMPONENT_VISIBLE)){
                    
                    if(pce.getOldValue().equals(ExplorerComponent.PROPERTIES_WINDOW)){
                        if(pce.getNewValue().equals(true)){
                            
                            jSplitPane1.setDividerLocation(splitPaneRatio);
                            revalidate();
                            
                        }
                        
                    }
                }
                
                
            }
        });


        //this.eC.addPropertyChangeListener(l);
    }
    
    private void updateView(){
        
        this.explorerHolder1.setExplorerComponent(this.eC.getView().getComponent());
        revalidate();
    }
    
    
    public JComponent getComponent(ExplorerComponent c){
        
        JComponent j = this.componentMap.get(c)[0];
        
        if(j.equals(this.explorerHolder1)){
            
            j=explorerHolder1.getExplorerComponent();
        }
        
        return j;
    }

    private boolean isSynchronized() {

        boolean tf = true;

        for (ExplorerComponent c : ExplorerComponent.values()) {
            tf = tf && (this.isEnabled(c) == this.eC.isComponentEnabled(c));
            tf = tf && (this.isVisible(c) == this.eC.isComponentVisible(c));

        }

        tf = tf && this.viewCombo.getSelectedItem().equals(this.eC.getView());
        tf = tf && (this.propertiesToggle.isSelected() == this.eC.isComponentVisible(ExplorerComponent.PROPERTIES_WINDOW));

        return tf;
    }
    
    private boolean isVisible(ExplorerComponent c){
        
        boolean tf = true;
        
        for(JComponent j:this.componentMap.get(c)){
            
            tf=tf&&j.isVisible();
            
        }
        
        return tf;
    }
    

    
    private boolean isEnabled(ExplorerComponent c){
        boolean tf = true;
        
        for(JComponent j:this.componentMap.get(c)){
            
            tf=tf&&j.isEnabled();
            
        }
        
        return tf;
        
    }

    public void load() {

        if (!isSynchronized()) {

            for (ExplorerComponent c : ExplorerComponent.values()) {
                this.setComponentEnabled(c, this.eC.isComponentEnabled(c));
                this.setComponentVisible(c, this.eC.isComponentVisible(c));
            }

            this.viewCombo.setSelectedItem(this.eC.getView());
            this.propertiesToggle.setSelected(this.eC.isComponentVisible(ExplorerComponent.PROPERTIES_WINDOW));

            revalidate();

        }


    }

    public boolean valid() {

        return true;
    }

    public void store() {

        if (!this.isSynchronized()) {
            for (ExplorerComponent c : ExplorerComponent.values()) {
                this.eC.setComponentEnabled(c, isEnabled(c));
                this.eC.setComponentVisible(c, isVisible(c));
                
            }

            this.eC.setView((View) this.viewCombo.getSelectedItem());
            this.eC.setComponentVisible(ExplorerComponent.PROPERTIES_WINDOW, this.propertiesToggle.isSelected());

        }




    }

    void setComponentEnabled(ExplorerComponent c, boolean e) {

        for(JComponent j:this.componentMap.get(c)){

            j.setEnabled(e);
        }

    }

    void setComponentVisible(ExplorerComponent c, boolean v) {

        for(JComponent j: this.componentMap.get(c)){
            
            j.setVisible(v);
            
        }


    }



    void updateRoot() {

        getExplorerManager().setRootContext(new DJNode(eC.getRoot()));

    }
    private final ExplorerController eC;

    private void initialiseComponents() {


        this.componentMap = new EnumMap<ExplorerComponent, JComponent[]>(ExplorerComponent.class);
        this.componentMap.put(ExplorerComponent.ADD_BUTTON, new JComponent[]{this.addButton});
        this.componentMap.put(ExplorerComponent.REMOVE_BUTTON, new JComponent[]{this.deleteButton});
        this.componentMap.put(ExplorerComponent.PROPERTIES_WINDOW, new JComponent[]{this.propertiesView});
        this.componentMap.put(ExplorerComponent.VIEW_PICKER, new JComponent[]{this.viewCombo,viewSep});
        this.componentMap.put(ExplorerComponent.PROPERTIES_TOGGLE, new JComponent[]{this.propertiesToggle,propSep});
        this.componentMap.put(ExplorerComponent.EXPLORER_WINDOW, new JComponent[]{this.explorerHolder1});

        this.viewCombo.setModel(new DefaultComboBoxModel(View.getValues().toArray()));
        this.explorerHolder1.initExplorer(eC.getRoot());


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar2 = new javax.swing.JToolBar();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        viewSep = new javax.swing.JToolBar.Separator();
        viewCombo = new javax.swing.JComboBox();
        propSep = new javax.swing.JToolBar.Separator();
        propertiesToggle = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jSplitPane1 = new javax.swing.JSplitPane();
        explorerHolder1 = new org.dj.explorer.ExplorerHolder();
        propertiesView = new org.openide.explorer.propertysheet.PropertySheetView();

        jToolBar2.setRollover(true);

        addButton.setText(org.openide.util.NbBundle.getMessage(ExplorerPanel.class, "ExplorerPanel.addButton.text_1")); // NOI18N
        addButton.setFocusable(false);
        addButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jToolBar2.add(addButton);

        deleteButton.setText(org.openide.util.NbBundle.getMessage(ExplorerPanel.class, "ExplorerPanel.deleteButton.text_1")); // NOI18N
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jToolBar2.add(deleteButton);
        jToolBar2.add(viewSep);

        viewCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        viewCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewComboActionPerformed(evt);
            }
        });
        jToolBar2.add(viewCombo);
        jToolBar2.add(propSep);

        propertiesToggle.setText(org.openide.util.NbBundle.getMessage(ExplorerPanel.class, "ExplorerPanel.propertiesToggle.text_1")); // NOI18N
        propertiesToggle.setFocusable(false);
        propertiesToggle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        propertiesToggle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        propertiesToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                propertiesToggleActionPerformed(evt);
            }
        });
        jToolBar2.add(propertiesToggle);
        jToolBar2.add(jSeparator3);

        jSplitPane1.setResizeWeight(1.0);

        javax.swing.GroupLayout explorerHolder1Layout = new javax.swing.GroupLayout(explorerHolder1);
        explorerHolder1.setLayout(explorerHolder1Layout);
        explorerHolder1Layout.setHorizontalGroup(
            explorerHolder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        explorerHolder1Layout.setVerticalGroup(
            explorerHolder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 270, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(explorerHolder1);
        jSplitPane1.setRightComponent(propertiesView);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void propertiesToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_propertiesToggleActionPerformed
        // TODO add your handling code here:
        this.eC.setComponentVisible(ExplorerComponent.PROPERTIES_WINDOW, this.propertiesToggle.isSelected());

    }//GEN-LAST:event_propertiesToggleActionPerformed

    private void viewComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewComboActionPerformed
        // TODO add your handling code here:
        this.eC.setView((View) this.viewCombo.getSelectedItem());
    }//GEN-LAST:event_viewComboActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        this.eC.add();
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        this.eC.remove();
    }//GEN-LAST:event_deleteButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private org.dj.explorer.ExplorerHolder explorerHolder1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar.Separator propSep;
    private javax.swing.JToggleButton propertiesToggle;
    private org.openide.explorer.propertysheet.PropertySheetView propertiesView;
    private javax.swing.JComboBox viewCombo;
    private javax.swing.JToolBar.Separator viewSep;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return this.explorerHolder1.getExplorerManager();
    }

    @Override
    public Lookup getLookup() {
        return this.explorerHolder1.getLookup();
    }
}
