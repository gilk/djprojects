/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.property.api;

/**
 *
 * @author djabry
 */
public interface PropertyFactory {
    
    <T extends Object> DJProperty<T> createProperty(String propertyName, T value);
    
}
