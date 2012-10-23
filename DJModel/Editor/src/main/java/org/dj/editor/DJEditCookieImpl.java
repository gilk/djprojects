/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.editor;

import org.dj.cookies.api.DJCookieAbstr;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.editor.api.DJEditCookie;
import org.dj.editor.api.DJTopComponent;

/**
 *
 * @author djabry
 */
public class DJEditCookieImpl extends DJCookieAbstr implements DJEditCookie{

    public DJEditCookieImpl(DJNodeObject obj){
        
        super(obj);
    }

    @Override
    public void run() {
        DJTopComponent tC = DJTopComponent.findInstance(getObject());
        tC.open();
        tC.requestActive();
    }
    
}
