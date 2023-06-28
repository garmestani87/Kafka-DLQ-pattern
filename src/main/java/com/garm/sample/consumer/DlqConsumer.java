package com.garm.sample.consumer;

import com.garm.sample.service.DlqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static com.garm.sample.consumer.KafkaConstants.GARM_DLQ_TOPIC;
import static com.garm.sample.consumer.KafkaConstants.GARM_GROUP_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DlqConsumer {

    private final DlqService dlqService;

    @KafkaListener(groupId = GARM_GROUP_ID, topics = GARM_DLQ_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, ? extends SpecificRecordBase> consumerRecord, Acknowledgment ack) {
        dlqService.consume(consumerRecord, ack);
    }
}