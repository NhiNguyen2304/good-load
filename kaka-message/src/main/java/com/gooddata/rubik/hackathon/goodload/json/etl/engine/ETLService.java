package com.gooddata.rubik.hackathon.goodload.json.etl.engine;

import jdk.nashorn.api.scripting.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;

@Component
public class ETLService {
    private final Logger logger = LoggerFactory.getLogger(ETLService.class);

    @Autowired
    private KafkaTemplate<Object, Object> template;

    public boolean transform(Object input) {
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        try {
            engine.eval(new java.io.InputStreamReader(ETLService.class.getClassLoader().getResourceAsStream("transform_script.js")));
            final Invocable inv = (Invocable) engine;

            Object result =  inv.invokeFunction("transform", input);
            sendMessage((String)result);
            return true;
        } catch (ScriptException | NoSuchMethodException e) {
            logger.warn("Script execution failed.", e);
        }

        return false;
    }

    public void sendMessage(String message){
        logger.info("Start to process: " + message);
        this.template.send("rubik-hack.output", message);
    }
}
