/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.file.api;

import com.google.common.collect.Iterators;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import javolution.util.FastList;
import org.lblrtm.lblrtmdata.api.AJParameter;

/**
 *
 * @author djabry
 */
public enum AJOutputFilePrefix {

    RDDERIV("RDderiv"),
    LEVEL("LEV_"),
    LAYER(""),
    DOWN("DNW"),
    UP("UPW"),
    OD("ODInt"),
    RDDN("RDDNlayer"),
    RDUP("RDUPlayer");
    public static final EnumSet<AJOutputFilePrefix> LEVEL_LAYER = EnumSet.of(LEVEL, LAYER);
    public static final EnumSet<AJOutputFilePrefix> UP_DOWN = EnumSet.of(UP, DOWN);
    private static final List<String> allPrefixes;

    static {
        allPrefixes = new FastList<String>();
        for (AJParameter p : AJParameter.values()) {
            for (AJOutputFilePrefix levLay : LEVEL_LAYER) {
                for (AJOutputFilePrefix upDown : UP_DOWN) {


                    String pref = "AJ/" + levLay.prefixString
                            + RDDERIV.prefixString
                            + upDown.prefixString
                            + "_"
                            + String.format("%02d", p.parameterIndex)
                            + "_";

                    allPrefixes.add(pref);

                }
            }
        }

        allPrefixes.add(RDDN.prefixString + "_");
        allPrefixes.add(RDUP.prefixString + "_");
        allPrefixes.add(OD.prefixString+"_");
    }

    AJOutputFilePrefix(String prefixString) {
        this.prefixString = prefixString;

    }
    public final String prefixString;

    public static Iterator<String> getAJFilePrefixIterator() {

        return (new FastList(allPrefixes).iterator());


    }

    public static List<String> getAJFilePrefixes() {

        return new FastList(allPrefixes);
    }
}
