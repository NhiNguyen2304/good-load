package com.rubik.goodloadkafka.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;

public class GoodTopicConfig {
    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;
    @Value("${opportunity.topic.name}")
    private String topicName;

    public GoodTopicConfig() {
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap();
        configs.put("bootstrap.servers", this.bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic opportunity() {
        return new NewTopic("topicName", 1, (short)1);
    }
}
