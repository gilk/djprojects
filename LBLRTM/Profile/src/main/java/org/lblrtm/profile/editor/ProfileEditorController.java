/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.editor;

import org.lblrtm.profile.api.ProfileEditor;
import java.awt.Component;
import java.util.Map;
import javolution.util.FastMap;
import org.dj.editor.api.EditorControllerAbstr;
import org.lblrtm.profile.api.DJProfile;

/**
 *
 * @author djabry
 */
public class ProfileEditorController extends EditorControllerAbstr<DJProfile>{
    
    public ProfileEditorController(DJProfile p){
        
        super(p);
        this.setComponent(new ProfileEditor(p));
    }

    @Override
    protected void updateComponentWithValue(Component c, Object o) {
        
    }

    @Override
    protected Object getComponentValue(Component c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Map<String, Component> getComponentMap() {
        return new FastMap<String,Component>();
    }
    
}
