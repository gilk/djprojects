/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javolution.util.FastSet;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.remote.api.HostInfo;
import org.lblrtm.host.api.LBLRTMHostInfo;
import org.lblrtm.host.api.LBLRTMHostManager;
import org.lblrtm.task.api.LBLRTMParallelTask;
import org.lblrtm.task.editor.HostSelectorCombo.HostSelectorModel.HostInfoAdaptor;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class HostSelectorCombo extends JComboBox {

    private static final LBLRTMHostManager mgr = Lookup.getDefault().lookup(LBLRTMHostManager.class);
    private final LBLRTMParallelTask task;

    void setSelectedHosts() {
        Set<LBLRTMHostInfo> hosts = (Set<LBLRTMHostInfo>) task.getHosts();
        if(hosts!=null){
            
            HostInfoAdaptor hIA = (HostInfoAdaptor) getSelectedItem();
            hosts.clear();
            
            if(hIA!=null){
                
                hosts.addAll(hIA.getSelectedHosts());
            } 
        }

    }

    public HostSelectorCombo(LBLRTMParallelTask t) {

        this.task = t;
        this.setModel(new HostSelectorModel());
        setSelectedHosts();
        
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                setSelectedHosts();
            }
        });
    }

    public class HostSelectorModel extends DefaultComboBoxModel implements PropertyChangeListener {

        public static final String AUTO = "Auto";

        public HostSelectorModel() {

            updateElements();
            
            mgr.addPropertyChangeListener(DJNodeObject.PROP_CHILDREN, this);


        }

        private void updateElements() {

            this.removeAllElements();

            for (LBLRTMHostInfo hI : mgr.getHosts()) {

                this.addElement(new HostSelectorModel.HostInfoAdaptor(hI));
            }

            this.addElement(new HostSelectorModel.HostInfoAdaptor(null));

        }

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            updateElements();
        }

        class HostInfoAdaptor {

            private final Set<LBLRTMHostInfo> hIs= new FastSet<LBLRTMHostInfo>();

            public HostInfoAdaptor(LBLRTMHostInfo hI) {


                if (hI == null) {


                    hIs.addAll(mgr.getHosts());
                } else {

                    hIs.add(hI);
                }
            }

            public Set<LBLRTMHostInfo> getSelectedHosts() {
                return hIs;
            }

            @Override
            public String toString() {

                if (hIs.size() == 1) {

                    return hIs.iterator().next().getName();
                }

                return "Auto";
            }
        }
    }
}
