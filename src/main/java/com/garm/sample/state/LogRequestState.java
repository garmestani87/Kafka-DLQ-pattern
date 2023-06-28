package com.garm.sample.state;

import com.garm.sample.dto.DlqDto;
import com.garm.sample.event.LogRequest;
import com.garm.sample.service.DlqBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static com.garm.sample.consumer.KafkaConstants.GARM_REQUEST_TOPIC;
import static com.garm.sample.consumer.KafkaConstants.RETRY_COUNT_HEADER_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogRequestState implements DlqState {
    private final DlqBase<String, LogRequest> dlq;

    @Override
    public void resend(ConsumerRecord<String, ? extends SpecificRecordBase> consumerRecord, Acknowledgment ack) {
        dlq.resend(new DlqDto<String, LogRequest>().setAck(ack)
                .setOriginalTopic(GARM_REQUEST_TOPIC)
                .setKey(consumerRecord.key())
                .setValue((LogRequest) consumerRecord.value())
                .setRetryCountHeader(consumerRecord.headers().lastHeader(RETRY_COUNT_HEADER_KEY)));
    }
}
