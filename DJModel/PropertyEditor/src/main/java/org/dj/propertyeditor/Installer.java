/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertyeditor;

import java.beans.PropertyEditorManager;
import java.util.Collection;
import org.dj.propertyeditor.api.Password;
import org.dj.propertyeditor.api.PasswordImpl;
import org.dj.propertyeditor.api.PropertyEditorProvider;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

public class Installer extends ModuleInstall {
    


    @Override
    public void restored() {

        Collection<? extends PropertyEditorProvider> allInstances = Lookup.getDefault().lookupResult(PropertyEditorProvider.class).allInstances();
      
        for(PropertyEditorProvider p:allInstances){
            
            PropertyEditorManager.registerEditor(p.getTargetClass(), p.getPropertyEditorSupportClass());
        }
    }
}
