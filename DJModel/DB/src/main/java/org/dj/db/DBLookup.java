/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db;

import com.sun.org.apache.bcel.internal.util.Class2HTML;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.dj.db.api.DBObjectAbstr;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;

/**
 *
 * @author djabry
 */
public class DBLookup extends Lookup{
    
    private final EntityManager em;
    
    public DBLookup(EntityManager eM){
        
        this.em=eM;
        
        
    }
    
    
    

    @Override
    public <T> Collection<? extends T> lookupAll(Class<T> clazz) {
        TypedQuery<T> q = this.em.createNamedQuery("SELECT o FROM "+DBObjectAbstr.class.getSimpleName()+" o", clazz);
        return q.getResultList();
    }

    @Override
    public <T> Result<T> lookupResult(Class<T> clazz) {
        return super.lookupResult(clazz);
    }

    @Override
    public <T> T lookup(Class<T> type) {
        return lookupAll(type).iterator().next();
        
    }

    @Override
    public <T> Result<T> lookup(Template<T> tmplt) {
        Class<T> type = tmplt.getType();
        return lookupResult(type);
    }
    
    
    
    
}
