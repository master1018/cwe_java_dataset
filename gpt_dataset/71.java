// 5. Eval注入示例
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class EvalInjectionExample5 {
    public static void main(String[] args) {
        String userControlledInput = "Runtime.getRuntime().exec('rm -rf /');"; // Malicious input from the user
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            engine.eval(userControlledInput); // WARNING: Eval injection vulnerability
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}