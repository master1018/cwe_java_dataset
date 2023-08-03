
package spark.examples.staticresources;
import static spark.Spark.get;
import static spark.Spark.staticFileLocation;
public class StaticResources {
    public static void main(String[] args) {
        staticFileLocation("/public");
        get("/hello", (request, response) -> {
            return "Hello World!";
        });
    }
}
