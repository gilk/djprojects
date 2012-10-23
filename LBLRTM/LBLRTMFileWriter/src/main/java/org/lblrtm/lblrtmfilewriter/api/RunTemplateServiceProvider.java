/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import org.dj.service.api.Service;

/**
 *
 * @author djabry
 */
public interface RunTemplateServiceProvider<R extends RunTemplate> extends Service.Provider<R,DJTAPE5>{
    

    String getTemplateName();
    
}
