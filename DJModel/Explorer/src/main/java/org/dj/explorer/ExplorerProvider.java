/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.explorer;

import org.dj.domainmodel.api.DJObject;
import org.dj.domainmodel.api.DJObjectManager;
import org.dj.editor.api.EditorController;
import org.dj.editor.api.EditorServiceProvider;
import org.dj.explorer.api.ExplorerController;
import org.dj.service.api.ServiceProviderAbstr;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

@ServiceProvider (service = EditorServiceProvider.class)
public class ExplorerProvider extends ServiceProviderAbstr<EditorController,DJObject> implements EditorServiceProvider{

    @Override
    public boolean filter(DJObject obj) {
        if(obj instanceof DJObjectManager){
            return true;
        }
        return false;
    }

    @Override
    public ExplorerController createObject(DJObject obj) {
        return new ExplorerControllerImpl((DJObjectManager)obj);
    }

    
    
}
