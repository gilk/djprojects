/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.node.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObject;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author djabry
 */
public class DJNodeChildFactory extends ChildFactory<DJNodeObject> implements PropertyChangeListener {

    private DJNodeObject obj;
    private final long delay = 200;
    public DJNodeChildFactory(DJNodeObject obj) {

        this.obj = obj;
        obj.addPropertyChangeListener(this);

    }

    @Override
    protected boolean createKeys(List<DJNodeObject> toPopulate) {
        

        ProgressHandle handle = ProgressHandleFactory.createHandle("Loading...");

        handle.start(100);

        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
        }
        handle.progress(25);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
        }
        handle.progress(50);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
        }
        handle.progress(75);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
        }
        handle.finish();


        Comparator<DJObject> comp = new Comparator<DJObject>(){

            @Override
            public int compare(DJObject t, DJObject t1) {
                return t.getName().compareTo(t1.getName());
            }
        };

        Set<DJNodeObject> children = obj.getChildren();

        if (children != null) {
            
            try{
                toPopulate.addAll(children);
                Collections.sort(toPopulate, comp);
                System.out.println("Sorting keys");
                
                
            }catch(Exception e){
                System.out.println(e);
            }

            

        }



        return true;
    }

    @Override
    protected Node[] createNodesForKey(DJNodeObject key) {
        return new Node[]{new DJNode(key)};
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals(DJNodeObject.PROP_CHILDREN)) {

            this.refresh(false);
            
        }
    }
}
