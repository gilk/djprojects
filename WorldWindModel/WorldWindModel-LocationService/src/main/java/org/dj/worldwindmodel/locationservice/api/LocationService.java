/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.locationservice.api;

import gov.nasa.worldwind.geom.Position;



/**
 *
 * @author djabry
 */
public interface LocationService {
    
    Position getPositionForAddress(String address) throws PositionNotFoundException;

    public class PositionNotFoundException extends Exception {
        
        
    }
    
}
