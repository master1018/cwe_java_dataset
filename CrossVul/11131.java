package org.orbeon.oxf.xml.xerces;
import org.orbeon.oxf.common.OXFException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;
public class XercesSAXParserFactoryImpl extends SAXParserFactory {
    private static final Collection recognizedFeaturesNonValidatingXInclude;
    private static final Map defaultFeaturesNonValidatingXInclude;
    private static final Collection recognizedFeaturesNonValidatingNoXInclude;
    private static final Map defaultFeaturesNonValidatingNoXInclude;
    private static final Collection recognizedFeaturesValidatingXInclude;
    private static final Map defaultFeaturesValidatingXInclude;
    private static final Collection recognizedFeaturesValidatingNoXInclude;
    private static final Map defaultFeaturesValidatingNoXInclude;
    static {
        {
            final OrbeonParserConfiguration configuration = XercesSAXParser.makeConfig(false, true);
            final Collection recognizedFeatures = configuration.getRecognizedFeatures();
            recognizedFeaturesNonValidatingXInclude = Collections.unmodifiableCollection(recognizedFeatures);
            defaultFeaturesNonValidatingXInclude = configuration.getFeatures();
            addDefaultFeatures(defaultFeaturesNonValidatingXInclude);
        }
        {
            final OrbeonParserConfiguration configuration = XercesSAXParser.makeConfig(false, false);
            final Collection features = configuration.getRecognizedFeatures();
            recognizedFeaturesNonValidatingNoXInclude = Collections.unmodifiableCollection(features);
            defaultFeaturesNonValidatingNoXInclude = configuration.getFeatures();
            addDefaultFeatures(defaultFeaturesNonValidatingNoXInclude);
        }
        {
            final OrbeonParserConfiguration configuration = XercesSAXParser.makeConfig(true, true);
            final Collection features = configuration.getRecognizedFeatures();
            recognizedFeaturesValidatingXInclude = Collections.unmodifiableCollection(features);
            defaultFeaturesValidatingXInclude = configuration.getFeatures();
            addDefaultFeatures(defaultFeaturesValidatingXInclude);
        }
        {
            final OrbeonParserConfiguration configuration = XercesSAXParser.makeConfig(true, false);
            final Collection features = configuration.getRecognizedFeatures();
            recognizedFeaturesValidatingNoXInclude = Collections.unmodifiableCollection(features);
            defaultFeaturesValidatingNoXInclude = configuration.getFeatures();
            addDefaultFeatures(defaultFeaturesValidatingNoXInclude);
        }
    }
    private static void addDefaultFeatures(Map features) {
        features.put("http:
        features.put("http:
        features.put("http:
        features.put("http:
    }
    private final Hashtable features;
    private final boolean validating;
    private final boolean handleXInclude;
    public XercesSAXParserFactoryImpl() {
        this(false, false);
    }
    public XercesSAXParserFactoryImpl(boolean validating, boolean handleXInclude) {
        this.validating = validating;
        this.handleXInclude = handleXInclude;
        if (!validating) {
            features = new Hashtable(handleXInclude ? defaultFeaturesNonValidatingXInclude : defaultFeaturesNonValidatingNoXInclude);
        } else {
            features = new Hashtable(handleXInclude ? defaultFeaturesValidatingXInclude : defaultFeaturesValidatingNoXInclude);
        }
        setNamespaceAware(true); 
    }
    public boolean getFeature(final String key) throws SAXNotRecognizedException {
        if (!getRecognizedFeatures().contains(key)) throw new SAXNotRecognizedException(key);
        return features.get(key) == Boolean.TRUE;
    }
    public void setFeature(final String key, final boolean val) throws SAXNotRecognizedException {
        if (!getRecognizedFeatures().contains(key)) throw new SAXNotRecognizedException(key);
        features.put(key, val ? Boolean.TRUE : Boolean.FALSE);
    }
    public SAXParser newSAXParser() {
        final SAXParser ret;
        try {
            ret = new XercesJAXPSAXParser(this, features, validating, handleXInclude);
        } catch (final SAXException se) {
            throw new OXFException(se); 
        }
        return ret;
    }
    private Collection getRecognizedFeatures() {
        if (!validating) {
            return handleXInclude ? recognizedFeaturesNonValidatingXInclude : recognizedFeaturesNonValidatingNoXInclude;
        } else {
            return handleXInclude ? recognizedFeaturesValidatingXInclude : recognizedFeaturesValidatingNoXInclude;
        }
    }
}