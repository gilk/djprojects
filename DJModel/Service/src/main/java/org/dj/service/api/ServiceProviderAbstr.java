/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.service.api;

import org.dj.domainmodel.api.DJObject;

/**
 *
 * @author djabry
 */
public abstract class ServiceProviderAbstr<T,O extends DJObject> implements Service.Provider<T,O>, Service<T,O>{


    @Override
    public Service<T,O> getService() {
        return this;
    }

    
}
