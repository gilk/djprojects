/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.editor.api;

import javax.swing.JComponent;
import org.dj.domainmodel.api.DJObject;
import org.dj.filter.api.Filter;

/**
 *
 * @author djabry
 */
public interface EditorController<T extends DJObject> extends DJObject{
    
    public static final String PROP_OBJECT = "object";
    public static final String PROP_COMPONENT  = "component";
    
    public interface Provider {
        
        EditorController getEditorController();
    }

    JComponent getComponent();
    T getObject();
    void updateComponent();
    void updateObject();
    void close();
    void requestActive();
    
    
 
}
