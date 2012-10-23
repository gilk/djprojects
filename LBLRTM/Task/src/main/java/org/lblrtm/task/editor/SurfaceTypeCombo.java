/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import org.lblrtm.lblrtmdata.api.Surface;
import org.lblrtm.lblrtmdata.api.Surface.SurfaceType;

/**
 *
 * @author djabry
 */
public class SurfaceTypeCombo extends JComboBox{
    
    private final Surface s;
    
    public SurfaceTypeCombo(Surface surf){
        super(SurfaceType.values());
        this.s=surf;
        this.setSelectedItem(s.getSurfaceType());
        
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                s.setSurfaceType((SurfaceType)getSelectedItem());
            }
        });
        
    }
    
}
