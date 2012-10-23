/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.icon.api;

import javax.swing.Icon;
import org.dj.domainmodel.api.DJObject;
import org.dj.service.api.Service;

/**
 *
 * @author djabry
 */


public interface IconService extends Service<Icon,DJObject>{
    public static final String DEFAULT_ICON_BASE =  "images/sat2.png";

    String getIconBaseFor(DJObject obj);
}
