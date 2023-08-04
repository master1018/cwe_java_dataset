import java.net.URL;

public final class UrlTool extends Applet {
    public final static URL[] urls;

     public static URL getUrlAtIndex(int index) {
         if (index >= 0 && index < urls.length) {
             return urls[index];
         } else {
             throw new IndexOutOfBoundsException("Invalid index");
         }
     }
}
