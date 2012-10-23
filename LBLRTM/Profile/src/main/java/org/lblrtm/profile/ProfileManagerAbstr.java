/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.lblrtm.profile.api.ProfileAbstr;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManagerAbstr;
import org.dj.matrix.api.DJMatrix;
import org.dj.matrix.api.MatrixFileType;
import org.dj.matrix.api.MatrixIO;
import org.lblrtm.lblrtmdata.api.AtmosphericModel;
import org.lblrtm.profile.api.DJProfile;
import org.lblrtm.profile.api.Profile;
import org.lblrtm.profile.api.ProfileManager;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = ProfileManager.class)
public class ProfileManagerAbstr extends DJObjectManagerAbstr<DJProfile> implements ProfileManager {

    private static final Map<AtmosphericModel, String> profileLocations;
    
    private static final String location = "resources/";
    private static final String extension = ".csv";
    private static final MatrixIO mIO = Lookup.getDefault().lookup(MatrixIO.class);
    private final LoadingCache<AtmosphericModel,DJMatrix> cache = CacheBuilder.newBuilder().maximumSize(3000).build(new CacheLoader<AtmosphericModel, DJMatrix>() {

            @Override
            public DJMatrix load(AtmosphericModel m) throws Exception {
                return doLoadMatrix(m);
            }
        });

    static {

        profileLocations = new EnumMap<AtmosphericModel, String>(AtmosphericModel.class);
        
        for (AtmosphericModel m : AtmosphericModel.values()) {
            if (!m.equals(AtmosphericModel.USER_DEFINED)) {
                profileLocations.put(m, location + m.name() + extension);
                
            }

        }

    }

    @Override
    public DJNodeObject createObject() {
        DJProfile p = new ProfileAbstr();
        this.addChild(p);
        return p;
    }

    @Override
    public DJProfile createProfileFromTemplate(AtmosphericModel t) throws IOException {
        
        DJProfile p = new ProfileAbstr();
        if(!t.equals(AtmosphericModel.USER_DEFINED)){
            DJMatrix m = loadMatrixForTemplate(t);
            p.importMatrix(m);
            
        }

        this.addChild(p);
        return p;
    }
    
    private DJMatrix doLoadMatrix(AtmosphericModel t) throws IOException{
        
         DJMatrix mat = null;
        if (!t.equals(AtmosphericModel.USER_DEFINED)) {
            String loc = profileLocations.get(t);
            InputStream iS = this.getClass().getClassLoader().getResourceAsStream(loc);
            
            mat = mIO.loadFromStream(iS, MatrixFileType.CSV);
          
        }
        return mat;
    }

    @Override
    public DJMatrix loadMatrixForTemplate(AtmosphericModel t) throws IOException {
        try {
            return cache.get(t);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return null;
    }
}
