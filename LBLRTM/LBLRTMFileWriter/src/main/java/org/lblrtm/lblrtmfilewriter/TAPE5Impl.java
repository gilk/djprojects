/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javolution.util.FastSet;
import org.dj.fileutilities.api.FileUtilities;
import org.dj.filter.api.Filter;
import org.dj.property.api.DJProperty;
import org.lblrtm.lblrtmdata.api.*;
import org.lblrtm.lblrtmfilewriter.api.*;
import org.lblrtm.profile.api.PhysicalProperty;
import org.lblrtm.profile.api.Profile;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class TAPE5Impl extends RepeatedRecordSet implements DJTAPE5 {
    private static final Predicate<Record> VALIDATOR = new Predicate<Record>() {

            @Override
            public boolean apply(Record t) {
                return t.isValid();
            }
        };

    private static final TAPE5Factory t5f = Lookup.getDefault().lookup(TAPE5Factory.class);
    //private static final CalculationTemplates cTs = Lookup.getDefault().lookup(CalculationTemplates.class);
    private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);
    private static final CalculationTemplateService cTS = Lookup.getDefault().lookup(CalculationTemplateService.class);
    private static final ScannerTemplateService sTS = Lookup.getDefault().lookup(ScannerTemplateService.class);

    @Override
    public DJTAPE5 getParentTAPE5() {
        return this;
    }

    public TAPE5Impl(double v1, double v2, double dv,
            Profile profile, String runString, Surface surface,
            Observer observer) {

        super("TAPE5");

        DJProperty cTProp = getPropertyFactory().createProperty(PROP_CALCULATION_TEMPLATE, null);
        cTProp.setCanRead(false);
        this.putProperty(cTProp);

        DJProperty rTProp = getPropertyFactory().createProperty(PROP_RUN_TEMPLATE, null);
        //this.addChild((CalculationTemplateAbstr) cT);
        rTProp.setCanRead(false);
        this.putProperty(rTProp);

        DJProperty sTProp = getPropertyFactory().createProperty(PROP_SCANNER_TEMPLATE, null);
        sTProp.setCanRead(false);
        this.putProperty(sTProp);

        DJProperty endHeightProp = getPropertyFactory().createProperty(DJTAPE5.PROP_END_HEIGHT, 0.0);
        endHeightProp.setDisplayName("End height (km)");
        this.putProperty(endHeightProp);


        DJProperty dVProp = getPropertyFactory().createProperty(DJTAPE5.PROP_DV, dv);
        dVProp.setDisplayName("<html>Wavenumber spacing (cm<sup>-1</sup>)</html>");
        this.putProperty(dVProp);


        DJProperty modelProp = getPropertyFactory().createProperty(DJTAPE5.PROP_ATMOSPHERIC_MODEL, AtmosphericModel.TROPICAL);
        modelProp.setDisplayName("Atmospheric model");
        this.putProperty(modelProp);

        DJProperty observerProp = getPropertyFactory().createProperty(DJTAPE5.PROP_OBSERVER, observer);
        observerProp.setCanWrite(false);
        this.putProperty(observerProp);

        DJProperty surfaceProp = getPropertyFactory().createProperty(DJTAPE5.PROP_SURFACE, surface);
        surfaceProp.setCanRead(false);
        this.putProperty(surfaceProp);

        DJProperty profileProp = getPropertyFactory().createProperty(DJTAPE5.PROP_PROFILE, profile);
        profileProp.setCanRead(false);
        this.putProperty(profileProp);


        DJProperty rStringProp = getPropertyFactory().createProperty(DJTAPE5.PROP_RUN_STRING, runString);
        rStringProp.setDisplayName("Description text");
        rStringProp.setFilter(Filter.STRING);
        this.putProperty(rStringProp);

        DJProperty v2Prop = getPropertyFactory().createProperty(DJTAPE5.PROP_V2, v2);
        v2Prop.setDisplayName("<html>End wavenumber (cm<sup>-1</sup>)</html>");
        v2Prop.setFilter(Filter.DOUBLE);
        this.putProperty(v2Prop);


        DJProperty v1Prop = getPropertyFactory().createProperty(DJTAPE5.PROP_V1, v1);
        v1Prop.setDisplayName("<html>Start wavenumber (cm<sup>-1</sup>)</html>");
        v1Prop.setFilter(Filter.DOUBLE);
        this.putProperty(v1Prop);

        DJProperty angleProp = getPropertyFactory().createProperty(DJTAPE5.PROP_ANGLE, 180.0);
        angleProp.setDisplayName("<html>Angle (<sup>o</sup>)</html>");
        angleProp.setFilter(Filter.DOUBLE);
        this.putProperty(angleProp);
        
//        DJProperty custLevelsProp = getPropertyFactory().createProperty(DJTAPE5.PROP_CUSTOM_LEVELS, new CustomLevels((DJProfile)profile,true,false));
//        custLevelsProp.setCanRead(false);
//        this.putProperty(custLevelsProp);

        RepeatedRuns rR = new RepeatedRuns(this);


    }

    @Override
    public double getDV() {

        return (Double) this.getPropertyValue(PROP_DV);

    }

    @Override
    public void setDV(double dV) {

        this.setPropertyValue(PROP_DV, dV);


    }

    @Override
    public double getEndHeight() {

        return (Double) this.getPropertyValue(PROP_END_HEIGHT);
    }

    @Override
    public void setEndHeight(double endHeight) {

        this.setPropertyValue(PROP_END_HEIGHT, endHeight);
    }

    @Override
    public AtmosphericModel getAtmosphericModel() {

        return (AtmosphericModel) this.getPropertyValue(PROP_ATMOSPHERIC_MODEL);
    }

    @Override
    public void setAtmosphericModel(AtmosphericModel m) {

        this.setPropertyValue(PROP_ATMOSPHERIC_MODEL, m);
    }

    @Override
    public double getV1() {
        return (Double) this.getPropertyValue(PROP_V1);


    }

    @Override
    public double getV2() {
        return (Double) this.getPropertyValue(PROP_V2);

    }

    @Override
    public void setV1(double v1) {
        this.setPropertyValue(PROP_V1, v1);


    }

    @Override
    public void setV2(double v2) {
        this.setPropertyValue(PROP_V2, v2);


    }

    @Override
    public File writeToFile(File file) throws IOException {


        if (!file.exists()) {

            file.createNewFile();
        }

        

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

        try {
            TAPE5Impl dplct = ((TAPE5Impl)duplicate());
            
     
                List<Record> allRecords = dplct.getAllRecords();
                Iterator<Record> iterator = Iterators.filter(allRecords.iterator(),VALIDATOR);
            
            while (iterator.hasNext()) {
                Record r = iterator.next();
                

                    writer.write(r.getRecordString());

                    //Only add a new line character when there is a record after the current one
                    if (iterator.hasNext()) {

                        writer.newLine();
                    }
                
            }
            
            
            
           
        } finally {

            writer.close();
            return file;

        }

    }

    @Override
    public File writeToDir(File dir) throws IOException {
        File f = new File(dir.getPath() + File.separator + CALCULATOR_FILENAME);

        return writeToFile(f);
    }

    @Override
    public InputStream getInputStream() throws IOException {

        File t5 = File.createTempFile(CALCULATOR_FILENAME, "", fU.getTempDir());
        writeToFile(t5);

        return new FileInputStream(t5);
        


    }
    
    
    

    

    private String generateString() {

        String s = "";
        Iterator<Record> iterator = getAllRecords().iterator();
        while (iterator.hasNext()) {

            Record r = iterator.next();
            s += r.getRecordString();

            //Only add a new line character when there is a record after the current one
            if (iterator.hasNext()) {

                s += "\n";
            }
        }

        return s;
    }
    private final Filter<org.lblrtm.profile.api.Level> LEVEL_FILTER = new Filter<org.lblrtm.profile.api.Level>() {
        @Override
        public boolean filter(org.lblrtm.profile.api.Level obj) {

            double alt = obj.getValueForVariable(PhysicalProperty.ALTITUDE.name());
            double endHeight = getEndHeight();
            double obsHeight = getObserver().getAltitude();

            double minH = Math.min(endHeight, obsHeight);
            double maxH = Math.max(endHeight, obsHeight);

            return alt >= minH && alt <= maxH;



        }
    };

    @Override
    public TAPE5 duplicate() {

        return this.copy(false);
    }

    @Override
    public DJTAPE5 copy(boolean linkedProfile) {

        
        Profile p = this.getProfile();
        
        if(!linkedProfile){
            
            p = p.duplicate().section(LEVEL_FILTER);
        }
        
        DJTAPE5 t5 = new TAPE5Impl(getV1(), getV2(), getDV(),
            p, getRunString(), getSurface().duplicate(),
            getObserver().duplicate());

        t5.setCalculationTemplate(this.getCalculationTemplate().duplicate());
        t5.setEndHeight(this.getEndHeight());
        t5.setAngle(this.getAngle());
        t5.setAtmosphericModel(this.getAtmosphericModel());
        t5.setScannerTemplate(this.getScannerTemplate().duplicate());
        
        RunTemplate runTemplate = this.getRunTemplate();
        if(runTemplate!=null){

            t5.setRunTemplate(runTemplate.duplicate());
        }
        

        return t5;


    }

    @Override
    public String getRunString() {
        return (String) this.getPropertyValue(PROP_RUN_STRING);
    }

    @Override
    public Field getField(String recordName, String fieldName) {

        Record record = getRecord(recordName);

        if (record != null) {

            return record.getField(fieldName);
        }

        return null;
    }

    @Override
    public Profile getProfile() {
        return (Profile) this.getPropertyValue(PROP_PROFILE);
    }

    public void setProfile(Profile profile) {

        this.setPropertyValue(PROP_PROFILE, profile);

    }

    @Override
    public Surface getSurface() {
        return (Surface) this.getPropertyValue(PROP_SURFACE);
    }

    public void setSurface(Surface surface) {

        this.setPropertyValue(PROP_SURFACE, surface);
    }

    @Override
    public Observer getObserver() {
        return (Observer) this.getPropertyValue(PROP_OBSERVER);
    }

    public void setObserver(Observer observer) {

        this.setPropertyValue(PROP_OBSERVER, observer);
    }

    @Override
    public int getNumberOfRepetitions() {
        return 1;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void initRecords() {
    }

    @Override
    public Set<LBLRTMOutputFile> getAllOutputFiles() {
        Set<LBLRTMOutputFile> outFiles = new FastSet<LBLRTMOutputFile>();
        outFiles.add(LBLRTMOutputFile.TAPE5);
        outFiles.add(LBLRTMOutputFile.TAPE6);
        outFiles.add(LBLRTMOutputFile.TAPE7);
        return outFiles;

    }

//    @Override
//    public Set<FieldModifier> getFieldModifiers() {
//        return (Set<FieldModifier>) this.getPropertyValue(PROP_FIELD_MODIFIERS);
//    }
    @Override
    public CalculationTemplate getCalculationTemplate() {
        return (CalculationTemplate) this.getPropertyValue(PROP_CALCULATION_TEMPLATE);
    }

//    @Override
//    public DJTAPE5 createScannerTAPE5() {
//        DJTAPE5 t5 = (DJTAPE5) this.duplicate();
//        t5.setRunTemplate(this.getScannerTemplate().duplicate());
//        return t5;
//    }
//
//    @Override
//    public File writeScannerToDir(File dir) {
//
//        File out = new File(dir.getAbsolutePath() + File.separator + DJTAPE5.SCANNER_FILENAME);
//        return createScannerTAPE5().writeToFile(out);
//
//    }
//
//    @Override
//    public FileInputStream getScannerInputStream() {
//
//        return createScannerTAPE5().getInputStream();
//
//    }
    private void transferProperties(RunTemplate oldCT, RunTemplate newCT) {

        if (oldCT != null && newCT != null) {
            newCT.importPropertiesFrom(oldCT);

        }


    }

    @Override
    public void setCalculationTemplate(CalculationTemplate cT) {

        CalculationTemplate oldCT = this.getCalculationTemplate();
        this.transferProperties(oldCT, cT);
        this.setPropertyValue(PROP_CALCULATION_TEMPLATE, cT);



    }

    @Override
    public boolean getDirection() {
        return getAngle() > 90.0;
    }

    @Override
    public double getAngle() {
        return (Double) this.getPropertyValue(PROP_ANGLE);
    }

    @Override
    public void setAngle(double angle) {
        this.setPropertyValue(PROP_ANGLE, angle);
    }

    @Override
    public ScannerTemplate getScannerTemplate() {
        return (ScannerTemplate) this.getPropertyValue(PROP_SCANNER_TEMPLATE);
    }

    @Override
    public void setScannerTemplate(ScannerTemplate sT) {

        ScannerTemplate oldST = this.getScannerTemplate();
        this.transferProperties(oldST, sT);

        this.setPropertyValue(PROP_SCANNER_TEMPLATE, sT);
    }

    @Override
    public void setRunTemplate(RunTemplate rT) {

        if (rT != null) {


            RunTemplate oldRT = this.getRunTemplate();

            if (oldRT != null) {
                this.removeChild(this.getRunTemplate());

            }
            this.setPropertyValue(PROP_RUN_TEMPLATE, rT);
            this.addChild(rT);
        }

    }

    @Override
    public RunTemplate getRunTemplate() {
        return (RunTemplate) this.getPropertyValue(PROP_RUN_TEMPLATE);
    }

//    @Override
//    public CustomLevels getCustomLevels() {
//        return (CustomLevels) this.getPropertyValue(PROP_CUSTOM_LEVELS);
//    }
    
    private class T5InputStream extends InputStream {
        
        private final Iterator<Record> allRecords;
        
        
        private InputStream iS;
        

        public T5InputStream(){
            
            allRecords = Iterators.filter(((TAPE5Impl)duplicate()).getAllRecords().iterator(),VALIDATOR);
            
            this.iS = this.getNextLineStream();
        }
        
        
        private InputStream getNextLineStream(){
            
            String str = allRecords.next().getRecordString();
            
            if(allRecords.hasNext()){
                
                str +="\n";
            }

            return new ByteArrayInputStream(str.getBytes());
            
        }
        
        
        @Override
        public int read() throws IOException {
            
            int val = iS.read();
            
            if(val==-1){
                
                if(allRecords.hasNext()){
                    
                    iS = getNextLineStream();
                    val = iS.read();
                }

            }
            
            return val;

        }
        
//        @Override
//        public void close() throws IOException{
//            this.iS.close();
//            super.close();
//            
//        }


        
    }
    
    

}
