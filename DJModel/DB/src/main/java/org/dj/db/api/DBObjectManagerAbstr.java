/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db.api;

import java.util.Set;
import javolution.util.FastSet;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManagerAbstr;
import org.dj.executor.api.Executor;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class DBObjectManagerAbstr<O extends DBObject> extends DJObjectManagerAbstr<O> implements DBObjectManager<O> {
    
    private static final DJObjectDB dB =Lookup.getDefault().lookup(DJObjectDB.class);
    private static final Executor ex =Lookup.getDefault().lookup(Executor.class);
    //Generate static Id based on class name
    @Override
    public String getId() {
        return this.getClass().getName();
    }

    @Override
    public String getDBName() {
        return DJObjectDB.DEFAULT_DATABASE;
    }
    
    
    
    public DBObjectManagerAbstr(){
        
        super();
        
        

        refreshFromDB();

    }
    
    private class ManagerRefresher implements Runnable {

        @Override
        public void run() {
            
            
            
            ProgressHandle handle = ProgressHandleFactory.createHandle("Retrieving from database...");
            handle.start();
            Set<DBObject> objs = dB.getAllDBObjectsWithParentId(getId());
            handle.switchToDeterminate(objs.size());
            
            int i = 0;
            
            for(DBObject obj:objs){
                handle.progress(i);
                addChild(obj);
                i++;
            }
            
            handle.finish();
        }
        
        
        
    }
    
    private class DBRefresher implements Runnable{

        @Override
        public void run() {
            ProgressHandle handle = ProgressHandleFactory.createHandle("Updating database...");
            handle.start();
            Set<? extends DJNodeObject> objs = getChildren();
            Set<DBObject> objsOut = new FastSet<DBObject>();
            
            for(DJNodeObject obj:objs){
                
                if(obj instanceof DBObject){
                    DBObject dbObject = (DBObject) obj;
                    
                    objsOut.add(dbObject);
                }
            }
            
            dB.updateDatabase(objsOut);
            
            handle.finish();
        }
    }
    
    @Override
    public void refreshFromDB(){
        
        ex.execute(new DBObjectManagerAbstr.ManagerRefresher());
        
        
    }
    
    
    

   

    @Override
    public void addChild(DJNodeObject obj, Integer dim,boolean notify) {
        
        try{
            
            if(obj instanceof DBObject){
                DBObject dbObject = (DBObject) obj;
                dB.store(dbObject);
 
            }
        }finally{
            
            super.addChild(obj, dim,notify);
        }
        
        
        
    }

    @Override
    public void removeChild(DJNodeObject child, Integer dim,boolean notify) {
        
        try{
            if(child instanceof DBObject){
                DBObject dbObject = (DBObject) child;
                dB.remove(dbObject);
            }
            
        }finally{
            super.removeChild(child, dim,notify);
            
        }
         
    }


   
    
    @Override
    public void refreshDB(){
        ex.execute(new DBObjectManagerAbstr.DBRefresher());
        
    }
    
  
    
    
}
