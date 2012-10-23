/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.editor;

import org.dj.domainmodel.api.DJObject;
import org.dj.editor.api.DefaultEditorProvider;
import org.dj.editor.api.EditorController;
import org.dj.editor.api.EditorService;
import org.dj.editor.api.EditorServiceProvider;
import org.dj.service.api.ProxyServiceAbstr;
import org.dj.service.api.Service;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

@ServiceProvider (service = EditorService.class)
public class EditorServiceImpl extends ProxyServiceAbstr<EditorController,DJObject> implements EditorService {
    
    private static final Result<EditorServiceProvider> result = Lookup.getDefault().lookupResult(EditorServiceProvider.class);
    private static final DefaultEditorProvider dEP = Lookup.getDefault().lookup(DefaultEditorProvider.class);

            
    @Override
    public Result<? extends Provider<EditorController,DJObject>> getResult() {
        return result;
    }
    

    public EditorServiceImpl(){
        
        super();
        this.setCacheResults(true);

    }

    @Override
    public EditorController createObject(DJObject obj) {
        
        EditorController eC = super.createObject(obj);
        
        if(eC==null&&dEP!=null){
            eC =  dEP.getEditorController(obj);
        }
        
        return eC;
    }
    
    

    @Override
    public boolean filter(DJObject obj) {
        boolean tf = findService(obj)!=null;
        
        if(!tf){
            
            if(dEP!=null){
                tf = dEP.filter(obj);
            }
        }
        
        return tf;
    }




   
    
    
    
}
