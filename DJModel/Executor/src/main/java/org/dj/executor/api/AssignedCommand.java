/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.executor.api;

import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public interface AssignedCommand extends Command, HostInfo.Provider,Runnable{
    void setHostInfo(HostInfo hI);
}
