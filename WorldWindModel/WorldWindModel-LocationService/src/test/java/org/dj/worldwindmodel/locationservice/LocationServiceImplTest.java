/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.locationservice;

import gov.nasa.worldwind.geom.Position;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.dj.worldwindmodel.locationservice.api.LocationService.PositionNotFoundException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.openide.util.Exceptions;
import org.xml.sax.SAXException;

/**
 *
 * @author djabry
 */
public class LocationServiceImplTest {
    
    public LocationServiceImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getLongitudeLatitude method, of class LocationServiceImpl.
     */
    @Test
    @Ignore
    public void testGetLongitudeLatitude() {
        try {
            System.out.println("getLongitudeLatitude");
            String address = "";
            Position expResult = null;
            Position result = LocationServiceImpl.getLongitudeLatitude(address);
            assertEquals(expResult, result);
            // TODO review the generated test code and remove the default call to fail.
            fail("The test case is a prototype.");
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (XPathExpressionException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SAXException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ParserConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Test of getPositionForAddress method, of class LocationServiceImpl.
     */
    @Test
    public void testGetPositionForAddress() {
        try {
            System.out.println("getPositionForAddress");
            String address = "SW7 2AZ";
            LocationServiceImpl instance = new LocationServiceImpl();

            Position result = instance.getPositionForAddress(address);
            System.out.println(result.toString());
            
            Position result2 = instance.getPositionForAddress("London UK");
            System.out.println(result2.toString());
            
            result = instance.getPositionForAddress(address);
            System.out.println(result.toString());
            
            
            //assertEquals(expResult, result);
            // TODO review the generated test code and remove the default call to fail.
            //fail("The test case is a prototype.");
        } catch (PositionNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
