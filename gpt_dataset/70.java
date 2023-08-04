// 4. Eval注入示例
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class EvalInjectionExample4 {
    public static void main(String[] args) {
        String userControlledInput = "new java.util.Date();"; // User-controlled input
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            engine.eval(userControlledInput); // WARNING: Eval injection vulnerability
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}