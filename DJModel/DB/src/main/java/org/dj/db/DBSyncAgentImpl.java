/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractScheduledService;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.db.api.DBObject;
import org.dj.db.api.DBObjectManager;
import org.dj.db.api.DBSyncAgent;
import org.dj.db.api.DJObjectDB;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.executor.api.DJRunnable;
import org.dj.executor.api.DJRunnableAbstr;
import org.dj.listener.api.ListenerAbstr;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = DBSyncAgent.class)
public class DBSyncAgentImpl extends AbstractScheduledService implements DBSyncAgent {

    private static final DJObjectDB dB = Lookup.getDefault().lookup(DJObjectDB.class);
    private final Map<DBObjectManager, Set<DBObject>> toAdd = new FastMap<DBObjectManager, Set<DBObject>>();
    private final Map<DBObjectManager, Set<DBObject>> toRemove = new FastMap<DBObjectManager, Set<DBObject>>();
    
    private final PropertyChangeListener childrenRemoveListener = new ListenerAbstr() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if (pce.getPropertyName().equals(DJNodeObject.MESSAGE_CHILD_REMOVED)) {

                DBObjectManager mgr = (DBObjectManager) pce.getSource();
                DBObject child = (DBObject) pce.getNewValue();
                toRemove.get(mgr).add(child);
                toAdd.get(mgr).remove(child);

            }
        }
    };
    private final PropertyChangeListener childrenAddListener = new ListenerAbstr() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if (pce.getPropertyName().equals(DJNodeObject.MESSAGE_CHILD_ADDED)) {
                DBObjectManager mgr = (DBObjectManager) pce.getSource();
                DBObject child = (DBObject) pce.getNewValue();
                toAdd.get(mgr).add(child);
                toRemove.get(mgr).remove(child);
            }
            super.propertyChange(pce);
        }
    };
    
    private HashSet<DBObjectManager> getAllRegisteredManagers(){
        return Sets.newHashSet(Iterables.concat(toAdd.keySet(), toRemove.keySet()).iterator());
    }
    
    
    
    
    private final DJRunnable dBPuller = new DJRunnableAbstr() {
        
                @Override
        public String getName() {
            return "Loading objects from database...";
        }


        @Override
        public void doRun() {
            Iterator<DBObjectManager> iterator = getAllRegisteredManagers().iterator();
        while(iterator.hasNext()){
            DBObjectManager mgr = iterator.next();
            Set<DBObject> objs = dB.getAllDBObjectsWithParentId(mgr.getId(), mgr.getDBName());
             System.out.println("Pulling "+objs.size()+" objects from database");
            Iterator<DBObject> iterator1 = objs.iterator();
            while(iterator1.hasNext()){
                DBObject child = iterator1.next();
                mgr.addChild(child);
            }
            
        };
        }

        @Override
        public boolean cancel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    private final DJRunnable dBUpdater  = new DJRunnableAbstr() {
        
        @Override
        public String getName() {
            return "Updating database...";
        }

        @Override
        public void doRun() {
        Iterator<DBObjectManager> iterator = getAllRegisteredManagers().iterator();
        while(iterator.hasNext()){
            DBObjectManager mgr = iterator.next();
            Set<DBObject> objs = toAdd.get(mgr);

                if (objs != null) {
                    dB.store(objs, mgr.getDBName());
                }

                objs.clear();

                objs = toRemove.get(mgr);
                if (objs != null) {

                    dB.remove(objs, mgr.getDBName());
                }

                objs.clear();
        };
        }

        @Override
        public boolean cancel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    
   private final DJRunnable dBRefresher = new DJRunnableAbstr() {

        @Override
        public String getName() {
            return "Refreshing database...";
        }

       
       
        @Override
        public void doRun() {
            Iterator<DBObjectManager> iterator = getAllRegisteredManagers().iterator();
        while(iterator.hasNext()){
            Set<DBObject> children = new FastSet<DBObject>();
            DBObjectManager mgr = iterator.next();
            Iterator<DJNodeObject> iterator1 = mgr.getChildren().iterator();
            while(iterator1.hasNext()){
                DJNodeObject next = iterator1.next();
                if(next instanceof DBObject){
                    children.add((DBObject)next);
                    
                }
            }
            
            dB.updateDatabase(children, mgr.getDBName());
        }
        }

        @Override
        public boolean cancel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    

    @Override
    protected void startUp() throws Exception {

       System.out.println("Starting database sync agent");
       dBPuller.run();

        super.startUp();
    }

    @Override
    protected void shutDown() throws Exception {
        System.out.println("Stopping database sync agent");
        try{
            dBUpdater.run();
            
        }finally{
            
            dBRefresher.run();
        }
        
        
        super.shutDown();
    }

    

    @Override
    public void registerDBManager(DBObjectManager mgr) {
        if (!this.toAdd.containsKey(mgr)) {
            this.toAdd.put(mgr, new FastSet<DBObject>());
            this.toRemove.put(mgr, new FastSet<DBObject>());

        }

        mgr.addPropertyChangeListener(DJNodeObject.MESSAGE_CHILD_ADDED, childrenAddListener);
        mgr.addPropertyChangeListener(DJNodeObject.MESSAGE_CHILD_REMOVED, childrenRemoveListener);

    }

    @Override
    public void unRegisterDBManager(DBObjectManager mgr) {
        if (this.toAdd.containsKey(mgr)) {

            //Managers are removed and the changes are not propagated to DB
            this.toAdd.remove(mgr);
            this.toRemove.remove(mgr);
            mgr.removePropertyChangeListener(DJNodeObject.MESSAGE_CHILD_ADDED, childrenAddListener);
            mgr.removePropertyChangeListener(DJNodeObject.MESSAGE_CHILD_REMOVED, childrenRemoveListener);
        }
    }

    @Override
    public void sync() {
        
        
    }

    @Override
    protected void runOneIteration() throws Exception {
        System.out.println("Running database sync iteration");
        dBUpdater.run();
        
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(2, 60, TimeUnit.SECONDS);
    }
}
