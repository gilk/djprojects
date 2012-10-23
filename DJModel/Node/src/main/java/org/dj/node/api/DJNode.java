/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.node.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import org.dj.cookies.api.CookieService;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObject;
import org.dj.icon.api.IconService;
import org.dj.propertysheetgenerator.api.PropertySheetGenerator;
import org.dj.service.api.Service;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author djabry
 */
public class DJNode extends AbstractNode implements PropertyChangeListener {
    private static final PropertySheetGenerator gen 
            = Lookup.getDefault().lookup(PropertySheetGenerator.class);
    
    private static final IconService iS = Lookup.getDefault().lookup(IconService.class);
    private static final CookieService cS = Lookup.getDefault().lookup(CookieService.class);

    private final DJNodeObject obj;

    @Override
    public Sheet createSheet() {

        
        return gen.generatePropertySheet(getObject());

    }

    public DJNode(DJNodeObject obj) {

        this(obj,new ProxyLookup(cS.createObject(obj).getLookup(),Lookups.singleton(obj)));
    }

    public DJNode(DJNodeObject obj, Lookup lookup) {

        super(obj instanceof DJNodeObject
                ? Children.create(new DJNodeChildFactory((DJNodeObject) obj), true) : Children.LEAF, lookup);

        
        this.obj = obj;
        this.setName(obj.getId());
        this.setDisplayName(obj.getName());
        this.setIconBaseWithExtension(iS.getIconBaseFor(obj));
        
        this.obj.addPropertyChangeListener(this,PROP_NAME);

    }
    
        static List<? extends Action> registeredActions;

    protected static List<? extends Action> getRegisteredActions() {
        if (registeredActions == null) {
            registeredActions = Utilities.actionsForPath("Actions/DJModel");


        }
        return registeredActions;

    }

    @Override
    public String getHtmlDisplayName() {
        return this.obj.getHtmlDisplayName();
    }
    
    
    
    @Override
    public Action getPreferredAction() {
        
        
        Action[] actions = getActions(true);

        if(actions.length!=0){
            
            for(Action a:actions){
                if(((String)a.getValue(Action.NAME)).toLowerCase().contains("edit")){
                    return a;
                }
            
            }
            
            
            return actions[actions.length-1];
        }
        
        return null;
        
    }

    @Override
    public Action[] getActions(boolean context) {

        List<Action> actions = new ArrayList<Action>();
        
        actions.addAll(getRegisteredActions());

        actions.addAll(Arrays.asList(super.getActions(context)));
        
        return actions.toArray(new Action[actions.size()]);

    }

    public DJObject getObject() {

        return obj;
    }
    

    

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals(DJObject.PROP_NAME)) {

            this.setDisplayName(this.obj.getName());
        }
    }
}
