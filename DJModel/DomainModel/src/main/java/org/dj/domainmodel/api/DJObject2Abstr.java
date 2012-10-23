/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;

import lombok.BoundSetter;
import lombok.Data;
import org.dj.idgenerator.api.IDGenerator;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
//@Bean(
//        properties = {
//    @Property(name = DJObject2.PROP_NAME, type= String.class, bound = true),
//    @Property(name = DJObject2.PROP_DESCRIPTION, type = String.class,bound = true)
//})
@Data
public class DJObject2Abstr implements DJObject2 {

    private static final IDGenerator iDGen = Lookup.getDefault().lookup(IDGenerator.class);

    private final String id = iDGen.generateID();
    
    @BoundSetter
    private String name = "";
    
    @BoundSetter
    private String description = "";
}
