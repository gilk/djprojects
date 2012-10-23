/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertyeditor;

import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import org.dj.propertyeditor.api.PasswordImpl;
import org.dj.propertyeditor.api.PropertyEditorAbstr;
import org.openide.explorer.propertysheet.InplaceEditor;

/**
 *
 * @author djabry
 */
public class PasswordPropertyEditor2 extends PropertyEditorAbstr{
    
    private final JPasswordField pF; 
    private final char passChar;
    
    public PasswordPropertyEditor2(){
        super();
        this.pF= new JPasswordField();
        this.passChar = pF.getEchoChar();
        this.pF.setActionCommand(InplaceEditor.COMMAND_SUCCESS);
    }


    @Override
    public Object getInplaceComponentValue() {
        JPasswordField f = (JPasswordField) this.getInplaceComponent();
        return new PasswordImpl(new String(f.getPassword()));
    }

    @Override
    public void setInplaceComponentValue(Object value) {
        JPasswordField f = (JPasswordField) this.getInplaceComponent();
        f.setText(((PasswordImpl)value).getPasswordText());
    }

    @Override
    public boolean isTextEntrySupported() {
        return false;
    }
    
    @Override
    public String getAsText() {
        
        String pwd = ((PasswordImpl)this.getInplaceComponentValue()).getPasswordText();
        
        char[] password = new char[pwd.length()];
        for(int i = 0;i<password.length;i++){
            
            password[i]=passChar;
        }
        
        return new String(password);
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        ((PasswordImpl)this.getInplaceComponentValue()).setPasswordText(string);
    }


    @Override
    public void addCustomActionListener(ActionListener aL) {
        ((JPasswordField)this.getInplaceComponent()).addActionListener(aL);
    }

    @Override
    public void removeCustomActionListener(ActionListener aL) {
       ((JPasswordField)this.getInplaceComponent()).removeActionListener(aL);
    }

    
    @Override
    public JComponent getInplaceComponent() {
       
        return pF;
    }
    
}
