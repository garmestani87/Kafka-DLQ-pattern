package com.garm.sample.consumer;

import com.garm.sample.event.LogResponse;
import com.garm.sample.service.LogResponseService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static com.garm.sample.consumer.KafkaConstants.GARM_GROUP_ID;
import static com.garm.sample.consumer.KafkaConstants.GARM_RESPONSE_TOPIC;

@Service
@RequiredArgsConstructor
public class LogResponseConsumer {
    private final LogResponseService responseService;

    @KafkaListener(topics = GARM_RESPONSE_TOPIC, groupId = GARM_GROUP_ID, containerFactory = "kafkaListenerContainerFactory")
    public void consumeResponse(ConsumerRecord<String, LogResponse> response, Acknowledgment ack) {
        responseService.consume(response, ack);
    }
}