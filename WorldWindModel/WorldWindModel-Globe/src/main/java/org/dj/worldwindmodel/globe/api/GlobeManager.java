/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.globe.api;

import java.util.Date;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManager;
import org.dj.worldwindmodel.object.api.WWDJObject;

/**
 *
 * @author djabry
 */
public interface GlobeManager extends DJNodeObject{
    public static final Integer GLOBE_DIMENSION = DEFAULT_DIMENSION+1;
    public static final String PROP_TIME = "time";
    Date getTime();
    void setTime(Date d);
    
    void attachSource(DJObjectManager source);
    void removeSource(DJObjectManager source);
    
}
