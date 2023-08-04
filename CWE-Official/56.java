@Stateless
public class LoaderSessionBean implements LoaderSessionRemote {
    public LoaderSessionBean() {
        try {
            ClassLoader loader = new CustomClassLoader();
            Class<?> c = loader.loadClass("someClass");
            Object obj = c.newInstance();
            // ...
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class CustomClassLoader extends ClassLoader {
        // Implement class loading logic here if needed
        // For example, you can override the findClass method to load classes in a custom way
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            // Your custom class loading logic here
            return super.findClass(name);
        }
    }

}
