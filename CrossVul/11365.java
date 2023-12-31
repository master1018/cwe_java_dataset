
package com.thoughtworks.xstream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.ConverterRegistry;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.SingleValueConverterWrapper;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;
import com.thoughtworks.xstream.converters.basic.BigIntegerConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.basic.ByteConverter;
import com.thoughtworks.xstream.converters.basic.CharConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.converters.basic.NullConverter;
import com.thoughtworks.xstream.converters.basic.ShortConverter;
import com.thoughtworks.xstream.converters.basic.StringBufferConverter;
import com.thoughtworks.xstream.converters.basic.StringConverter;
import com.thoughtworks.xstream.converters.basic.URIConverter;
import com.thoughtworks.xstream.converters.basic.URLConverter;
import com.thoughtworks.xstream.converters.collections.ArrayConverter;
import com.thoughtworks.xstream.converters.collections.BitSetConverter;
import com.thoughtworks.xstream.converters.collections.CharArrayConverter;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.converters.collections.PropertiesConverter;
import com.thoughtworks.xstream.converters.collections.SingletonCollectionConverter;
import com.thoughtworks.xstream.converters.collections.SingletonMapConverter;
import com.thoughtworks.xstream.converters.collections.TreeMapConverter;
import com.thoughtworks.xstream.converters.collections.TreeSetConverter;
import com.thoughtworks.xstream.converters.extended.ColorConverter;
import com.thoughtworks.xstream.converters.extended.DynamicProxyConverter;
import com.thoughtworks.xstream.converters.extended.EncodedByteArrayConverter;
import com.thoughtworks.xstream.converters.extended.FileConverter;
import com.thoughtworks.xstream.converters.extended.FontConverter;
import com.thoughtworks.xstream.converters.extended.GregorianCalendarConverter;
import com.thoughtworks.xstream.converters.extended.JavaClassConverter;
import com.thoughtworks.xstream.converters.extended.JavaFieldConverter;
import com.thoughtworks.xstream.converters.extended.JavaMethodConverter;
import com.thoughtworks.xstream.converters.extended.LocaleConverter;
import com.thoughtworks.xstream.converters.extended.LookAndFeelConverter;
import com.thoughtworks.xstream.converters.extended.SqlDateConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimeConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimestampConverter;
import com.thoughtworks.xstream.converters.extended.TextAttributeConverter;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.core.DefaultConverterLookup;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.core.MapBackedDataHolder;
import com.thoughtworks.xstream.core.ReferenceByIdMarshallingStrategy;
import com.thoughtworks.xstream.core.ReferenceByXPathMarshallingStrategy;
import com.thoughtworks.xstream.core.TreeMarshallingStrategy;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.core.util.CustomObjectInputStream;
import com.thoughtworks.xstream.core.util.CustomObjectOutputStream;
import com.thoughtworks.xstream.core.util.SelfStreamingInstanceChecker;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StatefulWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.AnnotationConfiguration;
import com.thoughtworks.xstream.mapper.ArrayMapper;
import com.thoughtworks.xstream.mapper.AttributeAliasingMapper;
import com.thoughtworks.xstream.mapper.AttributeMapper;
import com.thoughtworks.xstream.mapper.CachingMapper;
import com.thoughtworks.xstream.mapper.ClassAliasingMapper;
import com.thoughtworks.xstream.mapper.DefaultImplementationsMapper;
import com.thoughtworks.xstream.mapper.DefaultMapper;
import com.thoughtworks.xstream.mapper.DynamicProxyMapper;
import com.thoughtworks.xstream.mapper.ElementIgnoringMapper;
import com.thoughtworks.xstream.mapper.FieldAliasingMapper;
import com.thoughtworks.xstream.mapper.ImmutableTypesMapper;
import com.thoughtworks.xstream.mapper.ImplicitCollectionMapper;
import com.thoughtworks.xstream.mapper.LocalConversionMapper;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.thoughtworks.xstream.mapper.OuterClassMapper;
import com.thoughtworks.xstream.mapper.PackageAliasingMapper;
import com.thoughtworks.xstream.mapper.SecurityMapper;
import com.thoughtworks.xstream.mapper.SystemAttributeAliasingMapper;
import com.thoughtworks.xstream.mapper.XStream11XmlFriendlyMapper;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.ArrayTypePermission;
import com.thoughtworks.xstream.security.ExplicitTypePermission;
import com.thoughtworks.xstream.security.InterfaceTypePermission;
import com.thoughtworks.xstream.security.NoPermission;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import com.thoughtworks.xstream.security.RegExpTypePermission;
import com.thoughtworks.xstream.security.TypeHierarchyPermission;
import com.thoughtworks.xstream.security.TypePermission;
import com.thoughtworks.xstream.security.WildcardTypePermission;
public class XStream {
    private ReflectionProvider reflectionProvider;
    private HierarchicalStreamDriver hierarchicalStreamDriver;
    private ClassLoaderReference classLoaderReference;
    private MarshallingStrategy marshallingStrategy;
    private ConverterLookup converterLookup;
    private ConverterRegistry converterRegistry;
    private Mapper mapper;
    private PackageAliasingMapper packageAliasingMapper;
    private ClassAliasingMapper classAliasingMapper;
    private FieldAliasingMapper fieldAliasingMapper;
    private ElementIgnoringMapper elementIgnoringMapper;
    private AttributeAliasingMapper attributeAliasingMapper;
    private SystemAttributeAliasingMapper systemAttributeAliasingMapper;
    private AttributeMapper attributeMapper;
    private DefaultImplementationsMapper defaultImplementationsMapper;
    private ImmutableTypesMapper immutableTypesMapper;
    private ImplicitCollectionMapper implicitCollectionMapper;
    private LocalConversionMapper localConversionMapper;
    private SecurityMapper securityMapper;
    private AnnotationConfiguration annotationConfiguration;
    private transient boolean securityInitialized;
    private transient boolean securityWarningGiven;
    public static final int NO_REFERENCES = 1001;
    public static final int ID_REFERENCES = 1002;
    public static final int XPATH_RELATIVE_REFERENCES = 1003;
    public static final int XPATH_ABSOLUTE_REFERENCES = 1004;
    public static final int SINGLE_NODE_XPATH_RELATIVE_REFERENCES = 1005;
    public static final int SINGLE_NODE_XPATH_ABSOLUTE_REFERENCES = 1006;
    public static final int PRIORITY_VERY_HIGH = 10000;
    public static final int PRIORITY_NORMAL = 0;
    public static final int PRIORITY_LOW = -10;
    public static final int PRIORITY_VERY_LOW = -20;
    private static final String ANNOTATION_MAPPER_TYPE = "com.thoughtworks.xstream.mapper.AnnotationMapper";
    private static final Pattern IGNORE_ALL = Pattern.compile(".*");
    private static final Pattern LAZY_ITERATORS = Pattern.compile(".*\\$LazyIterator");
    private static final Pattern JAVAX_CRYPTO = Pattern.compile("javax\\.crypto\\..*");
    public XStream() {
        this(null, (Mapper)null, new XppDriver());
    }
    public XStream(ReflectionProvider reflectionProvider) {
        this(reflectionProvider, (Mapper)null, new XppDriver());
    }
    public XStream(HierarchicalStreamDriver hierarchicalStreamDriver) {
        this(null, (Mapper)null, hierarchicalStreamDriver);
    }
    public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver hierarchicalStreamDriver) {
        this(reflectionProvider, (Mapper)null, hierarchicalStreamDriver);
    }
    public XStream(ReflectionProvider reflectionProvider, Mapper mapper, HierarchicalStreamDriver driver) {
        this(reflectionProvider, driver, new CompositeClassLoader(), mapper);
    }
    public XStream(
            ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver,
            ClassLoaderReference classLoaderReference) {
        this(reflectionProvider, driver, classLoaderReference, null);
    }
    public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader) {
        this(reflectionProvider, driver, classLoader, null);
    }
    public XStream(
            ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader,
            Mapper mapper) {
        this(reflectionProvider, driver, new ClassLoaderReference(classLoader), mapper, new DefaultConverterLookup());
    }
    public XStream(
            ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver,
            ClassLoaderReference classLoaderReference, Mapper mapper) {
        this(reflectionProvider, driver, classLoaderReference, mapper, new DefaultConverterLookup());
    }
    private XStream(
            ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoaderReference classLoader,
            Mapper mapper, final DefaultConverterLookup defaultConverterLookup) {
        this(reflectionProvider, driver, classLoader, mapper, new ConverterLookup() {
            public Converter lookupConverterForType(Class type) {
                return defaultConverterLookup.lookupConverterForType(type);
            }
        }, new ConverterRegistry() {
            public void registerConverter(Converter converter, int priority) {
                defaultConverterLookup.registerConverter(converter, priority);
            }
        });
    }
    public XStream(
            ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader,
            Mapper mapper, ConverterLookup converterLookup, ConverterRegistry converterRegistry) {
        this(reflectionProvider, driver, new ClassLoaderReference(classLoader), mapper, converterLookup,
            converterRegistry);
    }
    public XStream(
            ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver,
            ClassLoaderReference classLoaderReference, Mapper mapper, ConverterLookup converterLookup,
            ConverterRegistry converterRegistry) {
        if (reflectionProvider == null) {
            reflectionProvider = JVM.newReflectionProvider();
        }
        this.reflectionProvider = reflectionProvider;
        this.hierarchicalStreamDriver = driver;
        this.classLoaderReference = classLoaderReference;
        this.converterLookup = converterLookup;
        this.converterRegistry = converterRegistry;
        this.mapper = mapper == null ? buildMapper() : mapper;
        setupMappers();
        setupSecurity();
        setupAliases();
        setupDefaultImplementations();
        setupConverters();
        setupImmutableTypes();
        setMode(XPATH_RELATIVE_REFERENCES);
    }
    private Mapper buildMapper() {
        Mapper mapper = new DefaultMapper(classLoaderReference);
        if (useXStream11XmlFriendlyMapper()) {
            mapper = new XStream11XmlFriendlyMapper(mapper);
        }
        mapper = new DynamicProxyMapper(mapper);
        mapper = new PackageAliasingMapper(mapper);
        mapper = new ClassAliasingMapper(mapper);
        mapper = new ElementIgnoringMapper(mapper);
        mapper = new FieldAliasingMapper(mapper);
        mapper = new AttributeAliasingMapper(mapper);
        mapper = new SystemAttributeAliasingMapper(mapper);
        mapper = new ImplicitCollectionMapper(mapper, reflectionProvider);
        mapper = new OuterClassMapper(mapper);
        mapper = new ArrayMapper(mapper);
        mapper = new DefaultImplementationsMapper(mapper);
        mapper = new AttributeMapper(mapper, converterLookup, reflectionProvider);
        if (JVM.isVersion(5)) {
            mapper = buildMapperDynamically("com.thoughtworks.xstream.mapper.EnumMapper", new Class[]{Mapper.class},
                new Object[]{mapper});
        }
        mapper = new LocalConversionMapper(mapper);
        mapper = new ImmutableTypesMapper(mapper);
        if (JVM.isVersion(8)) {
            mapper = buildMapperDynamically("com.thoughtworks.xstream.mapper.LambdaMapper", new Class[]{Mapper.class},
                new Object[]{mapper});
        }
        mapper = new SecurityMapper(mapper);
        if (JVM.isVersion(5)) {
            mapper = buildMapperDynamically(ANNOTATION_MAPPER_TYPE, new Class[]{
                Mapper.class, ConverterRegistry.class, ConverterLookup.class, ClassLoaderReference.class,
                ReflectionProvider.class}, new Object[]{
                    mapper, converterRegistry, converterLookup, classLoaderReference, reflectionProvider});
        }
        mapper = wrapMapper((MapperWrapper)mapper);
        mapper = new CachingMapper(mapper);
        return mapper;
    }
    private Mapper buildMapperDynamically(String className, Class[] constructorParamTypes,
            Object[] constructorParamValues) {
        try {
            Class type = Class.forName(className, false, classLoaderReference.getReference());
            Constructor constructor = type.getConstructor(constructorParamTypes);
            return (Mapper)constructor.newInstance(constructorParamValues);
        } catch (Exception e) {
            throw new com.thoughtworks.xstream.InitializationException("Could not instantiate mapper : " + className,
                e);
        } catch (LinkageError e) {
            throw new com.thoughtworks.xstream.InitializationException("Could not instantiate mapper : " + className,
                e);
        }
    }
    protected MapperWrapper wrapMapper(MapperWrapper next) {
        return next;
    }
    protected boolean useXStream11XmlFriendlyMapper() {
        return false;
    }
    private void setupMappers() {
        packageAliasingMapper = (PackageAliasingMapper)this.mapper.lookupMapperOfType(PackageAliasingMapper.class);
        classAliasingMapper = (ClassAliasingMapper)this.mapper.lookupMapperOfType(ClassAliasingMapper.class);
        elementIgnoringMapper = (ElementIgnoringMapper)this.mapper.lookupMapperOfType(ElementIgnoringMapper.class);
        fieldAliasingMapper = (FieldAliasingMapper)this.mapper.lookupMapperOfType(FieldAliasingMapper.class);
        attributeMapper = (AttributeMapper)this.mapper.lookupMapperOfType(AttributeMapper.class);
        attributeAliasingMapper = (AttributeAliasingMapper)this.mapper
            .lookupMapperOfType(AttributeAliasingMapper.class);
        systemAttributeAliasingMapper = (SystemAttributeAliasingMapper)this.mapper
            .lookupMapperOfType(SystemAttributeAliasingMapper.class);
        implicitCollectionMapper = (ImplicitCollectionMapper)this.mapper
            .lookupMapperOfType(ImplicitCollectionMapper.class);
        defaultImplementationsMapper = (DefaultImplementationsMapper)this.mapper
            .lookupMapperOfType(DefaultImplementationsMapper.class);
        immutableTypesMapper = (ImmutableTypesMapper)this.mapper.lookupMapperOfType(ImmutableTypesMapper.class);
        localConversionMapper = (LocalConversionMapper)this.mapper.lookupMapperOfType(LocalConversionMapper.class);
        securityMapper = (SecurityMapper)this.mapper.lookupMapperOfType(SecurityMapper.class);
        annotationConfiguration = (AnnotationConfiguration)this.mapper
            .lookupMapperOfType(AnnotationConfiguration.class);
    }
    protected void setupSecurity() {
        if (securityMapper == null) {
            return;
        }
        addPermission(AnyTypePermission.ANY);
        denyTypes(new String[]{"java.beans.EventHandler", "javax.imageio.ImageIO$ContainsFilter"});
        denyTypesByRegExp(new Pattern[]{LAZY_ITERATORS, JAVAX_CRYPTO});
        allowTypeHierarchy(Exception.class);
        securityInitialized = false;
    }
    public static void setupDefaultSecurity(final XStream xstream) {
        if (!xstream.securityInitialized) {
            xstream.addPermission(NoTypePermission.NONE);
            xstream.addPermission(NullPermission.NULL);
            xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
            xstream.addPermission(ArrayTypePermission.ARRAYS);
            xstream.addPermission(InterfaceTypePermission.INTERFACES);
            xstream.allowTypeHierarchy(Calendar.class);
            xstream.allowTypeHierarchy(Collection.class);
            xstream.allowTypeHierarchy(Map.class);
            xstream.allowTypeHierarchy(Map.Entry.class);
            xstream.allowTypeHierarchy(Member.class);
            xstream.allowTypeHierarchy(Number.class);
            xstream.allowTypeHierarchy(Throwable.class);
            xstream.allowTypeHierarchy(TimeZone.class);
            Class type = JVM.loadClassForName("java.lang.Enum");
            if (type != null) {
                xstream.allowTypeHierarchy(type);
            }
            type = JVM.loadClassForName("java.nio.file.Path");
            if (type != null) {
                xstream.allowTypeHierarchy(type);
            }
            final Set types = new HashSet();
            types.add(BitSet.class);
            types.add(Charset.class);
            types.add(Class.class);
            types.add(Currency.class);
            types.add(Date.class);
            types.add(DecimalFormatSymbols.class);
            types.add(File.class);
            types.add(Locale.class);
            types.add(Object.class);
            types.add(Pattern.class);
            types.add(StackTraceElement.class);
            types.add(String.class);
            types.add(StringBuffer.class);
            types.add(JVM.loadClassForName("java.lang.StringBuilder"));
            types.add(URL.class);
            types.add(URI.class);
            types.add(JVM.loadClassForName("java.util.UUID"));
            if (JVM.isSQLAvailable()) {
                types.add(JVM.loadClassForName("java.sql.Timestamp"));
                types.add(JVM.loadClassForName("java.sql.Time"));
                types.add(JVM.loadClassForName("java.sql.Date"));
            }
            if (JVM.isVersion(8)) {
                xstream.allowTypeHierarchy(JVM.loadClassForName("java.time.Clock"));
                types.add(JVM.loadClassForName("java.time.Duration"));
                types.add(JVM.loadClassForName("java.time.Instant"));
                types.add(JVM.loadClassForName("java.time.LocalDate"));
                types.add(JVM.loadClassForName("java.time.LocalDateTime"));
                types.add(JVM.loadClassForName("java.time.LocalTime"));
                types.add(JVM.loadClassForName("java.time.MonthDay"));
                types.add(JVM.loadClassForName("java.time.OffsetDateTime"));
                types.add(JVM.loadClassForName("java.time.OffsetTime"));
                types.add(JVM.loadClassForName("java.time.Period"));
                types.add(JVM.loadClassForName("java.time.Ser"));
                types.add(JVM.loadClassForName("java.time.Year"));
                types.add(JVM.loadClassForName("java.time.YearMonth"));
                types.add(JVM.loadClassForName("java.time.ZonedDateTime"));
                xstream.allowTypeHierarchy(JVM.loadClassForName("java.time.ZoneId"));
                types.add(JVM.loadClassForName("java.time.chrono.HijrahDate"));
                types.add(JVM.loadClassForName("java.time.chrono.JapaneseDate"));
                types.add(JVM.loadClassForName("java.time.chrono.JapaneseEra"));
                types.add(JVM.loadClassForName("java.time.chrono.MinguoDate"));
                types.add(JVM.loadClassForName("java.time.chrono.ThaiBuddhistDate"));
                types.add(JVM.loadClassForName("java.time.chrono.Ser"));
                xstream.allowTypeHierarchy(JVM.loadClassForName("java.time.chrono.Chronology"));
                types.add(JVM.loadClassForName("java.time.temporal.ValueRange"));
                types.add(JVM.loadClassForName("java.time.temporal.WeekFields"));
            }
            types.remove(null);
            final Iterator iter = types.iterator();
            final Class[] classes = new Class[types.size()];
            for (int i = 0; i < classes.length; ++i) {
                classes[i] = (Class)iter.next();
            }
            xstream.allowTypes(classes);
        } else {
            throw new IllegalArgumentException("Security framework of XStream instance already initialized");
        }
    }
    protected void setupAliases() {
        if (classAliasingMapper == null) {
            return;
        }
        alias("null", Mapper.Null.class);
        alias("int", Integer.class);
        alias("float", Float.class);
        alias("double", Double.class);
        alias("long", Long.class);
        alias("short", Short.class);
        alias("char", Character.class);
        alias("byte", Byte.class);
        alias("boolean", Boolean.class);
        alias("number", Number.class);
        alias("object", Object.class);
        alias("big-int", BigInteger.class);
        alias("big-decimal", BigDecimal.class);
        alias("string-buffer", StringBuffer.class);
        alias("string", String.class);
        alias("java-class", Class.class);
        alias("method", Method.class);
        alias("constructor", Constructor.class);
        alias("field", Field.class);
        alias("date", Date.class);
        alias("uri", URI.class);
        alias("url", URL.class);
        alias("bit-set", BitSet.class);
        alias("map", Map.class);
        alias("entry", Map.Entry.class);
        alias("properties", Properties.class);
        alias("list", List.class);
        alias("set", Set.class);
        alias("sorted-set", SortedSet.class);
        alias("linked-list", LinkedList.class);
        alias("vector", Vector.class);
        alias("tree-map", TreeMap.class);
        alias("tree-set", TreeSet.class);
        alias("hashtable", Hashtable.class);
        alias("empty-list", Collections.EMPTY_LIST.getClass());
        alias("empty-map", Collections.EMPTY_MAP.getClass());
        alias("empty-set", Collections.EMPTY_SET.getClass());
        alias("singleton-list", Collections.singletonList(this).getClass());
        alias("singleton-map", Collections.singletonMap(this, null).getClass());
        alias("singleton-set", Collections.singleton(this).getClass());
        if (JVM.isAWTAvailable()) {
            alias("awt-color", JVM.loadClassForName("java.awt.Color", false));
            alias("awt-font", JVM.loadClassForName("java.awt.Font", false));
            alias("awt-text-attribute", JVM.loadClassForName("java.awt.font.TextAttribute"));
        }
        Class type = JVM.loadClassForName("javax.activation.ActivationDataFlavor");
        if (type != null) {
            alias("activation-data-flavor", type);
        }
        if (JVM.isSQLAvailable()) {
            alias("sql-timestamp", JVM.loadClassForName("java.sql.Timestamp"));
            alias("sql-time", JVM.loadClassForName("java.sql.Time"));
            alias("sql-date", JVM.loadClassForName("java.sql.Date"));
        }
        alias("file", File.class);
        alias("locale", Locale.class);
        alias("gregorian-calendar", Calendar.class);
        if (JVM.isVersion(4)) {
            aliasDynamically("auth-subject", "javax.security.auth.Subject");
            alias("linked-hash-map", JVM.loadClassForName("java.util.LinkedHashMap"));
            alias("linked-hash-set", JVM.loadClassForName("java.util.LinkedHashSet"));
            alias("trace", JVM.loadClassForName("java.lang.StackTraceElement"));
            alias("currency", JVM.loadClassForName("java.util.Currency"));
            aliasType("charset", JVM.loadClassForName("java.nio.charset.Charset"));
        }
        if (JVM.isVersion(5)) {
            aliasDynamically("xml-duration", "javax.xml.datatype.Duration");
            alias("concurrent-hash-map", JVM.loadClassForName("java.util.concurrent.ConcurrentHashMap"));
            alias("enum-set", JVM.loadClassForName("java.util.EnumSet"));
            alias("enum-map", JVM.loadClassForName("java.util.EnumMap"));
            alias("string-builder", JVM.loadClassForName("java.lang.StringBuilder"));
            alias("uuid", JVM.loadClassForName("java.util.UUID"));
        }
        if (JVM.isVersion(7)) {
            aliasType("path", JVM.loadClassForName("java.nio.file.Path"));
        }
        if (JVM.isVersion(8)) {
            alias("fixed-clock", JVM.loadClassForName("java.time.Clock$FixedClock"));
            alias("offset-clock", JVM.loadClassForName("java.time.Clock$OffsetClock"));
            alias("system-clock", JVM.loadClassForName("java.time.Clock$SystemClock"));
            alias("tick-clock", JVM.loadClassForName("java.time.Clock$TickClock"));
            alias("day-of-week", JVM.loadClassForName("java.time.DayOfWeek"));
            alias("duration", JVM.loadClassForName("java.time.Duration"));
            alias("instant", JVM.loadClassForName("java.time.Instant"));
            alias("local-date", JVM.loadClassForName("java.time.LocalDate"));
            alias("local-date-time", JVM.loadClassForName("java.time.LocalDateTime"));
            alias("local-time", JVM.loadClassForName("java.time.LocalTime"));
            alias("month", JVM.loadClassForName("java.time.Month"));
            alias("month-day", JVM.loadClassForName("java.time.MonthDay"));
            alias("offset-date-time", JVM.loadClassForName("java.time.OffsetDateTime"));
            alias("offset-time", JVM.loadClassForName("java.time.OffsetTime"));
            alias("period", JVM.loadClassForName("java.time.Period"));
            alias("year", JVM.loadClassForName("java.time.Year"));
            alias("year-month", JVM.loadClassForName("java.time.YearMonth"));
            alias("zoned-date-time", JVM.loadClassForName("java.time.ZonedDateTime"));
            aliasType("zone-id", JVM.loadClassForName("java.time.ZoneId"));
            aliasType("chronology", JVM.loadClassForName("java.time.chrono.Chronology"));
            alias("hijrah-date", JVM.loadClassForName("java.time.chrono.HijrahDate"));
            alias("hijrah-era", JVM.loadClassForName("java.time.chrono.HijrahEra"));
            alias("japanese-date", JVM.loadClassForName("java.time.chrono.JapaneseDate"));
            alias("japanese-era", JVM.loadClassForName("java.time.chrono.JapaneseEra"));
            alias("minguo-date", JVM.loadClassForName("java.time.chrono.MinguoDate"));
            alias("minguo-era", JVM.loadClassForName("java.time.chrono.MinguoEra"));
            alias("thai-buddhist-date", JVM.loadClassForName("java.time.chrono.ThaiBuddhistDate"));
            alias("thai-buddhist-era", JVM.loadClassForName("java.time.chrono.ThaiBuddhistEra"));
            alias("chrono-field", JVM.loadClassForName("java.time.temporal.ChronoField"));
            alias("chrono-unit", JVM.loadClassForName("java.time.temporal.ChronoUnit"));
            alias("iso-field", JVM.loadClassForName("java.time.temporal.IsoFields$Field"));
            alias("iso-unit", JVM.loadClassForName("java.time.temporal.IsoFields$Unit"));
            alias("julian-field", JVM.loadClassForName("java.time.temporal.JulianFields$Field"));
            alias("temporal-value-range", JVM.loadClassForName("java.time.temporal.ValueRange"));
            alias("week-fields", JVM.loadClassForName("java.time.temporal.WeekFields"));
        }
        if (JVM.loadClassForName("java.lang.invoke.SerializedLambda") != null) {
            aliasDynamically("serialized-lambda", "java.lang.invoke.SerializedLambda");
        }
    }
    private void aliasDynamically(String alias, String className) {
        Class type = JVM.loadClassForName(className);
        if (type != null) {
            alias(alias, type);
        }
    }
    protected void setupDefaultImplementations() {
        if (defaultImplementationsMapper == null) {
            return;
        }
        addDefaultImplementation(HashMap.class, Map.class);
        addDefaultImplementation(ArrayList.class, List.class);
        addDefaultImplementation(HashSet.class, Set.class);
        addDefaultImplementation(TreeSet.class, SortedSet.class);
        addDefaultImplementation(GregorianCalendar.class, Calendar.class);
    }
    protected void setupConverters() {
        registerConverter(new ReflectionConverter(mapper, reflectionProvider), PRIORITY_VERY_LOW);
        registerConverter(new SerializableConverter(mapper, reflectionProvider, classLoaderReference), PRIORITY_LOW);
        registerConverter(new ExternalizableConverter(mapper, classLoaderReference), PRIORITY_LOW);
        registerConverter(new NullConverter(), PRIORITY_VERY_HIGH);
        registerConverter(new IntConverter(), PRIORITY_NORMAL);
        registerConverter(new FloatConverter(), PRIORITY_NORMAL);
        registerConverter(new DoubleConverter(), PRIORITY_NORMAL);
        registerConverter(new LongConverter(), PRIORITY_NORMAL);
        registerConverter(new ShortConverter(), PRIORITY_NORMAL);
        registerConverter((Converter)new CharConverter(), PRIORITY_NORMAL);
        registerConverter(new BooleanConverter(), PRIORITY_NORMAL);
        registerConverter(new ByteConverter(), PRIORITY_NORMAL);
        registerConverter(new StringConverter(), PRIORITY_NORMAL);
        registerConverter(new StringBufferConverter(), PRIORITY_NORMAL);
        registerConverter(new DateConverter(), PRIORITY_NORMAL);
        registerConverter(new BitSetConverter(), PRIORITY_NORMAL);
        registerConverter(new URIConverter(), PRIORITY_NORMAL);
        registerConverter(new URLConverter(), PRIORITY_NORMAL);
        registerConverter(new BigIntegerConverter(), PRIORITY_NORMAL);
        registerConverter(new BigDecimalConverter(), PRIORITY_NORMAL);
        registerConverter(new ArrayConverter(mapper), PRIORITY_NORMAL);
        registerConverter(new CharArrayConverter(), PRIORITY_NORMAL);
        registerConverter(new CollectionConverter(mapper), PRIORITY_NORMAL);
        registerConverter(new MapConverter(mapper), PRIORITY_NORMAL);
        registerConverter(new TreeMapConverter(mapper), PRIORITY_NORMAL);
        registerConverter(new TreeSetConverter(mapper), PRIORITY_NORMAL);
        registerConverter(new SingletonCollectionConverter(mapper), PRIORITY_NORMAL);
        registerConverter(new SingletonMapConverter(mapper), PRIORITY_NORMAL);
        registerConverter(new PropertiesConverter(), PRIORITY_NORMAL);
        registerConverter((Converter)new EncodedByteArrayConverter(), PRIORITY_NORMAL);
        registerConverter(new FileConverter(), PRIORITY_NORMAL);
        if (JVM.isSQLAvailable()) {
            registerConverter(new SqlTimestampConverter(), PRIORITY_NORMAL);
            registerConverter(new SqlTimeConverter(), PRIORITY_NORMAL);
            registerConverter(new SqlDateConverter(), PRIORITY_NORMAL);
        }
        registerConverter(new DynamicProxyConverter(mapper, classLoaderReference), PRIORITY_NORMAL);
        registerConverter(new JavaClassConverter(classLoaderReference), PRIORITY_NORMAL);
        registerConverter(new JavaMethodConverter(classLoaderReference), PRIORITY_NORMAL);
        registerConverter(new JavaFieldConverter(classLoaderReference), PRIORITY_NORMAL);
        if (JVM.isAWTAvailable()) {
            registerConverter(new FontConverter(mapper), PRIORITY_NORMAL);
            registerConverter(new ColorConverter(), PRIORITY_NORMAL);
            registerConverter(new TextAttributeConverter(), PRIORITY_NORMAL);
        }
        if (JVM.isSwingAvailable()) {
            registerConverter(new LookAndFeelConverter(mapper, reflectionProvider), PRIORITY_NORMAL);
        }
        registerConverter(new LocaleConverter(), PRIORITY_NORMAL);
        registerConverter(new GregorianCalendarConverter(), PRIORITY_NORMAL);
        if (JVM.isVersion(4)) {
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.SubjectConverter",
                PRIORITY_NORMAL, new Class[]{Mapper.class}, new Object[]{mapper});
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.ThrowableConverter",
                PRIORITY_NORMAL, new Class[]{ConverterLookup.class}, new Object[]{converterLookup});
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.StackTraceElementConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.CurrencyConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.RegexPatternConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.CharsetConverter",
                PRIORITY_NORMAL, null, null);
        }
        if (JVM.isVersion(5)) {
            if (JVM.loadClassForName("javax.xml.datatype.Duration") != null) {
                registerConverterDynamically("com.thoughtworks.xstream.converters.extended.DurationConverter",
                    PRIORITY_NORMAL, null, null);
            }
            registerConverterDynamically("com.thoughtworks.xstream.converters.enums.EnumConverter", PRIORITY_NORMAL,
                null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.enums.EnumSetConverter", PRIORITY_NORMAL,
                new Class[]{Mapper.class}, new Object[]{mapper});
            registerConverterDynamically("com.thoughtworks.xstream.converters.enums.EnumMapConverter", PRIORITY_NORMAL,
                new Class[]{Mapper.class}, new Object[]{mapper});
            registerConverterDynamically("com.thoughtworks.xstream.converters.basic.StringBuilderConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.basic.UUIDConverter", PRIORITY_NORMAL,
                null, null);
        }
        if (JVM.loadClassForName("javax.activation.ActivationDataFlavor") != null) {
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.ActivationDataFlavorConverter",
                PRIORITY_NORMAL, null, null);
        }
        if (JVM.isVersion(7)) {
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.PathConverter", PRIORITY_NORMAL,
                null, null);
        }
        if (JVM.isVersion(8)) {
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.ChronologyConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.DurationConverter", PRIORITY_NORMAL,
                null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.HijrahDateConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.JapaneseDateConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.JapaneseEraConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.InstantConverter", PRIORITY_NORMAL,
                null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.LocalDateConverter", PRIORITY_NORMAL,
                null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.LocalDateTimeConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.LocalTimeConverter", PRIORITY_NORMAL,
                null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.MinguoDateConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.MonthDayConverter", PRIORITY_NORMAL,
                null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.OffsetDateTimeConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.OffsetTimeConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.PeriodConverter", PRIORITY_NORMAL,
                null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.SystemClockConverter",
                PRIORITY_NORMAL, new Class[]{Mapper.class}, new Object[]{mapper});
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.ThaiBuddhistDateConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.ValueRangeConverter",
                PRIORITY_NORMAL, new Class[]{Mapper.class}, new Object[]{mapper});
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.WeekFieldsConverter",
                PRIORITY_NORMAL, new Class[]{Mapper.class}, new Object[]{mapper});
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.YearConverter", PRIORITY_NORMAL,
                null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.YearMonthConverter", PRIORITY_NORMAL,
                null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.ZonedDateTimeConverter",
                PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.time.ZoneIdConverter", PRIORITY_NORMAL,
                null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.reflection.LambdaConverter",
                PRIORITY_NORMAL, new Class[]{Mapper.class, ReflectionProvider.class, ClassLoaderReference.class},
                new Object[]{mapper, reflectionProvider, classLoaderReference});
        }
        registerConverter(new SelfStreamingInstanceChecker(converterLookup, this), PRIORITY_NORMAL);
    }
    private void registerConverterDynamically(String className, int priority, Class[] constructorParamTypes,
            Object[] constructorParamValues) {
        try {
            Class type = Class.forName(className, false, classLoaderReference.getReference());
            Constructor constructor = type.getConstructor(constructorParamTypes);
            Object instance = constructor.newInstance(constructorParamValues);
            if (instance instanceof Converter) {
                registerConverter((Converter)instance, priority);
            } else if (instance instanceof SingleValueConverter) {
                registerConverter((SingleValueConverter)instance, priority);
            }
        } catch (Exception e) {
            throw new com.thoughtworks.xstream.InitializationException("Could not instantiate converter : " + className,
                e);
        } catch (LinkageError e) {
            throw new com.thoughtworks.xstream.InitializationException("Could not instantiate converter : " + className,
                e);
        }
    }
    protected void setupImmutableTypes() {
        if (immutableTypesMapper == null) {
            return;
        }
        addImmutableType(boolean.class, false);
        addImmutableType(Boolean.class, false);
        addImmutableType(byte.class, false);
        addImmutableType(Byte.class, false);
        addImmutableType(char.class, false);
        addImmutableType(Character.class, false);
        addImmutableType(double.class, false);
        addImmutableType(Double.class, false);
        addImmutableType(float.class, false);
        addImmutableType(Float.class, false);
        addImmutableType(int.class, false);
        addImmutableType(Integer.class, false);
        addImmutableType(long.class, false);
        addImmutableType(Long.class, false);
        addImmutableType(short.class, false);
        addImmutableType(Short.class, false);
        addImmutableType(Mapper.Null.class, false);
        addImmutableType(BigDecimal.class, false);
        addImmutableType(BigInteger.class, false);
        addImmutableType(String.class, false);
        addImmutableType(URL.class, false);
        addImmutableType(File.class, false);
        addImmutableType(Class.class, false);
        if (JVM.isVersion(7)) {
            Class type = JVM.loadClassForName("java.nio.file.Paths");
            if (type != null) {
                Method methodGet;
                try {
                    methodGet = type.getDeclaredMethod("get", new Class[]{String.class, String[].class});
                    if (methodGet != null) {
                        Object path = methodGet.invoke(null, new Object[]{".", new String[0]});
                        if (path != null) {
                            addImmutableType(path.getClass(), false);
                        }
                    }
                } catch (NoSuchMethodException e) {
                } catch (SecurityException e) {
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
        if (JVM.isAWTAvailable()) {
            addImmutableTypeDynamically("java.awt.font.TextAttribute", false);
        }
        if (JVM.isVersion(4)) {
            addImmutableTypeDynamically("java.nio.charset.Charset", true);
            addImmutableTypeDynamically("java.util.Currency", true);
        }
        if (JVM.isVersion(5)) {
            addImmutableTypeDynamically("java.util.UUID", true);
        }
        addImmutableType(URI.class, true);
        addImmutableType(Collections.EMPTY_LIST.getClass(), true);
        addImmutableType(Collections.EMPTY_SET.getClass(), true);
        addImmutableType(Collections.EMPTY_MAP.getClass(), true);
        if (JVM.isVersion(8)) {
            addImmutableTypeDynamically("java.time.Duration", false);
            addImmutableTypeDynamically("java.time.Instant", false);
            addImmutableTypeDynamically("java.time.LocalDate", false);
            addImmutableTypeDynamically("java.time.LocalDateTime", false);
            addImmutableTypeDynamically("java.time.LocalTime", false);
            addImmutableTypeDynamically("java.time.MonthDay", false);
            addImmutableTypeDynamically("java.time.OffsetDateTime", false);
            addImmutableTypeDynamically("java.time.OffsetTime", false);
            addImmutableTypeDynamically("java.time.Period", false);
            addImmutableTypeDynamically("java.time.Year", false);
            addImmutableTypeDynamically("java.time.YearMonth", false);
            addImmutableTypeDynamically("java.time.ZonedDateTime", false);
            addImmutableTypeDynamically("java.time.ZoneId", false);
            addImmutableTypeDynamically("java.time.ZoneOffset", false);
            addImmutableTypeDynamically("java.time.ZoneRegion", false);
            addImmutableTypeDynamically("java.time.chrono.HijrahChronology", false);
            addImmutableTypeDynamically("java.time.chrono.HijrahDate", false);
            addImmutableTypeDynamically("java.time.chrono.IsoChronology", false);
            addImmutableTypeDynamically("java.time.chrono.JapaneseChronology", false);
            addImmutableTypeDynamically("java.time.chrono.JapaneseDate", false);
            addImmutableTypeDynamically("java.time.chrono.JapaneseEra", false);
            addImmutableTypeDynamically("java.time.chrono.MinguoChronology", false);
            addImmutableTypeDynamically("java.time.chrono.MinguoDate", false);
            addImmutableTypeDynamically("java.time.chrono.ThaiBuddhistChronology", false);
            addImmutableTypeDynamically("java.time.chrono.ThaiBuddhistDate", false);
            addImmutableTypeDynamically("java.time.temporal.IsoFields$Field", false);
            addImmutableTypeDynamically("java.time.temporal.IsoFields$Unit", false);
            addImmutableTypeDynamically("java.time.temporal.JulianFields$Field", false);
        }
    }
    private void addImmutableTypeDynamically(String className, boolean isReferenceable) {
        Class type = JVM.loadClassForName(className);
        if (type != null) {
            addImmutableType(type, isReferenceable);
        }
    }
    public void setMarshallingStrategy(MarshallingStrategy marshallingStrategy) {
        this.marshallingStrategy = marshallingStrategy;
    }
    public String toXML(Object obj) {
        Writer writer = new StringWriter();
        toXML(obj, writer);
        return writer.toString();
    }
    public void toXML(Object obj, Writer out) {
        HierarchicalStreamWriter writer = hierarchicalStreamDriver.createWriter(out);
        try {
            marshal(obj, writer);
        } finally {
            writer.flush();
        }
    }
    public void toXML(Object obj, OutputStream out) {
        HierarchicalStreamWriter writer = hierarchicalStreamDriver.createWriter(out);
        try {
            marshal(obj, writer);
        } finally {
            writer.flush();
        }
    }
    public void marshal(Object obj, HierarchicalStreamWriter writer) {
        marshal(obj, writer, null);
    }
    public void marshal(Object obj, HierarchicalStreamWriter writer, DataHolder dataHolder) {
        marshallingStrategy.marshal(writer, obj, converterLookup, mapper, dataHolder);
    }
    public Object fromXML(String xml) {
        return fromXML(new StringReader(xml));
    }
    public Object fromXML(Reader reader) {
        return unmarshal(hierarchicalStreamDriver.createReader(reader), null);
    }
    public Object fromXML(InputStream input) {
        return unmarshal(hierarchicalStreamDriver.createReader(input), null);
    }
    public Object fromXML(URL url) {
        return fromXML(url, null);
    }
    public Object fromXML(File file) {
        return fromXML(file, null);
    }
    public Object fromXML(String xml, Object root) {
        return fromXML(new StringReader(xml), root);
    }
    public Object fromXML(Reader xml, Object root) {
        return unmarshal(hierarchicalStreamDriver.createReader(xml), root);
    }
    public Object fromXML(URL url, Object root) {
        return unmarshal(hierarchicalStreamDriver.createReader(url), root);
    }
    public Object fromXML(File file, Object root) {
        HierarchicalStreamReader reader = hierarchicalStreamDriver.createReader(file);
        try {
            return unmarshal(reader, root);
        } finally {
            reader.close();
        }
    }
    public Object fromXML(InputStream input, Object root) {
        return unmarshal(hierarchicalStreamDriver.createReader(input), root);
    }
    public Object unmarshal(HierarchicalStreamReader reader) {
        return unmarshal(reader, null, null);
    }
    public Object unmarshal(HierarchicalStreamReader reader, Object root) {
        return unmarshal(reader, root, null);
    }
    public Object unmarshal(HierarchicalStreamReader reader, Object root, DataHolder dataHolder) {
        try {
            if (!securityInitialized && !securityWarningGiven) {
                securityWarningGiven = true;
                System.err
                    .println(
                        "Security framework of XStream not explicitly initialized, using predefined black list on your own risk.");
            }
            return marshallingStrategy.unmarshal(root, reader, dataHolder, converterLookup, mapper);
        } catch (ConversionException e) {
            Package pkg = getClass().getPackage();
            String version = pkg != null ? pkg.getImplementationVersion() : null;
            e.add("version", version != null ? version : "not available");
            throw e;
        }
    }
    public void alias(String name, Class type) {
        if (classAliasingMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + ClassAliasingMapper.class.getName()
                + " available");
        }
        classAliasingMapper.addClassAlias(name, type);
    }
    public void aliasType(String name, Class type) {
        if (classAliasingMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + ClassAliasingMapper.class.getName()
                + " available");
        }
        classAliasingMapper.addTypeAlias(name, type);
    }
    public void alias(String name, Class type, Class defaultImplementation) {
        alias(name, type);
        addDefaultImplementation(defaultImplementation, type);
    }
    public void aliasPackage(String name, String pkgName) {
        if (packageAliasingMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + PackageAliasingMapper.class.getName()
                + " available");
        }
        packageAliasingMapper.addPackageAlias(name, pkgName);
    }
    public void aliasField(String alias, Class definedIn, String fieldName) {
        if (fieldAliasingMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + FieldAliasingMapper.class.getName()
                + " available");
        }
        fieldAliasingMapper.addFieldAlias(alias, definedIn, fieldName);
    }
    public void aliasAttribute(String alias, String attributeName) {
        if (attributeAliasingMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + AttributeAliasingMapper.class.getName()
                + " available");
        }
        attributeAliasingMapper.addAliasFor(attributeName, alias);
    }
    public void aliasSystemAttribute(String alias, String systemAttributeName) {
        if (systemAttributeAliasingMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + SystemAttributeAliasingMapper.class.getName()
                + " available");
        }
        systemAttributeAliasingMapper.addAliasFor(systemAttributeName, alias);
    }
    public void aliasAttribute(Class definedIn, String attributeName, String alias) {
        aliasField(alias, definedIn, attributeName);
        useAttributeFor(definedIn, attributeName);
    }
    public void useAttributeFor(String fieldName, Class type) {
        if (attributeMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + AttributeMapper.class.getName()
                + " available");
        }
        attributeMapper.addAttributeFor(fieldName, type);
    }
    public void useAttributeFor(Class definedIn, String fieldName) {
        if (attributeMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + AttributeMapper.class.getName()
                + " available");
        }
        attributeMapper.addAttributeFor(definedIn, fieldName);
    }
    public void useAttributeFor(Class type) {
        if (attributeMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + AttributeMapper.class.getName()
                + " available");
        }
        attributeMapper.addAttributeFor(type);
    }
    public void addDefaultImplementation(Class defaultImplementation, Class ofType) {
        if (defaultImplementationsMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + DefaultImplementationsMapper.class.getName()
                + " available");
        }
        defaultImplementationsMapper.addDefaultImplementation(defaultImplementation, ofType);
    }
    public void addImmutableType(Class type) {
        addImmutableType(type, true);
    }
    public void addImmutableType(final Class type, final boolean isReferenceable) {
        if (immutableTypesMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + ImmutableTypesMapper.class.getName()
                + " available");
        }
        immutableTypesMapper.addImmutableType(type, isReferenceable);
    }
    public void registerConverter(Converter converter) {
        registerConverter(converter, PRIORITY_NORMAL);
    }
    public void registerConverter(Converter converter, int priority) {
        if (converterRegistry != null) {
            converterRegistry.registerConverter(converter, priority);
        }
    }
    public void registerConverter(SingleValueConverter converter) {
        registerConverter(converter, PRIORITY_NORMAL);
    }
    public void registerConverter(SingleValueConverter converter, int priority) {
        if (converterRegistry != null) {
            converterRegistry.registerConverter(new SingleValueConverterWrapper(converter), priority);
        }
    }
    public void registerLocalConverter(Class definedIn, String fieldName, Converter converter) {
        if (localConversionMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + LocalConversionMapper.class.getName()
                + " available");
        }
        localConversionMapper.registerLocalConverter(definedIn, fieldName, converter);
    }
    public void registerLocalConverter(Class definedIn, String fieldName, SingleValueConverter converter) {
        registerLocalConverter(definedIn, fieldName, (Converter)new SingleValueConverterWrapper(converter));
    }
    public Mapper getMapper() {
        return mapper;
    }
    public ReflectionProvider getReflectionProvider() {
        return reflectionProvider;
    }
    public ConverterLookup getConverterLookup() {
        return converterLookup;
    }
    public void setMode(int mode) {
        switch (mode) {
        case NO_REFERENCES:
            setMarshallingStrategy(new TreeMarshallingStrategy());
            break;
        case ID_REFERENCES:
            setMarshallingStrategy(new ReferenceByIdMarshallingStrategy());
            break;
        case XPATH_RELATIVE_REFERENCES:
            setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(
                ReferenceByXPathMarshallingStrategy.RELATIVE));
            break;
        case XPATH_ABSOLUTE_REFERENCES:
            setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(
                ReferenceByXPathMarshallingStrategy.ABSOLUTE));
            break;
        case SINGLE_NODE_XPATH_RELATIVE_REFERENCES:
            setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.RELATIVE
                | ReferenceByXPathMarshallingStrategy.SINGLE_NODE));
            break;
        case SINGLE_NODE_XPATH_ABSOLUTE_REFERENCES:
            setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.ABSOLUTE
                | ReferenceByXPathMarshallingStrategy.SINGLE_NODE));
            break;
        default:
            throw new IllegalArgumentException("Unknown mode : " + mode);
        }
    }
    public void addImplicitCollection(Class ownerType, String fieldName) {
        addImplicitCollection(ownerType, fieldName, null, null);
    }
    public void addImplicitCollection(Class ownerType, String fieldName, Class itemType) {
        addImplicitCollection(ownerType, fieldName, null, itemType);
    }
    public void addImplicitCollection(Class ownerType, String fieldName, String itemFieldName, Class itemType) {
        addImplicitMap(ownerType, fieldName, itemFieldName, itemType, null);
    }
    public void addImplicitArray(Class ownerType, String fieldName) {
        addImplicitCollection(ownerType, fieldName);
    }
    public void addImplicitArray(Class ownerType, String fieldName, Class itemType) {
        addImplicitCollection(ownerType, fieldName, itemType);
    }
    public void addImplicitArray(Class ownerType, String fieldName, String itemName) {
        addImplicitCollection(ownerType, fieldName, itemName, null);
    }
    public void addImplicitMap(Class ownerType, String fieldName, Class itemType, String keyFieldName) {
        addImplicitMap(ownerType, fieldName, null, itemType, keyFieldName);
    }
    public void addImplicitMap(Class ownerType, String fieldName, String itemName, Class itemType,
            String keyFieldName) {
        if (implicitCollectionMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + ImplicitCollectionMapper.class.getName()
                + " available");
        }
        implicitCollectionMapper.add(ownerType, fieldName, itemName, itemType, keyFieldName);
    }
    public DataHolder newDataHolder() {
        return new MapBackedDataHolder();
    }
    public ObjectOutputStream createObjectOutputStream(Writer writer) throws IOException {
        return createObjectOutputStream(hierarchicalStreamDriver.createWriter(writer), "object-stream");
    }
    public ObjectOutputStream createObjectOutputStream(HierarchicalStreamWriter writer) throws IOException {
        return createObjectOutputStream(writer, "object-stream");
    }
    public ObjectOutputStream createObjectOutputStream(Writer writer, String rootNodeName) throws IOException {
        return createObjectOutputStream(hierarchicalStreamDriver.createWriter(writer), rootNodeName);
    }
    public ObjectOutputStream createObjectOutputStream(OutputStream out) throws IOException {
        return createObjectOutputStream(hierarchicalStreamDriver.createWriter(out), "object-stream");
    }
    public ObjectOutputStream createObjectOutputStream(OutputStream out, String rootNodeName) throws IOException {
        return createObjectOutputStream(hierarchicalStreamDriver.createWriter(out), rootNodeName);
    }
    public ObjectOutputStream createObjectOutputStream(final HierarchicalStreamWriter writer, final String rootNodeName)
            throws IOException {
        return createObjectOutputStream(writer, rootNodeName, null);
    }
    public ObjectOutputStream createObjectOutputStream(final HierarchicalStreamWriter writer, final String rootNodeName,
            final DataHolder dataHolder)
            throws IOException {
        final StatefulWriter statefulWriter = new StatefulWriter(writer);
        statefulWriter.startNode(rootNodeName, null);
        return new CustomObjectOutputStream(new CustomObjectOutputStream.StreamCallback() {
            public void writeToStream(final Object object) {
                marshal(object, statefulWriter, dataHolder);
            }
            public void writeFieldsToStream(Map fields) throws NotActiveException {
                throw new NotActiveException("not in call to writeObject");
            }
            public void defaultWriteObject() throws NotActiveException {
                throw new NotActiveException("not in call to writeObject");
            }
            public void flush() {
                statefulWriter.flush();
            }
            public void close() {
                if (statefulWriter.state() != StatefulWriter.STATE_CLOSED) {
                    statefulWriter.endNode();
                    statefulWriter.close();
                }
            }
        });
    }
    public ObjectInputStream createObjectInputStream(Reader xmlReader) throws IOException {
        return createObjectInputStream(hierarchicalStreamDriver.createReader(xmlReader));
    }
    public ObjectInputStream createObjectInputStream(InputStream in) throws IOException {
        return createObjectInputStream(hierarchicalStreamDriver.createReader(in));
    }
    public ObjectInputStream createObjectInputStream(final HierarchicalStreamReader reader) throws IOException {
        return createObjectInputStream(reader, null);
    }
    public ObjectInputStream createObjectInputStream(final HierarchicalStreamReader reader, final DataHolder dataHolder)
            throws IOException {
        return new CustomObjectInputStream(new CustomObjectInputStream.StreamCallback() {
            public Object readFromStream() throws EOFException {
                if (!reader.hasMoreChildren()) {
                    throw new EOFException();
                }
                reader.moveDown();
                final Object result = unmarshal(reader, null, dataHolder);
                reader.moveUp();
                return result;
            }
            public Map readFieldsFromStream() throws IOException {
                throw new NotActiveException("not in call to readObject");
            }
            public void defaultReadObject() throws NotActiveException {
                throw new NotActiveException("not in call to readObject");
            }
            public void registerValidation(ObjectInputValidation validation, int priority) throws NotActiveException {
                throw new NotActiveException("stream inactive");
            }
            public void close() {
                reader.close();
            }
        }, classLoaderReference);
    }
    public void setClassLoader(ClassLoader classLoader) {
        classLoaderReference.setReference(classLoader);
    }
    public ClassLoader getClassLoader() {
        return classLoaderReference.getReference();
    }
    public ClassLoaderReference getClassLoaderReference() {
        return classLoaderReference;
    }
    public void omitField(Class definedIn, String fieldName) {
        if (elementIgnoringMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + ElementIgnoringMapper.class.getName()
                + " available");
        }
        elementIgnoringMapper.omitField(definedIn, fieldName);
    }
    public void ignoreUnknownElements() {
        ignoreUnknownElements(IGNORE_ALL);
    }
    public void ignoreUnknownElements(String pattern) {
        ignoreUnknownElements(Pattern.compile(pattern));
    }
    public void ignoreUnknownElements(final Pattern pattern) {
        if (elementIgnoringMapper == null) {
            throw new com.thoughtworks.xstream.InitializationException("No "
                + ElementIgnoringMapper.class.getName()
                + " available");
        }
        elementIgnoringMapper.addElementsToIgnore(pattern);
    }
    public void processAnnotations(final Class[] types) {
        if (annotationConfiguration == null) {
            throw new com.thoughtworks.xstream.InitializationException("No " + ANNOTATION_MAPPER_TYPE + " available");
        }
        annotationConfiguration.processAnnotations(types);
    }
    public void processAnnotations(final Class type) {
        processAnnotations(new Class[]{type});
    }
    public void autodetectAnnotations(boolean mode) {
        if (annotationConfiguration != null) {
            annotationConfiguration.autodetectAnnotations(mode);
        }
    }
    public void addPermission(TypePermission permission) {
        if (securityMapper != null) {
            securityInitialized |= permission.equals(NoTypePermission.NONE) || permission.equals(AnyTypePermission.ANY);
            securityMapper.addPermission(permission);
        }
    }
    public void allowTypes(String[] names) {
        addPermission(new ExplicitTypePermission(names));
    }
    public void allowTypes(Class[] types) {
        addPermission(new ExplicitTypePermission(types));
    }
    public void allowTypeHierarchy(Class type) {
        addPermission(new TypeHierarchyPermission(type));
    }
    public void allowTypesByRegExp(String[] regexps) {
        addPermission(new RegExpTypePermission(regexps));
    }
    public void allowTypesByRegExp(Pattern[] regexps) {
        addPermission(new RegExpTypePermission(regexps));
    }
    public void allowTypesByWildcard(String[] patterns) {
        addPermission(new WildcardTypePermission(patterns));
    }
    public void denyPermission(TypePermission permission) {
        addPermission(new NoPermission(permission));
    }
    public void denyTypes(String[] names) {
        denyPermission(new ExplicitTypePermission(names));
    }
    public void denyTypes(Class[] types) {
        denyPermission(new ExplicitTypePermission(types));
    }
    public void denyTypeHierarchy(Class type) {
        denyPermission(new TypeHierarchyPermission(type));
    }
    public void denyTypesByRegExp(String[] regexps) {
        denyPermission(new RegExpTypePermission(regexps));
    }
    public void denyTypesByRegExp(Pattern[] regexps) {
        denyPermission(new RegExpTypePermission(regexps));
    }
    public void denyTypesByWildcard(String[] patterns) {
        denyPermission(new WildcardTypePermission(patterns));
    }
    private Object readResolve() {
        securityWarningGiven = true;
        return this;
    }
    public static class InitializationException extends XStreamException {
        public InitializationException(String message, Throwable cause) {
            super(message, cause);
        }
        public InitializationException(String message) {
            super(message);
        }
    }
}
