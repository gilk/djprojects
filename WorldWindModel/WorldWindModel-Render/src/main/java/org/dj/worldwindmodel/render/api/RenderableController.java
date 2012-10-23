/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.render.api;

import gov.nasa.worldwind.render.Renderable;
import java.util.Set;
import org.dj.worldwindmodel.object.api.WWDJObject;

/**
 *
 * @author djabry
 */
public interface RenderableController {
    
    void setVisible(boolean v);
    boolean isVisible();
    void setHighlighted(boolean tf);
    boolean isHighlighted();
    Set getRenderables();
    WWDJObject getObject();
    
    
    
}
