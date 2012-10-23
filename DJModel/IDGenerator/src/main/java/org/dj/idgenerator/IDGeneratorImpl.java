/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.idgenerator;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javolution.util.FastSet;
import org.dj.idgenerator.api.IDGenerator;
import org.dj.idgenerator.api.ObjectIDGenerator;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Template;
import org.openide.util.lookup.ServiceProvider;



/**
 *
 * @author djabry
 */
@ServiceProvider (service = IDGenerator.class)
public class IDGeneratorImpl implements IDGenerator{
    
    private Set<ObjectIDGenerator> gens;
    
    public IDGeneratorImpl(){

        gens = new FastSet<ObjectIDGenerator>();
        gens.addAll(Lookup.getDefault().lookup(new Template(ObjectIDGenerator.class)).allInstances());
        
    }
    
    private Random random = new Random();

    @Override
    public String generateID() {

        return UUID.randomUUID().toString();
    }

    @Override
    public Object generateID(Object obj) {
        
        Iterator<ObjectIDGenerator> iterator = gens.iterator();
        
        while (iterator.hasNext()){
            ObjectIDGenerator next = iterator.next();
            
            if(next.filter(obj)){
                
                return next.generateID(obj);
            }
        }
        
        return generateID();
    }

    @Override
    public boolean filter(Object obj) {
        return true;
    }

  
    
}
