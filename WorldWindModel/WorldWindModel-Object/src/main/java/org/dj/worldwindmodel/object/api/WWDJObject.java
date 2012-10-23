/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.object.api;

import gov.nasa.worldwind.geom.Position;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObject;

/**
 *
 * @author djabry
 */
public interface WWDJObject extends DJNodeObject{
    public static final String PROP_POSITION = "position";
    Position getPosition();
    void setPosition(Position p);
//    double getLatitude();
//    void setLatitude(double lat);
//    double getLongitude();
//    void setLongitude(double lon);
//    double getElevation();
//    void setElevation(double el);
//    double getAltitude();
            
    
    
}
