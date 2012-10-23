/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.explorer;

import java.awt.Component;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.swing.JComponent;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.db.api.DBObjectAbstr;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJNodeObject.Source;
import org.dj.editor.api.EditorControllerAbstr;
import org.dj.explorer.api.ExplorerComponent;
import org.dj.explorer.api.ExplorerController;
import org.dj.explorer.api.View;
import org.dj.property.api.DJProperty;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author djabry
 */


public class ExplorerControllerImpl extends EditorControllerAbstr implements ExplorerController{
    
    public static final String PROP_CVISIBLE = "cVisible";
    public static final String PROP_CENABLED = "cEnabled";
    
    private static final Map<String,Component> m = new FastMap<String,Component>();
    
    private Lookup.Result<DJNodeObject> result;
    
public void update() {
        getPanel().load();
        changed = false;
    }

    public void applyChanges() {
        getPanel().store();
        
        changed = false;
    }

    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    public boolean isValid() {
        return getPanel().valid();
    }

    public boolean isChanged() {
        return changed;
    }

    @Override
    protected void updateComponentWithValue(Component c, Object o) {
        
    }

    @Override
    protected Object getComponentValue(Component c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Map getComponentMap() {
        return m;
    }


    @Override
    public JComponent getComponent(ExplorerComponent c) {
        return this.getPanel().getComponent(c);
    }
    
     private class SelectedNodeHandler implements LookupListener {
         
        @Override
        public void resultChanged(LookupEvent le) {
            
            Set<DJNodeObject> selectedObjects = getSelectedObjects();
            
            if(selectedObjects.isEmpty()){
                
                setComponentEnabled(ExplorerComponent.REMOVE_BUTTON,false);
                
            
            }else{
                
                setComponentEnabled(ExplorerComponent.REMOVE_BUTTON,true);

            }
            
            selectedObjects.add(getRoot());

            for(DJNodeObject o: selectedObjects){
                
                if(o instanceof DJNodeObject.Source){
                    
                    setComponentEnabled(ExplorerComponent.ADD_BUTTON,true);
                    return;
                }
                 
            }
            
            setComponentEnabled(ExplorerComponent.ADD_BUTTON,false);;
            
        }
    }
     
     

    public ExplorerControllerImpl(DJNodeObject obj){
        
        super(obj);

        
        DJProperty viewProp = getPropertyFactory().createProperty(PROP_VIEW, View.LIST_VIEW);
        viewProp.setNotify(true);
        putProperty(viewProp);
        
        for(String s:new String[]{PROP_CVISIBLE,PROP_CENABLED}){
            
            Map<ExplorerComponent,Boolean> m = new EnumMap<ExplorerComponent,Boolean>(ExplorerComponent.class);
            
            for(ExplorerComponent c:ExplorerComponent.values()){
                
                m.put(c,true);
            }
            
            DJProperty cProp = getPropertyFactory().createProperty(s, m);
            //cProp.setNotify(true);
            putProperty(cProp);
            
            
        }
 
    }
    
    private Set<DJNodeObject> getSelectedObjects(){
        
        if(result ==null){
            
            result = getPanel().getLookup().lookupResult(DJNodeObject.class);
            result.addLookupListener(new  SelectedNodeHandler() );
        }
        
       
        Set<DJNodeObject> objs = new FastSet<DJNodeObject>();
        objs.addAll(result.allInstances());
        
        return objs;
        
    }
    
    @Override
    public JComponent getComponent(){
        
        return getPanel();
        
    }
    
    private ExplorerPanel getPanel(){
        
        if(p==null){
            
            p=new ExplorerPanel(this);
            
            
        }
        
        return p;
    }
    
    private ExplorerPanel p;
    private boolean changed;

    
    
    
    
    
   
    @Override
    public JComponent getComponent(Lookup masterLookup) {
        
        return getPanel();
        
    }
    
    @Override
    public void add(){
        Set<DJNodeObject> selectedObjects = this.getSelectedObjects();  
        
        if(selectedObjects.isEmpty()){
            
            selectedObjects.add(this.getRoot());
        }
        
        Iterator<DJNodeObject> iterator = selectedObjects.iterator();
        
        int addCount = 0;
        
        while(iterator.hasNext()){
            DJNodeObject next = iterator.next();
            
            if(next instanceof DJNodeObject.Source){
                Source source = (Source) next;
                source.createObject();
                addCount++;
                
            }
            
            if(!iterator.hasNext()){
                if(addCount==0){
                    
                    DJNodeObject r = getRoot();
                    if(r instanceof DJNodeObject.Source){
                        Source s = (Source) r;
                        s.createObject();
                        
                    }
                    
                }
            }
        }
        
    }
    
    @Override
    public void remove(){
        
        Set<DJNodeObject> selectedObjects = this.getSelectedObjects();
 
        for(DJNodeObject obj: selectedObjects){

            Map<Integer, DJNodeObject> allParents = obj.getAllParents();
            DJNodeObject root = this.getRoot();
            if(allParents.containsValue(root)){
                
                root.removeChild(obj);
                
            }else{
                
                obj.getParent().removeChild(obj);
            }

        }
    }
    
    
    
    public Map<ExplorerComponent,Boolean> getCVisible(){
        
        return (Map<ExplorerComponent, Boolean>) this.getPropertyValue(PROP_CVISIBLE);
    }
    
    public void setCVisible(Map<ExplorerComponent,Boolean> m){
        
        this.setPropertyValue(PROP_CVISIBLE, m);
    }
    
    public Map<ExplorerComponent,Boolean> getCEnabled(){
        
        return (Map<ExplorerComponent, Boolean>) this.getPropertyValue(PROP_CENABLED);
        
    }
    
    public void setCEnabled(Map<ExplorerComponent,Boolean> m){
        
        this.setPropertyValue(PROP_CENABLED, m);
        
    }

    @Transient
    @Override
    public DJNodeObject getRoot() {
        return (DJNodeObject) this.getObject();
    }

    @Override
    public void setRoot(DJNodeObject obj) {
        this.setPropertyValue(PROP_OBJECT, obj);
    }

    @Override
    public View getView() {
        return (View) this.getPropertyValue(PROP_VIEW);
    }

    @Override
    public void setView(View v) {
        this.setPropertyValue(PROP_VIEW, v);
    }

    @Override
    public void setComponentVisible(ExplorerComponent c, boolean visible) {
        this.getCVisible().put(c, visible);
        firePropertyChange(PROP_EXPLORER_COMPONENT_VISIBLE,c,visible);
        
    }

    @Transient
    @Override
    public boolean isComponentVisible(ExplorerComponent c) {
        return this.getCVisible().get(c);
    }

    @Override
    public void setComponentEnabled(ExplorerComponent c, boolean enabled) {
        this.getCEnabled().put(c, enabled);
        firePropertyChange(PROP_EXPLORER_COMPONENT_ENABLED,c,enabled);
    }

    @Transient
    @Override
    public boolean isComponentEnabled(ExplorerComponent c) {
        return this.getCEnabled().get(c);
    }
    
    
    void changed() {
        if (!changed) {
            changed = true;
            firePropertyChange(PROP_CHANGED, false, true);
        }
        firePropertyChange(PROP_VALID, null, null);
    }
    
    
}
