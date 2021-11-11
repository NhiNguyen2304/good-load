package com.rubik.goodloadkafka.config;

import com.rubik.goodloadkafka.dto.Opportunity;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class GoodConsumerConfig {
    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    public GoodConsumerConfig() {
    }

    @Bean
    public ConsumerFactory<String, Opportunity> opportunityConsumerFactory() {
        Map<String, Object> props = new HashMap();
        props.put("bootstrap.servers", this.bootstrapAddress);
        props.put("group.id", "op");
        props.put("enable.auto.commit", false);
        props.put("auto.commit.interval.ms", "100");
        props.put("session.timeout.ms", "15000");
        props.put("auto.offset.reset", "earliest");
        return new DefaultKafkaConsumerFactory(props, new StringDeserializer(), new JsonDeserializer(Opportunity.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Opportunity> opportunityKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Opportunity> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(this.opportunityConsumerFactory());
        return factory;
    }
}
