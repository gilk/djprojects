/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.jogl;

import fr.iscpif.jogl.JOGLWrapper;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        JOGLWrapper.init();
        System.out.println("JOGL Wrapper started");
        // TODO
    }
}
