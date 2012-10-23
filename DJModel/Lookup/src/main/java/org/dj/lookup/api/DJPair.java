/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.lookup.api;

import org.dj.idgenerator.api.IDGenerator;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;

/**
 *
 * @author djabry
 */
public class DJPair extends AbstractLookup.Pair{
    
    private static final IDGenerator idGen = Lookup.getDefault().lookup(IDGenerator.class);
    
    
    private final Object o;
    private final String id;
    
    public DJPair(Object o){
        this.o=o;
        this.id= idGen.generateID();
    }
    
    public Object getObject(){
        
        return this.o;
        
    }

    @Override
    protected boolean instanceOf(Class type) {
        return type.isAssignableFrom(o.getClass());
    }

    @Override
    protected boolean creatorOf(Object o) {
        return false;
    }

    @Override
    public Object getInstance() {
       return o;
    }

    @Override
    public Class getType() {
        return o.getClass();
    }

    @Override
    public String getId() {
       return this.id;
    }

    @Override
    public String getDisplayName() {
        return o.toString();
    }
    
}
