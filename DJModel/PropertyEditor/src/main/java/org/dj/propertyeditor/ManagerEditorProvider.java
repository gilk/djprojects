/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertyeditor;

import java.beans.PropertyEditorSupport;
import org.dj.domainmodel.api.DJObjectManager;
import org.dj.propertyeditor.api.PropertyEditorProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

@ServiceProvider (service = PropertyEditorProvider.class)
public class ManagerEditorProvider implements PropertyEditorProvider{

    @Override
    public <T extends PropertyEditorSupport> Class<T> getPropertyEditorSupportClass() {
        return (Class<T>) DJObjectManagerEditor.class;
    }

    @Override
    public Class getTargetClass() {
        return DJObjectManager.class;
    }
    
}
