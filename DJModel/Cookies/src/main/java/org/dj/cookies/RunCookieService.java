/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.cookies;

import org.dj.cookies.api.*;
import org.dj.domainmodel.api.DJObject;
import org.dj.executor.api.DJRunnable;
import org.dj.executor.api.Executor;
import org.dj.service.api.ServiceProviderAbstr;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = CookieServiceProvider.class)
public class RunCookieService extends ServiceProviderAbstr<CookieManager,DJObject> implements CookieServiceProvider {

    private static final Executor ex = Lookup.getDefault().lookup(Executor.class);
    
    @Override
    public boolean filter(DJObject obj) {
        if(obj instanceof DJRunnable){
            
            return true;
        }
        
        return false;
    }

    @Override
    public CookieManager createObject(DJObject obj) {
       return new RunCookieManager((DJRunnable)obj);
    }

    
    public class RunCookieManager extends CookieManagerAbstr {

        public RunCookieManager(DJRunnable obj) {
            super(obj);
            this.addCookie(new RunCookieImpl(obj));

        }

        public class RunCookieImpl extends DJCookieAbstr implements RunCookie {

            public RunCookieImpl(DJRunnable r) {

                super(r);
            }

            @Override
            public void run() {

                //removeCookie(this);
                ex.execute((DJRunnable) this.getObject());
            }
        }
    }
}
