/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.cookies.api;

import java.util.List;
import java.util.Set;
import javolution.util.FastList;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author djabry
 */
public final class ProxyCookieManager implements CookieManager{
    

    private final Lookup lkp;
            
    public ProxyCookieManager(Set<CookieManager> cMs){
        
        List<Lookup> lkps = new FastList<Lookup>();
        
        for(CookieManager c:cMs){
            Lookup lookup = c.getLookup();
            
            if(!lookup.lookupResult(DJCookie.class).allInstances().isEmpty()){
                
                lkps.add(lookup);
            }
            
        }

        this.lkp=new ProxyLookup(lkps.toArray(new Lookup[lkps.size()]));
 
    }

    @Override
    public Lookup getLookup() {
       return lkp;
    }

}
