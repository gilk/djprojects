/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.locationservice;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import gov.nasa.worldwind.geom.Position;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.dj.worldwindmodel.locationservice.api.LocationService;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = LocationService.class)
public class LocationServiceImpl implements LocationService {

    private static final String GEOCODE_REQUEST_URL = "http://maps.googleapis.com/maps/api/geocode/xml?sensor=false&";
    private static HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
    private static final LoadingCache<String, Position> addressCache = CacheBuilder.newBuilder().maximumSize(10000).build(new CacheLoader<String, Position>() {
        @Override
        public Position load(String k) throws Exception {
            return getLongitudeLatitude(k);
        }
    });

    public static Position getLongitudeLatitude(String address) throws UnsupportedEncodingException, IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        Position p = Position.ZERO;

            StringBuilder urlBuilder = new StringBuilder(GEOCODE_REQUEST_URL);
            if (StringUtils.isNotBlank(address)) {
                urlBuilder.append("&address=").append(URLEncoder.encode(address, "UTF-8"));
            }

            final GetMethod getMethod = new GetMethod(urlBuilder.toString());
            try {
                httpClient.executeMethod(getMethod);
                Reader reader = new InputStreamReader(getMethod.getResponseBodyAsStream(), getMethod.getResponseCharSet());

                int data = reader.read();
                char[] buffer = new char[1024];
                Writer writer = new StringWriter();
                while ((data = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, data);
                }

                //String result = writer.toString();
                //System.out.println(result.toString());

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader("<" + writer.toString().trim()));
                Document doc = db.parse(is);

                String strLatitude = getXpathValue(doc, "//GeocodeResponse/result/geometry/location/lat/text()");
                System.out.println("Latitude:" + strLatitude);

                String strLongitude = getXpathValue(doc, "//GeocodeResponse/result/geometry/location/lng/text()");
                System.out.println("Longitude:" + strLongitude);

                if(strLatitude!=null&&strLongitude!=null){
                    Double lat = Double.parseDouble(strLatitude);
                    Double lon = Double.parseDouble(strLongitude);
                    p = Position.fromDegrees(lat, lon);
                }

            } finally {
                getMethod.releaseConnection();


            }


        return p;
    }

    private static String getXpathValue(Document doc, String strXpath) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xPath.compile(strXpath);
        String resultData = null;
        Object result4 = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result4;
        for (int i = 0; i < nodes.getLength(); i++) {
            resultData = nodes.item(i).getNodeValue();
        }
        return resultData;


    }

    @Override
    public Position getPositionForAddress(String address) throws PositionNotFoundException {

        Position p = Position.ZERO;
        try {
            p = addressCache.get(address);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            throw new PositionNotFoundException();
        }

        return p;
    }
}
