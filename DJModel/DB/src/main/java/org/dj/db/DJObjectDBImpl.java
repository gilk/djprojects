/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javolution.util.FastSet;
import org.dj.db.api.DBObject;
import org.dj.db.api.DBObjectAbstr;
import org.dj.db.api.DJObjectDB;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
//@ServiceProvider(service = DJObjectDB.class)
public class DJObjectDBImpl implements DJObjectDB {

    @Override
    public EntityManager getEntityManager() {
        return getEntityManager(DEFAULT_DATABASE);
    }

    @Override
    public EntityManager getEntityManager(String dbName) {
        EntityManagerFactory eMF = Persistence.createEntityManagerFactory(dbName);
        return eMF.createEntityManager();
        
    }

    @Override
    public void store(DBObject obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void store(DBObject obj, String dbName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void store(Set<DBObject> objs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void store(Set<DBObject> objs, String dbName) {
        EntityManager eM = this.getEntityManager(dbName);
        eM.getTransaction().begin();
        Iterator<DBObject> iterator = objs.iterator();
        while(iterator.hasNext()){
            DBObject next = iterator.next();
            if(eM.contains(next)){
                eM.merge(next);
            }else{
                eM.persist(next);
            }
        }
        
        eM.getTransaction().commit();
        //eM.close();
        
    }

    @Override
    public void remove(Set<DBObject> objs, String dbName) {
        EntityManager eM = this.getEntityManager(dbName);
        eM.getTransaction().begin();
        Iterator<DBObject> iterator = objs.iterator();
        while(iterator.hasNext()){
            DBObject next = iterator.next();
            if(eM.contains(next)){
                eM.remove(next);
            }
        }
        
        eM.getTransaction().commit();
        eM.close();
    }

    @Override
    public void remvoe(Set<DBObject> objs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(DBObject obj, String dbName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(DBObject obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateFromDatabase(Set<DBObject> objs, String dbName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateFromDatabase(Set<DBObject> objs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateFromDatabase(DBObject obj, String dbName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateFromDatabase(DBObject obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contains(DBObject obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contains(DBObject obj, String dbName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<DBObject> getAllDBObjectsWithParentId(String id, String dBName) {
        EntityManager eM = this.getEntityManager(dBName);
        String qString = "SELECT o FROM " + DBObjectAbstr.class.getSimpleName() + " o WHERE '" + id + "' IN " + DBObject.PROP_ALL_PARENT_IDS;
        TypedQuery<DBObjectAbstr> q = eM.createQuery(qString, DBObjectAbstr.class);
        Set<DBObject> results = new FastSet<DBObject>();
        List<DBObjectAbstr> resultList = q.getResultList();
        if(resultList!=null){
             results.addAll(q.getResultList());
        } 
        return results;
    }

    @Override
    public Set<DBObject> getAllDBObjectsWithParentId(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flush(String dbName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateDatabase(Set<DBObject> objs, String dbName) {
         EntityManager eM = this.getEntityManager(dbName);
        eM.getTransaction().begin();
        Iterator<DBObject> iterator = objs.iterator();
        while(iterator.hasNext()){
            DBObject next = iterator.next();
            eM.merge(next);
        }
        
        eM.getTransaction().commit();
        //eM.close();
    }

    @Override
    public void updateDatabase(Set<DBObject> objs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateDatabase(DBObject obj, String dbName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateDatabase(DBObject obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<DBObject> getAllDBObjectsForClass(Class c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<DBObject> getAllDBObjectsForClass(Class c, String dBName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
}
