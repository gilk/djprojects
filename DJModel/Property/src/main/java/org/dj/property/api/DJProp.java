/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.property.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

/**
 *
 * @author djabry
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface DJProp {
    String displayName() default "";
    String propertyName() default "";
    String description() default "";
    String shortDescription() default "";
    boolean canWrite() default false;
    boolean canRead() default true;
    Class type() default String.class;
    
    
    
}
