/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.globeviewer.api;

import gov.nasa.worldwind.globes.FlatGlobe;
import lombok.EnumId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author djabry
 */
@RequiredArgsConstructor
public enum Projection {
     
     
     SPHERICAL("Spherical","Spherical"),
     FLAT_LAT_LON("Lat-lon",FlatGlobe.PROJECTION_LAT_LON),
     FLAT_MERCATOR("Mercator",FlatGlobe.PROJECTION_MERCATOR),
     FLAT_SINUSODAL("Sinusodal",FlatGlobe.PROJECTION_MODIFIED_SINUSOIDAL),
     FLAT_MODIFIED_SINUSODAL("Modified sinusodal",FlatGlobe.PROJECTION_SINUSOIDAL);
     

     @Getter
     private final String fullName;
     
     @Getter
     private final String projectionString;

    @Override
    public String toString() {
        return this.fullName;
    }
     
   
}
