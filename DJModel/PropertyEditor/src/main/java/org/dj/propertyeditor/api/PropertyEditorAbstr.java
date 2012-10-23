/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertyeditor.api;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.dj.property.api.DJProperty;
import org.dj.property.api.PropertyAbstr;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;

/**
 *
 * @author djabry
 */
public abstract class PropertyEditorAbstr<C extends JComponent> extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory{

    private InplaceEditor iE;
    public abstract Object getInplaceComponentValue();
    public abstract void setInplaceComponentValue(Object value);
    public abstract boolean isTextEntrySupported();
    public abstract void addCustomActionListener(ActionListener aL);
    public abstract void removeCustomActionListener(ActionListener aL);
    public abstract C getInplaceComponent();

    
    
    @Override
    public void attachEnv(PropertyEnv pe) {
        
        pe.registerInplaceEditorFactory(this);
       
        
    }

    
    @Override
    public InplaceEditor getInplaceEditor() {
        if(this.iE==null){
            
            iE = new CustomInplaceEditor();
        }
        return iE;
    }
    
    
    private class CustomInplaceEditor implements InplaceEditor{
        
        private PropertyEditor editor=null;
        private PropertyModel model = null;

        @Override
        public void connect(PropertyEditor pe, PropertyEnv pe1) {
            this.editor=pe;
            reset();
        }

        @Override
        public JComponent getComponent() {
            return getInplaceComponent();
        }

        @Override
        public void clear() {
            editor=null;
            model= null;
        }

        @Override
        public Object getValue() {
            return getInplaceComponentValue();
        }

        @Override
        public void setValue(Object o) {
           setInplaceComponentValue(o);
        }

        @Override
        public boolean supportsTextEntry() {
            return isTextEntrySupported();
        }

        @Override
        public void reset() {
            setValue(editor.getValue());
        }

        @Override
        public void addActionListener(ActionListener al) {
            addCustomActionListener(al);
        }

        @Override
        public void removeActionListener(ActionListener al) {
            removeCustomActionListener(al);
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            return new KeyStroke[0];
        }

        @Override
        public PropertyEditor getPropertyEditor() {
           return editor;
        }

        @Override
        public PropertyModel getPropertyModel() {
            return this.model;
        }

        @Override
        public void setPropertyModel(PropertyModel pm) {
           this.model=pm;
        }

        @Override
        public boolean isKnownComponent(Component cmpnt) {
            
            JComponent component = this.getComponent();
            return component==cmpnt||component.isAncestorOf(cmpnt);
        }
        
        
        
        
    }
    
}
