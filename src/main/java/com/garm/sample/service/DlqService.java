package com.garm.sample.service;

import com.garm.sample.state.DlqContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class DlqService {

    private final DlqContext context;

    public void consume(ConsumerRecord<String, ? extends SpecificRecordBase> consumerRecord, Acknowledgment ack) {
        context.resend(consumerRecord, ack);
    }
}
