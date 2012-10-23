/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.listener.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author djabry
 */


public interface Listener extends PropertyChangeListener{
    
    public static final String MESSAGE_DELETED = "deleted";
    
    void addPropertyChangeListener(PropertyChangeListener listener);
    
    
    
    void removePropertyChangeListener(PropertyChangeListener listener);
            
    void firePropertyChange(String prop, Object o1, Object o2);
    
    
    void firePropertyChange(PropertyChangeEvent pCE);
    
    
    PropertyChangeListener[] getPropertyChangeListeners();
    
    PropertyChangeListener[] getPropertyChangeListeners(String propertyName);
    
    void addPropertyChangeListener(String propName,PropertyChangeListener listener);
    void removePropertyChangeListener(String propName,PropertyChangeListener listener);
    
    PropertyChangeSupport getPropertyChangeSupport();

    
}
