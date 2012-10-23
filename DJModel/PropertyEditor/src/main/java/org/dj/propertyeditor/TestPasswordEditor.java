/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertyeditor;

import java.beans.PropertyEditorSupport;
import javax.swing.JPasswordField;
import org.dj.propertyeditor.api.PasswordImpl;

/**
 *
 * @author djabry
 */
public class TestPasswordEditor extends PropertyEditorSupport{
    
    private final char passChar;
    
    public TestPasswordEditor(){
        
        passChar = (new JPasswordField()).getEchoChar();
        
    }
    
    



    @Override
    public String getAsText() {
        
        String pwd = ((PasswordImpl)getValue()).getPasswordText();
        
        char[] password = new char[pwd.length()];
        for(int i = 0;i<password.length;i++){
            
            password[i]=passChar;
        }
        
        return new String(password);
    }
    
    
    @Override
    public void setAsText(String text){
        
        String oldText = this.getAsText();
        
        char[] password = new char[text.length()];
        
        
        for(int i = 0;i<text.length();i++){
            
            char currChar = text.charAt(i);
            
            if(currChar == passChar&&i<oldText.length()){
                
                currChar = oldText.charAt(i);
            }
            
            password[i]=currChar;
        }
        
        this.setValue(new String(password));
        
        
    }
    
    
    
    
    
    
    
}
