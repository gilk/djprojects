/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.cookies;

import org.dj.cookies.api.DJCookieAbstr;
import org.dj.cookies.api.RemoveCookie;
import org.dj.domainmodel.api.DJNodeObject;

/**
 *
 * @author djabry
 */
public class RemoveCookieImpl extends DJCookieAbstr implements RemoveCookie{

    public RemoveCookieImpl(DJNodeObject obj){
        
        super(obj);
    }

    @Override
    public void run() {
        DJNodeObject parent = this.getObject().getParent();
        if(parent!=null){
            parent.removeChild(getObject());
        }
        
    }

  
    
}
