/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertyeditor.api;

import java.beans.PropertyEditorSupport;

/**
 *
 * @author djabry
 */
public interface PropertyEditorProvider {
    
    <T extends PropertyEditorSupport> Class<T> getPropertyEditorSupportClass();
    Class getTargetClass();
    
    
}
