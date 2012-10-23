/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.editor;

import org.dj.cookies.api.CookieManager;
import org.dj.cookies.api.CookieManagerAbstr;
import org.dj.cookies.api.CookieServiceProvider;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObject;
import org.dj.editor.api.EditorService;
import org.dj.service.api.ServiceProviderAbstr;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = CookieServiceProvider.class)
public class DJEditCookieService extends ServiceProviderAbstr<CookieManager,DJObject> implements CookieServiceProvider{
    
    private static final EditorService eS = Lookup.getDefault().lookup(EditorService.class);

    @Override
    public boolean filter(DJObject obj) {
        boolean tf = eS.filter(obj);
        return tf;
    }

    @Override
    public CookieManager createObject(DJObject obj) {
        
        return new EditCookieManager((DJNodeObject)obj);
    }

    class EditCookieManager extends CookieManagerAbstr {

        public EditCookieManager(DJNodeObject obj) {

            super(obj, new DJEditCookieImpl(obj));
        }
    }


}
