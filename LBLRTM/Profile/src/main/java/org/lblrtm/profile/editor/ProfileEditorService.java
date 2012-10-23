/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.editor;

import org.dj.domainmodel.api.DJObject;
import org.dj.editor.api.EditorController;
import org.dj.editor.api.EditorServiceProvider;
import org.dj.service.api.ServiceProviderAbstr;
import org.lblrtm.profile.api.DJProfile;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = EditorServiceProvider.class)
public class ProfileEditorService extends ServiceProviderAbstr<EditorController,DJObject> implements EditorServiceProvider{

    @Override
    public boolean filter(DJObject obj) {
        if(obj instanceof DJProfile){
            
            return true;
        }
       return false;
    }

    @Override
    public EditorController createObject(DJObject obj) {
        return new ProfileEditorController((DJProfile)obj);
    }
    
}
