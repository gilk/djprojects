/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.defaulteditor;

import javax.swing.JComponent;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObject;
import org.dj.domainmodel.api.DJObjectAbstr;
import org.dj.editor.api.EditorController;

/**
 *
 * @author djabry
 */
public class DefaultEditorController extends DJObjectAbstr implements EditorController<DJNodeObject> {

    private final DJNodeObject obj;
    private final DefaultEditorPanel p;
    
    public DefaultEditorController(DJNodeObject obj){
        
        this.obj=obj;
        this.p=new DefaultEditorPanel(obj);
    }
    
    @Override
    public JComponent getComponent() {
        return p;
    }

    @Override
    public DJNodeObject getObject() {
        return obj;
    }

    @Override
    public void updateComponent() {
        
    }

    @Override
    public void updateObject() {
        
    }

    @Override
    public void close() {
        
    }

    @Override
    public void requestActive() {
       
    }
    
}
