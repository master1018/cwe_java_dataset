
package hudson.model;
import hudson.DescriptorExtensionList;
import hudson.PluginWrapper;
import hudson.RelativePath;
import hudson.XmlFile;
import hudson.BulkChange;
import hudson.Util;
import hudson.model.listeners.SaveableListener;
import hudson.util.FormApply;
import hudson.util.ReflectionUtils;
import hudson.util.ReflectionUtils.Parameter;
import hudson.views.ListViewColumn;
import jenkins.model.Jenkins;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.*;
import org.kohsuke.stapler.jelly.JellyCompatibleFacet;
import org.kohsuke.stapler.lang.Klass;
import org.springframework.util.StringUtils;
import org.jvnet.tiger_types.Types;
import org.apache.commons.io.IOUtils;
import static hudson.Functions.*;
import static hudson.util.QuotedStringTokenizer.*;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.beans.Introspector;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
public abstract class Descriptor<T extends Describable<T>> implements Saveable {
    public transient final Class<? extends T> clazz;
    private transient final Map<String,String> checkMethods = new ConcurrentHashMap<String,String>();
    private transient volatile Map<String, PropertyType> propertyTypes,globalPropertyTypes;
    public static final class PropertyType {
        public final Class clazz;
        public final Type type;
        private volatile Class itemType;
        public final String displayName;
        PropertyType(Class clazz, Type type, String displayName) {
            this.clazz = clazz;
            this.type = type;
            this.displayName = displayName;
        }
        PropertyType(Field f) {
            this(f.getType(),f.getGenericType(),f.toString());
        }
        PropertyType(Method getter) {
            this(getter.getReturnType(),getter.getGenericReturnType(),getter.toString());
        }
        public Enum[] getEnumConstants() {
            return (Enum[])clazz.getEnumConstants();
        }
        public Class getItemType() {
            if(itemType==null)
                itemType = computeItemType();
            return itemType;
        }
        private Class computeItemType() {
            if(clazz.isArray()) {
                return clazz.getComponentType();
            }
            if(Collection.class.isAssignableFrom(clazz)) {
                Type col = Types.getBaseClass(type, Collection.class);
                if (col instanceof ParameterizedType)
                    return Types.erasure(Types.getTypeArgument(col,0));
                else
                    return Object.class;
            }
            return null;
        }
        public Descriptor getItemTypeDescriptor() {
            return Jenkins.getInstance().getDescriptor(getItemType());
        }
        public Descriptor getItemTypeDescriptorOrDie() {
            Class it = getItemType();
            if (it == null) {
                throw new AssertionError(clazz + " is not an array/collection type in " + displayName + ". See https:
            }
            Descriptor d = Jenkins.getInstance().getDescriptor(it);
            if (d==null)
                throw new AssertionError(it +" is missing its descriptor in "+displayName+". See https:
            return d;
        }
        public List<? extends Descriptor> getApplicableDescriptors() {
            return Jenkins.getInstance().getDescriptorList(clazz);
        }
        public List<? extends Descriptor> getApplicableItemDescriptors() {
            return Jenkins.getInstance().getDescriptorList(getItemType());
        }
    }
    private transient final Map<String,String> helpRedirect = new HashMap<String, String>();
    protected Descriptor(Class<? extends T> clazz) {
        if (clazz==self())
            clazz = (Class)getClass();
        this.clazz = clazz;
    }
    protected Descriptor() {
        this.clazz = (Class<T>)getClass().getEnclosingClass();
        if(clazz==null)
            throw new AssertionError(getClass()+" doesn't have an outer class. Use the constructor that takes the Class object explicitly.");
        Type bt = Types.getBaseClass(getClass(), Descriptor.class);
        if (bt instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) bt;
            Class t = Types.erasure(pt.getActualTypeArguments()[0]);
            if(!t.isAssignableFrom(clazz))
                throw new AssertionError("Outer class "+clazz+" of "+getClass()+" is not assignable to "+t+". Perhaps wrong outer class?");
        }
        try {
            Method getd = clazz.getMethod("getDescriptor");
            if(!getd.getReturnType().isAssignableFrom(getClass())) {
                throw new AssertionError(getClass()+" must be assignable to "+getd.getReturnType());
            }
        } catch (NoSuchMethodException e) {
            throw new AssertionError(getClass()+" is missing getDescriptor method.");
        }
    }
    public abstract String getDisplayName();
    public String getId() {
        return clazz.getName();
    }
    public Class<T> getT() {
        Type subTyping = Types.getBaseClass(getClass(), Descriptor.class);
        if (!(subTyping instanceof ParameterizedType)) {
            throw new IllegalStateException(getClass()+" doesn't extend Descriptor with a type parameter.");
        }
        return Types.erasure(Types.getTypeArgument(subTyping, 0));
    }
    public String getDescriptorUrl() {
        return "descriptorByName/"+getId();
    }
    public final String getDescriptorFullUrl() {
        return getCurrentDescriptorByNameUrl()+'/'+getDescriptorUrl();
    }
    public static String getCurrentDescriptorByNameUrl() {
        StaplerRequest req = Stapler.getCurrentRequest();
        Object url = req.getAttribute("currentDescriptorByNameUrl");
        if (url!=null)  return url.toString();
        Ancestor a = req.findAncestor(DescriptorByNameOwner.class);
        return a.getUrl();
    }
    public String getCheckUrl(String fieldName) {
        String method = checkMethods.get(fieldName);
        if(method==null) {
            method = calcCheckUrl(fieldName);
            checkMethods.put(fieldName,method);
        }
        if (method.equals(NONE)) 
            return null;
        return '\'' + jsStringEscape(getCurrentDescriptorByNameUrl()) + "/'+" + method;
    }
    private String calcCheckUrl(String fieldName) {
        String capitalizedFieldName = StringUtils.capitalize(fieldName);
        Method method = ReflectionUtils.getPublicMethodNamed(getClass(),"doCheck"+ capitalizedFieldName);
        if(method==null)
            return NONE;
        return '\'' + getDescriptorUrl() + "/check" + capitalizedFieldName + '\'' + buildParameterList(method, new StringBuilder()).append(".toString()");
    }
    private StringBuilder buildParameterList(Method method, StringBuilder query) {
        for (Parameter p : ReflectionUtils.getParameters(method)) {
            QueryParameter qp = p.annotation(QueryParameter.class);
            if (qp!=null) {
                String name = qp.value();
                if (name.length()==0) name = p.name();
                if (name==null || name.length()==0)
                    continue;   
                RelativePath rp = p.annotation(RelativePath.class);
                if (rp!=null)
                    name = rp.value()+'/'+name;
                if (query.length()==0)  query.append("+qs(this)");
                if (name.equals("value")) {
                    query.append(".addThis()");
                } else {
                    query.append(".nearBy('"+name+"')");
                }
                continue;
            }
            Method m = ReflectionUtils.getPublicMethodNamed(p.type(), "fromStapler");
            if (m!=null)    buildParameterList(m,query);
        }
        return query;
    }
    public void calcFillSettings(String field, Map<String,Object> attributes) {
        String capitalizedFieldName = StringUtils.capitalize(field);
        String methodName = "doFill" + capitalizedFieldName + "Items";
        Method method = ReflectionUtils.getPublicMethodNamed(getClass(), methodName);
        if(method==null)
            throw new IllegalStateException(String.format("%s doesn't have the %s method for filling a drop-down list", getClass(), methodName));
        List<String> depends = buildFillDependencies(method, new ArrayList<String>());
        if (!depends.isEmpty())
            attributes.put("fillDependsOn",Util.join(depends," "));
        attributes.put("fillUrl", String.format("%s/%s/fill%sItems", getCurrentDescriptorByNameUrl(), getDescriptorUrl(), capitalizedFieldName));
    }
    private List<String> buildFillDependencies(Method method, List<String> depends) {
        for (Parameter p : ReflectionUtils.getParameters(method)) {
            QueryParameter qp = p.annotation(QueryParameter.class);
            if (qp!=null) {
                String name = qp.value();
                if (name.length()==0) name = p.name();
                if (name==null || name.length()==0)
                    continue;   
                RelativePath rp = p.annotation(RelativePath.class);
                if (rp!=null)
                    name = rp.value()+'/'+name;
                depends.add(name);
                continue;
            }
            Method m = ReflectionUtils.getPublicMethodNamed(p.type(), "fromStapler");
            if (m!=null)
                buildFillDependencies(m,depends);
        }
        return depends;
    }
    public void calcAutoCompleteSettings(String field, Map<String,Object> attributes) {
        String capitalizedFieldName = StringUtils.capitalize(field);
        String methodName = "doAutoComplete" + capitalizedFieldName;
        Method method = ReflectionUtils.getPublicMethodNamed(getClass(), methodName);
        if(method==null)
            return;    
        attributes.put("autoCompleteUrl", String.format("%s/%s/autoComplete%s", getCurrentDescriptorByNameUrl(), getDescriptorUrl(), capitalizedFieldName));
    }
    public @CheckForNull PropertyType getPropertyType(@Nonnull Object instance, @Nonnull String field) {
        return instance==this ? getGlobalPropertyType(field) : getPropertyType(field);
    }
    public @Nonnull PropertyType getPropertyTypeOrDie(@Nonnull Object instance, @Nonnull String field) {
        PropertyType propertyType = getPropertyType(instance, field);
        if (propertyType != null) {
            return propertyType;
        } else if (instance == this) {
            throw new AssertionError(getClass().getName() + " has no property " + field);
        } else {
            throw new AssertionError(clazz.getName() + " has no property " + field);
        }
    }
    public PropertyType getPropertyType(String field) {
        if(propertyTypes==null)
            propertyTypes = buildPropertyTypes(clazz);
        return propertyTypes.get(field);
    }
    public PropertyType getGlobalPropertyType(String field) {
        if(globalPropertyTypes==null)
            globalPropertyTypes = buildPropertyTypes(getClass());
        return globalPropertyTypes.get(field);
    }
    private Map<String, PropertyType> buildPropertyTypes(Class<?> clazz) {
        Map<String, PropertyType> r = new HashMap<String, PropertyType>();
        for (Field f : clazz.getFields())
            r.put(f.getName(),new PropertyType(f));
        for (Method m : clazz.getMethods())
            if(m.getName().startsWith("get"))
                r.put(Introspector.decapitalize(m.getName().substring(3)),new PropertyType(m));
        return r;
    }
    public final String getJsonSafeClassName() {
        return getId().replace('.','-');
    }
    public T newInstance(StaplerRequest req) throws FormException {
        throw new UnsupportedOperationException(getClass()+" should implement newInstance(StaplerRequest,JSONObject)");
    }
    public T newInstance(StaplerRequest req, JSONObject formData) throws FormException {
        try {
            Method m = getClass().getMethod("newInstance", StaplerRequest.class);
            if(!Modifier.isAbstract(m.getDeclaringClass().getModifiers())) {
                return verifyNewInstance(newInstance(req));
            } else {
                if (req==null) {
                    return verifyNewInstance(clazz.newInstance());
                }
                return verifyNewInstance(req.bindJSON(clazz,formData));
            }
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e); 
        } catch (InstantiationException e) {
            throw new Error("Failed to instantiate "+clazz+" from "+formData,e);
        } catch (IllegalAccessException e) {
            throw new Error("Failed to instantiate "+clazz+" from "+formData,e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to instantiate "+clazz+" from "+formData,e);
        }
    }
    private T verifyNewInstance(T t) {
        if (t!=null && t.getDescriptor()!=this) {
            LOGGER.warning("Father of "+ t+" and its getDescriptor() points to two different instances. Probably malplaced @Extension. See http:
        }
        return t;
    }
    public Klass<?> getKlass() {
        return Klass.java(clazz);
    }
    public String getHelpFile() {
        return getHelpFile(null);
    }
    public String getHelpFile(final String fieldName) {
        return getHelpFile(getKlass(),fieldName);
    }
    public String getHelpFile(Klass<?> clazz, String fieldName) {
        String v = helpRedirect.get(fieldName);
        if (v!=null)    return v;
        for (Klass<?> c : clazz.getAncestors()) {
            String page = "/descriptor/" + getId() + "/help";
            String suffix;
            if(fieldName==null) {
                suffix="";
            } else {
                page += '/'+fieldName;
                suffix='-'+fieldName;
            }
            try {
                if(Stapler.getCurrentRequest().getView(c,"help"+suffix)!=null)
                    return page;
            } catch (IOException e) {
                throw new Error(e);
            }
            if(getStaticHelpUrl(c, suffix) !=null)    return page;
        }
        return null;
    }
    protected void addHelpFileRedirect(String fieldName, Class<? extends Describable> owner, String fieldNameToRedirectTo) {
        helpRedirect.put(fieldName,
            Jenkins.getInstance().getDescriptor(owner).getHelpFile(fieldNameToRedirectTo));
    }
    public final boolean isInstance( T instance ) {
        return clazz.isInstance(instance);
    }
    public final boolean isSubTypeOf(Class type) {
        return type.isAssignableFrom(clazz);
    }
    public boolean configure( StaplerRequest req ) throws FormException {
        return true;
    }
    public boolean configure( StaplerRequest req, JSONObject json ) throws FormException {
        return configure(req);
    }
    public String getConfigPage() {
        return getViewPage(clazz, getPossibleViewNames("config"), "config.jelly");
    }
    public String getGlobalConfigPage() {
        return getViewPage(clazz, getPossibleViewNames("global"), null);
    }
    private String getViewPage(Class<?> clazz, String pageName, String defaultValue) {
        return getViewPage(clazz,Collections.singleton(pageName),defaultValue);
    }
    private String getViewPage(Class<?> clazz, Collection<String> pageNames, String defaultValue) {
        while(clazz!=Object.class && clazz!=null) {
            for (String pageName : pageNames) {
                String name = clazz.getName().replace('.', '/').replace('$', '/') + "/" + pageName;
                if(clazz.getClassLoader().getResource(name)!=null)
                    return '/'+name;
            }
            clazz = clazz.getSuperclass();
        }
        return defaultValue;
    }
    protected final String getViewPage(Class<?> clazz, String pageName) {
        return getViewPage(clazz,pageName,pageName);
    }
    protected List<String> getPossibleViewNames(String baseName) {
        List<String> names = new ArrayList<String>();
        for (Facet f : WebApp.get(Jenkins.getInstance().servletContext).facets) {
            if (f instanceof JellyCompatibleFacet) {
                JellyCompatibleFacet jcf = (JellyCompatibleFacet) f;
                for (String ext : jcf.getScriptExtensions())
                    names.add(baseName +ext);
            }
        }
        return names;
    }
    public synchronized void save() {
        if(BulkChange.contains(this))   return;
        try {
            getConfigFile().write(this);
            SaveableListener.fireOnChange(this, getConfigFile());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to save "+getConfigFile(),e);
        }
    }
    public synchronized void load() {
        XmlFile file = getConfigFile();
        if(!file.exists())
            return;
        try {
            file.unmarshal(this);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load "+file, e);
        }
    }
    protected final XmlFile getConfigFile() {
        return new XmlFile(new File(Jenkins.getInstance().getRootDir(),getId()+".xml"));
    }
    protected PluginWrapper getPlugin() {
        return Jenkins.getInstance().getPluginManager().whichPlugin(clazz);
    }
    public void doHelp(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        String path = req.getRestOfPath();
        if(path.contains("..")) throw new ServletException("Illegal path: "+path);
        path = path.replace('/','-');
        PluginWrapper pw = getPlugin();
        if (pw!=null) {
            rsp.setHeader("X-Plugin-Short-Name",pw.getShortName());
            rsp.setHeader("X-Plugin-Long-Name",pw.getLongName());
            rsp.setHeader("X-Plugin-From", Messages.Descriptor_From(
                    pw.getLongName().replace("Hudson","Jenkins").replace("hudson","jenkins"), pw.getUrl()));
        }
        for (Klass<?> c= getKlass(); c!=null; c=c.getSuperClass()) {
            RequestDispatcher rd = Stapler.getCurrentRequest().getView(c, "help"+path);
            if(rd!=null) {
                rd.forward(req,rsp);
                return;
            }
            URL url = getStaticHelpUrl(c, path);
            if(url!=null) {
                rsp.setContentType("text/html;charset=UTF-8");
                InputStream in = url.openStream();
                try {
                    String literal = IOUtils.toString(in,"UTF-8");
                    rsp.getWriter().println(Util.replaceMacro(literal, Collections.singletonMap("rootURL",req.getContextPath())));
                } finally {
                    IOUtils.closeQuietly(in);
                }
                return;
            }
        }
        rsp.sendError(SC_NOT_FOUND);
    }
    private URL getStaticHelpUrl(Klass<?> c, String suffix) {
        Locale locale = Stapler.getCurrentRequest().getLocale();
        String base = "help"+suffix;
        URL url;
        url = c.getResource(base + '_' + locale.getLanguage() + '_' + locale.getCountry() + '_' + locale.getVariant() + ".html");
        if(url!=null)    return url;
        url = c.getResource(base + '_' + locale.getLanguage() + '_' + locale.getCountry() + ".html");
        if(url!=null)    return url;
        url = c.getResource(base + '_' + locale.getLanguage() + ".html");
        if(url!=null)    return url;
        return c.getResource(base + ".html");
    }
    public static <T> T[] toArray( T... values ) {
        return values;
    }
    public static <T> List<T> toList( T... values ) {
        return new ArrayList<T>(Arrays.asList(values));
    }
    public static <T extends Describable<T>>
    Map<Descriptor<T>,T> toMap(Iterable<T> describables) {
        Map<Descriptor<T>,T> m = new LinkedHashMap<Descriptor<T>,T>();
        for (T d : describables) {
            m.put(d.getDescriptor(),d);
        }
        return m;
    }
    public static <T extends Describable<T>>
    List<T> newInstancesFromHeteroList(StaplerRequest req, JSONObject formData, String key,
                Collection<? extends Descriptor<T>> descriptors) throws FormException {
        return newInstancesFromHeteroList(req,formData.get(key),descriptors);
    }
    public static <T extends Describable<T>>
    List<T> newInstancesFromHeteroList(StaplerRequest req, Object formData,
                Collection<? extends Descriptor<T>> descriptors) throws FormException {
        List<T> items = new ArrayList<T>();
        if (formData!=null) {
            for (Object o : JSONArray.fromObject(formData)) {
                JSONObject jo = (JSONObject)o;
                String kind = jo.getString("kind");
                items.add(find(descriptors,kind).newInstance(req,jo));
            }
        }
        return items;
    }
    public static <T extends Descriptor> T find(Collection<? extends T> list, String className) {
        for (T d : list) {
            if(d.getClass().getName().equals(className))
                return d;
        }
        for (T d : list) {
            if(d.getId().equals(className))
                return d;
        }
        return null;
    }
    public static Descriptor find(String className) {
        return find(Jenkins.getInstance().getExtensionList(Descriptor.class),className);
    }
    public static final class FormException extends Exception implements HttpResponse {
        private final String formField;
        public FormException(String message, String formField) {
            super(message);
            this.formField = formField;
        }
        public FormException(String message, Throwable cause, String formField) {
            super(message, cause);
            this.formField = formField;
        }
        public FormException(Throwable cause, String formField) {
            super(cause);
            this.formField = formField;
        }
        public String getFormField() {
            return formField;
        }
        public void generateResponse(StaplerRequest req, StaplerResponse rsp, Object node) throws IOException, ServletException {
            if (FormApply.isApply(req)) {
                FormApply.applyResponse("notificationBar.show(" + quote(getMessage())+ ",notificationBar.defaultOptions.ERROR)")
                        .generateResponse(req, rsp, node);
            } else {
                new Failure(getMessage()).generateResponse(req,rsp,node);
            }
        }
    }
    private static final Logger LOGGER = Logger.getLogger(Descriptor.class.getName());
    private static final String NONE = "\u0000";
    public static final class Self {}
    protected static Class self() { return Self.class; }
}
