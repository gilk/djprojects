/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.explorer.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javolution.util.FastList;

import org.openide.explorer.view.*;
import org.openide.util.Exceptions;

/**
 *
 * @author djabry
 */
public enum View {
    
    ICON_VIEW("Icon view",IconView.class),
    LIST_VIEW("List view",ListView.class),
    TREE_VIEW("Tree view",BeanTreeView.class),
    OUTLINE_VIEW("Outline view",OutlineView.class),
    CHOICE_VIEW("Choice view",ChoiceView.class),
    MENU_VIEW("Menu view", MenuView.class);
   
    
    private Class viewClass;
    private String displayName;
    

    View(String displayName,Class viewClass){
        
        this.viewClass=viewClass;
        this.displayName=displayName;
 
    }
    
    public String getDisplayName(){
        
        return this.displayName;
    }
    

    public static List<View> getValues(){
        List<View> list = new FastList<View>();
        list.addAll(Arrays.asList(View.values()));
        Collections.sort(list, new Comparator<View>(){

            @Override
            public int compare(View t, View t1) {
                return t.getDisplayName().compareTo(t1.getDisplayName());
            }
        });
        
        return list;
       
    }
    
    @Override
    public String toString(){
        
        return this.displayName;
    }
    
    public JComponent getComponent(){
        
        JComponent p = null;
        try {
            Object n = viewClass.newInstance();
            
            if(n instanceof JComponent){
                JComponent jComponent = (JComponent) n;
                
                p=jComponent;
                
            }
            
            
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
     
        return p;
    }
    
    
    
}
