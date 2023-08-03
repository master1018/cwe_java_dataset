
package com.redhat.rhn.common.localization;
import com.redhat.rhn.common.conf.Config;
import com.redhat.rhn.common.conf.ConfigDefaults;
import com.redhat.rhn.common.db.datasource.DataResult;
import com.redhat.rhn.common.db.datasource.ModeFactory;
import com.redhat.rhn.common.db.datasource.SelectMode;
import com.redhat.rhn.common.util.StringUtil;
import com.redhat.rhn.frontend.context.Context;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import java.text.Collator;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
public class LocalizationService {
    public static final String RHN_DB_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String RHN_CUSTOM_DATEFORMAT = "yyyy-MM-dd HH:mm:ss z";
    private static Logger log = Logger.getLogger(LocalizationService.class);
    private static Logger msgLogger = Logger
            .getLogger("com.redhat.rhn.common.localization.messages");
    public static final Locale DEFAULT_LOCALE = new Locale("EN", "US");
    private static LocalizationService instance = new LocalizationService();
    private Map<String, String> keyToBundleMap;
    private final Map<String, LocaleInfo> supportedLocales =
            new HashMap<String, LocaleInfo>();
    private LocalizationService() {
        initService();
    }
    protected void initService() {
        if (keyToBundleMap != null) {
            log.warn("Reloading XML StringResource files.");
            XmlMessages.getInstance().resetBundleCache();
        }
        keyToBundleMap = new HashMap<String, String>();
        String[] packages = Config.get().getStringArray(
                ConfigDefaults.WEB_L10N_RESOURCEBUNDLES);
        for (int i = 0; i < packages.length; i++) {
            addKeysToMap(packages[i]);
        }
        if (supportedLocales.size() > 0) {
            supportedLocales.clear();
        }
        loadSupportedLocales();
    }
    private void addKeysToMap(String className) {
        try {
            Class z = Class.forName(className);
            Enumeration<String> e = XmlMessages.getInstance().getKeys(z, Locale.US);
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                keyToBundleMap.put(key, className);
            }
        }
        catch (ClassNotFoundException ce) {
            String message = "Class not found when trying to initalize " +
                    "the LocalizationService: " + ce.toString();
            log.error(message, ce);
            throw new LocalizationException(message, ce);
        }
    }
    private void loadSupportedLocales() {
        String rawLocales = Config.get().getString("java.supported_locales");
        if (rawLocales == null) {
            return;
        }
        List<String> compoundLocales = new LinkedList<String>();
        for (Enumeration<Object> locales = new StringTokenizer(rawLocales, ","); locales
                .hasMoreElements();) {
            String locale = (String) locales.nextElement();
            if (locale.indexOf('_') > -1) {
                compoundLocales.add(locale);
            }
            LocaleInfo li = new LocaleInfo(locale);
            this.supportedLocales.put(locale, li);
        }
        for (Iterator<String> iter = compoundLocales.iterator(); iter.hasNext();) {
            String cl = iter.next();
            String[] parts = cl.split("_");
            LocaleInfo li = new LocaleInfo(parts[0], cl);
            if (this.supportedLocales.get(parts[0]) == null) {
                this.supportedLocales.put(parts[0], li);
            }
        }
    }
    public static LocalizationService getInstance() {
        return instance;
    }
    public boolean reloadResourceFiles() {
        if (Config.get().getBoolean("java.development_environment")) {
            initService();
            return true;
        }
        log.error("Tried to reload XML StringResource files but " +
                "we aren't in java.development_environment mode");
        return false;
    }
    public String getMessage(String messageId) {
        Context ctx = Context.getCurrentContext();
        return getMessage(messageId, ctx.getLocale(), new Object[0]);
    }
    public String getMessage(String messageId, Locale locale) {
        return getMessage(messageId, locale, new Object[0]);
    }
    public String getMessage(String messageId, Object... args) {
        Context ctx = Context.getCurrentContext();
        return getMessage(messageId, ctx.getLocale(), args);
    }
    public String getPlainText(String messageId, Object... args) {
        String msg = getMessage(messageId, args);
        String unescaped = StringEscapeUtils.unescapeHtml(msg);
        return StringUtil.toPlainText(unescaped);
    }
    public String getPlainText(String messageId) {
        return getPlainText(messageId, (Object[])null);
    }
    public String[] getMessages(String[] keys) {
        String[] retval = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            retval[i] = getMessage(keys[i]);
        }
        return retval;
    }
    public String getMessage(String messageId, Locale locale, Object... args) {
        log.debug("getMessage() called with messageId: " + messageId +
                " and locale: " + locale);
        if (messageId == null) {
            return getMissingMessageString(messageId);
        }
        String userLocale = locale == null ? "null" : locale.toString();
        if (msgLogger.isDebugEnabled()) {
            msgLogger.debug("Resolving message \"" + messageId +
                    "\" for locale " + userLocale);
        }
        String mess = null;
        Class z = null;
        try {
            if (!keyToBundleMap.containsKey(messageId)) {
                return getMissingMessageString(messageId);
            }
            z = Class.forName(keyToBundleMap.get(messageId));
            mess = XmlMessages.getInstance().format(z, locale,
                    messageId, args);
        }
        catch (MissingResourceException e) {
            if (msgLogger.isDebugEnabled()) {
                msgLogger.debug("Resolving message \"" + messageId +
                        "\" for locale " + userLocale +
                        " failed -  trying again with default " + "locale " +
                        DEFAULT_LOCALE.toString());
            }
            try {
                mess = XmlMessages.getInstance().format(z, DEFAULT_LOCALE,
                        messageId, args);
            }
            catch (MissingResourceException mre) {
                if (msgLogger.isDebugEnabled()) {
                    msgLogger.debug("Resolving message \"" + messageId + "\" " +
                            "for default locale " + DEFAULT_LOCALE.toString() +
                            " failed");
                }
                return getMissingMessageString(messageId);
            }
        }
        catch (ClassNotFoundException ce) {
            String message = "Class not found when trying to fetch a message: " +
                    ce.toString();
            log.error(message, ce);
            throw new LocalizationException(message, ce);
        }
        return getDebugVersionOfString(mess);
    }
    private String getDebugVersionOfString(String mess) {
        boolean debugMode = Config.get().getBoolean("java.l10n_debug");
        if (debugMode) {
            StringBuilder debug = new StringBuilder();
            String marker = Config.get().getString("java.l10n_debug_marker",
                    "$$$");
            debug.append(marker);
            debug.append(mess);
            debug.append(marker);
            mess = debug.toString();
        }
        return mess;
    }
    private StackTraceElement getCallingMethod() {
        try {
            throw new RuntimeException("Stacktrace Dummy Exception");
        }
        catch (RuntimeException e) {
            try {
                final String prefix = this.getClass().getPackage().getName();
                for (StackTraceElement element : e.getStackTrace()) {
                    if (!element.getClassName().startsWith(prefix)) {
                        return element;
                    }
                }
            }
            catch (Throwable t) {
                return null;
            }
        }
        return null;
    }
    private String getMissingMessageString(String messageId) {
        String caller = "";
        StackTraceElement callerElement = getCallingMethod();
        if (callerElement != null) {
            caller = " called by " + callerElement;
        }
        if (messageId == null) {
            messageId = "null";
        }
        String message = "*** ERROR: Message with id: [" + messageId +
                "] not found.***" + caller;
        log.error(message);
        boolean exceptionMode = Config.get().getBoolean(
                "java.l10n_missingmessage_exceptions");
        if (exceptionMode) {
            throw new IllegalArgumentException(message);
        }
        return "**" + messageId + "**";
    }
    public String getDebugMessage(String messageId) {
        return getMessage(messageId, Locale.US);
    }
    public String formatDate(Date date) {
        Context ctx = Context.getCurrentContext();
        return formatDate(date, ctx.getLocale());
    }
    public String formatShortDate(Date date) {
        Context ctx = Context.getCurrentContext();
        return formatShortDate(date, ctx.getLocale());
    }
    public String formatShortDate(Date date, Locale locale) {
        StringBuilder dbuff = new StringBuilder();
        DateFormat dateI = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        dbuff.append(dateI.format(date));
        return getDebugVersionOfString(dbuff.toString());
    }
    public String getBasicDate() {
        return formatDate(new Date());
    }
    public String formatDate(Date date, Locale locale) {
        StringBuilder dbuff = new StringBuilder();
        DateFormat dateI = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        dateI.setTimeZone(determineTimeZone());
        DateFormat timeI = DateFormat.getTimeInstance(DateFormat.LONG, locale);
        timeI.setTimeZone(determineTimeZone());
        dbuff.append(dateI.format(date));
        dbuff.append(" ");
        dbuff.append(timeI.format(date));
        return getDebugVersionOfString(dbuff.toString());
    }
    public String formatCustomDate(Date date) {
        TimeZone tz = determineTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat(RHN_CUSTOM_DATEFORMAT);
        sdf.setTimeZone(tz);
        return sdf.format(date);
    }
    public String formatNumber(Number numberIn) {
        Context ctx = Context.getCurrentContext();
        return formatNumber(numberIn, ctx.getLocale());
    }
    public String formatNumber(Number numberIn, int fractionalDigits) {
        Context ctx = Context.getCurrentContext();
        return formatNumber(numberIn, ctx.getLocale(), fractionalDigits);
    }
    public String formatNumber(Number numberIn, Locale localeIn) {
        return getDebugVersionOfString(NumberFormat.getInstance(localeIn)
                .format(numberIn));
    }
    public String formatNumber(Number numberIn, Locale localeIn,
            int fractionalDigits) {
        NumberFormat nf = NumberFormat.getInstance(localeIn);
        nf.setMaximumFractionDigits(fractionalDigits);
        return getDebugVersionOfString(nf.format(numberIn));
    }
    public List<String> getAlphabet() {
        return StringUtil.stringToList(getMessage("alphabet"));
    }
    public List<String> getDigits() {
        return StringUtil.stringToList(getMessage("digits"));
    }
    public SortedSet<String> availablePrefixes() {
        SelectMode prefixMode = ModeFactory.getMode("util_queries",
                "available_prefixes");
        DataResult<Map<String, Object>> dr = prefixMode.execute(new HashMap());
        SortedSet<String> ret = new TreeSet<String>();
        Iterator<Map<String, Object>> i = dr.iterator();
        while (i.hasNext()) {
            Map<String, Object> row = i.next();
            ret.add((String) row.get("prefix"));
        }
        return ret;
    }
    public SortedMap<String, String> availableCountries() {
        List<String> validCountries = new LinkedList<String>(
                Arrays.asList(Locale
                .getISOCountries()));
        String[] excluded = Config.get().getStringArray(
                ConfigDefaults.WEB_EXCLUDED_COUNTRIES);
        if (excluded != null) {
            validCountries.removeAll(new LinkedList<String>(Arrays
                    .asList(excluded)));
        }
        SortedMap<String, String> ret = new TreeMap<String, String>();
        for (Iterator<String> iter = validCountries.iterator(); iter.hasNext();) {
            String isoCountry = iter.next();
            ret.put(this.getMessage(isoCountry), isoCountry);
        }
        return ret;
    }
    public boolean hasMessage(String messageId) {
        return this.keyToBundleMap.containsKey(messageId);
    }
    public List<String> getSupportedLocales() {
        List<String> tmp = new LinkedList<String>(this.supportedLocales.keySet());
        Collections.sort(tmp);
        return Collections.unmodifiableList(tmp);
    }
    public List<String> getConfiguredLocales() {
        List<String> tmp = new LinkedList<String>();
        for (Iterator<String> iter = this.supportedLocales.keySet().iterator(); iter
                .hasNext();) {
            String key = iter.next();
            LocaleInfo li = this.supportedLocales.get(key);
            if (!li.isAlias()) {
                tmp.add(key);
            }
        }
        Collections.sort(tmp);
        return Collections.unmodifiableList(tmp);
    }
    public boolean isLocaleSupported(Locale locale) {
        return this.supportedLocales.get(locale.toString()) != null;
    }
    private TimeZone determineTimeZone() {
        TimeZone retval = null;
        Context ctx = Context.getCurrentContext();
        if (ctx != null) {
            retval = ctx.getTimezone();
        }
        if (retval == null) {
            log.debug("Context is null");
            retval = TimeZone.getDefault();
        }
        if (log.isDebugEnabled()) {
            log.debug("Determined timeZone to be: " + retval);
        }
        return retval;
    }
    public Collator newCollator() {
        Context context = Context.getCurrentContext();
        if (context != null && context.getLocale() != null) {
            return Collator.getInstance(context.getLocale());
        }
        return Collator.getInstance();
    }
}
