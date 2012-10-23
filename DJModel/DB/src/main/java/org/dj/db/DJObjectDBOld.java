/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.db.api.DBObject;
import org.dj.db.api.DBObjectAbstr;
import org.dj.db.api.DJObjectDB;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = DJObjectDB.class)
public class DJObjectDBOld implements DJObjectDB{
    
     private static final Map<String, EntityManager> eMs = new FastMap<String, EntityManager>();;
    private static final Map<EntityManager, EntityTransaction> eTs = new FastMap<EntityManager, EntityTransaction>();

    public DJObjectDBOld() {

        
    }

    @Override
    public EntityManager getEntityManager(String dbName) {

        if (!eMs.containsKey(dbName)) {
            eMs.put(dbName, Persistence.createEntityManagerFactory(dbName).createEntityManager());
            

        }

        EntityManager eM =  eMs.get(dbName);
        EntityTransaction eT = getTransaction(eM);
        
        return eM;

       
    }

    public EntityTransaction getTransaction(EntityManager eM) {

        
        if(!eTs.containsKey(eM)){
            
            eTs.put(eM, eM.getTransaction());
        }
        
        EntityTransaction eT = eTs.get(eM);
        
        if(!eT.isActive()){
            
            eT.begin();
        }

       
        return eT;
    }

    @Override
    public void store(DBObject obj, String dbName) {
        Set<DBObject> objs = new FastSet<DBObject>();
        objs.add(obj);
        this.store(objs, dbName);

    }

    @Override
    public EntityManager getEntityManager() {
        return this.getEntityManager(DJObjectDB.DEFAULT_DATABASE);
    }

    @Override
    public void store(DBObject obj) {
        this.store(obj, DJObjectDB.DEFAULT_DATABASE);
    }

    @Override
    public Set<DBObject> getAllDBObjectsForClass(Class c) {
        return getAllDBObjectsForClass(c, DJObjectDB.DEFAULT_DATABASE);
    }

    @Override
    public Set<DBObject> getAllDBObjectsForClass(Class c, String dBName) {

        EntityManager eM = this.getEntityManager(dBName);
        TypedQuery qry = eM.createQuery("SELECT c from " + c.getSimpleName() + " c", c);
        List resultList = qry.getResultList();
        Set<DBObject> results = new FastSet<DBObject>();
        results.addAll(resultList);

        return results;
    }

    @Override
    public void store(Set<DBObject> objs) {
        this.store(objs, DJObjectDB.DEFAULT_DATABASE);
    }

    @Override
    public void store(Set<DBObject> objs, String dbName) {
        EntityManager eM = this.getEntityManager(dbName);


        for (DBObject obj : objs) {
            if (!eM.contains(obj)) {
                try{
                    eM.persist(obj);
                    
                }catch(Exception e){
                    
                    System.out.println("Error when trying to store object" 
                            +obj.toString()+": "+e.getMessage());
                }
                
            }
        }

        //eM.close();
    }

    @Override
    public void remove(Set<DBObject> objs, String dbName) {
        EntityManager eM = this.getEntityManager(dbName);
        

        for (DBObject obj : objs) {

            if (eM.contains(obj)) {
                eM.remove(obj);
            }

        }
        ;
        //eM.close();
    }

    @Override
    public void remove(DBObject obj, String dbName) {
        Set<DBObject> objs = new FastSet<DBObject>();
        objs.add(obj);
        this.remove(objs, dbName);
    }

    @Override
    public void remove(DBObject obj) {
        Set<DBObject> objs = new FastSet<DBObject>();
        objs.add(obj);
        this.remvoe(objs);
    }

