/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.cookies;

import org.dj.cookies.api.AddCookie;
import org.dj.cookies.api.DJCookieAbstr;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJNodeObject.Source;

/**
 *
 * @author djabry
 */
public class AddCookieImpl extends DJCookieAbstr implements AddCookie{
    

    
    public AddCookieImpl(DJNodeObject obj){
        
       super(obj);
        
    }

    @Override
    public void run() {
        
        DJNodeObject obj = getObject();
        
        if(obj instanceof DJNodeObject.Source){
            Source source = (Source) obj;
            source.createObject();
        }
    }
    
}
