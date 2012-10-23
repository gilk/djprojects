/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;



import org.lblrtm.lblrtmdata.api.Observer;
import org.lblrtm.lblrtmdata.api.Surface;
import org.lblrtm.lblrtmdata.api.TAPE5;
import org.lblrtm.profile.api.Profile;

/**
 *
 * @author djabry
 */
public interface TAPE5Factory {
    
    TAPE5 createTAPE5(
            Profile profile,
            Surface surface,
            Observer observer,
            double v1,
            double v2,
            double dv
            );
    
}
