/**
 * Copyright © 2016 Open Text.  All Rights Reserved.
 */
package com.opentext.otag.service.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This type handles the loading and extraction of data from the
 * deployments web.xml file. The managing Gateway seeds values as
 * it deploys the app/service. This contains info that allows the
 * Gateway to identify requests from the app/service.
 *
 * @author Rhys Evans rhyse@opentext.com
 * @version 16.2
 */
public class AWConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AWConfig.class);

    public static final String APP_NAME = "appName";
    public static final String APP_KEY = "appKey";
    public static final String MANAGING_OTAG_URL = "otagUrl";
    public static final String DEPLOYMENT_TYPE = "types";
    public static final String SERVICE_TYPE = "service";
    public static final String EIM_CONNECTOR_TYPE = "eimconnector";

    public static final String WEB_XML_LOCATION = "../web.xml";

    private String appName;
    private String appKey;
    private String gatewayUrl;
    private String types;

    /**
     * Construct an AppWorks config loader for an app with the name provided.
     */
    public AWConfig() {
        // read web.xml, we are looking for an init-param for 'appKey' and one for 'otagUrl'
        processWebXml();
    }

    public String getAppName() {
        return appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public boolean isService() {
        try {
            return SERVICE_TYPE.contains(types) || EIM_CONNECTOR_TYPE.contains(types);
        } catch (Exception e) {
            throw new RuntimeException("We must be able to resolve the deployments " +
                    "types to allow it to start! Missing \"types\" in management properties");
        }
    }

    private void processWebXml() {
        try (InputStream webXmlStream = this.getClass().getClassLoader().getResourceAsStream(WEB_XML_LOCATION)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document webXmlDoc = builder.parse(webXmlStream);

            NodeList initParams = webXmlDoc.getElementsByTagName("init-param");
            for (int i = 0; i < initParams.getLength(); i++) {
                Element element = (Element) initParams.item(i);
                Node paramName = element.getFirstChild();
                Node paramValue = element.getLastChild();

                String name = (paramName != null) ? paramName.getTextContent() : null;
                String val = (paramValue != null) ? paramValue.getTextContent() : null;

                if (name != null && val != null) {
                    switch (name) {
                        case APP_NAME:
                            appName = val;
                            break;
                        case APP_KEY:
                            appKey = val;
                            break;
                        case MANAGING_OTAG_URL:
                            gatewayUrl = val;
                            break;
                        case DEPLOYMENT_TYPE:
                            types = val;
                            break;
                        default:
                    }
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException("Unable to load service configuration, web.xml " +
                    "could not be loaded", e);
        }

        logIfMissing(APP_NAME, appName);
        logIfMissing(APP_KEY, appName);
        logIfMissing(MANAGING_OTAG_URL, appName);
        logIfMissing(DEPLOYMENT_TYPE, appName);
    }

    private void logIfMissing(String field, String value) {
        if (value == null || value.trim().isEmpty())
            LOG.error("We were unable to derive the {} field via web.xml", field);
    }

}
