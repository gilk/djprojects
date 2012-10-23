/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.globeviewer;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Service.State;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.EarthFlat;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.SkyColorLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.FlatOrbitView;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.util.Map;
import javolution.util.FastMap;
import lombok.Getter;
import lombok.Setter;
import org.dj.editor.api.EditorControllerAbstr;
import org.dj.listener.api.ListenerAbstr;
import org.dj.worldwindmodel.globe.api.GlobeManager;
import org.dj.worldwindmodel.globeviewer.api.GlobeController;
import org.dj.worldwindmodel.globeviewer.api.Projection;
import org.dj.worldwindmodel.globeviewer.editor.GlobePanel;
import org.dj.worldwindmodel.render.api.RenderableRefresher;
import org.dj.worldwindmodel.worldwindow.api.WorldWindowService;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class GlobeEditorController extends EditorControllerAbstr<GlobeManager> implements GlobeController {

    private GlobePanel p;
    private final RenderableRefresher rR;
    private final WWDJObjectSelectionListener l;
    private static final WorldWindowService wW = Lookup.getDefault().lookup(WorldWindowService.class);
    @Getter
    private Projection projection = Projection.SPHERICAL;

    public GlobeEditorController(GlobeManager m) {

        super(m);

        p = new GlobePanel(m);
        this.setComponent(p);
        this.rR = new RenderableRefresher(m);
        m.addPropertyChangeListener(GlobeManager.PROP_TIME, new TimeUpdater());
        ListenableFuture<State> start = this.rR.start();
        l = new WWDJObjectSelectionListener(m);

    }

    private void updateGlobeWithTime() {
        //Update glboe lighting etc to reflect time
    }

    @Override
    public void setProjection(Projection proj) {


        if (proj.equals(this.projection)) {
            return;
        }

        WorldWindow w = wW.createObject(this.getObject());
        View oldView = w.getView();
        Globe oldGlobe = w.getModel().getGlobe();
        Globe g = null;
        View v = null;

        if (proj.equals(Projection.SPHERICAL)) {

            g = new Earth();
            v = new BasicOrbitView();

            ((BasicOrbitView) v).setCenterPosition(((FlatOrbitView) oldView).getCenterPosition());
            ((BasicOrbitView) v).setZoom(((FlatOrbitView) oldView).getZoom());
            v.setPitch(oldView.getPitch());
            v.setEyePosition(oldView.getEyePosition());
            v.setHeading(oldView.getHeading());
            w.getModel().setGlobe(g);
            w.setView(v);

        } else {

            Globe earthFlat = oldGlobe;
            if (projection.equals(Projection.SPHERICAL)) {
                earthFlat = new EarthFlat();

                v = new FlatOrbitView();
                g = earthFlat;
                ((FlatOrbitView) v).setCenterPosition(((BasicOrbitView) oldView).getCenterPosition());
                ((FlatOrbitView) v).setZoom(((BasicOrbitView) oldView).getZoom());
                v.setPitch(oldView.getPitch());
                v.setEyePosition(oldView.getEyePosition());
                v.setHeading(oldView.getHeading());
                w.getModel().setGlobe(g);
                w.setView(v);


            }

            ((EarthFlat) earthFlat).setProjection(proj.getProjectionString());

        }








        if (proj.equals(Projection.SPHERICAL)) {

            LayerList layers = w.getModel().getLayers();
            for (int i = 0; i < layers.size(); i++) {
                if (layers.get(i) instanceof SkyColorLayer) {
                    layers.set(i, new SkyGradientLayer());
                }
            }


        } else {

            LayerList layers = w.getModel().getLayers();
            for (int i = 0; i < layers.size(); i++) {
                if (layers.get(i) instanceof SkyGradientLayer) {
                    layers.set(i, new SkyColorLayer());
                }
            }

        }
        this.projection = proj;


    }

    private class TimeUpdater extends ListenerAbstr {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {

            if (pce.getPropertyName().equals(GlobeManager.PROP_TIME)) {
                updateGlobeWithTime();

            }
            super.propertyChange(pce);
        }
    }

    @Override
    protected void updateComponentWithValue(Component c, Object o) {
    }

    @Override
    protected Object getComponentValue(Component c) {
        return null;
    }

    @Override
    protected Map<String, Component> getComponentMap() {
        return new FastMap<String, Component>();
    }
}
