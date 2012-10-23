/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.cookies.api;

import org.dj.domainmodel.api.DJNodeObject;

/**
 *
 * @author djabry
 */
public abstract class DJCookieAbstr implements DJCookie {
    
    private final DJNodeObject obj;
    
    public DJCookieAbstr(DJNodeObject obj){
        
        this.obj=obj;
        
    }
    
    public DJNodeObject getObject(){
        
        return this.obj;
    }

    
    
}
