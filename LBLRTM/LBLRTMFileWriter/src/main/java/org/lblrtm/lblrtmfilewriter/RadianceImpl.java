/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import java.util.*;
import javolution.util.FastSet;
import org.lblrtm.file.api.TAPE6;
import org.lblrtm.lblrtmdata.api.LBLRTMInputFile;
import org.lblrtm.lblrtmdata.api.LBLRTMOutputFile;
import org.lblrtm.lblrtmfilewriter.api.*;

/**
 *
 * @author djabry
 */
public class RadianceImpl extends CalculationTemplateAbstr implements Radiance{

  
   
    private RecordRepetitionSpecifier rRS = new URRecordRepetitionSpecifier();
    private static final Map<String, String> FILES_TO_SCAN;
    private static final List<LBLRTMOutputFile> OUTPUT_FILES = Arrays.asList(
            new LBLRTMOutputFile[]{
                new RadianceOutputFile(TAPE6.RADIANCE_OUTPUT_FILE)});

    public RadianceImpl() {

        super(CalculationTemplate.RADIANCE_CALCULATION);
        
        this.getProperty(PROP_JACOBIAN_OUTPUT_UNITS).setCanRead(false);
        this.getFieldValues().addAll(this.generateFieldValues());
        
        
        
    }

    static {

        FILES_TO_SCAN = new HashMap<String, String>();
        FILES_TO_SCAN.put(TAPE6.RADIANCE_OUTPUT_FILE, TAPE6.RADIANCE_OUTPUT_FILE);
    }

    @Override
    public List<LBLRTMOutputFile> getOutputFiles(TAPE6 t6) {

        return OUTPUT_FILES;
    }

    @Override
    public Map<String, String> getFilesToScan(TAPE6 t6) {
        return FILES_TO_SCAN;
    }

    @Override
    public Set<String> getInputFiles() {
        Set<String> files = new FastSet<String>();
        //files.add(LBLRTMInputFile.TAPE1.name());
        files.add(LBLRTMInputFile.TAPE3.name());
        //files.add(LBLRTMInputFile.EMISSIVITY.name());
        //files.add(LBLRTMInputFile.REFLECTIVITY.name());

        return files;

    }

    @Override
    public CalculationTemplate duplicate() {
        RadianceImpl r = new RadianceImpl();
        r.importPropertiesFrom(this);
        return r;
    }

    private Set<DefaultFieldValue> generateFieldValues() {

        Set<DefaultFieldValue> fVs = new FastSet<DefaultFieldValue>();
        DefaultFieldValue fIMRG = new DefaultFieldValue("1.2", "IMRG", -1, 0);
        fVs.add(fIMRG);

        DefaultFieldValue fIOD = new DefaultFieldValue("1.2", "IOD", -1, 0);
        fVs.add(fIOD);

        return fVs;

    }




    @Override
    public RecordRepetitionSpecifier getRecordRepetitionSpecifier() {
        return rRS;
    }

    

    private class URRecordRepetitionSpecifier implements RecordRepetitionSpecifier {

        @Override
        public Integer getNumberOfRepetitionsForRecord(Record record) {

            if (record.getName().equals("All Records")) {

                return 1;
            }

            return null;
        }
    }
}
