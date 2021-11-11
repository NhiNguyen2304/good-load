package com.rubik.goodloadkafka.listener;

import com.rubik.goodloadkafka.dto.Opportunity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OpportunityListener implements ApplicationRunner {
    @Value("${opportunity.topic.name}")
    private String topicName;
    @Autowired
    private KafkaTemplate<String, Opportunity> opportunityKafkaTemplate;

    public OpportunityListener() {
    }

    public void sendMessage() {
        Opportunity opportunity = new Opportunity("2", "car", "car title", "950765", "2015-01-01");
        this.opportunityKafkaTemplate.send(this.topicName, opportunity);
    }

    @KafkaListener(
            topics = {"topicName"},
            groupId = "op",
            containerFactory = "opportunityKafkaListenerContainerFactory"
    )
    public void listenOpportunity(Opportunity opp) {
        System.out.println("Received Message by Testing");
    }

    public void run(ApplicationArguments args) throws Exception {
        for(int i = 0; i < 5; ++i) {
            this.sendMessage();
            Thread.sleep(2000L);
        }

    }
}
