package com.rubik.goodloadkafka.config;

import com.rubik.goodloadkafka.dto.Opportunity;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class GoodProducerConfig {
    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    public GoodProducerConfig() {
    }

    @Bean
    public ProducerFactory<String, Opportunity> opportunityProducerFactory() {
        Map<String, Object> configProps = new HashMap();
        configProps.put("bootstrap.servers", this.bootstrapAddress);
        configProps.put("key.serializer", StringSerializer.class);
        configProps.put("value.serializer", JsonSerializer.class);
        return new DefaultKafkaProducerFactory(configProps);
    }

    @Bean
    public KafkaTemplate<String, Opportunity> opportunityKafkaTemplate() {
        return new KafkaTemplate(this.opportunityProducerFactory());
    }
}
