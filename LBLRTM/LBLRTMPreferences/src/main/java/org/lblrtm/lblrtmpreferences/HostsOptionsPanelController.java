/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmpreferences;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.dj.editor.api.EditorService;
import org.dj.explorer.api.ExplorerComponent;
import org.dj.explorer.api.ExplorerController;
import org.lblrtm.host.api.LBLRTMHostManager;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.SubRegistration(location = "LBLRTM",
displayName = "#AdvancedOption_DisplayName_Hosts",
keywords = "#AdvancedOption_Keywords_Hosts",
keywordsCategory = "LBLRTM/Hosts")
public final class HostsOptionsPanelController extends OptionsPanelController {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;
    private ExplorerController eC;
    private JComponent p;
    
    
    

    public void update() {
        //getPanel().load();
        changed = false;
    }

    public void applyChanges() {
        //getPanel().store();
        
        changed = false;
    }

    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    public boolean isValid() {
        return true;
    }

    public boolean isChanged() {
        return changed;
    }

    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    public JComponent getComponent(Lookup masterLookup) {
        
        if(this.eC==null){
            
            this.eC = (ExplorerController)Lookup.getDefault().lookup(EditorService.class)
                    .createObject(Lookup.getDefault().lookup(LBLRTMHostManager.class));
            this.eC.setComponentVisible(ExplorerComponent.VIEW_PICKER, false);
            this.p=eC.getComponent();
            
        }
        
        return p;
        
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

//    private HostsPanel getPanel() {
//        if (panel == null) {
//            panel = new HostsPanel(this);
//        }
//        return panel;
//    }

    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
