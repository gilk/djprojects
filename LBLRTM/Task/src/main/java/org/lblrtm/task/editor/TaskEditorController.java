/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.editor;

import java.awt.Component;
import java.util.Map;
import javolution.util.FastMap;
import org.dj.editor.api.EditorControllerAbstr;
import org.lblrtm.task.api.LBLRTMParallelTask;
import org.lblrtm.task.api.LBLRTMTask;

/**
 *
 * @author djabry
 */
public class TaskEditorController extends EditorControllerAbstr<LBLRTMParallelTask>{
    
    public TaskEditorController(LBLRTMParallelTask t){
        
        super(t);
        this.setComponent(new TaskEditorPanel(t));
        
    }

    @Override
    protected void updateComponentWithValue(Component c, Object o) {
        
    }

    @Override
    protected Object getComponentValue(Component c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Map getComponentMap() {
        return new FastMap<String,Component>();
    }
    
}
