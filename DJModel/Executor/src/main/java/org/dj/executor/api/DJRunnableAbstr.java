/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.executor.api;

import java.util.Iterator;
import java.util.List;
import javolution.util.FastList;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

/**
 *
 * @author djabry
 */
public abstract class DJRunnableAbstr extends DJNodeObjectAbstr implements DJRunnable{
    
    public static final String PROP_PROGRESS_HANDLE = "progressHandle";
    private boolean progressHandleRunning=false;
    
    
    
    public DJRunnableAbstr(){
        
        super();
        setPropertyValue(PROP_COMPLETE, false);
        this.getProperty(PROP_COMPLETE).setNotify(true);
        
        setPropertyValue(PROP_RUNNING, false);
        this.getProperty(PROP_RUNNING).setNotify(true);
        
        
        setPropertyValue(PROP_PROGRESS_HANDLE, ProgressHandleFactory.createHandle("Running task..."));
        

    }
    
    public ProgressHandle getProgressHandle(){
        
        
        return (ProgressHandle) this.getPropertyValue(PROP_PROGRESS_HANDLE);
    }
    
    public DJRunnableAbstr(ProgressHandle handle){
        
        this();
        
        if(handle!=null){
            
            this.setPropertyValue(PROP_PROGRESS_HANDLE, handle);
        }
        
    }


    @Override
    public boolean isComplete() {
        return (Boolean)this.getPropertyValue(PROP_COMPLETE);
    }

    @Override
    public void updateStatus() {
        
        ProgressHandle handle = this.getProgressHandle();
        
        if(!this.progressHandleRunning
                &&this.isRunning()
                &&!this.getName().equals("")){
            
            handle.start();
            this.progressHandleRunning=true;
        }
        
        
        if(this.progressHandleRunning){
            
            handle.setDisplayName(this.getDisplayName());
            handle.progress(this.getMessage());
        }
        
        if(this.progressHandleRunning&&this.isComplete()){
            
            handle.finish();
            progressHandleRunning = false;
        }
        
        
    }
    
    @Override
    public boolean isRunning(){
        
       return (Boolean)this.getPropertyValue(PROP_RUNNING);
    }
    
    public void setRunning(boolean tf){
            
            this.setPropertyValue(PROP_RUNNING, tf);
    }
    
    public void setComplete(boolean tf){
        
        this.setPropertyValue(PROP_COMPLETE, tf);
    }

    @Override
    public String getDisplayName() {
        return this.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        updateStatus();
    }
    
    

    @Override
    public String getMessage() {
        return getDescription();
        
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
        updateStatus();
    }
    
    
    
    @Override
    public List<DJRunnable> getDependencies(){
        
        List<DJRunnable> deps = new FastList<DJRunnable>();
        
        for(DJNodeObject ch:this.getChildren()){
            if(ch instanceof DJRunnable){
                DJRunnable djRunnable = (DJRunnable) ch;
                deps.add(djRunnable);
                
            }
            
        }
        return deps;
        
    }

    @Override
    public void run() {
        this.setComplete(false);
        
        
        if(!this.isComplete()&&!this.isRunning()){
            try{
   
            this.setRunning(true);
            
            
            updateStatus();
            
            
            //Run all sub tasks (dependencies)
            Iterator<DJRunnable> iterator = this.getDependencies().iterator();
            while(iterator.hasNext()){ 
                DJRunnable next = iterator.next();
                next.run();
            }
            
            updateStatus();
            
            //Then run current tasks
            doRun();
        
            
        }finally{
                
           
            
            this.setComplete(true);
            this.setRunning(false);
            updateStatus();
            
        }
            
        }
        
    }
    
   
    
    public abstract void doRun();
    
}
