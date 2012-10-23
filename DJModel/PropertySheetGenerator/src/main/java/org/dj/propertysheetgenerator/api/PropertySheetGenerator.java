/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertysheetgenerator.api;


import org.dj.domainmodel.api.DJObject;
import org.openide.nodes.Sheet;

/**
 *
 * @author djabry
 */
public interface PropertySheetGenerator {
    
    Sheet generatePropertySheet(DJObject obj);
    
}
