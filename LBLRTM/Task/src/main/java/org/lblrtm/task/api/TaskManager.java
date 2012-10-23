/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import java.io.Closeable;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManager;

/**
 *
 * @author djabry
 */
public interface TaskManager extends DJObjectManager,DJNodeObject.Source, Closeable{
    
}
