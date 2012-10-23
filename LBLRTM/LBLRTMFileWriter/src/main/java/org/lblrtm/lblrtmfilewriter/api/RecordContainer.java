/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import java.util.List;
import org.lblrtm.lblrtmfilewriter.api.Record;

/**
 *
 * @author djabry
 */
public interface RecordContainer extends Record{
    
    List<Record> getRecords();
    
}
