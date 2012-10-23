/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db.api;

import java.util.*;
import java.util.Map.Entry;
import javax.persistence.*;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJNodeObjectAbstr;

/**
 *
 * @author djabry
 */

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class DBObjectAbstr extends DJNodeObjectAbstr implements DBObject{
    
    @Override
    @Id
    public String getId(){
        
        return super.getId();
        
    }
    
    public Set<String> getAllParentIds(){

        Set<String> ids = new HashSet<String>();
        for(DJNodeObject parent: this.getAllParents().values()){
            
            ids.add(parent.getId());
        }
        return ids;

    }
    
    public void setAllParentIds(Set<String> ids){
        //dummy method
    }
    
    @Override
    public void setId(String id){
        
        super.setId(id);
    }

    @Override
    public String getName(){
        
        return super.getName();
    }
    
    @Override
    public void setName(String name){
        
        super.setName(name);
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }
    

    

    //Only add child objects if they are database compatible
//    @Override
//    public void addChild(DJNodeObject obj) {
//        
//        if(obj instanceof DBObject){
//            
//            super.addChild(obj);
//        }
//        
//    }
//    
    
   
    
    @OneToMany(cascade = CascadeType.ALL) 
    public Map<Integer,Set<DBObjectAbstr>> getDBChildren(){
        
        Map<Integer, Set<DJNodeObject>> allChildren = getAllChildren();
        Map<Integer, Set<DBObjectAbstr>> dBChildren  = new HashMap<Integer,Set<DBObjectAbstr>>();
        
      
        for(Entry<Integer,Set<DJNodeObject>> children:allChildren.entrySet()){
            
            int dim = children.getKey();
            
            Set<DBObjectAbstr> childrenOut = new HashSet<DBObjectAbstr>();
            
            
            for(DJNodeObject child:children.getValue()){
                
                if(child instanceof DBObjectAbstr){
                    DBObjectAbstr dbObjectAbstr = (DBObjectAbstr) child;
                    childrenOut.add(dbObjectAbstr);
                }
            }
            
            if(!childrenOut.isEmpty()){
                
                dBChildren.put(dim, childrenOut);
            }
        }
        
        return dBChildren;
       

    }

    
    public void setDBChildren(Map<Integer,Set<DBObjectAbstr>> children){
        
        Set<Entry<Integer, Set<DBObjectAbstr>>> entrySet = children.entrySet();
        
        for(Entry<Integer,Set<DBObjectAbstr>> childEntry:entrySet){
            
            int dim = childEntry.getKey();
            
            for(DBObjectAbstr child:childEntry.getValue()){
                
                this.addChild(child, dim);
            }
        }
    }
    
}