    @Override
    public void remvoe(Set<DBObject> objs) {
        this.remove(objs, DJObjectDB.DEFAULT_DATABASE);
    }

//    @Override
//    public Set<DBObject> getAllDBObjectsWithParentId(String parentId) {
//        
//        return getAllDBObjectsWithParentId(parentId, DJObjectDB.DEFAULT_DATABASE);
//    }
//
//    @Override
//    public Set<DBObject> getAllDBObjectsWithParentId(String parentId, String dBName) {
//        findAllObjectsFor(DBObject.class,DJNodeObject.PROP_PARENTS)
//    }
    public <O extends DBObject> Collection<O> findAllDBObjectsFor(Class<O> c, String propertyName, Object value, String dbName) {

        String qString = "SELECT o from " + c.getSimpleName().toUpperCase() + " WHERE o." + propertyName + " = " + value;
        return this.getEntityManager(dbName).createQuery(qString).getResultList();

    }

    @Override
    public void updateFromDatabase(Set<DBObject> objs, String dbName) {
        EntityManager eM = this.getEntityManager(dbName);


        for (DBObject obj : objs) {
            eM.refresh(obj);
        }


        //eM.close();
    }

    @Override
    public void updateFromDatabase(DBObject obj) {
        this.updateFromDatabase(obj, DEFAULT_DATABASE);
    }

    @Override
    public void updateFromDatabase(Set<DBObject> objs) {
        this.updateFromDatabase(objs, DEFAULT_DATABASE);
    }

    @Override
    public void updateFromDatabase(DBObject obj, String dbName) {
        Set<DBObject> objs = new FastSet<DBObject>();
        objs.add(obj);
        updateFromDatabase(objs, dbName);
    }

    @Override
    public boolean contains(DBObject obj) {
        return contains(obj, DEFAULT_DATABASE);
    }

    @Override
    public boolean contains(DBObject obj, String dbName) {

        EntityManager eM = this.getEntityManager(dbName);

        DBObject find = eM.find(obj.getClass(), obj.getId());


        if (find != null) {

            return true;
        }

//        try{
//           Class c = DBObjectAbstr.class;
//            Query q = eM.createQuery("SELECT o FROM " + c.getSimpleName() + " WHERE o.id = '"+obj.getId()+"'");
//
//            Object singleResult = q.getSingleResult();
//
//            if (singleResult != null) {
//
//                return true;
//            } 
//            
//        }catch(NoResultException nRE){
//            
//            
//        }


        return false;
    }

    @Override
    public Set<DBObject> getAllDBObjectsWithParentId(String id, String dBName) {

        EntityManager eM = this.getEntityManager(dBName);
        String qString = "SELECT o FROM " + DBObjectAbstr.class.getSimpleName() + " o WHERE '" + id + "' IN " + DBObject.PROP_ALL_PARENT_IDS;
        TypedQuery<DBObjectAbstr> q = eM.createQuery(qString, DBObjectAbstr.class);
        Set<DBObject> results = new FastSet<DBObject>();

        results.addAll(q.getResultList());
        return results;

    }

    @Override
    public Set<DBObject> getAllDBObjectsWithParentId(String id) {
        return this.getAllDBObjectsWithParentId(id, DEFAULT_DATABASE);
    }

    @Override
    public void flush() {
        this.flush(DEFAULT_DATABASE);
    }

    @Override
    public void flush(String dbName) {
        EntityManager eM = this.getEntityManager(dbName);
        eM.flush();
    }

    @Override
    public void updateDatabase(Set<DBObject> objs, String dbName) {
        this.flush(dbName);

    }

    @Override
    public void updateDatabase(Set<DBObject> objs) {
        updateDatabase(objs, DEFAULT_DATABASE);
    }

    @Override
    public void updateDatabase(DBObject obj, String dbName) {
        Set<DBObject> objs = new FastSet<DBObject>();
        objs.add(obj);
        this.updateDatabase(objs, dbName);

    }

    @Override
    public void close() {
        
        for(EntityTransaction eT:this.eTs.values()){
            
            if(eT.isActive()){
                
                eT.commit();
                
            }
        }

        for (EntityManager eM : this.eMs.values()) {

            if(eM.isOpen()){
                
                eM.close();
            }
        }
    }

    @Override
    public void updateDatabase(DBObject obj) {
        this.updateDatabase(obj, DEFAULT_DATABASE);
    }
    
}
