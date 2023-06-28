package com.garm.sample.state;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.support.Acknowledgment;

import static com.garm.sample.consumer.KafkaConstants.ORIGINAL_TOPIC_HEADER_KEY;
import static java.nio.charset.StandardCharsets.UTF_8;

public interface DlqState {
    void resend(ConsumerRecord<String, ? extends SpecificRecordBase> consumerRecord, Acknowledgment ack);

    default String getOriginalTopic(ConsumerRecord<String, ? extends SpecificRecordBase> consumerRecord) {
        Header originalTopicHeader = consumerRecord.headers().lastHeader(ORIGINAL_TOPIC_HEADER_KEY);
        return new String(originalTopicHeader.value(), UTF_8);
    }
}
