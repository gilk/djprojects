/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.defaulteditor;

import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObject;
import org.dj.editor.api.DefaultEditorProvider;
import org.dj.editor.api.EditorController;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

@ServiceProvider(service = DefaultEditorProvider.class)
public class DefaultEditorProviderImpl implements DefaultEditorProvider{

    @Override
    public EditorController getEditorController(DJObject obj) {
        return new DefaultEditorController((DJNodeObject)obj);
    }

    @Override
    public boolean filter(DJObject obj) {
        if(obj instanceof DJNodeObject){
            
            return true;
            
        }
        
        return false;
    }
    
}
