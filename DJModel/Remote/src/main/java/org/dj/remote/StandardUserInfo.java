/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.remote;

import com.jcraft.jsch.UserInfo;

/**
 *
 * @author djabry
 */
public class StandardUserInfo implements UserInfo {

    private String password;

    public StandardUserInfo(String password) {

        this.password = password;
    }

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean promptPassword(String s) {
        return true;
    }

    @Override
    public boolean promptPassphrase(String s) {
        return true;
    }

    @Override
    public boolean promptYesNo(String s) {
        return true;
    }

    @Override
    public void showMessage(String s) {
        System.out.println("message = " + s);
    }
}
