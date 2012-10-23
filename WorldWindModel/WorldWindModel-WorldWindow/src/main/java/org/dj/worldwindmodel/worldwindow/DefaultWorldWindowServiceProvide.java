/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.worldwindow;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwindx.examples.util.HighlightController;
import gov.nasa.worldwindx.examples.util.ToolTipController;
import org.dj.service.api.ServiceProviderAbstr;
import org.dj.worldwindmodel.globe.api.GlobeManager;
import org.dj.worldwindmodel.worldwindow.api.WorldWindowServiceProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = WorldWindowServiceProvider.class)
public class DefaultWorldWindowServiceProvide extends ServiceProviderAbstr<WorldWindow, GlobeManager> implements WorldWindowServiceProvider {

    //private final Map<GlobeManager, WorldWindowGLCanvas> m = new FastMap<GlobeManager, WorldWindowGLCanvas>();
    @Override
    public boolean filter(GlobeManager obj) {
        return true;
    }

    @Override
    public WorldWindow createObject(GlobeManager obj) {

        //WorldWindowGLCanvas c = m.get(obj);

        //if (c == null) {

        System.out.println("Creating new worldwindow");
        WorldWindowGLCanvas c = new WorldWindowGLCanvas();
        c.setModel(new BasicModel());
        ViewControlsLayer viewControlsLayer = new ViewControlsLayer();

        // Add controllers to manage highlighting and tool tips.
        ToolTipController toolTipController = new ToolTipController(c, AVKey.DISPLAY_NAME, null);
        HighlightController highlightController = new HighlightController(c, SelectEvent.ROLLOVER);

        LayerList layers = c.getModel().getLayers();
        int compassPosition = 0;
        for (Layer l : layers) {
            if (l instanceof CompassLayer) {
                compassPosition = layers.indexOf(l);
            }
        }
        layers.add(compassPosition, viewControlsLayer);
        c.addSelectListener(new ViewControlsSelectListener(c, viewControlsLayer));


        c.addRenderingExceptionListener(new RenderingExceptionListener() {
            @Override
            public void exceptionThrown(Throwable t) {
                if (t instanceof WWAbsentRequirementException) {
                    String message = "Computer does not meet minimum graphics requirements.\n";
                    message += "Please install up-to-date graphics driver and try again.\n";
                    message += "Reason: " + t.getMessage() + "\n";
                    message += "This program will end when you press OK.";

                    System.exit(-1);
                }
            }
        });

        // m.put(obj, c);

        //}

        return c;

    }
}
