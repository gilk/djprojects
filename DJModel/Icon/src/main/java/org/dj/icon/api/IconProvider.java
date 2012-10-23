/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.icon.api;

import javax.swing.Icon;
import org.dj.domainmodel.api.DJObject;
import org.dj.filter.api.Filter;
import org.dj.service.api.Service;

/**
 *
 * @author djabry
 */
public interface IconProvider extends Service.Provider<Icon,DJObject>, Service<Icon,DJObject>{
    
    String getIconBaseFor(DJObject obj);

}
