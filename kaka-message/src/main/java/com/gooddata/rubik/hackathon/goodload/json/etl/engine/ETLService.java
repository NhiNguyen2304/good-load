package com.gooddata.rubik.hackathon.goodload.json.etl.engine;

import jdk.nashorn.api.scripting.JSObject;
import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;

@Component
public class ETLService {

    public Object transform(Object input) {
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        try {
            engine.eval(new java.io.FileReader("/tmp/tranform_script.js"));
            final Invocable inv = (Invocable) engine;

            engine.put("input", input);
            JSObject obj = (JSObject)engine.eval("JSON.parse(input)");

            return inv.invokeFunction("transform", obj);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptRuntimeException("Script execution failed.", e);
        } catch (FileNotFoundException e) {
            throw new ScriptRuntimeException("Script file not found.", e);
        }

    }
}
