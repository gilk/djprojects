/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javolution.util.FastSet;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManagerAbstr;
import org.dj.index.api.IntegerIndices;
import org.lblrtm.host.api.LBLRTMHostInfo;
import org.lblrtm.host.api.LBLRTMHostManager;
import org.lblrtm.lblrtmdata.api.AtmosphericModel;
import org.lblrtm.lblrtmdata.api.ObserverAbstr;
import org.lblrtm.lblrtmdata.api.SurfaceAbstr;
import org.lblrtm.lblrtmfilewriter.api.CalculationTemplate;
import org.lblrtm.lblrtmfilewriter.api.CalculationTemplateService;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;
import org.lblrtm.lblrtmfilewriter.api.TAPE5Factory;
import org.lblrtm.profile.api.ProfileAbstr;
import org.lblrtm.task.api.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

@ServiceProvider(service = TaskManager.class)
public class TaskManagerImpl extends DJObjectManagerAbstr implements TaskManager{
    private static final IntegerIndices iI = Lookup.getDefault().lookup(IntegerIndices.class);
    public TaskManagerImpl(){
        super();
        this.setName(MANAGER_NAME);
        
    }
    
    public static final String MANAGER_NAME = "Task Manager";
    
    public static final double DEFAULT_V1 = 100.0;
    public static final double DEFAULT_V2 = 110.0;
    public static final double DEFAULT_OBSERVER_ALT = 95.0;
    public static final double DEFAULT_DV = 0.01;
    public static final int DEFAULT_START = 10000;
    public static final int DEFAULT_END  = 10999;
    
    
    private static final TAPE5Factory f = Lookup.getDefault().lookup(TAPE5Factory.class);
    private static final CalculationTemplateService cTs = Lookup.getDefault().lookup(CalculationTemplateService.class);
    private static final LBLRTMHostManager hmgr = Lookup.getDefault().lookup(LBLRTMHostManager.class);

    @Override
    public DJNodeObject createObject() {
        
        
        DJTAPE5 t5 = (DJTAPE5) f.createTAPE5(new ProfileAbstr(), new SurfaceAbstr(), new ObserverAbstr(DEFAULT_OBSERVER_ALT), DEFAULT_V1, DEFAULT_V2,DEFAULT_DV);
        t5.setAtmosphericModel(AtmosphericModel.TROPICAL);
        Set<LBLRTMHostInfo> hosts = new FastSet<LBLRTMHostInfo>();
        hosts.addAll(hmgr.getHosts());
        LBLRTMHostInfo hI = null;
        if(!hosts.isEmpty()){
            
           hI = hmgr.getHosts().iterator().next();
        }

        LBLRTMTask t = new LBLRTMTaskAbstr(t5, hI, new File(""), true);

        LBLRTMParallelTask tsk = new LBLRTMParallelTaskAbstr(t,hosts);
        tsk.setRanges(iI.fromRange(DEFAULT_START,DEFAULT_END));
        
        this.addChild(tsk);
        
        tsk.setName("Test task");
        
        return tsk;
        
    }

    @Override
    public void close() throws IOException {
        Iterator iterator = this.getChildren().iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Closeable){
                Closeable c = (Closeable) next;
                c.close();
                
            }
        }
    }
    
}
