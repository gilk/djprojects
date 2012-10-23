/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.icon.api;

import javax.swing.Icon;
import org.dj.domainmodel.api.DJObject;
import org.dj.service.api.Service;
import org.dj.service.api.ServiceProviderAbstr;
import org.openide.util.ImageUtilities;

/**
 *
 * @author djabry
 */
public abstract class IconProviderAbstr extends ServiceProviderAbstr<Icon,DJObject> implements IconProvider{
    
    private final String iconPath;
    
    public IconProviderAbstr(String iconPath){
        
        this.iconPath=iconPath;
    }


    @Override
    public Icon createObject(DJObject obj) {
        return ImageUtilities.loadImageIcon(iconPath, true);
    }
    
    
    @Override
    public String getIconBaseFor(DJObject obj ){
        
        return this.iconPath;
    }

    
    

    
    
    
}
