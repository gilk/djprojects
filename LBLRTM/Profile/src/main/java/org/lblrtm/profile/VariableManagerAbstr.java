/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile;

import java.util.*;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.listener.api.ListenerAbstr;
import org.lblrtm.lblrtmdata.api.Molecule;
import org.lblrtm.profile.api.PhysicalProperty;
import org.lblrtm.profile.api.VariableManager;

/**
 *
 * @author djabry
 */
public class VariableManagerAbstr extends ListenerAbstr implements VariableManager{
    
    private static final Comparator<String> cmp = new Comparator<String>() {

        @Override
        public int compare(String t, String t1) {
            return order.get(t).compareTo(order.get(t1));
        }
    };
    
    private void fillMaps(){
        
        if(molsMap.isEmpty()){
            
            for(PhysicalProperty p: PhysicalProperty.values()){
                String var = p.name();
                order.put(var, p.ordinal());
                physPropMap.put(var, p);
                variableFullNames.put(var,p.fullName); 
 
            }
            
            
            for(Molecule m:Molecule.values()){
                
                String var= m.name();
                order.put(var, m.ordinal()+PhysicalProperty.values().length);
                molsMap.put(var, m);
                variableFullNames.put(var, m.getName());
            }
            
            
        }
    }
    
    
    public VariableManagerAbstr(Set<String> vars){
        
        fillMaps();
        Iterator<String> iterator = vars.iterator();
        
        while(iterator.hasNext()){
            this.addVariable(iterator.next());
        }
        
    }
    
    public VariableManagerAbstr(){
        
        this(new HashSet(Arrays.asList(new String[]{PhysicalProperty.ALTITUDE.name(),
        PhysicalProperty.PRESSURE.name(),
        PhysicalProperty.TEMPERATURE.name()})));
    }
    
    
    
    private final Map<String,Enum> variables= new TreeMap<String,Enum>(cmp);
    private static final Map<String,Integer> order = new FastMap<String,Integer>();
    private static final Map<String,Molecule> molsMap = new FastMap<String,Molecule>();
    private static final Map<String,PhysicalProperty> physPropMap = new FastMap<String,PhysicalProperty>();
    private static final Map<String,String> variableFullNames = new FastMap<String,String>();

    @Override
    public Set<String> getVariables() {
        return variables.keySet();
    }
   
    

    @Override
    public void addVariable(String variable) {
        
       Enum o = molsMap.get(variable);
       
       if(o==null){
           
           o = physPropMap.get(variable);
       }
        
       if(o!=null){
            this.variables.put(variable, o);
            
            this.firePropertyChange(DJNodeObject.PROP_CHILDREN,null,null);
            
       
       }
 
    }

    @Override
    public void removeVariable(String variable) {
        this.variables.remove(variable);
        this.firePropertyChange(DJNodeObject.PROP_CHILDREN,null,null);
    }

    @Override
    public String getVariableFullName(String var) {
        return variableFullNames.get(var);
    }

    @Override
    public VariableManager duplicate() {
        return new VariableManagerAbstr(this.getVariables());
    }
    

    @Override
    public Set<Molecule> getMolecules() {

        Set<Molecule> molecules = new FastSet<Molecule>();
        Iterator<String> iterator = this.getVariables().iterator();
        
        while(iterator.hasNext()){
            String v = iterator.next();
            Molecule m = molsMap.get(v);
            
            if(m!=null){
                
                molecules.add(m);
            }
        }
        
        return molecules;

    }
    
    

    @Override
    public Set<PhysicalProperty> getPhysicalProperties() {
        Set<PhysicalProperty> props = new FastSet<PhysicalProperty>();
        Iterator<String> iterator = this.getVariables().iterator();
        
        while(iterator.hasNext()){
            String v = iterator.next();
            PhysicalProperty p = physPropMap.get(v);
            
            if(p!=null){
                
                props.add(p);
            }
        }
        
        return props;
    }

    @Override
    public void addMolecule(Molecule m) {
        this.addVariable(m.name());
    }

    @Override
    public void addPhysicalProperty(PhysicalProperty p) {
        this.addVariable(p.name());
    }

    @Override
    public void removeMolecule(Molecule m) {
        this.removeVariable(m.name());
    }

    @Override
    public void removePhysicalProperty(PhysicalProperty p) {
        this.removeVariable(p.name());
    }

    @Override
    public Enum getVariableEnum(String variable) {
       return this.variables.get(variable);
    }
    
    
    
}
