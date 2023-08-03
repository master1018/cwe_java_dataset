
package spark.examples.staticresources;
import static spark.Spark.get;
import static spark.Spark.staticFiles;
public class StaticResources {
    public static void main(String[] args) {
        staticFiles.location("/public");
        get("/hello", (request, response) -> {
            return "Hello World!";
        });
    }
}
