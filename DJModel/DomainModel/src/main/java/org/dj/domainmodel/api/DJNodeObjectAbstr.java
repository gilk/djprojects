/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;



import java.beans.PropertyChangeEvent;
import java.util.*;
import javolution.util.FastSet;
import org.dj.property.api.DJProperty;

/**
 *
 * @author djabry
 */
public class DJNodeObjectAbstr extends DJObjectAbstr implements DJNodeObject{
    
    public static final String PROP_CHILD_COMPARATOR = "childComparator";
    public static final String PROP_ORDER_MODE = "orderMode";
    public static final String PROP_ORDER_COUNTER = "orderCounter";
    private static final Comparator<? extends DJNodeObject> nameComparator = new Comparator<DJNodeObject>(){

            @Override
            public int compare(DJNodeObject o1, DJNodeObject o2) {
                return o1.getName().compareTo(o2.getName());
            }
 
        };
    
    
    public DJNodeObjectAbstr(){
        
        super();
        
        DJProperty<Integer> orderCounter = getPropertyFactory().createProperty(PROP_ORDER_COUNTER, DEFAULT_DIMENSION);
        orderCounter.setCanRead(false);
        putProperty(orderCounter);
        
        HashMap<Integer,DJNodeObject> parentMap = new HashMap<Integer,DJNodeObject>();
        
        DJProperty<HashMap<Integer,DJNodeObject>> parentsProp = getPropertyFactory().createProperty(PROP_PARENTS, parentMap);
        parentsProp.setCanRead(false);
        putProperty(parentsProp);
        

        DJProperty<OrderMode> orderMode = getPropertyFactory().createProperty(PROP_ORDER_MODE, OrderMode.NAME_ORDER);
        orderMode.setCanRead(false);
        putProperty(orderMode);

         Map<Integer, Comparator<? extends DJNodeObject>> cmps = new HashMap<Integer, Comparator<? extends DJNodeObject>>();
         cmps.put(DEFAULT_DIMENSION, nameComparator);

        DJProperty childComparator = getPropertyFactory().createProperty(PROP_CHILD_COMPARATOR, cmps);
        childComparator.setCanRead(false);
        this.putProperty(childComparator);
        
        HashMap<Integer, FastSet<DJNodeObject>> childMap = new HashMap<Integer,FastSet<DJNodeObject>>();
        childMap.put(DEFAULT_DIMENSION, new FastSet<DJNodeObject>());
        
        DJProperty childrenProp = getPropertyFactory().createProperty(PROP_CHILDREN,childMap);
        childrenProp.setCanRead(false);
        this.putProperty(childrenProp);
        
        
 
        
    }
    
    public Set<Integer> getDimensions(){
        
        return getAllChildren().keySet();
    }

    public void setOrderMode(OrderMode oM){
        
        this.setPropertyValue(PROP_ORDER_MODE, oM);
    }
    
    public OrderMode getOrderMode(){
        
        return (OrderMode) this.getPropertyValue(PROP_ORDER_MODE);
        
    }

    public void setChildComparator(Comparator<? extends DJNodeObject> cmp){
        this.getAllChildComparators().put(DEFAULT_DIMENSION, cmp);
        
    }
    
    public Map<Integer, Comparator<? extends DJNodeObject>> getAllChildComparators(){
        
        return (Map<Integer, Comparator<? extends DJNodeObject>>) getPropertyValue(PROP_CHILD_COMPARATOR);
    }
    
    public void setChildComparator(Comparator<? extends DJNodeObject> cmp, Integer dim){
        
        getAllChildComparators().put(dim, cmp);
    }
    
    public Comparator<? extends DJNodeObject> getChildComparator(){
        return getAllChildComparators().get(DEFAULT_DIMENSION);
        
    }

    @Override
    public String getHtmlDisplayName() {
        return this.getName();
    }


    @Override
    public Set<DJNodeObject> getChildren() {
        Set<DJNodeObject> get = getAllChildren().get(DEFAULT_DIMENSION);
        
        if(get==null){
            
            get=  new FastSet<DJNodeObject>();
        }
        
        return get;
    }
    
    public void setChildren(Set<DJNodeObject> children){
        
        setChildren(children,DEFAULT_DIMENSION);
    }
    
    public void setAllChildren(Map<Integer, Set<DJNodeObject>> childrenMap){
        
        Set<Integer> keySet = childrenMap.keySet();
        Iterator<Integer> iterator = keySet.iterator();
        
        while (iterator.hasNext()){
            Integer dim = iterator.next();
            setChildren(childrenMap.get(dim),dim);
        }
        
    }
    
