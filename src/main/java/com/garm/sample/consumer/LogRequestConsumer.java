package com.garm.sample.consumer;

import com.garm.sample.event.LogRequest;
import com.garm.sample.service.LogRequestService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static com.garm.sample.consumer.KafkaConstants.GARM_GROUP_ID;
import static com.garm.sample.consumer.KafkaConstants.GARM_REQUEST_TOPIC;

@Service
@RequiredArgsConstructor
public class LogRequestConsumer {
    private final LogRequestService requestService;

    @KafkaListener(topics = GARM_REQUEST_TOPIC, groupId = GARM_GROUP_ID, containerFactory = "kafkaListenerContainerFactory")
    public void consumeRequest(ConsumerRecord<String, LogRequest> request, Acknowledgment ack) {
        requestService.consume(request, ack);
    }
}