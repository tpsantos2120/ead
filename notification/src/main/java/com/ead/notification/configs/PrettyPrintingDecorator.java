package com.ead.notification.configs;

import com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.decorate.JsonGeneratorDecorator;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrettyPrintingDecorator implements JsonGeneratorDecorator {

    @Override
    public JsonGenerator decorate(JsonGenerator generator) {
        return generator.useDefaultPrettyPrinter();
    }

}
