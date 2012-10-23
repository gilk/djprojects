/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertyeditor;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import org.dj.domainmodel.api.DJObjectManager;
import org.dj.editor.api.EditorController;
import org.dj.editor.api.EditorService;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */


public class DJObjectManagerEditor extends PropertyEditorSupport{

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }
    
    

    @Override
    public Component getCustomEditor() {
        
        EditorController<DJObjectManager> eC = 
                Lookup.getDefault().lookup(EditorService.class).
                createObject((DJObjectManager)this.getSource());
        
        if(eC!=null){
            
            return  eC.getComponent();
        }
        
        
        return super.getCustomEditor();
        
    }
    
    
    
    
    
    
}
