/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.lblrtm.lblrtmdata.api.AtmosphericModel;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;

/**
 *
 * @author djabry
 */
public class AtmosphereTypeCombo extends JComboBox{
    
    private final DJTAPE5 t5;
    
    AtmosphereTypeCombo(DJTAPE5 t){
        
        this.t5=t;
        
        this.setModel(new DefaultComboBoxModel(AtmosphericModel.values()));
        
        this.setSelectedItem(t5.getAtmosphericModel());
        
        this.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
               t5.setAtmosphericModel((AtmosphericModel)getSelectedItem());
            }
        });
        
    }
    
    
}
