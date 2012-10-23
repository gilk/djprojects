/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.render.api;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractScheduledService;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.markers.Marker;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javolution.util.FastSet;
import lombok.LazyGetter;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.listener.api.ListenerAbstr;
import org.dj.worldwindmodel.globe.api.GlobeManager;
import org.dj.worldwindmodel.object.api.WWDJObject;
import org.dj.worldwindmodel.worldwindow.api.WorldWindowService;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class RenderableRefresher extends AbstractScheduledService{

    
    @Override
    protected void startUp(){
        
        sync();
        this.gM.addPropertyChangeListener(DJNodeObject.MESSAGE_CHILD_ADDED,new ObjectAdder());
        this.gM.addPropertyChangeListener(DJNodeObject.MESSAGE_CHILD_REMOVED, new ObjectRemover());
    }
    
    @Override
    protected void shutDown(){
        
        
        
    }
    
    
    
    
    @Override
    protected void runOneIteration() throws Exception {
        System.out.println("Running marker update iteration");
        updateMarkers();
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(3, 2, TimeUnit.SECONDS);
    }
    


    private class ObjectAdder extends ListenerAbstr {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(DJNodeObject.MESSAGE_CHILD_ADDED)) {
                DJNodeObject newValue = (DJNodeObject) evt.getNewValue();
                addObject(newValue);
            }

        }
    }

    private class ObjectRemover extends ListenerAbstr {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(DJNodeObject.MESSAGE_CHILD_REMOVED)) {
                DJNodeObject newValue = (DJNodeObject) evt.getNewValue();
                removeObject(newValue);
            }

        }
    }
    private final GlobeManager gM;
    private static final RenderableService rS = Lookup.getDefault().lookup(RenderableService.class);
    private static final WorldWindowService wS = Lookup.getDefault().lookup(WorldWindowService.class);

    
    private final Set<Marker> markersToAdd = new FastSet<Marker>();
    private final Set<Marker> markersToRemove = new FastSet<Marker>();
    
    @LazyGetter
    private RenderableLayer renderableLayer = addNewRenderableLayer();
    @LazyGetter
    private MarkerLayer markerLayer = addNewMarkerLayer();

    private RenderableLayer addNewRenderableLayer() {

        RenderableLayer r = new RenderableLayer();
        wS.createObject(gM).getModel().getLayers().add(r);
        return r;
    }

    private MarkerLayer addNewMarkerLayer() {

        MarkerLayer m = new MarkerLayer();
        wS.createObject(gM).getModel().getLayers().add(m);
        return m;

    }

    public RenderableRefresher(GlobeManager mgr) {

        gM = mgr;

    }

    private void sync() {

        this.getMarkerLayer().setMarkers(new HashSet<Marker>());
        this.getRenderableLayer().removeAllRenderables();
        this.addObjects(gM.getChildren());
    }

    private void addObject(DJNodeObject dobj) {

        if (!(dobj instanceof WWDJObject)) {

            return;
        }

        WWDJObject obj = (WWDJObject) dobj;
        RenderableController rC = rS.createObject(obj);
        Iterator iterator = rC.getRenderables().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();

            if (next instanceof Marker) {
                Marker marker = (Marker) next;
                
                this.addMarker(marker);
                

            } else if (next instanceof Renderable) {
                Renderable renderable = (Renderable) next;
                this.getRenderableLayer().addRenderable(renderable);
            }
        }

    }
    
    
    private synchronized void addMarker(Marker m){
        
        this.markersToAdd.add(m);
    }
    
    private synchronized void removeMarker(Marker m){
        
        this.markersToRemove.add(m);
        
    }

    private void addObjects(Collection<? extends DJNodeObject> objs) {
        Set<Marker> markers = new FastSet<Marker>();
        Set<Renderable> renderables = new FastSet<Renderable>();
        Iterator<? extends DJNodeObject> iterator = objs.iterator();
        while (iterator.hasNext()) {
            DJNodeObject next = iterator.next();
            if (next instanceof WWDJObject) {
                WWDJObject obj = (WWDJObject) next;
                RenderableController rC = rS.createObject(obj);
                Iterator iterator1 = rC.getRenderables().iterator();
                while (iterator1.hasNext()) {
                    Object next1 = iterator1.next();
                    if (next1 instanceof Renderable) {
                        Renderable renderable = (Renderable) next1;
                        renderables.add(renderable);
                    } else if (next1 instanceof Marker) {
                        Marker marker = (Marker) next1;
                        markers.add(marker);
                    }

                }
            }

        }

        getMarkerLayer().setMarkers(Iterables.concat(this.getMarkerLayer().getMarkers(), markers));
        getRenderableLayer().addRenderables(renderables);


    }

    private void removeObjects(Collection<? extends WWDJObject> objs) {

        Set<Marker> markers = new FastSet<Marker>();
        Set<Renderable> renderables = new FastSet<Renderable>();
        Iterator<? extends DJNodeObject> iterator = objs.iterator();
        while (iterator.hasNext()) {
            DJNodeObject next = iterator.next();
            if (next instanceof WWDJObject) {
                WWDJObject obj = (WWDJObject) next;
                RenderableController rC = rS.createObject(obj);
                Iterator iterator1 = rC.getRenderables().iterator();
                while (iterator1.hasNext()) {
                    Object next1 = iterator1.next();
                    if (next1 instanceof Renderable) {
                        Renderable renderable = (Renderable) next1;
                        renderables.add(renderable);
                    } else if (next1 instanceof Marker) {
                        Marker marker = (Marker) next1;
                        markers.add(marker);
                    }

                }
            }

        }
        HashSet<Marker> newMarkers = Sets.newHashSet(getMarkerLayer().getMarkers());
        newMarkers.removeAll(markers);
        getMarkerLayer().setMarkers(newMarkers);
        Iterator<Renderable> iterator1 = renderables.iterator();
        RenderableLayer renderableLayer1 = this.getRenderableLayer();
        while (iterator1.hasNext()) {
            Renderable next = iterator1.next();
            renderableLayer1.removeRenderable(next);
        }



    }

    private void removeObject(DJNodeObject dobj) {
        if (!(dobj instanceof WWDJObject)) {

            return;
        }

        WWDJObject obj = (WWDJObject) dobj;
        RenderableController rC = rS.createObject(obj);
        Iterator iterator = rC.getRenderables().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();

            if (next instanceof Marker) {
                Marker marker = (Marker) next;
                this.removeMarker(marker);

            } else if (next instanceof Renderable) {
                Renderable renderable = (Renderable) next;
                this.getRenderableLayer().removeRenderable(renderable);
            }
        }


    }
    
    private synchronized void updateMarkers(){
        
        if((!this.markersToAdd.isEmpty())||(!this.markersToRemove.isEmpty())){
            HashSet<Marker> markers = Sets.newHashSet(this.getMarkerLayer().getMarkers());
            markers.addAll(markersToAdd);
            markers.removeAll(markersToRemove);
            this.getMarkerLayer().setMarkers(markers);
            markersToAdd.clear();
            markersToRemove.clear();
            
        }
    }
}