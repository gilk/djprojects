/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.cookies.api;

import org.dj.domainmodel.api.DJNodeObject;
import org.dj.lookup.api.DJLookup;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class CookieManagerAbstr implements CookieManager {
    

    private final DJNodeObject obj;
    private final DJLookup lkp;

           
    
    public CookieManagerAbstr(DJNodeObject obj, DJCookie c){
        this(obj);
        this.addCookie(c);

    }
    
    public CookieManagerAbstr(DJNodeObject obj){
        
        this.obj=obj;
        this.lkp=new DJLookup();
    }
    
    
    
    protected void addCookie(DJCookie c){
        
        this.lkp.addObject(c);

    }
    
    protected void removeCookie(DJCookie c){
        
        this.lkp.removeObject(c);
    }

    public DJNodeObject getObject() {
        return obj;
    }

    @Override
    public Lookup getLookup() {
        
        return lkp;
    }
    
}
