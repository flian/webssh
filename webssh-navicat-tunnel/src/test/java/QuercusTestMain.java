import com.caucho.quercus.script.QuercusScriptEngine;

import javax.script.ScriptException;


public class QuercusTestMain {
    public static void main(String[] args) throws ScriptException {
        QuercusScriptEngine engine = new QuercusScriptEngine();
        String phpCode = "<?php echo 'Hello, World!'; ?>";
        Object result = engine.eval(phpCode);
        System.out.println(result);

    }
}
