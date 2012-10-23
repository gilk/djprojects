/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertyeditor;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;
import org.dj.propertyeditor.api.Password;
import org.dj.propertyeditor.api.PasswordImpl;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;

/**
 *
 * @author djabry
 */


public class PasswordPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory{

    private final char echoChar;
    
    public PasswordPropertyEditor(){
        JPasswordField pf = new JPasswordField();
        this.echoChar= pf.getEchoChar();
        
    }

    @Override
    public String getAsText() {
        char[] passChars =  new char[((PasswordImpl)getValue()).getPasswordText().length()];
        for(int i = 0;i<passChars.length;i++){
            
            passChars[i]=echoChar;
            
            
        }
        
        
        return new String(passChars);
    }
    
    public void setAsText(String text){
        
        
    }
    

    

    @Override
    public InplaceEditor getInplaceEditor() {
        return new PasswordEditor();
    }

    @Override
    public void attachEnv(PropertyEnv pe) {
       pe.registerInplaceEditorFactory(this);
    }
    
    

    private class PasswordEditor implements InplaceEditor {
        
        private JPasswordField p;
        private PropertyEditor pE;
        private PropertyModel pM;
        
        public PasswordEditor(){
            
            this.p=new JPasswordField();
        }
        
        

        @Override
        public void connect(PropertyEditor pe, PropertyEnv pe1) {
            this.pE=pe;
            this.reset();
        }

        @Override
        public JComponent getComponent() {
            return this.p;
        }

        @Override
        public void clear() {
            this.pE=null;
            this.pM=null;
        }

        @Override
        public Object getValue() {
            return new PasswordImpl(new String(this.p.getPassword()));
        }

        @Override
        public void setValue(Object o) {
           this.p.setText(((PasswordImpl)o).getPasswordText());
        }

        @Override
        public boolean supportsTextEntry() {
            return false;
        }

        @Override
        public void reset() {
            PasswordImpl pwd  = (PasswordImpl)this.pE.getValue();
            this.setValue(pwd);
            
        }

        @Override
        public void addActionListener(ActionListener al) {
            
        }

        @Override
        public void removeActionListener(ActionListener al) {
            
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            return new KeyStroke[0];
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            return this.pE;
        }

        @Override
        public PropertyModel getPropertyModel() {
            return this.pM;
        }

        @Override
        public void setPropertyModel(PropertyModel pm) {
            this.pM=pm;
        }

        @Override
        public boolean isKnownComponent(Component cmpnt) {
            return cmpnt== this.p||this.p.isAncestorOf(cmpnt);
            
        }
        
        
    }

 
}
