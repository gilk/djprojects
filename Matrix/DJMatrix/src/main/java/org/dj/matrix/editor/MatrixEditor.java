/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.editor;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import org.dj.matrix.api.DJMatrix;
import org.dj.propertyeditor.api.PropertyEditorAbstr;

/**
 *
 * @author djabry
 */
public class MatrixEditor extends PropertyEditorAbstr{
    
    

    private MatrixPickerPanel p = null;
    private MatrixEditorPanel vP=null;

    @Override
    public Object getInplaceComponentValue() {
        return ((MatrixPickerPanel)getInplaceComponent()).getMatrix();
        
    }

    @Override
    public Component getCustomEditor() {
        return new MatrixEditorPanel(this);
        
    }

    @Override
    public boolean supportsCustomEditor() {
        
        DJMatrix matrix = p.getMatrix();
        if(matrix!=null){
            if(matrix.getNumElements()<1E5){
                
                return true;
            }

        }
        
        return false;
    }
    
    

    @Override
    public void setInplaceComponentValue(Object value) {
        ((MatrixPickerPanel)getInplaceComponent()).setMatrix((DJMatrix)value);
    }

    @Override
    public JComponent getInplaceComponent() {
        if(p==null){
            
            p= new MatrixPickerPanel();
        }
        
        return p;
    }

    @Override
    public boolean isTextEntrySupported() {
        return false;
    }

    @Override
    public void addCustomActionListener(ActionListener aL) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeCustomActionListener(ActionListener aL) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }



    
    
}
