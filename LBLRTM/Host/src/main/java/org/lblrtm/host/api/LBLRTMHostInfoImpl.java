/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.host.api;

import java.io.File;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.dj.db.api.DBObjectAbstr;
import org.dj.property.api.DJProperty;
import org.dj.property.api.PropertyAbstr;
import org.dj.propertyeditor.api.Password;
import org.dj.propertyeditor.api.PasswordImpl;
import org.lblrtm.host.api.LBLRTMHostInfo;

/**
 *
 * @author djabry
 */
@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
public class LBLRTMHostInfoImpl extends DBObjectAbstr implements LBLRTMHostInfo{
    
    public LBLRTMHostInfoImpl(){
        
        DJProperty localProp = getPropertyFactory().createProperty(PROP_LOCAL, false);
        localProp.setDisplayName("Local host");
        putProperty(localProp);

        DJProperty serverProp = new PropertyAbstr(PROP_SERVER_NAME, ""){

            @Override
            public boolean isCanWrite() {
                if(isLocal()){
                    
                    return false;
                }
                
                return true;
            } 
        };
        
        serverProp.setDisplayName("Server name");
        putProperty(serverProp);
        
        DJProperty userNameProp = new PropertyAbstr(PROP_USER_NAME, ""){

            @Override
            public boolean isCanWrite() {
                if(isLocal()){
                    
                    return false;
                }
                
                return true;
            } 
        };
        
        userNameProp.setDisplayName("User name");
        putProperty(userNameProp);

        
        DJProperty passwordProp = new PropertyAbstr(PROP_PASSWORD, ""){

            @Override
            public boolean isCanWrite() {
                if(isLocal()){
                    
                    return false;
                }
                
                return true;
            }

            @Override
            public Object getValue() {
                return ((PasswordImpl)super.getValue()).getPasswordText();
            }

            @Override
            public boolean setValue(Object value) {
                Object val = super.getValue();
                
                if(val==null||val instanceof String){
                    
                    super.setValue(new PasswordImpl((String)value));
                }
                
                ((PasswordImpl)super.getValue()).setPasswordText((String)value);
                return true;
            }

            @Override
            public Object getDisplayValue() {
                return super.getValue();
            }
            
            
            
            

        };
        
        passwordProp.setDisplayName("Password");
        putProperty(passwordProp);

        
        DJProperty exProp = this.getPropertyFactory().createProperty(PROP_EXECUTABLE, null);
        exProp.setDisplayName("Executable file");
        exProp.setCanWrite(true);
        this.putProperty(exProp);
        
        DJProperty workingDirProp = this.getPropertyFactory().createProperty(PROP_WORKING_DIR, null);
        workingDirProp.setDisplayName("Working directory");
        workingDirProp.setCanWrite(true);
        this.putProperty(workingDirProp);
        
        
    }
 

    @Transient
    @Override
    public File getWorkingDir() {
        return (File) this.getPropertyValue(PROP_WORKING_DIR);
    }
    
    
    @Override
    public void setWorkingDir(File dir){
        this.setPropertyValue(PROP_WORKING_DIR, dir);
        
    }
    
    private File convertToFile(String path){
        
        return new File(path);
        
    }
    
    private String convertFromFile(File f){
        
        return f.getPath();
        
    }
    
    public String getExecutablePath(){
        
        return convertFromFile(getExecutable());
    }
    
    public void setExecutablePath(String path){
        
        this.setExecutable(convertToFile(path));
    }
    
    public String getWorkingDirPath(){
        
        return convertFromFile(getWorkingDir());
        
    }
    
    public void setWorkingDirPath(String path){
        
        this.setWorkingDir(convertToFile(path));
    }

    @Transient
    @Override
    public File getExecutable() {
        return (File) this.getPropertyValue(PROP_EXECUTABLE);
    }
    
    @Override
    public void setExecutable(File ex){
        
        this.setPropertyValue(PROP_EXECUTABLE, ex);
    }

    @Override
    public String getServerName() {
        return (String)this.getPropertyValue(PROP_SERVER_NAME);
    }
    
    @Override
    public void setServerName(String name){
        
        this.setPropertyValue(PROP_SERVER_NAME, name);
    }

    @Override
    public String getPassword() {
        return (String)this.getPropertyValue(PROP_PASSWORD);
    }
    
    @Override
    public void setPassword(String password){
        
        this.setPropertyValue(PROP_PASSWORD, password);
    }

    @Override
    public String getUserName() {
       return (String)this.getPropertyValue(PROP_USER_NAME);
    }
    
    @Override
    public void setUserName(String userName){
        
        this.setPropertyValue(PROP_USER_NAME, userName);
    }

    @Override
    public boolean isLocal() {
        return (Boolean)this.getPropertyValue(PROP_LOCAL);
    }
    
    @Override
    public void setLocal(boolean local){
        
        this.setPropertyValue(PROP_LOCAL, local);
    }
    
}
