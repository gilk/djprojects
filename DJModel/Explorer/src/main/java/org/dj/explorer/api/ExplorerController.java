/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.explorer.api;

import javax.swing.JComponent;
import org.dj.db.api.DBObject;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.editor.api.EditorController;
import org.dj.listener.api.Listener;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public interface ExplorerController extends EditorController{

    DJNodeObject getRoot();
    void setRoot(DJNodeObject obj);
    
    View getView();
    void setView(View v);
    
    
    
    void add();
    void remove();
    
    JComponent getComponent(Lookup masterLookup);
    JComponent getComponent(ExplorerComponent c);
    
    void setComponentVisible(ExplorerComponent c,boolean visible);
    boolean isComponentVisible(ExplorerComponent c);
    
    void setComponentEnabled(ExplorerComponent c,boolean enabled);
    boolean isComponentEnabled(ExplorerComponent c);
    
    public static final String PROP_EXPLORER_COMPONENT_ENABLED = "explorerComponentEnabled";
    public static final String PROP_EXPLORER_COMPONENT_VISIBLE = "explorerComponentVisible";
    public static final String PROP_VIEW = "view";
    public static final String PROP_ROOT = "root";
    public static final String PROP_CHANGED = "changed";
    public static final String PROP_VALID = "valid";
    
    
    
    
}
