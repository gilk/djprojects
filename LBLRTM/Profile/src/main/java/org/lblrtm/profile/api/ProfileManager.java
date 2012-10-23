/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import java.io.IOException;
import java.util.Set;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManager;
import org.dj.matrix.api.DJMatrix;
import org.lblrtm.lblrtmdata.api.AtmosphericModel;

/**
 *
 * @author djabry
 */
public interface ProfileManager extends DJObjectManager<DJProfile>, DJNodeObject.Source{
    
    DJProfile createProfileFromTemplate(AtmosphericModel t) throws IOException;
    DJMatrix loadMatrixForTemplate(AtmosphericModel t) throws IOException;

    
}
