package com.garm.sample.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static com.garm.sample.consumer.KafkaConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DlqContext implements DlqState {

    private final LogRequestState requestState;
    private final LogResponseState responseState;

    @Override
    public void resend(ConsumerRecord<String, ? extends SpecificRecordBase> consumerRecord, Acknowledgment ack) {
        Header originalTopicHeader = consumerRecord.headers().lastHeader(ORIGINAL_TOPIC_HEADER_KEY);
        if (originalTopicHeader != null) {
            String originalTopic = getOriginalTopic(consumerRecord);
            if (originalTopic.equals(GARM_REQUEST_TOPIC)) {
                requestState.resend(consumerRecord, ack);

            } else if (originalTopic.equals(GARM_RESPONSE_TOPIC)) {
                responseState.resend(consumerRecord, ack);
            }
        } else {
            log.error("Unable to resend DLQ message because it's missing the originalTopic header");
        }
    }

    @Override
    public String getOriginalTopic(ConsumerRecord<String, ? extends SpecificRecordBase> consumerRecord) {
        return DlqState.super.getOriginalTopic(consumerRecord);
    }
}
