/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.globeviewer.api;

import org.dj.editor.api.EditorController;
import org.dj.worldwindmodel.globe.api.GlobeManager;

/**
 *
 * @author djabry
 */
public interface GlobeController extends EditorController<GlobeManager>{
    
    void setProjection(Projection p);
    Projection getProjection();
    
}
