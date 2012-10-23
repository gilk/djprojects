/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Type;
import org.dj.propertyeditor.api.Password;
import org.dj.propertyeditor.api.PasswordImpl;
import org.dj.propertyeditor.api.PropertyEditorProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

@ServiceProvider(service = PropertyEditorProvider.class)
public class PasswordEditorProvider implements PropertyEditorProvider{

    @Override
    public Class getPropertyEditorSupportClass() {
        return PasswordPropertyEditor2.class;
    }

    @Override
    public Class getTargetClass() {
        return PasswordImpl.class;
    }



    
}
