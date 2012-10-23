/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.index.api.IntegerIndex;
import org.dj.index.api.IntegerIndices;
import org.dj.property.api.DJProperty;
import org.lblrtm.file.api.TAPE6;
import org.lblrtm.lblrtmdata.api.AJParameter;
import org.lblrtm.lblrtmdata.api.LBLRTMInputFile;
import org.lblrtm.lblrtmdata.api.LBLRTMOutputFile;
import org.lblrtm.lblrtmfilewriter.api.*;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class AnalyticalJacobianImpl extends CalculationTemplateAbstr implements AnalyticJacobian {
    
    private FieldValueModifier spec;
    private RecordRepetitionSpecifier rRSpec;
    private static final List<String> propNames;
    private static final IntegerIndices iI = Lookup.getDefault().lookup(IntegerIndices.class);
    
    static {
        
        propNames = new FastList<String>();
        propNames.add(PROP_AJPARAMETERS);
    }
    //private AJParameter aP;

    //private Map<String,String> filesToScan;
    @Override
    public Set<String> getInputFiles() {
        Set<String> files = new FastSet<String>();
        files.add(LBLRTMInputFile.TAPE3.name());
        files.add("xs");
        files.add("FSCDXS");
        
        
        return files;
        
    }
    
    @Override
    public RecordRepetitionSpecifier getRecordRepetitionSpecifier() {
        return rRSpec;
    }
    
    private Map<String, String> generateFilesToScan(TAPE6 t6) {
        
        Map<String, String> files = new FastMap<String, String>();
        int altSize = t6.getOutputProfile().getNumberOfLevels() + 1;
        
        for (int i = 0; i < altSize; i++) {
            
            for (AJParameter p : this.getAJParameters()) {
                
                String lev = generateAJLevelString(p, i);
                String lay = generateAJLayerString(p, i);
                files.put(lev, lev);
                
                if (i < altSize - 1) {
                    
                    files.put(lay, lay);
                }
            }
            
            String rddn = generateRadianceDownString(i);
            String rdup = generateRadianceUpString(i);
            
            
            
            
            if (i < altSize - 1) {
                
                files.put(rddn, rddn);
                files.put(rdup, rdup);
            }
        }
        
        String rddnsfc = "RDDN_sfc";
        files.put(rddnsfc, rddnsfc);
        
        return files;
        
    }
    
    @Override
    public EnumSet<AJParameter> getAJParameters() {
        
        return (EnumSet<AJParameter>) this.getPropertyValue(PROP_AJPARAMETERS);
    }
    
    @Override
    public void setAJParameters(EnumSet<AJParameter> aP) {
        
        this.getAJParameters().clear();
        this.getAJParameters().addAll(aP);
    }
    
    @Override
    public List<LBLRTMOutputFile> getOutputFiles(TAPE6 t6) {
        
        List<LBLRTMOutputFile> outputFiles = new FastList<LBLRTMOutputFile>();
        for (String fileName : generateFilesToScan(t6).values()) {
            outputFiles.add(new RadianceOutputFile(fileName));
        }
        return outputFiles;
    }
    
    @Override
    public Map<String, String> getFilesToScan(TAPE6 t6) {
        
        return generateFilesToScan(t6);
    }
    
    private String generateRadianceDownString(int layer) {
        
        return generateNumberSuffix("RDDNlayer", layer);
    }
    
    private String generateRadianceUpString(int layer) {
        return generateNumberSuffix("RDUPlayer", layer);
        
    }
    
    private String generateAJLevelString(AJParameter aJP, int level) {
        
        String molString = generateParamString(aJP);
        
        String levelString = String.format("%03d", level + 1);
        
        return "AJ/LEV_RDderiv" + generateDirectionString() + "_" + molString + "_" + levelString;
        
    }
    
    private String generateDirectionString() {
        
        if (this.getParentTAPE5().getDirection()) {
            
            return "UPW";
        } else {
            
            return "DNW";
        }
    }
    
    private String generateParamString(AJParameter p) {
        
        return String.format("%02d", p.parameterIndex);
    }
    
    private String generateNumberSuffix(String in, int suff) {

        //Java index starts at zero, convert to 1;
        String levelString = String.format("%03d", suff + 1);
        
        return in + "_" + levelString;
    }
    
    private String generateAJLayerString(AJParameter p, int layer) {
        
        String molString = this.generateParamString(p);
        String s = "AJ/RDderiv" + generateDirectionString() + "_" + molString;
        return generateNumberSuffix(s, layer);
        
    }
    
    @Override
    public List<String> getPropNames() {
       return Lists.newArrayList(Iterables.concat(super.getPropNames(),propNames));
    }


    /**
     * @return the v1
     */
    private class AJRecordRepetitionSpecifier implements RecordRepetitionSpecifier {
        
        @Override
        public Integer getNumberOfRepetitionsForRecord(Record record) {
            
            if (record.getName().equals("All Records")) {
                
                return 3 + getAJParameters().size();
            }
            
            return null;
        }
    }
    
    private boolean isAJLevel(int level) {
        
        return level > 2;
    }
    
    private Set<DefaultFieldValue> generateFVs() {
        
        Set<DefaultFieldValue> fVs = new FastSet<DefaultFieldValue>();

        //Record 1.2
        DefaultFieldValue fIEMIT0 = new DefaultFieldValue("1.2", "IEMIT", 0, 0);
        fVs.add(fIEMIT0);
        
        DefaultFieldValue fIEMIT2 = new DefaultFieldValue("1.2", "IEMIT", 3, 3) {
            @Override
            public boolean isLevelValid(int level) {
                return isAJLevel(level);
            }
        };
        
        fVs.add(fIEMIT2);
        
        DefaultFieldValue fIATM0 = new DefaultFieldValue("1.2", "IATM", 0, 1);
        fVs.add(fIATM0);
        
        DefaultFieldValue fIATM2 = new DefaultFieldValue("1.2", "IATM", 3, 0) {
            @Override
            public boolean isLevelValid(int level) {
                return isAJLevel(level);
            }
        };
        fVs.add(fIATM2);
        
        DefaultFieldValue fIMRG0 = new DefaultFieldValue("1.2", "IMRG", 0, 1);
        fVs.add(fIMRG0);
        
        
        DefaultFieldValue fIMRG2 = new DefaultFieldValue("1.2", "IMRG", 3, 41) {
            @Override
            public Object getValueForField(Field field) {
                
                if (this.filter(field)) {
                    
                    int iMRGDirn = 40;
                    
                    if (getParentTAPE5().getDirection()) {
                        
                        iMRGDirn = 41;
                    }
                    
                    return iMRGDirn;
                    
                }
                
                return super.getValueForField(field);
                
            }
            
            @Override
            public boolean isLevelValid(int level) {
                return isAJLevel(level);
                
            }
        };
        
        
        fVs.add(fIMRG2);
        
        DefaultFieldValue fIOD0 = new DefaultFieldValue("1.2", "IOD", 0, 3);
        fVs.add(fIOD0);
        
        DefaultFieldValue fIOD2 = new DefaultFieldValue("1.2", "IOD", 3, 3) {
            @Override
            public boolean isLevelValid(int level) {
                return isAJLevel(level);
            }
        };
        
        fVs.add(fIOD2);
        
        DefaultFieldValue fIPUNCH = new DefaultFieldValue("3.1", "IPUNCH", -1, 2);
        fVs.add(fIPUNCH);
        
        
        DefaultFieldValue fNSPCRT = new DefaultFieldValue("1.5", "NSPCRT", -1, AJParameter.H2O.parameterIndex) {
            @Override
            public Object getValueForField(Field field) {
                
                if (this.filter(field)) {
                    
                    int pIndex = field.getLevel() - 3;
                    
                    return Iterables.get(getAJParameters(), pIndex).parameterIndex;
                    
                }
                
                return super.getValueForField(field);
            }

            @Override
            public boolean isLevelValid(int level) {
                return level>2;
            }
            
            
            
        };
        
        fVs.add(fNSPCRT);
        
        DefaultFieldValue fPTHODL = new DefaultFieldValue("1.6a", "PTHODL", -1, "ODint_");
        fVs.add(fPTHODL);

        //Generate radiance down and up in runs 2 and 3

        
        IntegerIndex ix = iI.fromRange(1,2);
        DefaultFieldValue fIHIRAC = new DefaultFieldValue("1.2", "IHIRAC", ix, 0);
        fVs.add(fIHIRAC);
        
        DefaultFieldValue fILBL41 = new DefaultFieldValue("1.2", "ILBL4", ix, 0);
        fVs.add(fILBL41);
        
        DefaultFieldValue iCNTNM = new DefaultFieldValue("1.2", "CNTNM", ix, 1);
        fVs.add(iCNTNM);
        
        DefaultFieldValue fIEMIT1 = new DefaultFieldValue("1.2", "IEMIT", ix, 1);
        fVs.add(fIEMIT1);
        
        DefaultFieldValue fIATM1 = new DefaultFieldValue("1.2", "IATM", ix, 0);
        fVs.add(fIATM1);

        //Calculate Downwelling radiance on second run and upwelling on 3rd
        DefaultFieldValue fIMRG1 = new DefaultFieldValue("1.2", "IMRG", ix, 41) {
            @Override
            public Object getValueForField(Field field) {
                return 39 + field.getLevel();
            }
        };
        fVs.add(fIMRG1);
        
        DefaultFieldValue fIOD1 = new DefaultFieldValue("1.2", "IOD", ix, 3);
        fVs.add(fIOD1);
        
        
        return fVs;
    }
    
    public AnalyticalJacobianImpl() {
        
        super(CalculationTemplate.AJ_CALCULATION);
        
        DJProperty ajProp = getPropertyFactory().createProperty(PROP_AJPARAMETERS, EnumSet.of(AJParameter.TEMPERATURE,AJParameter.H2O));
        ajProp.setDisplayName("AJ parameters");
        this.putProperty(ajProp);



        //spec = new AJFieldValueSpecifier();
        rRSpec = new AJRecordRepetitionSpecifier();
        Set<DefaultFieldValue> fV = this.generateFVs();
        this.getFieldValues().addAll(fV);

        
        
    }


    @Override
    public CalculationTemplate duplicate() {
        AnalyticJacobian cT = new AnalyticalJacobianImpl();
        cT.importPropertiesFrom(this);
        return cT;
    }
}
