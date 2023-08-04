@Stateless
public class LoaderSessionBean implements LoaderSessionRemote {
public LoaderSessionBean() {
try {
ClassLoader loader = new CustomClassLoader();
Class c = loader.loadClass("someClass");
Object obj = c.newInstance();
...
} catch (Exception ex) {...}
}
public class CustomClassLoader extends ClassLoader {
}
}
