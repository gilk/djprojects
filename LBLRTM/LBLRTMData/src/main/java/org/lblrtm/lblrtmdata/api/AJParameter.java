/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import org.lblrtm.lblrtmdata.api.Molecule;

/**
 *
 * @author djabry
 */
public enum AJParameter {
    
    TEMPERATURE(0,"TEMPERATURE"),
    H2O(Molecule.H2O),
    CO2(Molecule.CO2),
    O3(Molecule.O3),
    N2O(Molecule.N2O),
    CO(Molecule.CO),
    CH4(Molecule.CH4),
    O2(Molecule.O2),
    NO(Molecule.NO),
    SO2(Molecule.SO2),
    NO2(Molecule.NO2),
    NH3(Molecule.NH3),
    HNO3(Molecule.HNO3),
    OH(Molecule.OH),
    HF(Molecule.HF),
    HCL(Molecule.HCL),
    HBR(Molecule.HBR),
    HI(Molecule.HI),
    CLO(Molecule.CLO),
    OCS(Molecule.OCS),
    H2CO(Molecule.H2CO),
    HOCL(Molecule.HOCL),
    N2(Molecule.N2),
    HCN(Molecule.HCN),
    CH3CL(Molecule.CH3CL),
    H2O2(Molecule.H2O2),
    C2H2(Molecule.C2H2),
    C2H6(Molecule.C2H6),
    PH3(Molecule.PH3),
    COF2(Molecule.COF2),
    SF6(Molecule.SF6),
    H2S(Molecule.H2S),
    HCOOH(Molecule.HCOOH),
    HO2(Molecule.HO2),
    O(Molecule.O),
    CLONO2(Molecule.CLONO2),
    NOPLUS(Molecule.NOPLUS),
    HOBR(Molecule.HOBR),
    C2H4(Molecule.C2H2),
    CH3OH(Molecule.CH3OH);
    

    
    private String generateParamString(int ix){
        
        return String.format("%02d", ix);
    }
    
    AJParameter(int idx, String name){
    
    
        parameterIndex = idx;
        fullName = name;
        this.isMolecule=false;
        this.paramString=generateParamString(idx);
    }
    
    AJParameter(Molecule mol){
        
        parameterIndex = mol.getMoleculeNumber();
        fullName = mol.name();
        this.isMolecule=true;
        this.paramString=generateParamString(parameterIndex);
        
    }
    
    
    
    public final String paramString;
    public final boolean isMolecule;
    public final int parameterIndex;
    public final String fullName;
    
    
}
