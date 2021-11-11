package com.gooddata.rubik.hackathon.goodload;

import com.gooddata.rubik.hackathon.goodload.common.Opportunity;
import com.gooddata.rubik.hackathon.goodload.json.etl.engine.ETLService;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
public class Application {

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    private final TaskExecutor exec = new SimpleAsyncTaskExecutor();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }


    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(getProps());
    }

    private Map<String, Object> getProps() {
        final Map<String, Object> props = new HashMap<>();
        logger.info("Created {} using address {}.", this.getClass(), bootstrapAddress);

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "rubik.inputGroup");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class);
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Opportunity.class);

        return props;
    }

    public ConsumerFactory<String, String> consumerFactory(String groupId) {
        return new DefaultKafkaConsumerFactory<>(getProps());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory("rubik.inputGroup"));
        return factory;
    }


    /*
     * Boot will autowire this into the container factory.
     */
    @Bean
    public SeekToCurrentErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
        return new SeekToCurrentErrorHandler(
                new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2));
    }


    @Bean
    public RecordMessageConverter converter() {
        return new StringJsonMessageConverter();
    }

    @KafkaListener(id = "rubik.inputGroup", topics = "rubik-hack.input", containerFactory="kafkaListenerContainerFactory")
    public void listen(String message) {
        logger.info("Received: message={}" , message);
        this.etlService().transform(message);
    }

    @KafkaListener(id = "rubik.etlGroup", topics = "rubik-hack.output", containerFactory="kafkaListenerContainerFactory")
    public void dltListen(String message) {
        logger.info("Received from ETL: message={}" , message);
        this.exec.execute(() -> System.out.println("Hit Enter to terminate..."));
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic("rubik-hack.input", 1, (short) 1);
    }

    @Bean
    public NewTopic dlt() {
        return new NewTopic("rubik-hack.output", 1, (short) 1);
    }

    @Bean
    @Profile("default") // Don't run from test(s)
    public ApplicationRunner runner() {
        return args -> {
            System.out.println("Hit Enter to terminate...");
            System.in.read();
        };
    }

    @Bean
    public ETLService etlService(){
        return new ETLService();
    }

}
