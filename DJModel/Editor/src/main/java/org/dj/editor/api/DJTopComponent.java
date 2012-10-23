/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.editor.api;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.util.Map;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.DefaultEditorKit;
import javolution.util.FastMap;
import org.dj.cookies.api.CookieService;
import org.dj.domainmodel.api.DJObject;
import org.dj.editor.api.EditorController.Provider;
import org.dj.listener.api.Listener;
import org.dj.listener.api.ListenerAbstr;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author djabry
 */
public class DJTopComponent extends TopComponent {

    private static Map<EditorController, DJTopComponent> tCByController;
    private static Map<DJObject, EditorController> eCByObj;
    private static final EditorService eS = Lookup.getDefault().lookup(EditorService.class);
    private final DJObject obj;
    private Lookup lkp;
    private static final CookieService cS = Lookup.getDefault().lookup(CookieService.class);

    @Override
    public String getName() {
        return obj.getName();
    }

    private class TCUpdater extends ListenerAbstr {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {

            if (pce.getPropertyName().equals(Listener.MESSAGE_DELETED)) {
                DJObject source = (DJObject) pce.getSource();

                DJTopComponent t = findInstance(source);

                if (t != null) {

                    EditorController ec = getECByObj().get(source);
                    getECByObj().remove(source);
                    getTCByController().remove(ec);

                    t.close();

                }
            } else if (pce.getPropertyName().equals(DJObject.PROP_NAME)) {
                //revalidate();
            }

            super.propertyChange(pce);
        }
    }

    public static DJTopComponent findInstance(DJObject obj) {

        EditorController eC = getECByObj().get(obj);


        if (eC != null) {

            return getTCByController().get(eC);


        } else {

            return new DJTopComponent(obj);
        }



    }

    public DJTopComponent(DJObject obj) {

        this.obj = obj;


        EditorController eC = eS.createObject(obj);

        if (eC != null) {
            if (!getTCByController().containsKey(eC)) {

                //this.setName(obj.getName());
                this.addEditorToComponent(this, eC);

                JComponent component = eC.getComponent();

                if (component instanceof Lookup.Provider) {
                    Lookup.Provider provider = (Lookup.Provider) component;
                    this.lkp = provider.getLookup();
                } else {

                    this.lkp = new ProxyLookup(new Lookup[]{Lookups.singleton(obj), cS.createObject(obj).getLookup()});

                }

                this.associateLookup(lkp);

                this.obj.addPropertyChangeListener(new TCUpdater());

                //init();

            }

        }



    }

    public void addEditorToComponent(JComponent c, EditorController eC) {

        c.setSize(new java.awt.Dimension(100, 100));
        c.setLayout(new java.awt.GridLayout(1, 1, 5, 5));
        c.add(eC.getComponent());
        getTCByController().put(eC, this);
        getECByObj().put(eC.getObject(), eC);


    }

//    @Override
//    protected void componentActivated() {
//        ActionMap actionMap = getActionMap();
//        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
//        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
//        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
//
//        super.componentActivated();
//    }
//
//    @Override
//    protected void componentDeactivated() {
//        ActionMap actionMap = getActionMap();
//        actionMap.put(DefaultEditorKit.copyAction,
//                SystemAction.get(org.openide.actions.CopyAction.class));
//
//        actionMap.put(DefaultEditorKit.cutAction,
//                SystemAction.get(org.openide.actions.CutAction.class));
//
//        actionMap.put(DefaultEditorKit.pasteAction,
//                SystemAction.get(org.openide.actions.PasteAction.class));
//
//        super.componentDeactivated();
//    }

    //Run this method in subclasses after components are initialised
    protected void init() {

        for (Component c : this.getComponents()) {

            if (c instanceof EditorController.Provider) {

                Provider provider = (Provider) c;

                if (provider instanceof JComponent) {
                    JComponent jComponent = (JComponent) provider;
                    EditorController editorController = provider.getEditorController();
                    addEditorToComponent(jComponent, editorController);
                }
            }
        }

    }

    public static Map<EditorController, DJTopComponent> getTCByController() {

        if (tCByController == null) {

            tCByController = new FastMap<EditorController, DJTopComponent>();

        }

        return tCByController;


    }

    public static Map<DJObject, EditorController> getECByObj() {

        if (eCByObj == null) {

            eCByObj = new FastMap<DJObject, EditorController>();

        }

        return eCByObj;
    }
}
