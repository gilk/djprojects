/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.editor.api;

import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObject;
import org.dj.filter.api.Filter;

/**
 *
 * @author djabry
 */
public interface DefaultEditorProvider extends Filter<DJObject> {
    
    EditorController getEditorController(DJObject obj);
    
}
