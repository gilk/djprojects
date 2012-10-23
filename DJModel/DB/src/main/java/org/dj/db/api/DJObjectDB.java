/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db.api;

import java.util.Set;
import javax.persistence.EntityManager;

/**
 *
 * @author djabry
 */
public interface DJObjectDB {
    
    EntityManager getEntityManager();
    EntityManager getEntityManager(String dbName);
    void store(DBObject obj);
    void store(DBObject obj,String dbName);
    void store(Set<DBObject> objs);
    void store(Set<DBObject> objs,String dbName);
    
    void remove(Set<DBObject> objs,String dbName);
    void remvoe(Set<DBObject> objs);
    void remove(DBObject obj, String dbName);
    void remove(DBObject obj);
    
    void updateFromDatabase(Set<DBObject> objs, String dbName);
    void updateFromDatabase(Set<DBObject> objs);
    void updateFromDatabase(DBObject obj, String dbName);
    void updateFromDatabase(DBObject obj);
    
    boolean contains(DBObject obj);
    boolean contains(DBObject obj, String dbName);
    
    Set<DBObject> getAllDBObjectsWithParentId(String id, String dBName);
    Set<DBObject> getAllDBObjectsWithParentId(String id);
    
    void flush();
    void flush(String dbName);
    
    void close();
    
    void updateDatabase(Set<DBObject> objs, String dbName);
    void updateDatabase(Set<DBObject> objs);
    void updateDatabase(DBObject obj, String dbName);
    void updateDatabase(DBObject obj);
    
    Set<DBObject> getAllDBObjectsForClass(Class c);
    Set<DBObject> getAllDBObjectsForClass(Class c,String dBName);
    //Set<DBObject> getAllDBObjectsWithParentId(String parentId);
    //Set<DBObject> getAllDBObjectsWithParentId(String parentId,String dBName);
    
    public static final String DEFAULT_DATABASE="$objectdb/db/DJDB.odb";
    //public static final String BASE_TABLE = "DJOBJECTTBL";
}
