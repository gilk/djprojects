/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.IllegalFormatException;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import org.dj.property.api.DJProperty;

/**
 *
 * @author djabry
 */
public class DoubleEditorField extends JTextField{
    
    private DJProperty prop;
    
    public DoubleEditorField(){
        
       this(null);
    }
    
    public DoubleEditorField(DJProperty prop){
        
        this.setProp(prop);
        this.setText(Double.toString((Double)prop.getValue()));
        this.addActionListener(new PropertyUpdater());
    }
    
    public void setProp(DJProperty prop){
        
        this.prop=prop;
        
        
    }
    
    private class PropertyUpdater implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
           
            if(prop!=null){

                try{
                    double val = Double.valueOf(getText());
                    prop.setValue(val);  
                    
                }catch(IllegalFormatException iFE){

                }
 
            }
            
        }
 
        
    }
    
}