     public void setChildren(Set<DJNodeObject> children, Integer dim){

         Iterator<? extends DJNodeObject> iterator = getAllChildren().get(dim).iterator();
        
        while(iterator.hasNext()){
            DJNodeObject child = iterator.next();
            if(!children.contains(child)){
                removeChild(child,dim,false);
            }  
        }
        
        Iterator<? extends DJNodeObject> newChildIterator = children.iterator();
        
        while(newChildIterator.hasNext()){
            addChild(newChildIterator.next(),dim,false);
        }
         
     }
    
    public void addChild(DJNodeObject obj,Integer dim, boolean notify){
        
        Set<DJNodeObject> get = getAllChildren().get(dim);
        
        if(get==null){
            get = getAllChildren().put(dim, new FastSet<DJNodeObject>());
        }
        
        if(getAllChildren().get(dim).add(obj)){
            
            obj.addPropertyChangeListener(this,dim);
            registerParent(obj,dim);
            
            if(notify){
                
                    firePropertyChange(PROP_CHILDREN,dim,obj.getAllChildren().get(dim));
                    firePropertyChange(MESSAGE_CHILD_ADDED,dim,obj);
                
            }
        }
    }
    
    public void removeChild(DJNodeObject obj,Integer dim, boolean notify){
        
        Set<DJNodeObject> get = getAllChildren().get(dim);
        
        if(get!=null){
            
            if(get.remove(obj)){ 
            obj.removePropertyChangeListener(this,dim);
            unregisterParent(obj,dim);
            if(notify){
     
                    firePropertyChange(PROP_CHILDREN,dim,obj.getAllChildren().get(dim));
                    firePropertyChange(MESSAGE_CHILD_REMOVED,dim,obj);
                
                if(obj.getAllParents().isEmpty()){
                    
                    obj.firePropertyChange(MESSAGE_DELETED, null,null);
                    
                }
            }
        }
            
            
        }
        
         
        
    }
    
    public void registerParent(DJNodeObject child, Integer dim){
        
        //Remove child from parent of the same dimension
        DJNodeObject parent = child.getAllParents().get(dim);
        
        if(parent!=null){
            
            if(!parent.equals(this)){
                
                parent.removeChild(child);
            }
        }
        
        child.getAllParents().put(dim, this);
 
    }
    
    public void unregisterParent(DJNodeObject child, Integer dim){
        
        child.getAllParents().remove(dim);
        
    }
    
    @Override
    public void addChild(DJNodeObject obj, Integer dim){
        
        addChild(obj,dim,true);
    }

    @Override
    public void addChild(DJNodeObject obj) {
        
        addChild(obj,DEFAULT_DIMENSION);

    }

    @Override
    public void removeChild(DJNodeObject obj) {
        
       removeChild(obj,DEFAULT_DIMENSION);
    }

    
    
    

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        
        if(pce.getPropertyName().equals(MESSAGE_REMOVE_ME)){
            
           if(pce.getNewValue() instanceof DJNodeObject){
                
               DJNodeObject obj = (DJNodeObject) pce.getNewValue();
               Integer dim = (Integer) pce.getOldValue();
               removeChild(obj,dim);
           }
 
        
        }
        
        super.propertyChange(pce);
    }

    @Override
    public DJNodeObject getParent() {
        return getAllParents().get(DEFAULT_DIMENSION);
    }
    
    public DJNodeObject getParent(Integer dim){
        
        return getAllParents().get(dim);
    }
    
    public void setParent(DJNodeObject parent){
        
        this.getAllParents().put(DEFAULT_DIMENSION, parent);
    }
    
    public void setAllParents(Map<Integer, DJNodeObject> parents){
        
        this.setPropertyValue(PROP_PARENTS, parents);
    }
    

    @Override
    public Map<Integer, Set<DJNodeObject>> getAllChildren() {
        return (Map<Integer, Set<DJNodeObject>>) getPropertyValue(PROP_CHILDREN);
    }

    @Override
    public Map<Integer,DJNodeObject> getAllParents() {
        return (Map<Integer,DJNodeObject>) getPropertyValue(PROP_PARENTS);
    }


    @Override
    public void removeChild(DJNodeObject child, Integer dim) {
        removeChild(child,dim,true);
    }

    
}
