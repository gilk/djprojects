/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.globeviewer;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import java.awt.EventQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.worldwindmodel.globe.api.GlobeManager;
import org.dj.worldwindmodel.object.api.WWDJObject;
import org.dj.worldwindmodel.render.api.RenderableController;
import org.dj.worldwindmodel.render.api.RenderableService;
import org.dj.worldwindmodel.worldwindow.api.WorldWindowService;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author djabry
 */
public class WWDJObjectSelectionListener implements LookupListener {

    private static final Double DEFAULT_DISTANCE = 1e5;
    private final GlobeManager mgr;
    private final Result<WWDJObject> selectedObjects = Utilities.actionsGlobalContext().lookupResult(WWDJObject.class);
    private static final WorldWindowService wS = Lookup.getDefault().lookup(WorldWindowService.class);
    private static final RenderableService rS = Lookup.getDefault().lookup(RenderableService.class);
    public WWDJObjectSelectionListener(GlobeManager mgr) {
        this.mgr = mgr;
        selectedObjects.addLookupListener(this);
    }
    
    private final Runnable viewUpdater = new Runnable() {
        @Override
        public void run() {
            Iterator<DJNodeObject> iterator1 = mgr.getChildren().iterator();
            
            
            while(iterator1.hasNext()){
                DJNodeObject next = iterator1.next();
                if(next instanceof WWDJObject){
                    WWDJObject w = (WWDJObject) next;
                    rS.createObject(w).setHighlighted(false);
                }
            }
            

            Collection<? extends WWDJObject> allInstances = selectedObjects.allInstances();
            if (allInstances.size() == 1) {
                Set<DJNodeObject> children = mgr.getChildren();
                Iterator<? extends WWDJObject> iterator = allInstances.iterator();
                WorldWindow wW = wS.createObject(mgr);
                BasicOrbitView v = (BasicOrbitView) wW.getView();
                double d = DEFAULT_DISTANCE;

                if (v.getEyePosition() != null) {
                    d = v.getEyePosition().getAltitude();
                }
                
                

                while (iterator.hasNext()) {
                    WWDJObject next = iterator.next();
                    if (children.contains(next)) {
                        RenderableController rC = rS.createObject(next);
                        rC.setHighlighted(true);
                        
                        Position position = next.getPosition();
                        Position to = Position.fromDegrees(position.latitude.degrees, position.longitude.degrees,d);
                        //v.addEyePositionAnimator(3000, position, to);
                        
                        v.goTo(to, d);
                        
                        
                    }
                }
            }
            
            wS.createObject(mgr).firePropertyChange(AVKey.LAYER, null, null);
        }
    };

    @Override
    public void resultChanged(LookupEvent le) {
        
        EventQueue.invokeLater(viewUpdater);
    }
}
