/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertyeditor.api;

import org.dj.propertyeditor.api.Password;

/**
 *
 * @author djabry
 */
public final class PasswordImpl implements Password{
    
    private String passwordText;
    
    public PasswordImpl(String t){
        
        this.passwordText=t;
    }
    
    public void setPasswordText(String text){
        
        this.passwordText=text;
    }

    @Override
    public String getPasswordText() {
        return this.passwordText;
    }
    
}
