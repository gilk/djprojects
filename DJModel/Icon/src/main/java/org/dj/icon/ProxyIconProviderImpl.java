/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dj.icon;
import javax.swing.Icon;
import org.dj.domainmodel.api.DJObject;
import org.dj.icon.api.IconProvider;
import org.dj.icon.api.IconService;
import org.dj.service.api.ProxyServiceAbstr;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.lookup.ServiceProvider;



/**
 *
 * @author djabry
 */
@ServiceProvider(service = IconService.class) 
public class ProxyIconProviderImpl extends ProxyServiceAbstr<Icon,DJObject> implements IconService{
    
    private static final Lookup.Result<IconProvider> providers = Lookup.getDefault().lookupResult(IconProvider.class);

    @Override
    public Result<? extends Provider<Icon,DJObject>> getResult() {
        return providers;
    }

    @Override
    public Icon createObject(DJObject obj) {
        Icon icon = super.createObject(obj);
        
        if(icon==null){
            
            icon= ImageUtilities.loadImageIcon(DEFAULT_ICON_BASE, true);
        }
        
        return icon;
    }
    
    

    @Override
    public String getIconBaseFor(DJObject obj) {
        IconProvider s = (IconProvider)this.findService(obj);
        
        if(s!=null){
            
            return s.getIconBaseFor(obj);
        }
        
        return DEFAULT_ICON_BASE;
    }



   




} 