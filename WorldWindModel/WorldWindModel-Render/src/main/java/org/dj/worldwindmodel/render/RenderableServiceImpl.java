/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.render;

import org.dj.service.api.ProxyServiceAbstr;
import org.dj.worldwindmodel.object.api.WWDJObject;
import org.dj.worldwindmodel.render.api.RenderableController;
import org.dj.worldwindmodel.render.api.RenderableService;
import org.dj.worldwindmodel.render.api.RenderableServiceProvider;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = RenderableService.class)
public class RenderableServiceImpl extends ProxyServiceAbstr<RenderableController,WWDJObject> implements RenderableService{

    public RenderableServiceImpl(){
        super();
        setCacheResults(true);
    }
    
    @Override
    public Result<? extends Provider<RenderableController, WWDJObject>> getResult() {
        return Lookup.getDefault().lookupResult(RenderableServiceProvider.class);
    }
    
    
}
