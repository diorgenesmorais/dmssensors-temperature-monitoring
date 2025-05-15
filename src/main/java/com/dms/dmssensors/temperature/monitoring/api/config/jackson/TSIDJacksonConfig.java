package com.dms.dmssensors.temperature.monitoring.api.config.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.hypersistence.tsid.TSID;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TSIDJacksonConfig {

    @Bean
    Module tsidModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(TSID.class, new TSIDToStringSerializer());
        module.addDeserializer(TSID.class, new StringToTSIDDeserializer());
        return module;
    }
}
