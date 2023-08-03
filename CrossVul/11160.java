package org.jboss.seam.remoting;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.seam.Component;
import org.jboss.seam.ComponentType;
import org.jboss.seam.Seam;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.jboss.seam.log.LogProvider;
import org.jboss.seam.log.Logging;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.jboss.seam.util.EJB;
import org.jboss.seam.util.Reflections;
import org.jboss.seam.web.ServletContexts;
public class InterfaceGenerator extends BaseRequestHandler implements RequestHandler
{
   private static final LogProvider log = Logging.getLogProvider(InterfaceGenerator.class);
  private static Map<Class,Set<String>> accessibleProperties = new HashMap<Class,Set<String>>();
  private Map<String,byte[]> interfaceCache = new HashMap<String,byte[]>();
  public void handle(final HttpServletRequest request, final HttpServletResponse response)
      throws Exception
  {
     new ContextualHttpServletRequest(request)
     {
        @Override
        public void process() throws Exception
        {
           ServletContexts.instance().setRequest(request);
           if (request.getQueryString() == null)
           {
              throw new ServletException("Invalid request - no component specified");
           }
           Set<Component> components = new HashSet<Component>();
           Set<Type> types = new HashSet<Type>();
           response.setContentType("text/javascript");
           Enumeration e = request.getParameterNames();
           while (e.hasMoreElements())
           {
              String componentName = ((String) e.nextElement()).trim();
              Component component = Component.forName(componentName);
              if (component == null)
              {                 
                 log.error(String.format("Component not found: [%s]", componentName));
                 throw new ServletException("Invalid request - component not found.");
              }
              else
              {
                 components.add(component);
              }
           }
           generateComponentInterface(components, response.getOutputStream(), types);            
       }
     }.run();
  }
  public void generateComponentInterface(Set<Component> components, OutputStream out, Set<Type> types)
      throws IOException
  {
    for (Component c : components)
    {
      if (c != null)
      {
        if (!interfaceCache.containsKey(c.getName()))
        {
          synchronized (interfaceCache)
          {
            if (!interfaceCache.containsKey(c.getName()))
            {
              ByteArrayOutputStream bOut = new ByteArrayOutputStream();
              appendComponentSource(bOut, c, types);
              interfaceCache.put(c.getName(), bOut.toByteArray());
            }
          }
        }
        out.write(interfaceCache.get(c.getName()));
      }
    }
  }
  public static Set<String> getAccessibleProperties(Class cls)
  {
    if (cls.getName().contains("EnhancerByCGLIB"))
      cls = cls.getSuperclass();
    if (!accessibleProperties.containsKey(cls))
    {
      synchronized(accessibleProperties)
      {
        if (!accessibleProperties.containsKey(cls))
        {
          Set<String> properties = new HashSet<String>();
          Class c = cls;
          while (c != null && !c.equals(Object.class))
          {
            for (Field f : c.getDeclaredFields())
            {
              if (!Modifier.isTransient(f.getModifiers()) &&
                  !Modifier.isStatic(f.getModifiers()))
              {
                String fieldName = f.getName().substring(0, 1).toUpperCase() +
                    f.getName().substring(1);
                String getterName = String.format("get%s", fieldName);
                String setterName = String.format("set%s", fieldName);
                Method getMethod = null;
                Method setMethod = null;
                try
                {
                  getMethod = c.getMethod(getterName);
                }
                catch (SecurityException ex)
                {}
                catch (NoSuchMethodException ex)
                {
                  getterName = String.format("is%s", fieldName);
                  try
                  {
                    getMethod = c.getMethod(getterName);
                  }
                  catch (NoSuchMethodException ex2)
                  { }
                }
                try
                {
                  setMethod = c.getMethod(setterName, new Class[] {f.getType()});
                }
                catch (SecurityException ex)
                {}
                catch (NoSuchMethodException ex)
                { }
                if (Modifier.isPublic(f.getModifiers()) ||
                    (getMethod != null &&
                     Modifier.isPublic(getMethod.getModifiers()) ||
                     (setMethod != null &&
                      Modifier.isPublic(setMethod.getModifiers()))))
                {
                  properties.add(f.getName());
                }
              }
            }
            for (Method m : c.getDeclaredMethods())
            {
              if (m.getName().startsWith("get") || m.getName().startsWith("is"))
              {
                int startIdx = m.getName().startsWith("get") ? 3 : 2;
                try
                {
                  c.getMethod(String.format("set%s",
                                            m.getName().substring(startIdx)), m.getReturnType());
                }
                catch (NoSuchMethodException ex)
                {
                  continue;
                }
                String propertyName = String.format("%s%s",
                    Character.toLowerCase(m.getName().charAt(startIdx)),
                    m.getName().substring(startIdx + 1));
                if (!properties.contains(propertyName))
                  properties.add(propertyName);
              }
            }
            c = c.getSuperclass();
          }
          accessibleProperties.put(cls, properties);
        }
      }
    }
    return accessibleProperties.get(cls);
  }
  private void appendComponentSource(OutputStream out, Component component, Set<Type> types)
      throws IOException
  {
    StringBuilder componentSrc = new StringBuilder();
    Set<Class> componentTypes = new HashSet<Class>();
    if (component.getType().isSessionBean() &&
        component.getBusinessInterfaces().size() > 0)
    {
      for (Class c : component.getBusinessInterfaces())
      {
         if (c.isAnnotationPresent(EJB.LOCAL))
         {
            componentTypes.add(c);
            break;
         }
      }
      if (componentTypes.isEmpty())
        throw new RuntimeException(String.format(
        "Type cannot be determined for component [%s]. Please ensure that it has a local interface.", component));
    }
    else if (component.getType().equals(ComponentType.ENTITY_BEAN))
    {
      appendTypeSource(out, component.getBeanClass(), types);
      return;
    }
    else if (component.getType().equals(ComponentType.JAVA_BEAN))
    {
      for (Method m : component.getBeanClass().getDeclaredMethods())
      {
        if (m.getAnnotation(WebRemote.class) != null)
        {
          componentTypes.add(component.getBeanClass());
          break;
        }
      }
      if (componentTypes.isEmpty())
      {
        appendTypeSource(out, component.getBeanClass(), types);
        return;
      }
    }
    else
    {
      componentTypes.add(component.getBeanClass());
    }
    boolean foundNew = false;
    for (Class type : componentTypes)
    {
      if (!types.contains(type)) 
      {
         foundNew = true;
         break;
      }
    }    
    if (!foundNew) return;    
    if (component.getName().contains("."))
    {
       componentSrc.append("Seam.Remoting.createNamespace('");
       componentSrc.append(component.getName().substring(0, component.getName().lastIndexOf('.')));
       componentSrc.append("');\n");
    }
    componentSrc.append("Seam.Remoting.type.");
    componentSrc.append(component.getName());
    componentSrc.append(" = function() {\n");
    componentSrc.append("  this.__callback = new Object();\n");
    for (Class type : componentTypes)
    {
       if (types.contains(type))
       {
          break;
       }
       else
       {
          types.add(type);
          for (Method m : type.getDeclaredMethods())
          {
            if (m.getAnnotation(WebRemote.class) == null) continue;
            appendTypeSource(out, m.getGenericReturnType(), types);
            componentSrc.append("  Seam.Remoting.type.");
            componentSrc.append(component.getName());
            componentSrc.append(".prototype.");
            componentSrc.append(m.getName());
            componentSrc.append(" = function(");
            for (int i = 0; i < m.getGenericParameterTypes().length; i++)
            {
              appendTypeSource(out, m.getGenericParameterTypes()[i], types);
              if (i > 0) componentSrc.append(", ");
              componentSrc.append("p");
              componentSrc.append(i);
            }
            if (m.getGenericParameterTypes().length > 0) componentSrc.append(", ");
            componentSrc.append("callback, exceptionHandler) {\n");
            componentSrc.append("    return Seam.Remoting.execute(this, \"");
            componentSrc.append(m.getName());
            componentSrc.append("\", [");
            for (int i = 0; i < m.getParameterTypes().length; i++)
            {
              if (i > 0) componentSrc.append(", ");
              componentSrc.append("p");
              componentSrc.append(i);
            }
            componentSrc.append("], callback, exceptionHandler);\n");
            componentSrc.append("  }\n");
          }
       }
       componentSrc.append("}\n");
       componentSrc.append("Seam.Remoting.type.");
       componentSrc.append(component.getName());
       componentSrc.append(".__name = \"");
       componentSrc.append(component.getName());
       componentSrc.append("\";\n\n");
       componentSrc.append("Seam.Component.register(Seam.Remoting.type.");
       componentSrc.append(component.getName());
       componentSrc.append(");\n\n");
       out.write(componentSrc.toString().getBytes());                 
    }       
  }
  private void appendTypeSource(OutputStream out, Type type, Set<Type> types)
      throws IOException
  {
    if (type instanceof Class)
    {
      Class classType = (Class) type;
      if (classType.isArray())
      {
        appendTypeSource(out, classType.getComponentType(), types);
        return;
      }
      if (classType.getName().startsWith("java.") ||
          types.contains(type) || classType.isPrimitive())
      return;
      types.add(type);
      appendClassSource(out, classType, types);
    }
    else if (type instanceof ParameterizedType)
    {
      for (Type t : ((ParameterizedType) type).getActualTypeArguments())
        appendTypeSource(out, t, types);
    }
  }
  private void appendClassSource(OutputStream out, Class classType, Set<Type> types)
      throws IOException
  {
    if (classType.isEnum())
      return;
    StringBuilder typeSource = new StringBuilder();
    String componentName = Seam.getComponentName(classType);
    if (componentName == null)
      componentName = classType.getName();
    String typeName = componentName.replace('.', '$');
    typeSource.append("Seam.Remoting.type.");
    typeSource.append(typeName);
    typeSource.append(" = function() {\n");
    StringBuilder fields = new StringBuilder();
    StringBuilder accessors = new StringBuilder();
    StringBuilder mutators = new StringBuilder();
    Map<String,String> metadata = new HashMap<String,String>();
    String getMethodName = null;
    String setMethodName = null;
    for ( String propertyName : getAccessibleProperties(classType) )
    {
      Type propertyType = null;
      Field f = null;
      try
      {
        f = classType.getDeclaredField(propertyName);
        propertyType = f.getGenericType();
      }
      catch (NoSuchFieldException ex)
      {
        setMethodName = String.format("set%s%s",
            Character.toUpperCase(propertyName.charAt(0)),
              propertyName.substring(1));
        try
        {
          getMethodName = String.format("get%s%s",
              Character.toUpperCase(propertyName.charAt(0)),
              propertyName.substring(1));
          propertyType = classType.getMethod(getMethodName).getGenericReturnType();
        }
        catch (NoSuchMethodException ex2)
        {
          try
          {
            getMethodName = String.format("is%s%s",
                Character.toUpperCase(propertyName.charAt(0)),
                propertyName.substring(1));
            propertyType = classType.getMethod(getMethodName).getGenericReturnType();
          }
          catch (NoSuchMethodException ex3)
          {
            continue;
          }
        }
      }
      appendTypeSource(out, propertyType, types);
      if (propertyType instanceof ParameterizedType)
      {
        for (Type t : ((ParameterizedType) propertyType).getActualTypeArguments())
        {
          if (t instanceof Class)
            appendTypeSource(out, t, types);
        }
      }
      if (f != null)
      {
        String fieldName = propertyName.substring(0, 1).toUpperCase() +
            propertyName.substring(1);
        String getterName = String.format("get%s", fieldName);
        String setterName = String.format("set%s", fieldName);
        try
        {
          classType.getMethod(getterName);
          getMethodName = getterName;
        }
        catch (SecurityException ex){}
        catch (NoSuchMethodException ex)
        {
          getterName = String.format("is%s", fieldName);
          try
          {
            if (Modifier.isPublic(classType.getMethod(getterName).getModifiers()))
              getMethodName = getterName;
          }
          catch (NoSuchMethodException ex2)
          { }
        }
        try
        {
          if (Modifier.isPublic(classType.getMethod(setterName, f.getType()).getModifiers()))
            setMethodName = setterName;
        }
        catch (SecurityException ex) {}
        catch (NoSuchMethodException ex) { }
      }
      if (getMethodName != null || setMethodName != null)
      {
        metadata.put(propertyName, getFieldType(propertyType));
        fields.append("  this.");
        fields.append(propertyName);
        fields.append(" = undefined;\n");
        if (getMethodName != null)
        {
          accessors.append("  Seam.Remoting.type.");
          accessors.append(typeName);
          accessors.append(".prototype.");
          accessors.append(getMethodName);
          accessors.append(" = function() { return this.");
          accessors.append(propertyName);
          accessors.append("; }\n");
        }
        if (setMethodName != null)
        {
          mutators.append("  Seam.Remoting.type.");
          mutators.append(typeName);
          mutators.append(".prototype.");
          mutators.append(setMethodName);
          mutators.append(" = function(");
          mutators.append(propertyName);
          mutators.append(") { this.");
          mutators.append(propertyName);
          mutators.append(" = ");
          mutators.append(propertyName);
          mutators.append("; }\n");
        }
      }
    }
    typeSource.append(fields);
    typeSource.append(accessors);
    typeSource.append(mutators);
    typeSource.append("}\n\n");
    typeSource.append("Seam.Remoting.type.");
    typeSource.append(typeName);
    typeSource.append(".__name = \"");
    typeSource.append(componentName);
    typeSource.append("\";\n");
    typeSource.append("Seam.Remoting.type.");
    typeSource.append(typeName);
    typeSource.append(".__metadata = [\n");
    boolean first = true;
    for (String key : metadata.keySet())
    {
      if (!first)
        typeSource.append(",\n");
      typeSource.append("  {field: \"");
      typeSource.append(key);
      typeSource.append("\", type: \"");
      typeSource.append(metadata.get(key));
      typeSource.append("\"}");
      first = false;
    }
    typeSource.append("];\n\n");
    if (classType.isAnnotationPresent(Name.class))
      typeSource.append("Seam.Component.register(Seam.Remoting.type.");
    else
      typeSource.append("Seam.Remoting.registerType(Seam.Remoting.type.");
    typeSource.append(typeName);
    typeSource.append(");\n\n");
    out.write(typeSource.toString().getBytes());
  }
  protected String getFieldType(Type type)
  {
    if (type.equals(String.class) ||
        (type instanceof Class && ( (Class) type).isEnum()) ||
        type.equals(BigInteger.class) || type.equals(BigDecimal.class))
      return "str";
    else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE))
      return "bool";
    else if (type.equals(Short.class) || type.equals(Short.TYPE) ||
             type.equals(Integer.class) || type.equals(Integer.TYPE) ||
             type.equals(Long.class) || type.equals(Long.TYPE) ||
             type.equals(Float.class) || type.equals(Float.TYPE) ||
             type.equals(Double.class) || type.equals(Double.TYPE) ||
             type.equals(Byte.class) || type.equals(Byte.TYPE))
      return "number";
    else if (type instanceof Class)
    {
      Class cls = (Class) type;
      if (Date.class.isAssignableFrom(cls) || Calendar.class.isAssignableFrom(cls))
        return "date";
      else if (cls.isArray())
        return "bag";
      else if (cls.isAssignableFrom(Map.class))
        return "map";
      else if (cls.isAssignableFrom(Collection.class))
        return "bag";
    }
    else if (type instanceof ParameterizedType)
    {
      ParameterizedType pt = (ParameterizedType) type;
      if (pt.getRawType() instanceof Class && Map.class.isAssignableFrom((Class) pt.getRawType()))
        return "map";
      else if (pt.getRawType() instanceof Class && Collection.class.isAssignableFrom((Class) pt.getRawType()))
        return "bag";
    }
    return "bean";
  }
}
